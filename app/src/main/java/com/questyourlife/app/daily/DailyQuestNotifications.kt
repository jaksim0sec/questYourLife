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

    private fun action(context: Context, id: String, type: String, profile: String): PendingIntent =
        PendingIntent.getBroadcast(
            context, 0,
            Intent(context, DailyQuestAlarmReceiver::class.java)
                .setAction(type)
                .setData(android.net.Uri.Builder().scheme("hyunsaengpatch").authority("daily").appendPath(profile).appendPath(type).appendPath(id).build())
                .putExtra(DailyQuestScheduler.EXTRA_ID, id)
                .putExtra("qyl_profile",profile),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun builder(context: Context, channel: String) =
        Notification.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_stat_quest)
            .setColor(com.questyourlife.app.brand.BrandColor.RED.toInt())
            .setContentIntent(openApp(context))
            .setAutoCancel(true)

    fun proposal(context: Context, quest: DailyQuestEntity, profile: String = com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) {
        if(profile!=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) return
        if (!allowed(context)) return
        setup(context)
        val notification = builder(context, PROPOSALS)
            .setContentTitle("📬 오늘의 퀘스트 · ${quest.title}")
            .setContentText("${quest.detail} · 예상 보상 +${quest.reward} C · 서버 확인 후 확정")
            .setStyle(Notification.BigTextStyle().bigText("${quest.detail}\n오늘 해볼까요? 수락하거나 거절할 수 있어요."))
            .addAction(Notification.Action.Builder(null, "✅ 수락",
                action(context, quest.id, DailyQuestScheduler.ACTION_ACCEPT,profile)).build())
            .addAction(Notification.Action.Builder(null, "🙅 거절",
                action(context, quest.id, DailyQuestScheduler.ACTION_REJECT,profile)).build())
            .build()
        manager(context).notify((profile+":"+quest.id).hashCode(), notification)
    }

    fun active(context: Context, quest: DailyQuestEntity, profile: String = com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) {
        if(profile!=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) return
        if (!allowed(context)) return
        setup(context)
        val notification = builder(context, ACTIVE)
            .setContentTitle("🏃 진행 중 · ${quest.title}")
            .setContentText("실천했다면 완료하기 · 보상은 동기화 후 확정")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .addAction(Notification.Action.Builder(
                null, "완료", action(context, quest.id, DailyQuestScheduler.ACTION_COMPLETE,profile)
            ).build())
            .addAction(Notification.Action.Builder(
                null, "30분 후", action(context, quest.id, DailyQuestScheduler.ACTION_SNOOZE,profile)
            ).build())
            .build()
        manager(context).notify((profile+":"+quest.id).hashCode(), notification)
    }

    fun dismissActive(context: Context, id: String, profile: String = com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) {
        if(profile!=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) return
        manager(context).cancel((profile+":"+id).hashCode())
    }

    fun reward(context: Context, quest: DailyQuestEntity, profile: String = com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) {
        if(profile!=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) return
        dismissActive(context, quest.id,profile)
        if (!allowed(context)) return
        val notification = builder(context, COMPLETED)
            .setContentTitle("🎉 퀘스트 완료!")
            .setContentText("${quest.title} · 연결된 계정의 보상은 동기화 후 확정돼요.")
            .build()
        manager(context).notify((profile+":"+quest.id).hashCode(), notification)
    }

    fun clearOld(context: Context, profile: String = com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) {
        if(profile!=com.questyourlife.app.sync.ProfileRegistry.get(context).file.value) return
        // All notifications belong to this app, not other apps.
        manager(context).cancelAll()
    }
}
