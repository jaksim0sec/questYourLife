package com.questyourlife.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quests WHERE dateKey = :dateKey AND dismissed = 0 ORDER BY id ASC")
    fun observeDay(dateKey: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests ORDER BY dateKey DESC, id ASC")
    fun observeHistory(): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE dateKey = :dateKey AND dismissed = 0 ORDER BY id ASC")
    suspend fun snapshotDay(dateKey: String): List<DailyQuestEntity>

    @Query("SELECT * FROM daily_quests WHERE id = :id AND dismissed = 0 LIMIT 1")
    suspend fun find(id: String): DailyQuestEntity?

    @Query("UPDATE daily_quests SET dateKey = :today WHERE dateKey < :today AND completedAt IS NULL AND dismissed = 0")
    suspend fun carryUnfinished(today: String): Int

    @Query("SELECT * FROM daily_quests WHERE goalId = :goalId AND stepPosition = :position AND dismissed = 0 AND completedAt IS NULL LIMIT 1")
    suspend fun activeForStep(goalId: String, position: Int): DailyQuestEntity?

    @Query("SELECT COUNT(*) FROM daily_quests WHERE dateKey = :dateKey")
    suspend fun countForDay(dateKey: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quests: List<DailyQuestEntity>)

    @Query("UPDATE daily_quests SET accepted = 1 WHERE id = :id AND dateKey = :dateKey AND accepted = 0 AND completedAt IS NULL AND dismissed = 0")
    suspend fun accept(id: String, dateKey: String): Int

    @Query("UPDATE daily_quests SET completedAt = :timestamp WHERE id = :id AND dateKey = :dateKey AND accepted = 1 AND completedAt IS NULL AND dismissed = 0")
    suspend fun complete(id: String, dateKey: String, timestamp: Long): Int

    @Query("UPDATE daily_quests SET dateKey = :tomorrow WHERE id = :id AND completedAt IS NULL AND dismissed = 0")
    suspend fun defer(id: String, tomorrow: String): Int

    @Query("UPDATE daily_quests SET dismissed = 1 WHERE id = :id AND completedAt IS NULL AND dismissed = 0")
    suspend fun abandon(id: String): Int

    @Query("UPDATE goal_steps SET completedAt = :timestamp WHERE completedAt IS NULL AND EXISTS (SELECT 1 FROM daily_quests d WHERE d.id = :id AND d.goalId = goal_steps.goalId AND d.stepPosition = goal_steps.position AND d.completedAt IS NOT NULL)")
    suspend fun advanceParent(id: String, timestamp: Long)

    @Transaction
    suspend fun completeAndAdvance(id: String, dateKey: String, timestamp: Long): Int {
        val changed = complete(id, dateKey, timestamp)
        if (changed == 1) advanceParent(id, timestamp)
        return changed
    }

    @Query("SELECT COALESCE(SUM(reward), 0) FROM daily_quests WHERE completedAt IS NOT NULL")
    fun observeEarnedCoins(): Flow<Int>

    @Query("SELECT COUNT(*) FROM daily_quests WHERE completedAt IS NOT NULL")
    fun observeCompletedCount(): Flow<Int>
}
