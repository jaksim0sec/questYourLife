package com.questyourlife.app.data

import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests", indices = [Index(value = ["syncId"], unique = true), Index("stepSyncId")])
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val dateKey: String,
    val title: String,
    val detail: String,
    val reward: Int,
    val accepted: Boolean = false,
    val completedAt: Long? = null,
    @ColumnInfo(defaultValue = "0") val dismissed: Boolean = false,
    val goalId: String? = null,
    val stepPosition: Int? = null,
    @ColumnInfo(defaultValue = "''") val syncId: String = java.util.UUID.randomUUID().toString(),
    val stepSyncId: String? = null,
    @ColumnInfo(defaultValue = "0") val legacyOrigin: Boolean = false
)
