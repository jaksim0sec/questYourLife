package com.questyourlife.app.sync

import androidx.room.withTransaction
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.sync.withLock
import java.util.UUID

object SyncResolution {
    suspend fun retryLocal(db: QuestDatabase, problem: SyncOperation) = SyncWorker.gate.withLock {
        db.withTransaction {
            val dao=db.syncDao(); val operations=dao.targetOperations(problem.entity,problem.targetId)
            val version=dao.record(problem.entity,problem.targetId)?.version ?: 0
            check(operations.none { it.state=="in_flight" })
            operations.forEach { dao.acknowledge(it.sequence) }
            operations.forEach { dao.enqueue(it.copy(sequence=0,eventId=UUID.randomUUID().toString(),baseVersion=version,
                sealedRequest=null,state="queued",leaseUntil=0,attempts=0,error=null,conflictSnapshot=null)) }
        }
    }
    suspend fun acceptRemote(db: QuestDatabase, problem: SyncOperation) = SyncWorker.gate.withLock {
        db.withTransaction {
            val dao=db.syncDao(); val operations=dao.targetOperations(problem.entity,problem.targetId)
            check(operations.none { it.state=="in_flight" })
            operations.forEach { dao.acknowledge(it.sequence) }
            val record=dao.record(problem.entity,problem.targetId)
            if(record!=null) {
                dao.setRecord(record.copy(dirty=false,localHash=null))
                if(record.body!="{}") {
                    val changes=org.json.JSONArray().put(org.json.JSONObject()
                        .put("entity",record.entity).put("id",record.id).put("version",record.version).put("deleted",record.deleted).put("data",org.json.JSONObject(record.body)))
                    SyncApply.snapshot(db,changes)
                }
            }
        }
    }
}
