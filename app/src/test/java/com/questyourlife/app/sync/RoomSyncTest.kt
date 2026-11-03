package com.questyourlife.app.sync

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import com.questyourlife.app.data.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[28], manifest=Config.NONE)
class RoomSyncTest {
    private val context: Context get()=ApplicationProvider.getApplicationContext()
    @Test fun v4UpgradePreservesRowsAndLinksStableSteps() {
        val name="v4-${UUID.randomUUID()}.db"
        val schema=java.io.File("schemas/com.questyourlife.app.data.QuestDatabase/4.json").readText()
        val entities=JSONObject(schema).getJSONObject("database").getJSONArray("entities")
        context.openOrCreateDatabase(name,Context.MODE_PRIVATE,null).use { old ->
            for(i in 0 until entities.length()) {
                val entity=entities.getJSONObject(i)
                old.execSQL(entity.getString("createSql").replace("\${TABLE_NAME}",entity.getString("tableName")))
                val indexes=entity.optJSONArray("indices") ?: org.json.JSONArray()
                for(j in 0 until indexes.length()) old.execSQL(indexes.getJSONObject(j).getString("createSql").replace("\${TABLE_NAME}",entity.getString("tableName")))
            }
            old.execSQL("INSERT INTO goals VALUES('old-goal','기존 목표','보존',10)")
            old.execSQL("INSERT INTO goal_steps VALUES('old-goal',0,1,'완료 단계',20,100)")
            old.execSQL("INSERT INTO daily_quests VALUES('old-daily','2026-11-01','완료','보존',20,1,100,0,'old-goal',0)")
            old.execSQL("INSERT INTO owned_items VALUES(2,10,50)")
            old.version=4
        }
        val db=Room.databaseBuilder(context,QuestDatabase::class.java,name).addMigrations(QuestDatabase.MIGRATION_4_5).build()
        try { runBlocking {
            val day=db.syncDao().daily("old-daily")!!
            val step=db.goalDao().getSteps("old-goal").single()
            assertEquals(100L,day.completedAt); assertEquals(100L,step.completedAt)
            UUID.fromString(day.syncId); UUID.fromString(step.syncId)
            assertEquals(step.syncId,day.stepSyncId)
            assertEquals(30L,db.syncDao().meta()!!.legacyCash)
            assertEquals(0L,db.syncDao().meta()!!.confirmedCash)
            assertEquals(10,db.goalDao().purchases())
            assertEquals("legacy",db.goalDao().watchItemsSnapshot().single().source)
        } } finally { db.close();context.deleteDatabase(name) }
    }
    @Test fun stableStepIdentitySurvivesReusedDisplayPositions() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            db.goalDao().insertGoal(GoalEntity("goal","책","",1))
            val old=GoalStepEntity("goal",0,1,"이전 완료 단계",20,100)
            val next=GoalStepEntity("goal",0,1,"새 단계",20)
            db.syncDao().putSteps(listOf(old,next))
            val rows=db.goalDao().watchSteps().first()
            assertEquals(2,rows.size)
            assertEquals(100L,rows.first { it.syncId==old.syncId }.completedAt)
        } finally { db.close() }
    }
    @Test fun domainAndOutboxRollbackTogether() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            try { db.withTransaction {
                db.goalDao().addGoal(GoalEntity("goal","실제 목표","",1),listOf(GoalStepEntity("goal",0,1,"단계",20)))
                SyncCapture.goal(db,"goal")
                throw IllegalStateException("crash")
            } } catch(_:IllegalStateException) { }
            assertEquals(0,db.goalDao().countGoals());assertTrue(db.syncDao().pending().isEmpty())
            db.withTransaction {
                db.goalDao().addGoal(GoalEntity("goal","실제 목표","",1),listOf(GoalStepEntity("goal",0,1,"단계",20)))
                SyncCapture.goal(db,"goal")
            }
            assertEquals(1,db.syncDao().pending().size)
            db.withTransaction { SyncCapture.goal(db,"goal") }
            assertEquals(1,db.syncDao().pending().size)
        } finally { db.close() }
    }
    @Test fun pulledConflictKeepsLocalDirtyRecordAndCursorCanCommit() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val id=UUID.randomUUID().toString()
            db.withTransaction {
                db.syncDao().ensureMeta()
                db.goalDao().insertGoal(GoalEntity("local","내 수정","",1,id))
                SyncCapture.goal(db,"local")
                val op=db.syncDao().pending().single()
                db.syncDao().updateOperation(op.copy(state="conflict"))
                val root=JSONObject().put("id",id).put("logical_id",id).put("book_id",id).put("type","book")
                    .put("name","다른 기기 수정").put("version",2).put("data",JSONObject())
                val event=JSONObject().put("id",UUID.randomUUID()).put("event_id",UUID.randomUUID()).put("sync_seq",1)
                    .put("result_status","applied").put("changes",org.json.JSONArray().put(JSONObject().put("entity","questbook").put("id",id).put("version",2).put("deleted",false).put("data",root)))
                SyncApply.event(db,event)
                db.syncDao().setMeta(db.syncDao().meta()!!.copy(cursor=1))
            }
            assertEquals("내 수정",db.syncDao().goal("local")!!.title)
            assertTrue(db.syncDao().record("questbook",id)!!.dirty)
            assertEquals(1L,db.syncDao().meta()!!.cursor)
            assertTrue(JSONObject(db.syncDao().record("questbook",id)!!.body).getString("name")=="다른 기기 수정")
        } finally { db.close() }
    }
    @Test fun allOlderMigrationChainsPreserveOriginalChecklist() = runBlocking {
        val entities=JSONObject(java.io.File("schemas/com.questyourlife.app.data.QuestDatabase/3.json").readText())
            .getJSONObject("database").getJSONArray("entities")
        for(version in 1..3) {
            val name="v$version-${UUID.randomUUID()}.db"
            context.openOrCreateDatabase(name,Context.MODE_PRIVATE,null).use { old ->
                for(i in 0 until entities.length()) {
                    val entity=entities.getJSONObject(i); val table=entity.getString("tableName")
                    if(version==1 && table!="quests") continue
                    if(version==2 && table !in setOf("quests","daily_quests")) continue
                    old.execSQL(entity.getString("createSql").replace("\${TABLE_NAME}",table))
                    val indexes=entity.optJSONArray("indices") ?: org.json.JSONArray()
                    for(j in 0 until indexes.length()) old.execSQL(indexes.getJSONObject(j).getString("createSql").replace("\${TABLE_NAME}",table))
                }
                old.execSQL("INSERT INTO quests(id,title,completed,createdAt) VALUES(7,'기존 체크',1,123)")
                if(version>=2) old.execSQL("INSERT INTO daily_quests VALUES('legacy','2026-11-01','보존','상세',20,1,100)")
                old.version=version
            }
            val db=Room.databaseBuilder(context,QuestDatabase::class.java,name).addMigrations(
                QuestDatabase.MIGRATION_1_2,QuestDatabase.MIGRATION_2_3,QuestDatabase.MIGRATION_3_4,QuestDatabase.MIGRATION_4_5).build()
            try {
                val task=db.syncDao().allQuick().single()
                assertEquals(7L,task.id);assertEquals("기존 체크",task.title);assertTrue(task.completed)
                UUID.fromString(task.syncId)
                if(version>=2) assertEquals(100L,db.syncDao().daily("legacy")!!.completedAt)
            } finally { db.close();context.deleteDatabase(name) }
        }
    }
    @Test fun receiptsAreIdempotentAndWrongAccountRollsBackPage() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val user=UUID.randomUUID().toString()
            db.syncDao().ensureMeta(SyncMeta(userId=user))
            val event=JSONObject().put("id",UUID.randomUUID()).put("event_id",UUID.randomUUID()).put("user_id",user)
                .put("sync_seq",1).put("cash_delta",20).put("xp_delta",20).put("result_status","applied").put("changes",org.json.JSONArray())
            repeat(2) { db.withTransaction { SyncApply.event(db,event) } }
            db.openHelper.readableDatabase.query("SELECT COUNT(*) FROM confirmed_events").use { it.moveToFirst();assertEquals(1,it.getInt(0)) }
            try {
                db.withTransaction {
                    db.syncDao().setMeta(db.syncDao().meta()!!.copy(cursor=10))
                    SyncApply.event(db,JSONObject(event.toString()).put("user_id",UUID.randomUUID()))
                }
                fail("foreign account event accepted")
            } catch(e:IllegalStateException) { assertEquals("event_owner_mismatch",e.message) }
            assertEquals(0L,db.syncDao().meta()!!.cursor)
        } finally { db.close() }
    }
    @Test fun cancelledExecutionCannotEraseCompletedStepHistory() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val step=GoalStepEntity("goal",0,1,"완료한 단계",20,100)
            db.syncDao().putSteps(listOf(step))
            val id=UUID.randomUUID().toString()
            val day=JSONObject().put("id",id).put("source_id",step.syncId).put("scheduled_date","2026-11-01")
                .put("status","cancelled").put("content_snapshot",JSONObject().put("title","이전 실행")).put("deleted_at",JSONObject.NULL)
            db.withTransaction { SyncApply.snapshot(db,org.json.JSONArray().put(JSONObject().put("entity","daily").put("id",id).put("version",1).put("data",day))) }
            assertEquals(100L,db.syncDao().stepBySync(step.syncId)!!.completedAt)
        } finally { db.close() }
    }

    @Test fun blockedTargetDoesNotStopIndependentCommands() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            db.syncDao().enqueue(SyncOperation(operation="goal_put",entity="questbook",targetId="blocked",localId="",body="{}",state="conflict"))
            repeat(30) { db.syncDao().enqueue(SyncOperation(operation="goal_put",entity="questbook",targetId="blocked",localId="",body="{}")) }
            db.syncDao().enqueue(SyncOperation(operation="quick_put",entity="quick",targetId="independent",localId="",body="{}"))
            assertEquals(listOf("independent"),SyncWorker.ready(db).map { it.targetId })
        } finally { db.close() }
    }
    @Test fun pendingStepCompletionProtectsLinkedDaily() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val step=GoalStepEntity("goal",0,1,"완료",20,100)
            val q=DailyQuestEntity("linked","2026-11-03","실행","",20,true,100,goalId="goal",stepPosition=0,stepSyncId=step.syncId)
            db.syncDao().putSteps(listOf(step));db.syncDao().putDaily(q)
            db.syncDao().enqueue(SyncOperation(operation="step_complete",entity="questbook",targetId=step.syncId,localId="goal",body="{}"))
            val body=JSONObject().put("id",q.syncId).put("source_id",step.syncId).put("scheduled_date",q.dateKey)
                .put("status","accepted").put("content_snapshot",JSONObject().put("title","과거 상태")).put("deleted_at",JSONObject.NULL)
            db.withTransaction { SyncApply.snapshot(db,org.json.JSONArray().put(JSONObject().put("entity","daily").put("id",q.syncId).put("version",1).put("data",body))) }
            assertEquals(100L,db.syncDao().daily("linked")!!.completedAt)
        } finally { db.close() }
    }
    @Test fun ownRewardAcknowledgementAdvancesOnlyMatchingUnsentSettings() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val user=UUID.randomUUID().toString()
            db.syncDao().ensureMeta();db.syncDao().setRecord(RemoteRecord("user",user,1))
            db.syncDao().enqueue(SyncOperation(operation="daily_complete",entity="daily",targetId="day",localId="day",body="{}"))
            db.syncDao().enqueue(SyncOperation(operation="settings_patch",entity="user",targetId=user,localId="",body="{}",baseVersion=1))
            val op=db.syncDao().pending().first()
            fun receipt(base:Long,version:Long)=JSONObject().put("id",UUID.randomUUID()).put("event_id",op.eventId).put("sync_seq",version)
                .put("result_status","applied").put("result",JSONObject().put("user_base_version",base)).put("changes",org.json.JSONArray().put(JSONObject()
                    .put("entity","user").put("id",user).put("version",version).put("data",JSONObject().put("id",user).put("cash",20).put("xp",20).put("inventory",JSONObject()))))
            db.withTransaction { SyncApply.event(db,receipt(1,2),op) }
            assertEquals(2L,db.syncDao().pending().single().baseVersion)
            db.withTransaction { SyncApply.event(db,receipt(3,4),op) }
            assertEquals(2L,db.syncDao().pending().single().baseVersion) // Another device intervened.
        } finally { db.close() }
    }
    @Test fun capturedProfileAndExistingAcceptedDailySurviveAccountSwitch() = runBlocking {
        val a="quests-${UUID.randomUUID()}.db";val b="quests-${UUID.randomUUID()}.db"
        val dbA=QuestDatabase.open(context,a);val dbB=QuestDatabase.open(context,b)
        try {
            val q=DailyQuestEntity("same-id",com.questyourlife.app.daily.DailyQuestRepository.today(),"A","",20,true,legacyOrigin=true)
            dbA.syncDao().putDaily(q);dbB.syncDao().putDaily(q.copy(title="B"))
            val repo=com.questyourlife.app.daily.DailyQuestRepository(context,a)
            ProfileRegistry.get(context).activate(b)
            repo.complete(q.id)
            assertNotNull(dbA.syncDao().daily(q.id)!!.completedAt)
            assertNull(dbB.syncDao().daily(q.id)!!.completedAt)
            dbA.withTransaction { SyncCapture.bootstrap(dbA) }
            val events=dbA.syncDao().pending()
            assertEquals(listOf("daily_put","daily_complete"),events.map { it.operation })
            assertEquals("accepted",JSONObject(events.first().body).getString("status"))
        } finally { dbA.close();dbB.close();context.deleteDatabase(a);context.deleteDatabase(b) }
    }

    @Test fun stepCompletionCapturesExistingLinkedExecutionBeforeMutation() = runBlocking {
        val file="quests-${UUID.randomUUID()}.db";ProfileRegistry.get(context).activate(file)
        val db=QuestDatabase.open(context,file)
        try {
            val goal=GoalEntity("real-goal","목표","",1)
            val step=GoalStepEntity(goal.id,0,1,"단계",20)
            db.goalDao().addGoal(goal,listOf(step))
            db.syncDao().putDaily(DailyQuestEntity("legacy-linked",com.questyourlife.app.daily.DailyQuestRepository.today(),"실행","",20,true,
                goalId=goal.id,stepPosition=0,stepSyncId=step.syncId,legacyOrigin=true))
            com.questyourlife.app.daily.GoalRepository(context).complete(step.syncId)
            db.withTransaction { SyncCapture.bootstrap(db) }
            val operations=db.syncDao().pending()
            assertEquals(listOf("goal_put","daily_put","step_complete"),operations.map { it.operation })
            assertEquals("accepted",JSONObject(operations[1].body).getString("status"))
        } finally { db.close();context.deleteDatabase(file) }
    }
    @Test fun oldOwnReceiptCannotRebaseSettingsOverAnExternalVersion() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,QuestDatabase::class.java).build()
        try {
            val user=UUID.randomUUID().toString()
            db.syncDao().ensureMeta();db.syncDao().setRecord(RemoteRecord("user",user,4))
            val op=SyncOperation(operation="settings_patch",entity="user",targetId=user,localId="",body="{}",baseVersion=1,sequence=1)
            db.syncDao().enqueue(op.copy(sequence=2,eventId=UUID.randomUUID().toString(),baseVersion=1))
            val old=JSONObject().put("id",UUID.randomUUID()).put("event_id",op.eventId).put("sync_seq",2).put("result_status","applied")
                .put("result",JSONObject().put("user_base_version",1)).put("changes",org.json.JSONArray().put(JSONObject().put("entity","user").put("id",user).put("version",2)
                    .put("data",JSONObject().put("id",user).put("cash",0).put("xp",0).put("inventory",JSONObject()))))
            db.withTransaction { SyncApply.event(db,old,op) }
            assertEquals(1L,db.syncDao().pending().single().baseVersion)
        } finally { db.close() }
    }

}
