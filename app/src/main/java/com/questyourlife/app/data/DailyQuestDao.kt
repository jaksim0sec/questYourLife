package com.questyourlife.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quests WHERE dateKey = :dateKey ORDER BY id ASC")
    fun observeDay(dateKey: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests ORDER BY dateKey DESC, id ASC")
    fun observeHistory(): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE dateKey = :dateKey ORDER BY id ASC")
    suspend fun snapshotDay(dateKey: String): List<DailyQuestEntity>

    @Query("SELECT * FROM daily_quests WHERE id = :id LIMIT 1")
    suspend fun find(id: String): DailyQuestEntity?

    @Query("SELECT COUNT(*) FROM daily_quests WHERE dateKey = :dateKey")
    suspend fun countForDay(dateKey: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quests: List<DailyQuestEntity>)

    @Query("UPDATE daily_quests SET accepted = 1 WHERE id = :id AND dateKey = :dateKey AND accepted = 0 AND completedAt IS NULL")
    suspend fun accept(id: String, dateKey: String): Int

    @Query("UPDATE daily_quests SET completedAt = :timestamp WHERE id = :id AND dateKey = :dateKey AND accepted = 1 AND completedAt IS NULL")
    suspend fun complete(id: String, dateKey: String, timestamp: Long): Int

    @Query("SELECT COALESCE(SUM(reward), 0) FROM daily_quests WHERE completedAt IS NOT NULL")
    fun observeEarnedCoins(): Flow<Int>

    @Query("SELECT COUNT(*) FROM daily_quests WHERE completedAt IS NOT NULL")
    fun observeCompletedCount(): Flow<Int>
}
