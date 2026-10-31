package com.questyourlife.app.daily

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyQuestAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val app = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = DailyQuestRepository(app)
                val id = intent.getStringExtra(DailyQuestScheduler.EXTRA_ID)
                when (intent.action) {
                    DailyQuestScheduler.ACTION_DAILY -> {
                        DailyQuestScheduler.scheduleNext(app)
                        if (DailyQuestScheduler.enabled(app)) {
                            val day = repo.ensureToday()
                            DailyQuestNotifications.clearOld(app)
                            val quests = repo.snapshotDay(day)
                            val pending = quests.count { !it.accepted }
                            if (pending > 0) DailyQuestNotifications.proposals(app, pending)
                            quests.filter { it.accepted && it.completedAt == null }.forEach {
                                DailyQuestNotifications.active(app, it)
                            }
                        }
                    }
                    DailyQuestScheduler.ACTION_ACCEPT -> {
                        if (id != null) repo.accept(id)?.let { DailyQuestNotifications.active(app, it) }
                    }
                    DailyQuestScheduler.ACTION_COMPLETE -> {
                        if (id != null) repo.complete(id)?.let { DailyQuestNotifications.reward(app, it) }
                    }
                    DailyQuestScheduler.ACTION_SNOOZE -> {
                        if (id != null) {
                            val quest = repo.get(id)
                            if (quest != null &&
                                quest.dateKey == DailyQuestRepository.today() &&
                                quest.accepted && quest.completedAt == null
                            ) {
                                DailyQuestNotifications.dismissActive(app, id)
                                DailyQuestScheduler.snooze(app, id)
                            }
                        }
                    }
                    DailyQuestScheduler.ACTION_SNOOZE_DUE -> {
                        if (id != null) {
                            val quest = repo.get(id)
                            if (quest != null &&
                                quest.dateKey == DailyQuestRepository.today() &&
                                quest.accepted && quest.completedAt == null
                            ) DailyQuestNotifications.active(app, quest)
                        }
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
