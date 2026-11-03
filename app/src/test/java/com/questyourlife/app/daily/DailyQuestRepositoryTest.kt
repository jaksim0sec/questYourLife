package com.questyourlife.app.daily

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[28], manifest=Config.NONE)
class DailyQuestRepositoryTest {
    private val context: Context get() = ApplicationProvider.getApplicationContext()

    @Test fun emptyGuestHasNoFabricatedDailyRewards() = runBlocking {
        val file = "quests-empty-${UUID.randomUUID()}.db"
        val repository = DailyQuestRepository(context, file)
        assertTrue(repository.snapshotDay(repository.ensureToday()).isEmpty())
    }

    @Test fun existingAcceptedDailySurvivesRefresh() = runBlocking {
        val file = "quests-manual-${UUID.randomUUID()}.db"
        val repository = DailyQuestRepository(context, file)
        val date = DailyQuestRepository.today()
        val entry = DailyQuestEntity("real-quest", date, "진짜 할 일", "수동 생성", 20, accepted=true)
        QuestDatabase.open(context,file).syncDao().putDaily(entry)
        repository.ensureToday()
        val entries = repository.snapshotDay(date)
        assertEquals(1, entries.size)
        assertEquals("real-quest", entries.single().id)
        assertTrue(entries.single().accepted)
    }
}
