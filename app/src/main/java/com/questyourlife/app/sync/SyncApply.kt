package com.questyourlife.app.sync

import com.questyourlife.app.data.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

/** All methods are called from a Room transaction. Cursor commits with every applied page. */
object SyncApply {
    suspend fun event(db: QuestDatabase, event: JSONObject, operation: SyncOperation? = null) {
        val dao = db.syncDao()
        val bound=dao.meta()?.userId
        if(bound!=null) check(event.getString("user_id")==bound) { "event_owner_mismatch" }
        dao.recordEvent(ConfirmedEvent(event.getString("id"), event.getString("event_id"), event.getLong("sync_seq"),
            event.optLong("cash_delta"), event.optLong("xp_delta"), event.getString("result_status"), event.toString()))
        val status = event.getString("result_status")
        if (operation != null) {
            if (status == "applied") dao.acknowledge(operation.sequence)
            else dao.updateOperation(operation.copy(state = if (status == "conflict") "conflict" else "rejected", leaseUntil = 0,
                error = event.optJSONObject("result")?.optString("error") ?: "version_conflict", conflictSnapshot = event.toString()))
        }
        applyChanges(db,event.getJSONArray("changes"),operation,status,event.optJSONObject("result")?.optLong("user_base_version",-1))
    }
    suspend fun snapshot(db: QuestDatabase, changes: JSONArray) = applyChanges(db,changes,null,"applied")
    private suspend fun applyChanges(db: QuestDatabase, changes: JSONArray, operation: SyncOperation?, status: String, userBase: Long? = null) {
        val dao=db.syncDao()
        val affected = linkedSetOf<String>()
        val participantBooks = dao.records("quest_join").map { JSONObject(it.body).optString("book_id") }.toMutableSet()
        for (i in 0 until changes.length()) {
            val c=changes.getJSONObject(i)
            if(c.optString("entity")=="quest_join" && c.getJSONObject("data").optString("user_id")==dao.meta()?.userId)
                participantBooks.add(c.getJSONObject("data").getString("book_id"))
        }
        for (index in 0 until changes.length()) {
            val change = changes.getJSONObject(index); val kind = change.getString("entity"); val id = change.getString("id")
            val version = change.getLong("version"); val data = change.getJSONObject("data")
            val bound=dao.meta()?.userId
            if(bound!=null && kind in setOf("user","questbook","quest_join","daily","quick")) {
                val owner=when(kind) { "user"->data.getString("id");"questbook"->data.getString("owner_id");else->data.getString("user_id") }
                check(owner==bound || (kind=="questbook" && data.optString("book_id") in participantBooks)) { "record_owner_mismatch" }
            }
            val existing = dao.record(kind, id)
            val source=data.optString("source_id").takeUnless { it.isEmpty() || it=="null" }
            val linkedPending=kind=="daily" && source!=null && dao.hasPending("questbook",source)>0
            val pending = linkedPending || dao.hasPending(kind, id) > 0 || (operation?.entity == kind && operation.targetId == id && status != "applied")
            if (existing != null && existing.version > version) continue
            dao.setRecord(RemoteRecord(kind, id, version, data.toString(), existing?.localHash, existing?.localId, pending, change.optBoolean("deleted")))
            if (kind == "user") {
                if(operation!=null && status=="applied" && userBase!=null && userBase>=0)
                    dao.advanceOwnUser(id,operation.sequence,userBase,version)
                val meta = dao.meta() ?: SyncMeta()
                val settingPending = dao.pendingSettings() > 0
                val appearance = data.optJSONObject("settings")?.optString("appearance", meta.appearance) ?: meta.appearance
                dao.setMeta(meta.copy(confirmedCash = data.getLong("cash"), confirmedXp = data.getLong("xp"),
                    appearance = if (settingPending || status == "conflict") meta.appearance else appearance,
                    equipped = if(dao.pendingOperation("equip")>0) meta.equipped else data.optJSONObject("equipped")?.optInt("item_id",meta.equipped) ?: meta.equipped))
                val inventory = data.getJSONObject("inventory")
                inventory.keys().forEach { key ->
                    val item = key.toIntOrNull() ?: return@forEach
                    val catalog = com.questyourlife.app.cosmetics.CosmeticCatalog.find(item)
                    if (catalog != null) {
                        db.goalDao().insertItem(OwnedItemEntity(item, catalog.price, System.currentTimeMillis(), "confirmed"))
                        dao.confirmItem(item)
                    }
                }
            } else if (kind == "questbook") affected.add(data.getString("book_id"))
            else if (kind == "quest_join") {
                val newRoot=data.getString("book_id")
                val previousRoot=existing?.body?.let { JSONObject(it).optString("book_id") }
                if(previousRoot!=null && previousRoot!=newRoot) {
                    // An explicitly accepted edition upgrade keeps the participant's
                    // Room goal and completion history instead of creating a second goal.
                    dao.goalBySync(previousRoot)?.let { oldGoal ->
                        if(dao.goalBySync(newRoot)==null) dao.putGoal(oldGoal.copy(syncId=newRoot))
                    }
                }
                affected.add(newRoot)
            }
            else if(kind=="quick" && !pending) {
                val old=dao.quick(id); val info=data.getJSONObject("content_snapshot")
                dao.putQuick(QuestEntity(old?.id ?: 0,info.getString("title"),data.getString("status")=="completed",info.optLong("created_at",System.currentTimeMillis()),id))
            }
            else if (kind == "daily" && !pending) projectDaily(db, data, existing?.localId)
        }
        affected.forEach { projectBook(db, it) }
        // Only own ACKs can advance the expected version of an unsealed successor.
        if (operation != null && status == "applied" && operation.entity!="user") {
            val ownChange=(0 until changes.length()).map { changes.getJSONObject(it) }.firstOrNull {
                it.getString("entity")==operation.entity && it.getString("id")==operation.targetId
            }
            val v=ownChange?.getLong("version")
            if(v!=null && dao.record(operation.entity,operation.targetId)?.version==v)
                dao.successorVersions(operation.entity,operation.targetId,operation.sequence,v)
            if(operation.entity=="quest_join" && ownChange!=null) {
                val previous=JSONObject(operation.body).optLong("edit_version",operation.baseVersion)
                val next=ownChange.getJSONObject("data").getLong("edit_version")
                dao.targetOperations("quest_join",operation.targetId).filter {
                    it.sequence>operation.sequence && it.sealedRequest==null && it.state=="queued" && it.operation=="join_edit"
                }.forEach { successor ->
                    val body=JSONObject(successor.body)
                    if(body.optLong("edit_version",successor.baseVersion)==previous)
                        dao.updateOperation(successor.copy(body=body.put("edit_version",next).toString()))
                }
            }
        }
    }
    suspend fun projectBook(db: QuestDatabase, id: String) {
        val dao = db.syncDao(); val root = dao.record("questbook", id) ?: return
        if (root.dirty) return
        val relatedJoins=dao.records("quest_join").filter { JSONObject(it.body).optString("book_id")==id }
        val joinRecord=relatedJoins.firstOrNull { JSONObject(it.body).optString("status")!="left" && !it.deleted }
        if(joinRecord?.dirty==true) return
        val join=joinRecord?.let { JSONObject(it.body) }
        val rawRows=dao.records("questbook").map { JSONObject(it.body) }.filter { it.optString("book_id")==id }
        val effective=join?.optJSONArray("effective_tree") ?: QuestTreeResolver.resolve(JSONArray(rawRows),join?.optJSONObject("overrides") ?: JSONObject())
        val rows=(0 until effective.length()).map { effective.getJSONObject(it) }
        val body=rows.firstOrNull { it.optString("type")=="book" } ?: JSONObject(root.body)
        val existing = dao.goalBySync(id)
        if (root.deleted || (relatedJoins.isNotEmpty() && joinRecord==null)) {
            if (existing != null) dao.putGoal(existing.copy(deletedAt=System.currentTimeMillis()))
            return
        }
        val localId = existing?.id ?: root.localId ?: id
        val goal = GoalEntity(localId, body.getString("name"), body.optJSONObject("data")?.optString("description") ?: "",
            existing?.createdAt ?: timestamp(body.optString("created_at")), id)
        dao.putGoal(goal)
        val oldSteps = db.goalDao().getAllSteps(localId).associateBy { it.syncId }
        val state = join?.optJSONObject("node_state") ?: JSONObject()
        val steps = rows.filter { it.optString("type") == "main" }.map { node ->
            val logical=node.optString("logical_id",node.getString("id"))
            val old = oldSteps[logical]
            val parent = rows.firstOrNull { it.optString("logical_id",it.getString("id")) == node.optString("head") }
            val completed = if (state.optString(logical) == "completed") old?.completedAt ?: System.currentTimeMillis() else old?.completedAt
            GoalStepEntity(localId, node.optInt("sort_order"), parent?.optInt("sort_order", 1) ?: 1,
                node.getString("name"), old?.reward ?: 0, completed, logical)
        }
        val visible=steps.map { it.syncId }.toSet()
        // Retain hidden/deleted rows as history, but remove them from the active plan projection.
        dao.putSteps(steps + oldSteps.values.filter { it.syncId !in visible }.map { it.copy(deletedAt=it.deletedAt ?: System.currentTimeMillis()) })
    }
    private suspend fun projectDaily(db: QuestDatabase, body: JSONObject, localId: String?) {
        val dao = db.syncDao(); val id = body.getString("id"); val old = dao.dailyBySync(id)
        val snapshot = body.getJSONObject("content_snapshot"); val source = body.optString("source_id").takeUnless { it.isEmpty() || it == "null" }
        val directStep=source?.let { dao.stepBySync(it) }
        val step = directStep ?: body.optString("main_id").takeUnless { it.isEmpty() || it=="null" }?.let { dao.stepBySync(it) }
        val status = body.getString("status")
        val completed = if (status == "completed") old?.completedAt ?: timestamp(body.optString("client_occurred_at").takeUnless { it == "null" || it.isEmpty() } ?: body.optString("completed_at")) else null
        val day=completed?.let { java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString() } ?: body.getString("scheduled_date")
        dao.putDaily(DailyQuestEntity(old?.id ?: localId?.takeIf { it.isNotEmpty() } ?: id, day,
            snapshot.optString("title"), snapshot.optString("detail"), if(status=="completed" && old?.legacyOrigin!=true) body.optInt("reward") else old?.reward ?: 20,
            accepted = status in setOf("accepted", "completed"), completedAt = completed,
            dismissed = status == "cancelled" || !body.isNull("deleted_at"), goalId = step?.goalId, stepPosition = step?.position,
            syncId = id, stepSyncId = source, legacyOrigin = old?.legacyOrigin ?: false))
        if (directStep != null && completed != null && directStep.completedAt == null && dao.hasPending("questbook",directStep.syncId)==0) dao.putSteps(listOf(directStep.copy(completedAt = completed)))
        if (directStep != null && completed == null && status=="accepted" && old?.completedAt!=null &&
            dao.hasPending("questbook",directStep.syncId)==0 && dao.allDaily().none { it.stepSyncId==source && it.completedAt!=null }) {
            dao.putSteps(listOf(directStep.copy(completedAt=null)))
        }
    }
    private fun timestamp(value: String): Long = runCatching { Instant.parse(value).toEpochMilli() }.getOrDefault(System.currentTimeMillis())
}
