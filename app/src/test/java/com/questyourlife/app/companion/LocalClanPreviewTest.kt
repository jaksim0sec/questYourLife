package com.questyourlife.app.companion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalClanPreviewTest {
    @Test fun previewWeeksHaveConsistentMembersAndContributions() {
        for (previous in listOf(false, true)) {
            val clan = LocalClanPreview.week(previous)
            assertTrue(clan.memberCount in 1..10)
            assertEquals(clan.memberCount, clan.members.size)
            assertEquals(clan.contributorCount, clan.members.count { it.cash > 0 })
            assertEquals(clan.cash, clan.members.sumOf { it.cash })
        }
    }
}
