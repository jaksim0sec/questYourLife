package com.questyourlife.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "goals", indices = [Index(value = ["syncId"], unique = true)])
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val createdAt: Long,
    @ColumnInfo(defaultValue = "''") val syncId: String = java.util.UUID.randomUUID().toString(),
    val deletedAt: Long? = null
)

@Entity(tableName = "stable_goal_steps", indices = [Index("goalId"), Index(value = ["syncId"], unique = true)])
data class GoalStepEntity(
    val goalId: String,
    val position: Int,
    val chapter: Int,
    val title: String,
    val reward: Int,
    val completedAt: Long? = null,
    @PrimaryKey val syncId: String = java.util.UUID.randomUUID().toString(),
    val deletedAt: Long? = null
)

@Entity(tableName = "owned_items")
data class OwnedItemEntity(
    @PrimaryKey val itemId: Int,
    val price: Int,
    val purchasedAt: Long,
    @ColumnInfo(defaultValue = "'legacy'") val source: String = "legacy"
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val finishedAt: Long,
    val durationSeconds: Int
)
