package com.questyourlife.app.sync

import android.content.Context
import androidx.room.withTransaction
import androidx.work.*
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SyncScheduler {
    fun enqueue(context: Context, file: String = ProfileRegistry.get(context).file.value, userId: String? = AccountRepository.get(context).state.value?.userId) {
        if (userId == null) return
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(workDataOf("file" to file, "user" to userId))
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,30,TimeUnit.SECONDS).build()
        WorkManager.getInstance(context).enqueueUniqueWork("qyl-sync-$file",ExistingWorkPolicy.APPEND_OR_REPLACE,request)
    }
}

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result = gate.withLock {
        val user = inputData.getString("user") ?: return@withLock Result.success()
        val file = inputData.getString("file") ?: return@withLock Result.success()
        val account = AccountRepository.get(applicationContext)
        if (account.state.value?.userId != user) return@withLock Result.success()
        val db = QuestDatabase.open(applicationContext,file); val dao = db.syncDao()
        if (dao.meta()?.userId != user) return@withLock Result.failure()
        db.withTransaction { dao.recoverLeases() }
        val deadline=android.os.SystemClock.elapsedRealtime()+480000
        val api = SupabaseTransport()
        suspend fun rpc(name: String, body: JSONObject): JSONObject {
            try { return api.post("/rest/v1/rpc/$name",body,account.accessToken(user)) }
            catch (e: ApiFailure) { if (e.status != 401) throw e }
            return api.post("/rest/v1/rpc/$name",body,account.accessToken(user,true))
        }
        try {
            SyncStatus.set(db,"running")
            val profile = rpc("questyourlife_bootstrap",JSONObject())
            db.withTransaction {
                val current = dao.meta()!!
                val epoch = profile.getString("epoch")
                if (current.epoch != null && current.epoch != epoch) throw ApiFailure(409,"cursor_epoch_mismatch")
                dao.setMeta(current.copy(epoch=epoch))
            }
            var sent = 0
            while (sent < 200 && android.os.SystemClock.elapsedRealtime()<deadline) {
                val operations = ready(db)
                if (operations.isEmpty()) break
                val batch = mutableListOf<SyncOperation>()
                val targets = mutableSetOf<Pair<String,String>>()
                for(raw in BatchPolicy.take(operations)) {
                    if(dao.blocked(raw.entity,raw.targetId)>0 || !targets.add(raw.entity to raw.targetId)) break
                    if(raw.state=="in_flight" && raw.leaseUntil>System.currentTimeMillis()) return@withLock Result.retry()
                    val request=raw.sealedRequest ?: JSONObject().put("event_id",raw.eventId).put("operation",raw.operation)
                        .put("base_version",raw.baseVersion).put("data",JSONObject(raw.body)).toString()
                    batch.add(raw.copy(sealedRequest=request,state="in_flight",leaseUntil=System.currentTimeMillis()+600000,attempts=raw.attempts+1))
                }
                if(batch.isEmpty()) {
                    val first=operations.first()
                    if((first.sealedRequest ?: first.body).toByteArray().size+512>BatchPolicy.MAX_BYTES)
                        db.withTransaction { dao.updateOperation(first.copy(state="rejected",error="request_too_large")) }
                    break
                }
                db.withTransaction { batch.forEach { dao.updateOperation(it) } }
                try {
                    val events=JSONArray(); batch.forEach { events.put(JSONObject(it.sealedRequest!!)) }
                    val social = batch.first().entity=="social"
                    val response=if(social) {
                        val receipts=JSONArray()
                        batch.forEach { op ->
                            val result=rpc("questyourlife_social_command",JSONObject().put("p_event_id",op.eventId)
                                .put("p_operation",op.operation).put("p_data",JSONObject(op.body)))
                            val returned=result.getJSONArray("receipts")
                            check(returned.length()==1) { "incomplete_social_receipt" }
                            receipts.put(returned.getJSONObject(0))
                        }
                        JSONObject().put("receipts",receipts)
                    } else rpc("questyourlife_apply_batch",JSONObject().put("p_events",events))
                    val receipts=response.getJSONArray("receipts")
                    check(receipts.length()==batch.size) { "incomplete_receipts" }
                    db.withTransaction {
                        batch.forEachIndexed { index, operation ->
                            val receipt=receipts.getJSONObject(index)
                            check(receipt.getString("event_id")==operation.eventId) { "receipt_identity_mismatch" }
                            SyncApply.event(db,receipt,operation)
                        }
                    }
                } catch(e: Exception) {
                    val permanent=e is ApiFailure && e.status in setOf(400,413,422)
                    db.withTransaction { batch.forEach { dao.updateOperation(it.copy(state=if(permanent) "rejected" else "queued",leaseUntil=0,error=if(e is ApiFailure) e.code else "network_unavailable")) } }
                    if(!permanent) throw e
                }
                sent+=batch.size
            }
            var watermark: Long? = null
            repeat(100) {
                if(android.os.SystemClock.elapsedRealtime()>=deadline) { SyncScheduler.enqueue(applicationContext,file,user);return@withLock Result.success() }
                val meta = dao.meta()!!
                val body = JSONObject().put("p_cursor",meta.cursor).put("p_limit",100).put("p_epoch",meta.epoch)
                if(watermark!=null) body.put("p_watermark",watermark)
                val page = rpc("questyourlife_pull_changes",body)
                watermark=page.getLong("watermark")
                db.withTransaction {
                    val events=page.getJSONArray("events")
                    for(i in 0 until events.length()) SyncApply.event(db,events.getJSONObject(i))
                    dao.setMeta(dao.meta()!!.copy(cursor=page.getLong("cursor"),epoch=page.getString("epoch")))
                }
                if(!page.getBoolean("has_more")) {
                    val books=rpc("questyourlife_joined_books",JSONObject())
                    db.withTransaction { SyncApply.snapshot(db,books.getJSONArray("changes")) }
                    val activeJoins=dao.records("quest_join").map { JSONObject(it.body) }.filter {
                        it.optString("status")=="active" && it.isNull("deleted_at")
                    }
                    for(join in activeJoins) {
                        val generated=rpc("questyourlife_generate_upcoming",JSONObject().put("p_join",join.getString("id")).put("p_limit",25))
                        val receipts=generated.getJSONArray("receipts")
                        db.withTransaction { for(index in 0 until receipts.length()) SyncApply.event(db,receipts.getJSONObject(index)) }
                        if(generated.optInt("generated")>0 || generated.optBoolean("has_more")) SyncScheduler.enqueue(applicationContext,file,user)
                    }
                    com.questyourlife.app.companion.SocialStore(applicationContext,db).refresh()
                    if(sent>=200) SyncScheduler.enqueue(applicationContext,file,user)
                    SyncStatus.set(db,"idle")
                    return@withLock Result.success()
                }
            }
            SyncScheduler.enqueue(applicationContext,file,user)
            Result.success()
        } catch(e: CancellationException) { throw e }
        catch(e: ApiFailure) { SyncStatus.set(db,if(e.status==401) "login_required" else "server_unavailable"); if(e.status in setOf(401,403,404,409)) Result.failure() else Result.retry() }
        catch(e: java.io.IOException) { SyncStatus.set(db,"offline");Result.retry() }
        catch(e: Exception) { SyncStatus.set(db,"protocol_error");Result.failure() }
    }
    companion object { val gate=Mutex()
        internal suspend fun ready(db: QuestDatabase): List<SyncOperation> {
            val dao=db.syncDao();val result=mutableListOf<SyncOperation>();var after=0L
            while(result.size<25) {
                val page=dao.runnable(after)
                if(page.isEmpty()) break
                for(op in page) {
                    after=op.sequence
                    val body=JSONObject(op.body)
                    val daily=if(op.operation=="daily_complete") dao.dailyBySync(op.targetId) else null
                    val source=if(op.operation=="step_complete") op.targetId else daily?.stepSyncId ?: body.optString("source_id").takeIf { it.isNotEmpty() && it!="null" }
                    val goalId=daily?.goalId ?: source?.let { dao.stepBySync(it)?.goalId }
                    val root=goalId?.let { dao.goal(it)?.syncId } ?: body.optString("join_id").takeIf { it.isNotEmpty() && it!="null" }
                    if(root!=null && dao.blocked("questbook",root)>0) continue
                    if(source!=null && dao.blocked("questbook",source)>0) continue
                    // Keep domains in separate wire batches; each retains its original event UUID.
                    if(result.isNotEmpty() && (result.first().entity=="social") != (op.entity=="social")) return result
                    result.add(op)
                    if(result.size==25) break
                }
            }
            return result
        }
    }
}
