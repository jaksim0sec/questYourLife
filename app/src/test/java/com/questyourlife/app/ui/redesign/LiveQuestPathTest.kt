package com.questyourlife.app.ui.redesign

import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.data.GoalEntity
import com.questyourlife.app.data.GoalStepEntity
import org.junit.Assert.*
import org.junit.Test

class LiveQuestPathTest {
    private val book = GoalEntity("my-book", "일본어 N3", "", 100, syncId = "remote-book")
    private val first = GoalStepEntity(book.id, 0, 1, "단어 복습", 20, completedAt = 300, syncId = "stable-one")
    private val next = GoalStepEntity(book.id, 1, 1, "일상 청해", 20, syncId = "stable-two")
    private val last = GoalStepEntity(book.id, 2, 2, "대화 연습", 20, syncId = "stable-three")

    @Test fun realStepsAreGroupedByTheirStoredChapters() {
        val path = LiveQuestPath.from(book, listOf(last, next, first))
        assertEquals("my-book", path.id)
        assertEquals("일본어 N3", path.title)
        assertEquals(listOf(1, 2), path.chapters.map { it.number })
        assertEquals(listOf("stable-one", "stable-two"), path.chapters[0].missions.map { it.id })
        assertEquals(3, path.total)
        assertEquals(1, path.done)
        assertEquals("stable-two", path.current()?.id)
    }

    @Test fun deletedOrForeignStepsNeverLeakIntoBook() {
        val deleted = GoalStepEntity(book.id, 4, 2, "삭제됨", 20, deletedAt = 999, syncId = "deleted")
        val foreign = GoalStepEntity("other", 0, 1, "다른 계정 목표", 20, syncId = "foreign")
        val path = LiveQuestPath.from(book, listOf(deleted, foreign, first))
        assertEquals(1, path.total)
        assertEquals(listOf("stable-one"), path.chapters.flatMap { it.missions }.map { it.id })
    }

    @Test fun linkedDailyUsesStableStepIdAndKeepsAcceptance() {
        val linked = DailyQuestEntity("daily-one", "2026-11-03", "청해", "", 20,
            accepted = true, goalId = book.id, stepPosition = 1, stepSyncId = next.syncId)
        val path = LiveQuestPath.from(book, listOf(first, next), listOf(linked))
        val mission = path.current()!!
        assertEquals(next.syncId, mission.id)
        assertEquals("daily-one", mission.dailyId)
        assertTrue(mission.dailyAccepted)
        assertFalse(mission.completed)
    }

    @Test fun emptyBookHasNoFabricatedChaptersOrRewards() {
        val path = LiveQuestPath.from(book, emptyList())
        assertTrue(path.chapters.isEmpty())
        assertNull(path.current())
        assertEquals(0, path.total)
        assertEquals(0, path.done)
    }

    @Test fun featuredBookPrefersActualUnfinishedGoal() {
        val finished = GoalEntity("done-book", "완주", "", 80)
        val finishedStep = GoalStepEntity(finished.id, 0, 1, "완료", 20, completedAt = 120)
        val result = LiveQuestPath.featured(listOf(finished, book),
            listOf(finishedStep, first, next))
        assertEquals(book.id, result?.id)
        assertEquals("stable-two", result?.current()?.id)
    }

    @Test fun emptyGuestHasNoFeaturedOrSeededDemoBook() {
        assertNull(LiveQuestPath.featured(emptyList(), emptyList()))
        val example = GoalEntity("example-japanese", "자동 생성 예시", "", 90)
        val untouched = GoalStepEntity(example.id, 0, 1, "시연", 20)
        assertNull(LiveQuestPath.featured(listOf(example), listOf(untouched)))
    }

    @Test fun chapterRouteResolvesOnlyAnActualStoredChapter() {
        val path = LiveQuestPath.from(book, listOf(first, next, last))
        assertEquals(2, path.chapterById("chapter-2")?.number)
        assertNull(path.chapterById("travel-2"))
        assertNull(path.chapterById("chapter-3"))
    }

    @Test fun completedBookHasNoPendingMission() {
        val path = LiveQuestPath.from(book, listOf(first, next.copy(completedAt = 400)))
        assertNull(path.current())
        assertEquals(2, path.done)
    }
}
