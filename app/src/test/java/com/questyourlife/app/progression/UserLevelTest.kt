package com.questyourlife.app.progression

import org.junit.Assert.*
import org.junit.Test

class UserLevelTest {
    @Test fun startsAtOneWithoutExperience() {
        assertEquals(1L, UserLevel.fromXp(-10).level)
        assertEquals(100L, UserLevel.fromXp(0).remainingXp)
    }
    @Test fun crossesBoundaryAndCarriesRemainder() {
        assertEquals(1L, UserLevel.fromXp(99).level)
        assertEquals(2L, UserLevel.fromXp(100).level)
        val result = UserLevel.fromXp(245)
        assertEquals(3L, result.level)
        assertEquals(45L, result.currentXp)
        assertEquals(55L, result.remainingXp)
    }
    @Test fun largeLifetimeTotalsDoNotOverflow() {
        val result = UserLevel.fromXp(Long.MAX_VALUE)
        assertTrue(result.level > 0)
        assertTrue(result.currentXp in 0..99)
    }
}
