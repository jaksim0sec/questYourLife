package com.questyourlife.app.sync

/** Prefix only: never leap over a dependency or change an already sealed request. */
object BatchPolicy {
    const val MAX_BYTES=240*1024
    fun take(operations: List<SyncOperation>): List<SyncOperation> {
        val result=mutableListOf<SyncOperation>();val seen=mutableSetOf<Pair<String,String>>();var bytes=0
        for(op in operations) {
            if(result.size==25 || !seen.add(op.entity to op.targetId)) break
            // Wait for our reward/purchase ACK before sealing a later user-version CAS.
            if(op.entity=="user" && result.any { it.operation in setOf("daily_complete","step_complete","purchase","settings_patch","equip") }) break
            val size=(op.sealedRequest ?: op.body).toByteArray(Charsets.UTF_8).size+512
            if(bytes+size>MAX_BYTES) break
            result.add(op);bytes+=size
        }
        return result
    }
}
