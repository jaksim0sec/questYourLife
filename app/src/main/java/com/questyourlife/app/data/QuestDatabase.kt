package com.questyourlife.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestEntity::class], version = 1, exportSchema = true)
abstract class QuestDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile private var instance: QuestDatabase? = null

        fun get(context: Context): QuestDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuestDatabase::class.java,
                    "quests.db"
                ).build().also { instance = it }
            }
    }
}
