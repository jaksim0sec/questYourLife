package com.questyourlife.app.sync

import androidx.room.withTransaction
import com.questyourlife.app.data.*
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

/** Called inside the same Room transaction as the domain mutation. */
object SyncCapture {
    suspend fun participation(db: QuestDatabase, bookId: String): String =
        db.syncDao().records("quest_join").map { JSONObject(it.body) }
            .firstOrNull { it.optString("book_id") == bookId && it.optString("status") != "left" }
            ?.getString("id") ?: StableIds.child(bookId, "participation")
    private fun hash(text: String) = MessageDigest.getInstance("SHA-256").digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
    suspend fun enqueue(db: QuestDatabase, operation: String, entity: String, targetId: String, localId: String, data: JSONObject) {
        val dao = db.syncDao(); dao.ensureMeta()
        val existing = dao.record(entity, targetId)
        val body = data.toString(); val fingerprint = hash(operation + body)
        if (existing?.localHash == fingerprint && operation in setOf("goal_put", "daily_put", "quick_put", "settings_patch", "join_edit")) return
        dao.enqueue(SyncOperation(operation = operation, entity = entity, targetId = targetId, localId = localId,
            body = body, baseVersion = existing?.version ?: 0))
        dao.setRecord((existing ?: RemoteRecord(entity, targetId)).copy(localHash = fingerprint, localId = localId, dirty = true))
    }
    suspend fun goal(db: QuestDatabase, id: String) {
        if (id == "example-japanese") return // UI fixture never becomes a real server quest.
        val goal = db.syncDao().goal(id)?.takeIf { it.deletedAt==null } ?: return
        val steps = db.goalDao().getSteps(id)
        val nodes = JSONArray()
        steps.map { it.chapter }.distinct().sorted().forEach { chapter ->
            nodes.put(JSONObject().put("id", StableIds.child(goal.syncId, "chapter:$chapter"))
                .put("head", goal.syncId).put("type", "chap").put("name", "챕터 $chapter").put("position", chapter))
        }
        steps.forEach { step -> nodes.put(JSONObject().put("id", step.syncId).put("head", StableIds.child(goal.syncId, "chapter:${step.chapter}"))
            .put("type", "main").put("name", step.title).put("position", step.position)) }
        val root = db.syncDao().record("questbook", goal.syncId)?.let { JSONObject(it.body) }
        val joinId = participation(db, goal.syncId)
        val join = db.syncDao().record("quest_join", joinId)?.let { JSONObject(it.body) }
        if (root != null && join != null) {
            val overrides = JSONObject((join.optJSONObject("overrides") ?: JSONObject()).toString())
            val patch = overrides.optJSONObject("patch") ?: JSONObject().also { overrides.put("patch", it) }
            val rootLogical = root.optString("logical_id", goal.syncId)
            val effective=join.optJSONArray("effective_tree") ?: JSONArray()
            val visible=(0 until effective.length()).map { effective.getJSONObject(it) }.associateBy { it.optString("logical_id",it.getString("id")) }
            var changed=false
            if(goal.title != (visible[rootLogical]?.optString("name") ?: root.optString("name"))) {
                patch.put(rootLogical, (patch.optJSONObject(rootLogical) ?: JSONObject()).put("name", goal.title))
                changed=true
            }
            steps.forEach { step ->
                val original=visible[step.syncId]
                val fields=patch.optJSONObject(step.syncId) ?: JSONObject()
                if(original!=null && original.optString("name")!=step.title) { fields.put("name",step.title);changed=true }
                if(original!=null && original.optInt("sort_order")!=step.position) { fields.put("sort_order",step.position);changed=true }
                if(fields.length()>0) patch.put(step.syncId,fields)
            }
            if(!changed) return
            enqueue(db,"join_edit","quest_join",joinId,goal.id,JSONObject().put("id",joinId)
                .put("edit_version",join.getLong("edit_version")).put("overrides",overrides))
            return
        }
        enqueue(db, "goal_put", "questbook", goal.syncId, goal.id,
            JSONObject().put("id", goal.syncId).put("join_id",joinId).put("title", goal.title).put("description", goal.description).put("nodes", nodes))
    }
    suspend fun daily(db: QuestDatabase, id: String, historical: Boolean = false) {
        val daily = db.syncDao().daily(id) ?: return
        if (daily.goalId == "example-japanese") return
        if (daily.completedAt != null && !historical) return
        val goal = daily.goalId?.let { db.syncDao().goal(it) }
        if (goal != null) SyncCapture.goal(db,goal.id)
        val step = daily.stepSyncId ?: daily.stepPosition?.let { pos -> goal?.let { db.goalDao().getSteps(it.id).firstOrNull { it.position == pos }?.syncId } }
        if (step != null && daily.stepSyncId != step) db.syncDao().linkStep(daily.id, step)
        val body = JSONObject().put("id", daily.syncId).put("title", daily.title).put("detail", daily.detail)
            .put("scheduled_date", daily.dateKey).put("status", when {
                daily.completedAt != null -> "completed"; daily.dismissed -> "cancelled"; daily.accepted -> "accepted"; else -> "proposed"
            }).put("legacy", historical && daily.completedAt != null)
        val remote = db.syncDao().record("daily", daily.syncId)?.let { JSONObject(it.body) }
        body.put("occurrence_key", remote?.optString("occurrence_key")?.takeUnless { it.isEmpty() || it=="null" } ?: daily.syncId)
        if (step != null && goal != null) body.put("source_id", remote?.optString("source_id")?.takeUnless { it.isEmpty() || it=="null" } ?: step)
            .put("main_id",remote?.optString("main_id")?.takeUnless { it.isEmpty() || it=="null" } ?: step)
            .put("join_id", remote?.optString("join_id")?.takeUnless { it.isEmpty() || it=="null" } ?: participation(db,goal.syncId))
        if (daily.completedAt != null) body.put("client_occurred_at", java.time.Instant.ofEpochMilli(daily.completedAt).toString())
        enqueue(db, "daily_put", "daily", daily.syncId, daily.id, body)
    }
    suspend fun completedDaily(db: QuestDatabase, id: String) {
        val q = db.syncDao().daily(id) ?: return
        if (q.goalId == "example-japanese") return
        enqueue(db, "daily_complete", "daily", q.syncId, q.id, JSONObject().put("id", q.syncId).put("client_occurred_at",java.time.Instant.ofEpochMilli(q.completedAt ?: System.currentTimeMillis()).toString()))
    }
    suspend fun completedStep(db: QuestDatabase, step: GoalStepEntity) {
        if (step.goalId == "example-japanese") return
        goal(db,step.goalId)
        val root = db.syncDao().goal(step.goalId) ?: return
        enqueue(db, "step_complete", "questbook", step.syncId, step.goalId, JSONObject().put("id", step.syncId)
            .put("join_id",participation(db,root.syncId)).put("client_occurred_at",java.time.Instant.ofEpochMilli(db.syncDao().stepBySync(step.syncId)?.completedAt ?: System.currentTimeMillis()).toString()))
    }
    suspend fun quick(db: QuestDatabase,q: QuestEntity) {
        enqueue(db,"quick_put","quick",q.syncId,q.id.toString(),JSONObject().put("id",q.syncId)
            .put("title",q.title).put("completed",q.completed).put("created_at",q.createdAt))
    }
    suspend fun bootstrap(db: QuestDatabase) {
        val dao = db.syncDao()
        dao.allQuick().forEach { quick(db,it) }
        dao.allGoals().forEach { goal(db, it.id) }
        // Existing completed executions are history imports, not reward requests.
        val pending=dao.pending(100000)
        val pendingSteps=pending.filter { it.operation=="step_complete" }.map { it.targetId }.toSet()
        val pendingDaily=pending.filter { it.operation=="daily_complete" }.map { it.targetId }.toSet()
        dao.allDaily().forEach {
            if(it.completedAt==null || (it.syncId !in pendingDaily && it.stepSyncId !in pendingSteps)) daily(db,it.id,historical=true)
        }
        dao.allGoals().filter { it.id != "example-japanese" }.forEach { goal ->
            db.goalDao().getSteps(goal.id).filter { it.completedAt != null && it.syncId !in pendingSteps && dao.allDaily().none { q -> q.stepSyncId==it.syncId && q.syncId in pendingDaily } }.forEach { step ->
                if (dao.allDaily().none { it.stepSyncId == step.syncId && it.completedAt != null }) {
                    val id = StableIds.child(step.syncId, "historical-execution")
                    enqueue(db, "daily_put", "daily", id, "", JSONObject().put("id", id).put("source_id", step.syncId)
                        .put("join_id", participation(db,goal.syncId)).put("main_id",step.syncId).put("occurrence_key",id).put("title", step.title).put("detail", "이전 기기의 완료 기록")
                        .put("scheduled_date", java.time.Instant.ofEpochMilli(step.completedAt!!).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString())
                        .put("status", "completed").put("legacy", true).put("client_occurred_at", java.time.Instant.ofEpochMilli(step.completedAt).toString()))
                }
            }
        }
    }
    suspend fun appearance(db: QuestDatabase, value: String) = db.withTransaction {
        require(value in setOf("system", "light", "dark"))
        db.syncDao().ensureMeta(); val meta = db.syncDao().meta()!!
        if (meta.appearance == value) return@withTransaction
        db.syncDao().setMeta(meta.copy(appearance = value))
        val target = meta.userId ?: StableIds.child("guest", "settings")
        enqueue(db, "settings_patch", "user", target, "", JSONObject().put("id", target).put("appearance", value))
    }
}
