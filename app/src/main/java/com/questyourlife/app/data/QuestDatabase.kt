package com.questyourlife.app.data

import android.content.Context
import com.questyourlife.app.sync.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [QuestEntity::class, DailyQuestEntity::class, GoalEntity::class,
        GoalStepEntity::class, OwnedItemEntity::class, FocusSessionEntity::class, CompanionMessageEntity::class, SyncMeta::class, SyncOperation::class, RemoteRecord::class, ConfirmedEvent::class],
    version = 5, exportSchema = true
)
abstract class QuestDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao
    abstract fun dailyQuestDao(): DailyQuestDao
    abstract fun goalDao(): GoalDao
    abstract fun companionDao(): CompanionDao
    abstract fun syncDao(): SyncDao

    companion object {
        // Every migration is additive. Existing quests and rewards must survive updates.
        internal val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS daily_quests (
                        id TEXT NOT NULL PRIMARY KEY,
                        dateKey TEXT NOT NULL,
                        title TEXT NOT NULL,
                        detail TEXT NOT NULL,
                        reward INTEGER NOT NULL,
                        accepted INTEGER NOT NULL,
                        completedAt INTEGER
                    )
                """.trimIndent())
            }
        }

        internal val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS goals (id TEXT NOT NULL PRIMARY KEY, title TEXT NOT NULL, description TEXT NOT NULL, createdAt INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS goal_steps (goalId TEXT NOT NULL, position INTEGER NOT NULL, chapter INTEGER NOT NULL, title TEXT NOT NULL, reward INTEGER NOT NULL, completedAt INTEGER, PRIMARY KEY(goalId, position))")
                db.execSQL("CREATE TABLE IF NOT EXISTS owned_items (itemId INTEGER NOT NULL PRIMARY KEY, price INTEGER NOT NULL, purchasedAt INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS focus_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, finishedAt INTEGER NOT NULL, durationSeconds INTEGER NOT NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goal_steps_goalId ON goal_steps(goalId)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE daily_quests ADD COLUMN dismissed INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE daily_quests ADD COLUMN goalId TEXT")
                db.execSQL("ALTER TABLE daily_quests ADD COLUMN stepPosition INTEGER")
                db.execSQL("CREATE TABLE IF NOT EXISTS companion_messages (id TEXT NOT NULL PRIMARY KEY, threadId TEXT NOT NULL, body TEXT NOT NULL, fromUser INTEGER NOT NULL, createdAt INTEGER NOT NULL, planTitle TEXT, planSteps TEXT, appliedGoalId TEXT)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_companion_messages_threadId ON companion_messages(threadId)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                for (table in listOf("quests", "goals", "goal_steps", "daily_quests")) {
                    db.execSQL("ALTER TABLE $table ADD COLUMN syncId TEXT NOT NULL DEFAULT ''")
                    db.query("SELECT rowid FROM $table").use { rows ->
                        while (rows.moveToNext()) db.execSQL("UPDATE $table SET syncId=? WHERE rowid=?", arrayOf<Any>(java.util.UUID.randomUUID().toString(), rows.getLong(0)))
                    }
                    db.execSQL("CREATE UNIQUE INDEX index_${table}_syncId ON $table(syncId)")
                }
                db.execSQL("ALTER TABLE goals ADD COLUMN deletedAt INTEGER")
                db.execSQL("ALTER TABLE goal_steps ADD COLUMN deletedAt INTEGER")
                db.execSQL("CREATE TABLE stable_goal_steps(goalId TEXT NOT NULL,position INTEGER NOT NULL,chapter INTEGER NOT NULL,title TEXT NOT NULL,reward INTEGER NOT NULL,completedAt INTEGER,syncId TEXT NOT NULL,deletedAt INTEGER,PRIMARY KEY(syncId))")
                db.execSQL("INSERT INTO stable_goal_steps SELECT goalId,position,chapter,title,reward,completedAt,syncId,deletedAt FROM goal_steps")
                db.execSQL("CREATE INDEX index_stable_goal_steps_goalId ON stable_goal_steps(goalId)")
                db.execSQL("CREATE UNIQUE INDEX index_stable_goal_steps_syncId ON stable_goal_steps(syncId)")
                db.execSQL("ALTER TABLE daily_quests ADD COLUMN stepSyncId TEXT")
                db.execSQL("ALTER TABLE daily_quests ADD COLUMN legacyOrigin INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE daily_quests SET legacyOrigin=1")
                db.execSQL("UPDATE daily_quests SET stepSyncId=(SELECT syncId FROM goal_steps WHERE goalId=daily_quests.goalId AND position=daily_quests.stepPosition)")
                db.execSQL("CREATE INDEX index_daily_quests_stepSyncId ON daily_quests(stepSyncId)")
                db.execSQL("ALTER TABLE owned_items ADD COLUMN source TEXT NOT NULL DEFAULT 'legacy'")
                db.execSQL("CREATE TABLE sync_meta(id INTEGER NOT NULL PRIMARY KEY,userId TEXT,cursor INTEGER NOT NULL,epoch TEXT,confirmedCash INTEGER NOT NULL,confirmedXp INTEGER NOT NULL,legacyCash INTEGER NOT NULL,legacyXp INTEGER NOT NULL,appearance TEXT NOT NULL,prefsImported INTEGER NOT NULL,equipped INTEGER NOT NULL)")
                db.execSQL("INSERT INTO sync_meta SELECT 1,NULL,0,NULL,0,0,(SELECT coalesce(sum(reward),0) FROM daily_quests WHERE completedAt IS NOT NULL)+(SELECT coalesce(sum(reward),0) FROM goal_steps WHERE completedAt IS NOT NULL)-(SELECT coalesce(sum(price),0) FROM owned_items),(SELECT coalesce(sum(reward),0) FROM daily_quests WHERE completedAt IS NOT NULL)+(SELECT coalesce(sum(reward),0) FROM goal_steps WHERE completedAt IS NOT NULL),'system',0,0")
                db.execSQL("CREATE TABLE sync_outbox(sequence INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,eventId TEXT NOT NULL,operation TEXT NOT NULL,entity TEXT NOT NULL,targetId TEXT NOT NULL,localId TEXT NOT NULL,body TEXT NOT NULL,baseVersion INTEGER NOT NULL,sealedRequest TEXT,state TEXT NOT NULL,leaseUntil INTEGER NOT NULL,attempts INTEGER NOT NULL,error TEXT,conflictSnapshot TEXT)")
                db.execSQL("CREATE UNIQUE INDEX index_sync_outbox_eventId ON sync_outbox(eventId)")
                db.execSQL("CREATE INDEX index_sync_outbox_state_sequence ON sync_outbox(state,sequence)")
                db.execSQL("CREATE TABLE sync_remote_records(entity TEXT NOT NULL,id TEXT NOT NULL,version INTEGER NOT NULL,body TEXT NOT NULL,localHash TEXT,localId TEXT,dirty INTEGER NOT NULL,deleted INTEGER NOT NULL,PRIMARY KEY(entity,id))")
                db.execSQL("CREATE TABLE confirmed_events(id TEXT NOT NULL PRIMARY KEY,eventId TEXT NOT NULL,sequence INTEGER NOT NULL,cashDelta INTEGER NOT NULL,xpDelta INTEGER NOT NULL,status TEXT NOT NULL,body TEXT NOT NULL)")
                db.execSQL("CREATE UNIQUE INDEX index_confirmed_events_eventId ON confirmed_events(eventId)")
            }
        }

        private val instances = java.util.concurrent.ConcurrentHashMap<String, QuestDatabase>()
        fun get(context: Context): QuestDatabase = open(context, ProfileRegistry.get(context).file.value)
        fun open(context: Context, file: String): QuestDatabase {
            require(file.matches(Regex("quests(?:-[a-z0-9-]+)?\\.db"))) { "invalid_database_scope" }
            return instances[file] ?: synchronized(instances) {
                instances[file] ?: Room.databaseBuilder(context.applicationContext, QuestDatabase::class.java, file)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build().also { instances[file] = it }
            }
        }
    }
}
