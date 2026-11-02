package com.questyourlife.app.companion

import com.questyourlife.app.data.*
import kotlinx.coroutines.flow.Flow

/** Presentation depends on this port, not an HTTP client or demo storage. */
interface CompanionGateway {
    fun watchMessages(thread: String): Flow<List<CompanionMessageEntity>>
    fun contacts(): List<DemoContact>
    fun demoClan(): DemoClan
    suspend fun openThread(thread: String)
    suspend fun send(thread: String, text: String)
    suspend fun applyPlan(messageId: String): String?
}
