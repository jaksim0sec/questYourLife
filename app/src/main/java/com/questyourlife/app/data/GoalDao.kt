package com.questyourlife.app.data

import java.time.LocalDate
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class GoalCompletion(val reward: Int, val dailyId: String?)

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun watchGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goal_steps ORDER BY goalId, position")
    fun watchSteps(): Flow<List<GoalStepEntity>>

    @Query("SELECT s.* FROM goal_steps s JOIN goals g ON g.id = s.goalId WHERE g.id LIKE 'companion-%' AND s.completedAt IS NULL AND NOT EXISTS (SELECT 1 FROM goal_steps earlier WHERE earlier.goalId = s.goalId AND earlier.position < s.position AND earlier.completedAt IS NULL) AND NOT EXISTS (SELECT 1 FROM daily_quests d WHERE d.goalId = s.goalId AND d.stepPosition = s.position AND d.dismissed = 0 AND d.completedAt IS NULL)")
    suspend fun nextCompanionSteps(): List<GoalStepEntity>

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
        // until history preservation is designed, to avoid silently altering earned cash.
        if (getSteps(id).any { it.completedAt != null }) return
        deleteSteps(id)
        deleteGoal(id)
    }

    @Query("UPDATE daily_quests SET accepted = 1, completedAt = :now, dateKey = :dateKey WHERE goalId = :goalId AND stepPosition = :position AND completedAt IS NULL AND dismissed = 0")
    suspend fun finishLinkedDaily(goalId: String, position: Int, now: Long, dateKey: String)

    @Transaction
    suspend fun completeStep(id: String, position: Int): Int {
        val steps = getSteps(id)
        val step = steps.firstOrNull { it.position == position && it.completedAt == null } ?: return 0
        if (steps.any { it.position < position && it.completedAt == null }) return 0
        val now = System.currentTimeMillis()
        val changed = markComplete(id, step.position, now)
        if (changed == 1) finishLinkedDaily(id, step.position, now, LocalDate.now().toString())
        return changed
    }

    @Query("SELECT * FROM daily_quests WHERE goalId = :id AND stepPosition = :position AND completedAt IS NULL AND dismissed = 0 LIMIT 1")
    suspend fun linkedDaily(id: String, position: Int): DailyQuestEntity?

    @Transaction
    suspend fun completeStepWithReward(id: String, position: Int): GoalCompletion? {
        val target = getSteps(id).firstOrNull { it.position == position } ?: return null
        val linked = linkedDaily(id, position)
        if (completeStep(id, position) != 1) return null
        return GoalCompletion(target.reward + (linked?.reward ?: 0), linked?.id)
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
