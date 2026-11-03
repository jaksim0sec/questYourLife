package com.questyourlife.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.data.QuestEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.room.withTransaction
import com.questyourlife.app.sync.*

class QuestViewModel(application: Application) : AndroidViewModel(application) {
    private val app=application.applicationContext
    private val db=QuestDatabase.get(app)
    private val dao = db.questDao()

    val quests = dao.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addQuest(title: String) {
        val clean = title.trim()
        if (clean.isEmpty() || clean.length > 120) return
        viewModelScope.launch {
            db.withTransaction { val q=QuestEntity(title=clean); dao.insert(q); SyncCapture.quick(db,db.syncDao().quick(q.syncId)!!) }
            SyncScheduler.enqueue(app)
        }
    }

    fun toggleQuest(quest: QuestEntity) {
        viewModelScope.launch {
            db.withTransaction { dao.setCompleted(quest.id,!quest.completed); SyncCapture.quick(db,db.syncDao().quick(quest.syncId)!!) }
            SyncScheduler.enqueue(app)
        }
    }
}
