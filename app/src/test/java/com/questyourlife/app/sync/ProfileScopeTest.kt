package com.questyourlife.app.sync

import org.junit.Assert.*
import org.junit.Test

class ProfileScopeTest {
    @Test fun oldNotificationCannotChangeAnotherAccountsTask() {
        assertFalse(ProfileScope.permitsAction("quests-user-a.db","quests-user-b.db"))
        assertFalse(ProfileScope.permitsAction(null,"quests-user-b.db"))
        assertTrue(ProfileScope.permitsAction("quests-user-b.db","quests-user-b.db"))
        assertTrue(ProfileScope.permitsAction(null,"quests.db"))
    }
}
