package com.questyourlife.app.ui

import android.content.Context

/**
 * Legacy installations have no onboarding key but may already have an active database.
 * Once onboarding starts, false is persisted so process death cannot turn it into a legacy install.
 */
object EntryPolicy {
    fun showOnboarding(completed: Boolean?, existedBeforeLaunch: Boolean): Boolean =
        completed?.not() ?: !existedBeforeLaunch
}

class OnboardingEntry(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences("qyl_onboarding", Context.MODE_PRIVATE)

    fun shouldShow(existingDatabaseBeforeLaunch: Boolean): Boolean {
        val recorded = if (preferences.contains("finished")) {
            preferences.getBoolean("finished", false)
        } else null
        val show = EntryPolicy.showOnboarding(recorded, existingDatabaseBeforeLaunch)
        if (show && recorded == null) {
            preferences.edit().putBoolean("finished", false).apply()
        }
        return show
    }

    fun complete() {
        preferences.edit().putBoolean("finished", true).apply()
    }
}
