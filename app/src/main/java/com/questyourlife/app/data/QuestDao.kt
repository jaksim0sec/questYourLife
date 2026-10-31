package com.questyourlife.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests ORDER BY completed ASC, createdAt DESC")
    fun observeAll(): Flow<List<QuestEntity>>

    @Insert
    suspend fun insert(quest: QuestEntity)

    @Query("UPDATE quests SET completed = :completed WHERE id = :id")
    suspend fun setCompleted(id: Long, completed: Boolean)
}
