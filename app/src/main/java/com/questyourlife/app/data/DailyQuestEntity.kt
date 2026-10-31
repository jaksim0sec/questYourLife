package com.questyourlife.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val dateKey: String,
    val title: String,
    val detail: String,
    val reward: Int,
    val accepted: Boolean = false,
    val completedAt: Long? = null
)
