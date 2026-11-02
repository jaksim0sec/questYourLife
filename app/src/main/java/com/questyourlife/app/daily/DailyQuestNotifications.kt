package com.questyourlife.app.daily

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.questyourlife.app.MainActivity
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object DailyQuestNotifications {
    private val dispatchMutex = Mutex()
    suspend fun <T> serialized(block: suspend () -> T): T = dispatchMutex.withLock { block() }
    private const val PROPOSALS = "daily_proposals"
    private const val ACTIVE = "active_daily_quests"
    private const val COMPLETED = "daily_quest_rewards"

    private fun manager(context: Context) = context.getSystemService(NotificationManager::class.java)

    fun setup(context: Context) {
        val manager = manager(context)
        manager.createNotificationChannel(NotificationChannel(PROPOSALS, "오늘의 퀘스트 제안", NotificationManager.IMPORTANCE_DEFAULT))
        manager.createNotificationChannel(NotificationChannel(ACTIVE, "진행 중인 퀘스트", NotificationManager.IMPORTANCE_LOW))
        manager.createNotificationChannel(NotificationChannel(COMPLETED, "퀘스트 완료", NotificationManager.IMPORTANCE_DEFAULT))
    }

    private fun allowed(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return false
        return manager(context).areNotificationsEnabled()
    }

    private fun openApp(context: Context): PendingIntent =
        PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun action(context: Context, id: String, type: String): PendingIntent =
        PendingIntent.getBroadcast(
            context, 0,
            Intent(context, DailyQuestAlarmReceiver::class.java)
                .setAction(type)
                .setData(android.net.Uri.parse("hyunsaengpatch://daily/$type/$id"))
                .putExtra(DailyQuestScheduler.EXTRA_ID, id),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun builder(context: Context, channel: String) =
        Notification.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_stat_quest)
            .setColor(0xFFD84340.toInt())
            .setContentIntent(openApp(context))
            .setAutoCancel(true)

    fun proposal(context: Context, quest: DailyQuestEntity) {
        if (!allowed(context)) return
        setup(context)
        val notification = builder(context, PROPOSALS)
            .setContentTitle("📬 오늘의 퀘스트 · ${quest.title}")
            .setContentText("${quest.detail} · 완료하면 +${quest.reward} C · +${quest.reward} XP")
            .setStyle(Notification.BigTextStyle().bigText("${quest.detail}\n오늘 해볼까요? 수락하거나 거절할 수 있어요."))
            .addAction(Notification.Action.Builder(null, "✅ 수락",
                action(context, quest.id, DailyQuestScheduler.ACTION_ACCEPT)).build())
            .addAction(Notification.Action.Builder(null, "🙅 거절",
                action(context, quest.id, DailyQuestScheduler.ACTION_REJECT)).build())
            .build()
        manager(context).notify(quest.id.hashCode(), notification)
    }

    fun active(context: Context, quest: DailyQuestEntity) {
        if (!allowed(context)) return
        setup(context)
        val notification = builder(context, ACTIVE)
            .setContentTitle("🏃 진행 중 · ${quest.title}")
            .setContentText("마쳤다면 완료를 눌러 +${quest.reward} C 받기")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .addAction(Notification.Action.Builder(
                null, "완료", action(context, quest.id, DailyQuestScheduler.ACTION_COMPLETE)
            ).build())
            .addAction(Notification.Action.Builder(
                null, "30분 후", action(context, quest.id, DailyQuestScheduler.ACTION_SNOOZE)
            ).build())
            .build()
        manager(context).notify(quest.id.hashCode(), notification)
    }

    fun dismissActive(context: Context, id: String) {
        manager(context).cancel(id.hashCode())
    }

    fun reward(context: Context, quest: DailyQuestEntity) {
        dismissActive(context, quest.id)
        if (!allowed(context)) return
        val notification = builder(context, COMPLETED)
            .setContentTitle("🎉 퀘스트 완료! +${quest.reward} C · +${quest.reward} XP")
            .setContentText(quest.title)
            .build()
        manager(context).notify(quest.id.hashCode(), notification)
    }

    fun clearOld(context: Context) {
        // All notifications belong to this app, not other apps.
        manager(context).cancelAll()
    }
}
