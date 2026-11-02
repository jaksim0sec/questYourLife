package com.questyourlife.app.daily

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

object DailyQuestScheduler {
    private const val PREFS = "daily_quest_settings"
    private const val HOUR = "hour"
    private const val MINUTE = "minute"
    private const val ENABLED = "enabled"
    const val ACTION_DAILY = "com.questyourlife.app.DAILY_PROPOSALS"
    const val ACTION_SNOOZE = "com.questyourlife.app.DAILY_SNOOZE"
    const val ACTION_SNOOZE_DUE = "com.questyourlife.app.DAILY_SNOOZE_DUE"
    const val ACTION_ACCEPT = "com.questyourlife.app.DAILY_ACCEPT"
    const val ACTION_REJECT = "com.questyourlife.app.DAILY_REJECT"
    const val ACTION_COMPLETE = "com.questyourlife.app.DAILY_COMPLETE"
    const val EXTRA_ID = "daily_quest_id"

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    fun hour(context: Context) = prefs(context).getInt(HOUR, 7)
    fun minute(context: Context) = prefs(context).getInt(MINUTE, 0)
    fun enabled(context: Context) = prefs(context).getBoolean(ENABLED, true)

    fun setTime(context: Context, hour: Int, minute: Int) {
        require(hour in 0..23 && minute in 0..59)
        prefs(context).edit().putInt(HOUR, hour).putInt(MINUTE, minute).apply()
        scheduleNext(context)
    }

    fun setEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(ENABLED, enabled).apply()
        scheduleNext(context)
    }

    private fun dailyIntent(context: Context) =
        PendingIntent.getBroadcast(
            context, 700,
            Intent(context, DailyQuestAlarmReceiver::class.java).setAction(ACTION_DAILY),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    fun scheduleNext(context: Context) {
        val alarm = context.getSystemService(AlarmManager::class.java)
        val intent = dailyIntent(context)
        alarm.cancel(intent)
        if (!enabled(context)) return
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(hour(context), minute(context))
        if (!next.isAfter(now)) next = next.plusDays(1)
        alarm.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            intent
        )
    }

    fun snooze(context: Context, id: String) {
        val alarm = context.getSystemService(AlarmManager::class.java)
        val intent = PendingIntent.getBroadcast(
            context, id.hashCode(),
            Intent(context, DailyQuestAlarmReceiver::class.java)
                .setAction(ACTION_SNOOZE_DUE)
                .setData(android.net.Uri.parse("hyunsaengpatch://snooze/$id"))
                .putExtra(EXTRA_ID, id),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarm.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 30L * 60L * 1000L,
            intent
        )
    }
}
