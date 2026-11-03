package com.questyourlife.app.companion

import android.content.Context
import androidx.room.withTransaction
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.sync.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** Account-isolated read cache and commands in the existing transactional outbox. No social Room tables. */
@OptIn(ExperimentalCoroutinesApi::class)
class SocialStore(private val context: Context, private val db: QuestDatabase = QuestDatabase.get(context)) {
    private val accounts = AccountRepository.get(context)
    private val refreshGate = Mutex()
    val problems = db.syncDao().observeProblems().map { rows -> rows.filter { it.entity == "social" } }
    val userId: Flow<String?> = combine(accounts.state, db.syncDao().observeMeta()) { session, meta ->
        session?.userId?.takeIf { it == meta?.userId }
    }.distinctUntilChanged()
    val snapshot: Flow<SocialSnapshot?> = userId.flatMapLatest { user ->
        if (user == null) flowOf(null) else combine(
            db.syncDao().observeRecord("social_snapshot", user), db.syncDao().observePendingCount()
        ) { record, _ ->
            val body = record?.let { JSONObject(it.body) } ?: JSONObject()
            val queued = db.syncDao().pending(100000).filter { it.entity == "social" }
            val readIds = queued.filter { it.operation == "alarm_read" }.map { it.targetId }.toSet()
            body.optJSONArray("alarms").objects().filter { it.optString("id") in readIds }.forEach { it.put("read", 1) }
            // Preserve locally queued messages until their immutable receipts acknowledge delivery.
            val pending = queued.filter { it.operation == "post_send" }
            val posts = body.optJSONArray("posts") ?: JSONArray().also { body.put("posts", it) }
            // A receipt can arrive before the next snapshot; mirror its server post after-image immediately.
            val mirrored = db.syncDao().records("post").map { JSONObject(it.body) }
            body.put("posts", mergePosts(posts.objects(), mirrored))
            val visiblePosts = body.getJSONArray("posts")
            val ids = visiblePosts.objects().map { it.optString("id") }.toSet()
            pending.forEach { operation ->
                val data = JSONObject(operation.body)
                if (data.optString("id") !in ids) visiblePosts.put(data.put("actor", "user").put("author_id", user).put("type", "chat"))
            }
            SocialSnapshot.parse(user, body).copy(loaded = record != null)
        }.flowOn(Dispatchers.IO)
    }
    val ownProfile: Flow<SocialProfile?> = userId.flatMapLatest { user ->
        if (user == null) flowOf(null) else db.syncDao().observeRecord("user", user).map { record ->
            record?.let { JSONObject(it.body) }?.let { SocialProfile(user, it.optString("name", "나"),
                it.optJSONObject("equipped")?.optInt("item_id") ?: 0) }
        }.flowOn(Dispatchers.IO)
    }
    val weeklyEarnings: Flow<Int?> = userId.flatMapLatest { user ->
        if (user == null) flowOf(null) else db.invalidationTracker.createFlow("confirmed_events").map {
            val zone = ZoneId.of("Asia/Seoul")
            val today = LocalDate.now(zone)
            val start = today.minusDays(today.dayOfWeek.value - 1L).atStartOfDay(zone).toInstant()
            val end = start.atZone(zone).plusWeeks(1).toInstant()
            var total = 0L
            db.openHelper.readableDatabase.query("SELECT body FROM confirmed_events WHERE status='applied'").use { rows ->
                while (rows.moveToNext()) {
                    val receipt = JSONObject(rows.getString(0))
                    if (receipt.optString("user_id") != user || receipt.optString("kind", "command") != "command") continue
                    val time = receipt.optionalString("server_created_at")?.let { Instant.parse(it) } ?: continue
                    if (time >= start && time < end && receipt.optString("operation") in
                        setOf("daily_complete", "step_complete", "daily_undo", "clan_chest_claim")) total += receipt.optLong("cash_delta")
                }
            }
            total.coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun owner(): String {
        val user = accounts.state.value?.userId ?: throw ApiFailure(401, "login_required")
        check(db.syncDao().meta()?.userId == user) { "account_scope_mismatch" }
        return user
    }
    private suspend fun rpc(name: String, body: JSONObject, user: String): JSONObject {
        val api = SupabaseTransport()
        return try { api.post("/rest/v1/rpc/$name", body, accounts.accessToken(user)) }
        catch (e: ApiFailure) {
            if (e.status != 401) throw e
            api.post("/rest/v1/rpc/$name", body, accounts.accessToken(user, true))
        }
    }
    suspend fun refresh() = refreshGate.withLock {
        val user = owner()
        val result = rpc("questyourlife_social_snapshot", JSONObject(), user)
        db.withTransaction {
            check(owner() == user) { "account_scope_mismatch" }
            val old = db.syncDao().record("social_snapshot", user)?.let { JSONObject(it.body).optJSONArray("posts").objects() }.orEmpty()
            val allowed = SocialSnapshot.parse(user, result).chats.map { it.id }.toSet()
            result.put("posts", mergePosts(old.filter { it.optString("chat_id") in allowed }, result.optJSONArray("posts").objects()))
            // Store a whole bounded snapshot atomically so revoked memberships disappear together.
            db.syncDao().setRecord(RemoteRecord("social_snapshot", user, body = result.toString()))
        }
    }
    suspend fun command(operation: String, target: String, data: JSONObject) = db.withTransaction {
        enqueueCommand(db, owner(), operation, target, data)
        // Schedule only after the transaction commits; callers invoke schedule().
    }
    fun schedule() = SyncScheduler.enqueue(context)
    suspend fun markRead(ids: Set<String>) {
        db.withTransaction {
            val user = owner()
            val record = db.syncDao().record("social_snapshot", user) ?: return@withTransaction
            val body = JSONObject(record.body)
            body.optJSONArray("alarms").objects().filter { it.optString("id") in ids && it.optInt("read") == 0 }.forEach {
                val id = it.getString("id")
                command("alarm_read", id, JSONObject().put("id", id))
                it.put("read", 1)
            }
            db.syncDao().setRecord(record.copy(body = body.toString()))
        }
        schedule()
    }
    suspend fun openAi(): String {
        val user = owner()
        val cached = db.syncDao().record("social_snapshot", user)?.let { SocialSnapshot.parse(user, JSONObject(it.body)) }
        cached?.chats?.firstOrNull { it.type == "ai" }?.let { return it.id }
        val id = StableIds.child(user, "ai-channel")
        db.withTransaction {
            if (db.syncDao().hasPending("social", id) == 0) command("chat_open_ai", id, JSONObject().put("id", id))
        }
        schedule()
        return id
    }
    suspend fun send(thread: String, text: String) {
        val user = owner()
        val snapshot = db.syncDao().record("social_snapshot", user)?.let { SocialSnapshot.parse(user, JSONObject(it.body)) }
        require(snapshot?.chats?.any { it.id == thread } == true) { "channel_membership_required" }
        val clean = text.trim()
        require(clean.isNotBlank() && clean.length <= 2000)
        val id = UUID.randomUUID().toString()
        command("post_send", id, JSONObject().put("id", id).put("chat_id", thread).put("content", clean)
            .put("created_at", java.time.Instant.now().toString()))
        schedule()
    }
    suspend fun messagesPage(chat: String, before: JSONObject? = null): JSONObject {
        val user = owner()
        val request = JSONObject().put("p_chat_id", chat).put("p_limit", 50)
        before?.let { request.put("p_before_created_at", it.getString("created_at")).put("p_before_id", it.getString("id")) }
        val result = rpc("questyourlife_posts", request, user)
        db.withTransaction {
            check(owner() == user)
            val record = db.syncDao().record("social_snapshot", user) ?: return@withTransaction
            val body = JSONObject(record.body)
            if (SocialSnapshot.parse(user, body).chats.none { it.id == chat }) return@withTransaction
            val old = body.optJSONArray("posts").objects()
            val newer = result.optJSONArray("posts").objects()
            val ids = newer.map { it.getString("id") }.toSet()
            body.put("posts", JSONArray((old.filter { it.optString("id") !in ids } + newer).map { it }))
            db.syncDao().setRecord(record.copy(body = body.toString()))
        }
        return result
    }
    suspend fun fetchProfile(id: String) {
        UUID.fromString(id)
        val user = owner()
        val response = rpc("questyourlife_public_profiles", JSONObject().put("p_ids", JSONArray().put(id)), user)
        db.withTransaction {
            check(owner() == user)
            val record = db.syncDao().record("social_snapshot", user) ?: return@withTransaction
            val body = JSONObject(record.body)
            val profiles = body.optJSONArray("public_profiles").objects().filter { it.optString("id") != id } +
                response.optJSONArray("profiles").objects().filter { it.optString("id") == id }
            body.put("public_profiles", JSONArray(profiles))
            db.syncDao().setRecord(record.copy(body = body.toString()))
        }
    }
    suspend fun confirmPlan(messageId: String) {
        val user = owner()
        db.withTransaction {
            val snapshot = db.syncDao().record("social_snapshot", user)?.let { JSONObject(it.body) }
                ?: error("plan_draft_unsupported")
            val post = db.syncDao().record("post", messageId)?.let { JSONObject(it.body) }
                ?: snapshot.optJSONArray("posts").objects().firstOrNull { it.optString("id") == messageId }
                ?: error("plan_draft_unsupported")
            val channel = SocialSnapshot.parse(user, snapshot).chats.firstOrNull { it.id == post.optString("chat_id") }
            require(channel?.type == "ai" && post.optString("actor") == "assistant" && post.isNull("deleted_at")) { "plan_draft_unsupported" }
            val payload = post.optJSONObject("payload") ?: error("plan_draft_unsupported")
            if (payload.optionalString("applied_goal_id") != null) return@withTransaction
            val previous = db.syncDao().targetOperations("social", messageId).firstOrNull { it.operation == "plan_confirm" }
            if (previous != null) {
                check(previous.state in setOf("queued", "in_flight")) { "plan_confirmation_requires_resolution" }
                return@withTransaction
            }
            val draft = payload.optJSONObject("plan_draft") ?: error("plan_draft_unsupported")
            val goal = SocialPlanDraft.goal(messageId, draft)
            enqueueCommand(db, user, "plan_confirm", messageId, JSONObject().put("id", messageId).put("confirmed", true).put("goal", goal))
        }
        schedule()
    }
    companion object {
        internal suspend fun enqueueCommand(db: QuestDatabase, user: String, operation: String, target: String, data: JSONObject) = db.withTransaction {
            check(db.syncDao().meta()?.userId == user) { "account_scope_mismatch" }
            UUID.fromString(target)
            require(data.optString("id") == target) { "social_target_mismatch" }
            require(operation in OPERATIONS) { "unsupported_social_operation" }
            SyncCapture.enqueue(db, operation, "social", target, "", data)
        }
        internal fun mergePosts(older: List<JSONObject>, newer: List<JSONObject>): JSONArray {
            val posts = linkedMapOf<String, JSONObject>()
            (older + newer).forEach { row ->
                val id = row.getString("id")
                val existing = posts[id]
                fun updated(value: JSONObject) = listOf("edited_at", "deleted_at", "created_at").mapNotNull {
                    value.optionalString(it)?.let { raw -> SocialSnapshot.time(raw) }
                }.maxOrNull() ?: 0
                if (existing == null || updated(row) >= updated(existing)) posts[id] = row
            }
            return JSONArray(posts.values.toList())
        }
        val OPERATIONS = setOf("clan_create", "clan_invite", "clan_join", "clan_leave", "clan_transfer", "clan_books",
            "clan_chest_unlock", "clan_chest_claim", "chat_open_ai", "chat_open_direct", "chat_create_group",
            "chat_add_member", "chat_leave", "chat_read", "friend_request", "friend_accept", "friend_block",
            "friend_unblock", "post_send", "post_edit", "post_delete", "plan_confirm", "alarm_read")
    }
}
