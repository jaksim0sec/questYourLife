package com.questyourlife.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.questyourlife.app.daily.DailyQuestNotifications
import com.questyourlife.app.daily.DailyQuestRepository
import com.questyourlife.app.daily.DailyQuestScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DailyQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application.applicationContext
    private val repo = DailyQuestRepository(app)
    private val dateKey = MutableStateFlow(DailyQuestRepository.today())

    val quests = dateKey.flatMapLatest(repo::observeDay).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val coins = repo.observeCoins().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val completedCount = repo.observeCompletionCount().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    var hour = androidx.compose.runtime.mutableIntStateOf(DailyQuestScheduler.hour(app))
        private set
    var minute = androidx.compose.runtime.mutableIntStateOf(DailyQuestScheduler.minute(app))
        private set
    var reminders = androidx.compose.runtime.mutableStateOf(DailyQuestScheduler.enabled(app))
        private set

    init {
        DailyQuestNotifications.setup(app)
        DailyQuestScheduler.scheduleNext(app)
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            dateKey.value = repo.ensureToday()
        }
    }

    fun accept(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.accept(id)?.let { DailyQuestNotifications.active(app, it) }
        }
    }

    fun complete(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.complete(id)?.let { DailyQuestNotifications.reward(app, it) }
        }
    }

    fun setTime(hour: Int, minute: Int) {
        DailyQuestScheduler.setTime(app, hour, minute)
        this.hour.intValue = hour
        this.minute.intValue = minute
    }

    fun setReminders(enabled: Boolean) {
        DailyQuestScheduler.setEnabled(app, enabled)
        reminders.value = enabled
    }
}
