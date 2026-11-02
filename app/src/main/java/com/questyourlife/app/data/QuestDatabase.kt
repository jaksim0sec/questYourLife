package com.questyourlife.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [QuestEntity::class, DailyQuestEntity::class, GoalEntity::class,
        GoalStepEntity::class, OwnedItemEntity::class, FocusSessionEntity::class, CompanionMessageEntity::class],
    version = 4, exportSchema = true
)
abstract class QuestDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao
    abstract fun dailyQuestDao(): DailyQuestDao
    abstract fun goalDao(): GoalDao
    abstract fun companionDao(): CompanionDao

    companion object {
        // Every migration is additive. Existing quests and rewards must survive updates.
        private val MIGRATION_1_2 = object : Migration(1, 2) {
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
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

        @Volatile private var instance: QuestDatabase? = null
        fun get(context: Context): QuestDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, QuestDatabase::class.java, "quests.db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build().also { instance = it }
        }
    }
}
