package com.questyourlife.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
abstract class CompanionDao {
    @Query("SELECT * FROM companion_messages WHERE threadId = :thread ORDER BY createdAt, rowid")
    abstract fun watch(thread: String): Flow<List<CompanionMessageEntity>>
    @Query("SELECT * FROM companion_messages WHERE id = :id")
    abstract suspend fun get(id: String): CompanionMessageEntity?
    @Query("SELECT planTitle FROM companion_messages WHERE threadId = :thread AND planTitle IS NOT NULL ORDER BY createdAt DESC, rowid DESC LIMIT 1")
    abstract suspend fun latestPlanTitle(thread: String): String?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(message: CompanionMessageEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertGoal(goal: GoalEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertSteps(steps: List<GoalStepEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertDaily(quest: DailyQuestEntity)
    @Query("UPDATE companion_messages SET appliedGoalId = :goalId WHERE id = :id AND appliedGoalId IS NULL")
    abstract suspend fun markApplied(id: String, goalId: String)

    /** One transaction makes repeated taps, process death and retries harmless. */
    @Transaction
    open suspend fun applyPlan(id: String): String? {
        val message = get(id) ?: return null
        message.appliedGoalId?.let { return it }
        if (message.threadId != "mascot" || message.fromUser) return null
        val title = message.planTitle?.takeIf { it.isNotBlank() } ?: return null
        val actions = message.planSteps?.lines()?.filter { it.isNotBlank() } ?: return null
        if (actions.isEmpty()) return null
        val goalId = "companion-$id"
        insertGoal(GoalEntity(goalId, title, "로컬 도우미와 만든 계획", System.currentTimeMillis()))
        insertSteps(actions.mapIndexed { i, action -> GoalStepEntity(goalId, i, 1, action, 0) })
        insertDaily(DailyQuestEntity("$goalId-0", LocalDate.now().toString(), actions.first(), "${title} · 첫 실행", 20, goalId = goalId, stepPosition = 0))
        markApplied(id, goalId)
        return goalId
    }
}
