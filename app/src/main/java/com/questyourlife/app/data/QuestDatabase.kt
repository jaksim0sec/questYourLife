package com.questyourlife.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [QuestEntity::class, DailyQuestEntity::class], version = 2, exportSchema = true)
abstract class QuestDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao
    abstract fun dailyQuestDao(): DailyQuestDao

    companion object {
        // Preserve the existing quests table and data when upgrading v1 -> v2.
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

        @Volatile private var instance: QuestDatabase? = null

        fun get(context: Context): QuestDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuestDatabase::class.java,
                    "quests.db"
                ).addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}
