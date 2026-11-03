package com.questyourlife.app.sync

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.questyourlife.app.data.GoalEntity
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class ProfileBindingTest {
    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test fun existingUserKeepsGuestUnbound() = runBlocking {
        val guestFile = "quests-guest-${UUID.randomUUID()}.db"
        val user = UUID.randomUUID().toString()
        val registry = ProfileRegistry.get(context)
        registry.activate(guestFile)
        val guest = QuestDatabase.open(context, guestFile)
        guest.goalDao().insertGoal(GoalEntity("guest-goal", "내 목표", "", 100))
        val (accountDb, accountFile) = registry.bind(user, importGuest = false)
        try {
            assertNotEquals(guestFile, accountFile)
            assertNull(guest.syncDao().meta()?.userId)
            assertEquals(1, guest.goalDao().countGoals())
            assertEquals(0, accountDb.goalDao().countGoals())
            assertEquals(user, accountDb.syncDao().meta()?.userId)
        } finally {
            registry.guest()
        }
    }

    @Test fun explicitFirstTimeImportRetainsLegacyData() = runBlocking {
        val guestFile = "quests-guest-${UUID.randomUUID()}.db"
        val user = UUID.randomUUID().toString()
        val registry = ProfileRegistry.get(context)
        registry.activate(guestFile)
        val guest = QuestDatabase.open(context, guestFile)
        guest.goalDao().insertGoal(GoalEntity("guest-goal", "기존 기록", "", 100))
        try {
            val (accountDb, accountFile) = registry.bind(user, importGuest = true)
            assertEquals(guestFile, accountFile)
            assertEquals(user, accountDb.syncDao().meta()?.userId)
            assertEquals(1, accountDb.goalDao().countGoals())
        } finally {
            registry.guest()
        }
    }

    @Test fun twoUsersHaveSeparateDatabases() = runBlocking {
        val registry = ProfileRegistry.get(context)
        registry.guest()
        val a = UUID.randomUUID().toString()
        val b = UUID.randomUUID().toString()
        val (dbA, fileA) = registry.bind(a, importGuest = false)
        dbA.goalDao().insertGoal(GoalEntity("private-a", "A-only", "", 100))
        registry.activate(fileA)
        val (dbB, fileB) = registry.bind(b, importGuest = false)
        try {
            assertNotEquals(fileA, fileB)
            assertEquals(1, dbA.goalDao().countGoals())
            assertEquals(0, dbB.goalDao().countGoals())
        } finally {
            registry.guest()
        }
    }

    @Test fun guestRecordsAreAccessibleAgainAfterLogout() = runBlocking {
        val registry = ProfileRegistry.get(context)
        val guestFile = "quests-guest-${UUID.randomUUID()}.db"
        val uid = UUID.randomUUID().toString()
        registry.activate(guestFile)
        val guest = QuestDatabase.open(context, guestFile)
        guest.goalDao().insertGoal(GoalEntity("guest-return", "돌아올 기록", "", 100))
        val (_, accountFile) = registry.bind(uid, importGuest = false)
        registry.activate(accountFile)
        registry.guest()
        assertEquals(guestFile, registry.file.value)
        assertEquals(1, QuestDatabase.open(context, registry.file.value).goalDao().countGoals())
    }

    @Test fun importedAccountRecordsCannotBeReopenedAsGuest() = runBlocking {
        val registry = ProfileRegistry.get(context)
        val file = "quests-guest-${UUID.randomUUID()}.db"
        val user = UUID.randomUUID().toString()
        registry.activate(file)
        val (_, boundFile) = registry.bind(user, importGuest = true)
        registry.activate(boundFile)
        registry.guest()
        assertNotEquals(boundFile, registry.file.value)
    }

    @Test fun accountMappingCanBeRecoveredFromBoundDatabase() = runBlocking {
        val registry = ProfileRegistry.get(context)
        registry.guest()
        val user = UUID.randomUUID().toString()
        val (_, file) = registry.bind(user, importGuest = false)
        registry.activate(file)
        context.getSharedPreferences("qyl_profiles", Context.MODE_PRIVATE).edit()
            .remove("user:$user").commit()
        try {
            assertEquals(file, registry.bind(user, importGuest = false).second)
        } finally {
            registry.guest()
        }
    }

    @Test fun repeatedBindIsIdempotent() = runBlocking {
        val registry = ProfileRegistry.get(context)
        registry.guest()
        val user = UUID.randomUUID().toString()
        val (_, original) = registry.bind(user, importGuest = false)
        val (_, again) = registry.bind(user, importGuest = false)
        assertEquals(original, again)
        registry.guest()
    }
}
