package com.questyourlife.app.companion

import org.junit.Assert.*
import org.junit.Test

class LocalPlanTemplatesTest {
    @Test fun japaneseStartsWithAnActionableAlphabetStep() {
        val plan = LocalPlanTemplates.draft("일본어로 대화하고 싶어. 하루 15분")
        assertEquals(15, plan.minutes)
        assertTrue(plan.actions.first().contains("あ행"))
        assertTrue(plan.actions.all { it.isNotBlank() })
    }
    @Test fun runningDoesNotReuseJapaneseSteps() {
        val plan = LocalPlanTemplates.draft("5km 달리기 하루 20분")
        assertTrue(plan.actions.first().contains("걷기"))
        assertFalse(plan.actions.any { it.contains("히라가나") })
    }
    @Test fun changingTimeKeepsTheExistingGoal() {
        val plan = LocalPlanTemplates.draft("오늘은 10분만", "일본어로 대화하기")
        assertEquals("일본어로 대화하기", plan.title)
        assertEquals(10, plan.minutes)
    }
    @Test fun unrelatedGoalUsesItsOwnTitleAndNoInventedExpertise() {
        val plan = LocalPlanTemplates.draft("방 정리하기")
        assertEquals("방 정리하기", plan.title)
        assertTrue(plan.actions.first().contains("방 정리하기"))
    }
    @Test(expected = IllegalArgumentException::class) fun blankIsRejected() {
        LocalPlanTemplates.draft("   ")
    }
    @Test(expected = IllegalArgumentException::class) fun zeroTimeIsRejected() {
        LocalPlanTemplates.draft("일본어 0분")
    }
}
