package com.questyourlife.app.daily

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyQuestAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val scope=intent.getStringExtra("qyl_profile")
        val active=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value
        if(intent.action!=DailyQuestScheduler.ACTION_DAILY && !com.questyourlife.app.sync.ProfileScope.permitsAction(scope,active)) return
        val pendingResult = goAsync()
        val app = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = DailyQuestRepository(app,active)
                val id = intent.getStringExtra(DailyQuestScheduler.EXTRA_ID)
                DailyQuestNotifications.serialized {
                if(com.questyourlife.app.sync.ProfileRegistry.get(app).file.value!=active) return@serialized
                when (intent.action) {
                    DailyQuestScheduler.ACTION_DAILY -> {
                        DailyQuestScheduler.scheduleNext(app)
                        if (DailyQuestScheduler.enabled(app)) {
                            val day = repo.ensureToday()
                            DailyQuestNotifications.clearOld(app,active)
                            val quests = repo.snapshotDay(day)
                            quests.filter { !it.accepted && it.completedAt == null }.forEach {
                                DailyQuestNotifications.proposal(app, it, active)
                            }
                            quests.filter { it.accepted && it.completedAt == null }.forEach {
                                DailyQuestNotifications.active(app, it, active)
                            }
                        }
                    }
                    DailyQuestScheduler.ACTION_ACCEPT -> {
                        if (id != null) repo.accept(id)?.let { DailyQuestNotifications.active(app, it, active) }
                    }
                    DailyQuestScheduler.ACTION_REJECT -> {
                        if (id != null && repo.reject(id) == 1) DailyQuestNotifications.dismissActive(app, id, active)
                    }
                    DailyQuestScheduler.ACTION_COMPLETE -> {
                        if (id != null) repo.complete(id)?.let { DailyQuestNotifications.reward(app, it, active) }
                    }
                    DailyQuestScheduler.ACTION_SNOOZE -> {
                        if (id != null) {
                            val quest = repo.get(id)
                            if (quest != null &&
                                quest.dateKey == DailyQuestRepository.today() &&
                                quest.accepted && quest.completedAt == null
                            ) {
                                DailyQuestNotifications.dismissActive(app, id, active)
                                DailyQuestScheduler.snooze(app, id, active)
                            }
                        }
                    }
                    DailyQuestScheduler.ACTION_SNOOZE_DUE -> {
                        if (id != null) {
                            val quest = repo.get(id)
                            if (quest != null &&
                                quest.dateKey == DailyQuestRepository.today() &&
                                quest.accepted && quest.completedAt == null
                            ) DailyQuestNotifications.active(app, quest, active)
                        }
                    }
                }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
