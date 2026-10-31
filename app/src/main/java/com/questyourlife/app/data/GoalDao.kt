package com.questyourlife.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun watchGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goal_steps ORDER BY goalId, position")
    fun watchSteps(): Flow<List<GoalStepEntity>>

    @Query("SELECT * FROM owned_items")
    fun watchItems(): Flow<List<OwnedItemEntity>>

    @Query("SELECT * FROM focus_sessions ORDER BY finishedAt DESC")
    fun watchSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT COUNT(*) FROM goals")
    suspend fun countGoals(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGoal(goal: GoalEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(steps: List<GoalStepEntity>)

    @Query("SELECT * FROM goal_steps WHERE goalId = :id ORDER BY position")
    suspend fun getSteps(id: String): List<GoalStepEntity>

    @Query("UPDATE goal_steps SET completedAt = :now WHERE goalId = :id AND position = :position AND completedAt IS NULL")
    suspend fun markComplete(id: String, position: Int, now: Long): Int

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoal(id: String)

    @Query("DELETE FROM goal_steps WHERE goalId = :id")
    suspend fun deleteSteps(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: OwnedItemEntity)

    @Query("SELECT COALESCE(SUM(reward), 0) FROM goal_steps WHERE completedAt IS NOT NULL")
    suspend fun mainRewards(): Int

    @Query("SELECT COALESCE(SUM(reward), 0) FROM daily_quests WHERE completedAt IS NOT NULL")
    suspend fun dailyRewards(): Int

    @Query("SELECT COALESCE(SUM(price), 0) FROM owned_items")
    suspend fun purchases(): Int

    @Insert
    suspend fun insertSession(session: FocusSessionEntity)

    @Transaction
    suspend fun addGoal(goal: GoalEntity, steps: List<GoalStepEntity>) {
        insertGoal(goal)
        insertSteps(steps)
    }

    @Transaction
    suspend fun removeGoal(id: String) {
        // Completed steps carry earned currency. Do not allow deleting a completed goal
        // until history preservation is designed, to avoid silently altering earned coins.
        if (getSteps(id).any { it.completedAt != null }) return
        deleteSteps(id)
        deleteGoal(id)
    }

    @Transaction
    suspend fun completeStep(id: String, position: Int): Int {
        val steps = getSteps(id)
        val step = steps.firstOrNull { it.position == position && it.completedAt == null } ?: return 0
        if (steps.any { it.position < position && it.completedAt == null }) return 0
        return markComplete(id, step.position, System.currentTimeMillis())
    }

    @Transaction
    suspend fun buyItem(itemId: Int, price: Int): Boolean {
        if (price < 0 || (dailyRewards() + mainRewards() - purchases()) < price) return false
        val before = currentOwned(itemId)
        if (before) return false
        insertItem(OwnedItemEntity(itemId, price, System.currentTimeMillis()))
        return true
    }

    @Query("SELECT COUNT(*) > 0 FROM owned_items WHERE itemId = :itemId")
    suspend fun currentOwned(itemId: Int): Boolean
}
