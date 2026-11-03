package com.questyourlife.app.sync

import org.junit.Assert.*
import org.junit.Test

class BatchPolicyTest {
    private fun op(id: String,body: String="{}")=SyncOperation(operation="daily_put",entity="daily",targetId=id,localId=id,body=body)
    @Test fun neverSkipsSameRecordDependency() {
        val first=op("one");val second=op("one");val child=op("child")
        assertEquals(listOf(first),BatchPolicy.take(listOf(first,second,child)))
    }
    @Test fun userCasWaitsForOwnFinancialPredecessorReceipt() {
        val reward=op("one").copy(operation="daily_complete")
        val settings=op("user").copy(operation="settings_patch",entity="user")
        assertEquals(listOf(reward),BatchPolicy.take(listOf(reward,settings)))
    }
    @Test fun boundedByCountAndUtf8Bytes() {
        assertEquals(25,BatchPolicy.take((1..50).map { op(it.toString()) }).size)
        val large=op("one","가".repeat(70000))
        assertEquals(1,BatchPolicy.take(listOf(large,op("two","가".repeat(30000)))).size)
    }
}
