package com.questyourlife.app.ui.redesign

import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.data.GoalEntity
import com.questyourlife.app.data.GoalStepEntity

/** UI projection only. Room entities own progress, and server receipts own confirmed rewards. */
data class LivePathMission(
    val id: String,
    val title: String,
    val completed: Boolean,
    val reward: Int,
    val dailyId: String?,
    val dailyAccepted: Boolean
)

data class LivePathChapter(
    val number: Int,
    val title: String,
    val missions: List<LivePathMission>
) {
    val id: String get() = "chapter-$number"
    val done: Int get() = missions.count { it.completed }
    val complete: Boolean get() = missions.isNotEmpty() && done == missions.size
}

data class LivePathBook(val id: String, val title: String, val chapters: List<LivePathChapter>) {
    val total: Int get() = chapters.sumOf { it.missions.size }
    val done: Int get() = chapters.sumOf { it.done }
    val progress: Float get() = if (total == 0) 0f else done.toFloat() / total
    fun current(): LivePathMission? = chapters.asSequence()
        .flatMap { it.missions.asSequence() }.firstOrNull { !it.completed }
    fun currentChapter(): LivePathChapter? = chapters.firstOrNull { !it.complete } ?: chapters.lastOrNull()
    fun chapterById(id: String?): LivePathChapter? = chapters.firstOrNull { it.id == id }
}

/** Preserve the persisted chapter number and stable step id; do not invent extra missions. */
object LiveQuestPath {
    /** Hide the untouched legacy example but keep any user-completed records accessible. */
    fun visibleBooks(goals: List<GoalEntity>, steps: List<GoalStepEntity>,
                     daily: List<DailyQuestEntity> = emptyList()): List<LivePathBook> =
        goals.filter { goal -> goal.id != "example-japanese" ||
            steps.any { it.goalId == goal.id && it.completedAt != null && it.deletedAt == null } }
            .map { from(it, steps, daily) }

    fun featured(goals: List<GoalEntity>, steps: List<GoalStepEntity>,
                 daily: List<DailyQuestEntity> = emptyList()): LivePathBook? {
        val paths = visibleBooks(goals, steps, daily)
        return paths.firstOrNull { it.current() != null } ?: paths.firstOrNull()
    }

    fun from(goal: GoalEntity, steps: List<GoalStepEntity>,
             daily: List<DailyQuestEntity> = emptyList()): LivePathBook {
        val sorted = steps.asSequence()
            .filter { it.goalId == goal.id && it.deletedAt == null }
            .sortedWith(compareBy<GoalStepEntity> { it.chapter }.thenBy { it.position })
            .toList()
        val chapters = sorted.groupBy { it.chapter }.map { (number, members) ->
            LivePathChapter(number, "챕터 $number", members.map { step ->
                val activeDaily = daily.firstOrNull {
                    !it.dismissed && it.completedAt == null && it.stepSyncId == step.syncId
                } ?: daily.firstOrNull {
                    !it.dismissed && it.completedAt == null && it.stepSyncId == null &&
                        it.goalId == goal.id && it.stepPosition == step.position
                }
                LivePathMission(
                    id = step.syncId,
                    title = step.title,
                    completed = step.completedAt != null,
                    reward = step.reward,
                    dailyId = activeDaily?.id,
                    dailyAccepted = activeDaily?.accepted ?: false
                )
            })
        }
        return LivePathBook(goal.id, goal.title, chapters)
    }
}
