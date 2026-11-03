package com.questyourlife.app.sync

import org.junit.Assert.*
import org.junit.Test

class SyncContractTest {
    @Test fun pendingRewardsCannotBeSpentAsConfirmedCash() {
        val balance = CashBalances(legacy = 1000, pending = 500, confirmed = 40)
        assertFalse(balance.canSpend(80))
        assertTrue(balance.canSpend(40))
        assertFalse(balance.canSpend(-1))
    }
    @Test fun namesNormalizeButPasswordsAreNeverNormalized() {
        assertEquals("my_id", AccountInput.username(" My_ID "))
        assertNull(AccountInput.username("이모리"))
        assertNull(AccountInput.username("../admin"))
        assertFalse(AccountInput.validPassword("123"))
        assertTrue(AccountInput.validPassword("  a pass phrase  "))
    }
    @Test fun stableIdsDoNotChangeOnRetry() {
        assertEquals(StableIds.child("goal-1", "chapter:1"), StableIds.child("goal-1", "chapter:1"))
        assertNotEquals(StableIds.child("goal-1", "chapter:1"), StableIds.child("goal-2", "chapter:1"))
    }
}
