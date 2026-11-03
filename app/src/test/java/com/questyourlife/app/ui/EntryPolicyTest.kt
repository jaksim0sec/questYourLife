package com.questyourlife.app.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntryPolicyTest {
    @Test fun oldInstallSkipsOnboarding() {
        assertFalse(EntryPolicy.showOnboarding(completed = null, existedBeforeLaunch = true))
    }

    @Test fun freshInstallOpensOnboarding() {
        assertTrue(EntryPolicy.showOnboarding(completed = null, existedBeforeLaunch = false))
    }

    @Test fun incompleteFirstRunStillOpensAfterDatabaseWasCreated() {
        assertTrue(EntryPolicy.showOnboarding(completed = false, existedBeforeLaunch = true))
    }

    @Test fun finishedFirstRunOpensHome() {
        assertFalse(EntryPolicy.showOnboarding(completed = true, existedBeforeLaunch = false))
    }
}
