package com.questyourlife.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.data.QuestEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = QuestDatabase.get(application).questDao()

    val quests = dao.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addQuest(title: String) {
        val clean = title.trim()
        if (clean.isEmpty() || clean.length > 120) return
        viewModelScope.launch { dao.insert(QuestEntity(title = clean)) }
    }

    fun toggleQuest(quest: QuestEntity) {
        viewModelScope.launch { dao.setCompleted(quest.id, !quest.completed) }
    }
}
