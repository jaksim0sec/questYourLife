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
    @Query("SELECT * FROM goals WHERE deletedAt IS NULL ORDER BY createdAt DESC")
    fun watchGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM stable_goal_steps ORDER BY goalId, position")
    fun watchSteps(): Flow<List<GoalStepEntity>>

    @Query("SELECT s.* FROM stable_goal_steps s JOIN goals g ON g.id = s.goalId WHERE g.id LIKE 'companion-%' AND g.deletedAt IS NULL AND s.deletedAt IS NULL AND s.completedAt IS NULL AND NOT EXISTS (SELECT 1 FROM stable_goal_steps earlier WHERE earlier.goalId = s.goalId AND earlier.position < s.position AND earlier.deletedAt IS NULL AND earlier.completedAt IS NULL) AND NOT EXISTS (SELECT 1 FROM daily_quests d WHERE (d.stepSyncId = s.syncId OR (d.stepSyncId IS NULL AND d.goalId = s.goalId AND d.stepPosition = s.position)) AND d.dismissed = 0 AND d.completedAt IS NULL)")
    suspend fun nextCompanionSteps(): List<GoalStepEntity>

    @Query("SELECT * FROM owned_items")
    suspend fun watchItemsSnapshot(): List<OwnedItemEntity>

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

    @Query("SELECT * FROM stable_goal_steps WHERE goalId = :id AND deletedAt IS NULL ORDER BY position")
    suspend fun getSteps(id: String): List<GoalStepEntity>

    @Query("SELECT * FROM stable_goal_steps WHERE goalId=:id ORDER BY position") suspend fun getAllSteps(id: String): List<GoalStepEntity>
    @Query("SELECT * FROM stable_goal_steps WHERE syncId=:id AND deletedAt IS NULL") suspend fun step(id: String): GoalStepEntity?
    @Query("UPDATE stable_goal_steps SET completedAt=:now WHERE syncId=:id AND completedAt IS NULL AND deletedAt IS NULL") suspend fun markStable(id: String,now: Long): Int
    @Query("UPDATE daily_quests SET accepted=1,completedAt=:now,dateKey=:day WHERE stepSyncId=:id AND completedAt IS NULL AND dismissed=0") suspend fun finishStableDaily(id: String,now: Long,day: String)
    @Query("SELECT * FROM daily_quests WHERE stepSyncId=:id AND completedAt IS NULL AND dismissed=0 LIMIT 1") suspend fun stableDaily(id: String): DailyQuestEntity?
    @Transaction suspend fun completeStable(id: String): GoalCompletion? {
        val target=step(id) ?: return null
        if(target.completedAt!=null || getSteps(target.goalId).any { it.position<target.position && it.completedAt==null }) return null
        val linked=stableDaily(id) ?: linkedDaily(target.goalId,target.position)
        val now=System.currentTimeMillis()
        if(markStable(id,now)!=1) return null
        finishStableDaily(id,now,LocalDate.now().toString())
        if(linked?.stepSyncId==null) finishLinkedDaily(target.goalId,target.position,now,LocalDate.now().toString())
        return GoalCompletion(target.reward+(linked?.reward ?: 0),linked?.id)
    }

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoal(id: String)

    @Query("DELETE FROM stable_goal_steps WHERE goalId = :id")
    suspend fun deleteSteps(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: OwnedItemEntity)

    @Query("SELECT COALESCE(SUM(reward), 0) FROM stable_goal_steps WHERE completedAt IS NOT NULL")
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
        if (getAllSteps(id).any { it.completedAt != null }) return
        deleteSteps(id)
        deleteGoal(id)
    }

    @Query("UPDATE daily_quests SET accepted = 1, completedAt = :now, dateKey = :dateKey WHERE stepSyncId IS NULL AND goalId = :goalId AND stepPosition = :position AND completedAt IS NULL AND dismissed = 0")
    suspend fun finishLinkedDaily(goalId: String, position: Int, now: Long, dateKey: String)

    @Transaction
    suspend fun completeStep(id: String, position: Int): Int {
        val target=getSteps(id).firstOrNull { it.position==position } ?: return 0
        return if(completeStable(target.syncId)!=null) 1 else 0
    }

    @Query("SELECT * FROM daily_quests WHERE stepSyncId IS NULL AND goalId = :id AND stepPosition = :position AND completedAt IS NULL AND dismissed = 0 LIMIT 1")
    suspend fun linkedDaily(id: String, position: Int): DailyQuestEntity?

    @Transaction
    suspend fun completeStepWithReward(id: String, position: Int): GoalCompletion? {
        val target=getSteps(id).firstOrNull { it.position==position } ?: return null
        return completeStable(target.syncId)
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
