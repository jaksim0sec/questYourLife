package com.questyourlife.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "companion_messages", indices = [Index("threadId")])
data class CompanionMessageEntity(
    @PrimaryKey val id: String,
    val threadId: String,
    val body: String,
    val fromUser: Boolean,
    val createdAt: Long,
    val planTitle: String? = null,
    val planSteps: String? = null,
    val appliedGoalId: String? = null
)

data class DemoContact(val id: String, val name: String, val preview: String, val weeklyCash: Int)
data class DemoClan(val name: String, val members: List<DemoContact>, val weeklyTarget: Int)
