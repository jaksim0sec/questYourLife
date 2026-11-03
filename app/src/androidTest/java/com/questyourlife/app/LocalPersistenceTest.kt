package com.questyourlife.app

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.questyourlife.app.data.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LocalPersistenceTest {
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test fun rejectionIsIdempotentAndCannotCancelAnAcceptedQuest() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, QuestDatabase::class.java).build()
        try {
            val dao = db.dailyQuestDao()
            val day = "2026-11-03"
            dao.insertAll(listOf(
                DailyQuestEntity("reject", day, "제안", "수락 전", 10),
                DailyQuestEntity("accept", day, "진행", "수락 후", 10)))
            assertEquals(0, dao.reject("reject", "2026-11-02"))
            assertEquals(1, dao.reject("reject", day))
            assertEquals(0, dao.reject("reject", day))
            assertEquals(0, dao.accept("reject", day))
            assertEquals(1, dao.accept("accept", day))
            assertEquals(0, dao.reject("accept", day))
            assertEquals(1, dao.completeAndAdvance("accept", day, 100L))
            assertEquals(0, dao.reject("accept", day))
        } finally { db.close() }
    }

    @Test fun upgradeFromV3PreservesCompletedCurrencyAndPurchases() {
        val name = "migration-${UUID.randomUUID()}.db"
        val schema = InstrumentationRegistry.getInstrumentation().context.assets
            .open("com.questyourlife.app.data.QuestDatabase/3.json").bufferedReader().use { it.readText() }
        val entities = JSONObject(schema).getJSONObject("database").getJSONArray("entities")
        context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null).use { old ->
            for (i in 0 until entities.length()) {
                val entity = entities.getJSONObject(i)
                old.execSQL(entity.getString("createSql").replace("\${TABLE_NAME}", entity.getString("tableName")))
                val indices = entity.optJSONArray("indices")
                for (j in 0 until (indices?.length() ?: 0)) old.execSQL(indices!!.getJSONObject(j).getString("createSql").replace("\${TABLE_NAME}", entity.getString("tableName")))
            }
            old.execSQL("INSERT INTO daily_quests VALUES ('old','2026-11-01','기존 완료','보존',20,1,100)")
            old.execSQL("INSERT INTO owned_items VALUES (7,10,100)")
            old.version = 3
        }
        val db = Room.databaseBuilder(context, QuestDatabase::class.java, name)
            .addMigrations(QuestDatabase.MIGRATION_3_4, QuestDatabase.MIGRATION_4_5).build()
        try {
            runBlocking {
                val preserved = db.dailyQuestDao().find("old")!!
                assertEquals(100L, preserved.completedAt)
                assertFalse(preserved.dismissed)
                assertNull(preserved.goalId)
                assertEquals(20, db.goalDao().dailyRewards())
                assertEquals(10, db.goalDao().purchases())
            }
        } finally { db.close(); context.deleteDatabase(name) }
    }

    @Test fun repeatedPlanApplyAndCompletionAreIdempotentAndSurviveReopen() {
        val name = "plan-${UUID.randomUUID()}.db"
        var db = Room.databaseBuilder(context, QuestDatabase::class.java, name).build()
        try {
            val id = runBlocking {
                val message = CompanionMessageEntity("plan", "mascot", "제안", false, 1,
                    "일본어 공부", "あ행 익히기\nか행 익히기")
                db.companionDao().insert(message)
                val first = db.companionDao().applyPlan("plan")!!
                assertEquals(first, db.companionDao().applyPlan("plan"))
                assertEquals(1, db.goalDao().countGoals())
                assertEquals(2, db.goalDao().getSteps(first).size)
                first
            }
            db.close()
            db = Room.databaseBuilder(context, QuestDatabase::class.java, name).build()
            runBlocking {
                assertEquals(id, db.companionDao().get("plan")!!.appliedGoalId)
                val daily = db.dailyQuestDao().find("$id-0")!!
                assertEquals(0, db.dailyQuestDao().completeAndAdvance(daily.id, daily.dateKey, 100))
                assertEquals(1, db.dailyQuestDao().accept(daily.id, daily.dateKey))
                assertEquals(1, db.dailyQuestDao().completeAndAdvance(daily.id, daily.dateKey, 100))
                assertEquals(0, db.dailyQuestDao().completeAndAdvance(daily.id, daily.dateKey, 200))
                assertEquals(20, db.goalDao().dailyRewards())
                assertEquals(0, db.goalDao().mainRewards())
                assertEquals(100L, db.goalDao().getSteps(id).first().completedAt)
                assertEquals(1, db.goalDao().nextCompanionSteps().single().position)
            }
        } finally { db.close(); context.deleteDatabase(name) }
    }
    @Test fun overdueWorkCarriesForwardAndAbandonDoesNotBlockTheParentForever() = runBlocking {
        val name = "rollover-${UUID.randomUUID()}.db"
        val db = Room.databaseBuilder(context, QuestDatabase::class.java, name).build()
        try {
            db.companionDao().insert(CompanionMessageEntity("rollover", "mascot", "제안", false, 1,
                "독서", "첫 부분 읽기\n문장 남기기"))
            val id = db.companionDao().applyPlan("rollover")!!
            val dao = db.dailyQuestDao()
            val daily = dao.find("$id-0")!!
            dao.defer(daily.id, "2000-01-01")
            assertEquals(1, dao.carryUnfinished("2026-11-02"))
            assertEquals("2026-11-02", dao.find(daily.id)!!.dateKey)
            dao.defer(daily.id, "2099-01-01")
            assertEquals(0, dao.carryUnfinished("2026-11-02"))
            assertEquals("2099-01-01", dao.find(daily.id)!!.dateKey)
            assertEquals(1, dao.abandon(daily.id))
            assertNull(dao.find(daily.id))
            assertEquals(0, dao.completeAndAdvance(daily.id, "2099-01-01", 5))
            assertEquals(0, db.goalDao().nextCompanionSteps().single().position)
        } finally { db.close(); context.deleteDatabase(name) }
    }

    @Test fun directStepCompletionSettlesTheLinkedDailyOnce() = runBlocking {
        val name = "direct-${UUID.randomUUID()}.db"
        val db = Room.databaseBuilder(context, QuestDatabase::class.java, name).build()
        try {
            db.companionDao().insert(CompanionMessageEntity("direct", "mascot", "제안", false, 1,
                "일본어", "あ행 익히기"))
            val id = db.companionDao().applyPlan("direct")!!
            assertEquals(1, db.goalDao().completeStep(id, 0))
            val daily = db.dailyQuestDao().find("$id-0")!!
            assertNotNull(daily.completedAt)
            assertEquals(0, db.goalDao().completeStep(id, 0))
            assertEquals(0, db.dailyQuestDao().completeAndAdvance(daily.id, daily.dateKey, 100))
            assertEquals(20, db.goalDao().dailyRewards())
        } finally { db.close(); context.deleteDatabase(name) }
    }

}
