package com.questyourlife.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val createdAt: Long
)

@Entity(tableName = "goal_steps", primaryKeys = ["goalId", "position"], indices = [Index("goalId")])
data class GoalStepEntity(
    val goalId: String,
    val position: Int,
    val chapter: Int,
    val title: String,
    val reward: Int,
    val completedAt: Long? = null
)

@Entity(tableName = "owned_items")
data class OwnedItemEntity(
    @PrimaryKey val itemId: Int,
    val price: Int,
    val purchasedAt: Long
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val finishedAt: Long,
    val durationSeconds: Int
)
