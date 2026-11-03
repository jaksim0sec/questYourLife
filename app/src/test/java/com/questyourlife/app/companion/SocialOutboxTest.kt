package com.questyourlife.app.companion

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.sync.*
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class SocialOutboxTest {
    private val context: Context get() = ApplicationProvider.getApplicationContext()
    private val user = "00000000-0000-0000-0000-000000000001"
    private fun id() = UUID.randomUUID().toString()

    @Test fun offlinePostRetainsEventIdentityAndBodyAcrossDatabaseReopen() = runBlocking {
        val file = "social-${id()}.db"
        var db = Room.databaseBuilder(context, QuestDatabase::class.java, file).build()
        try {
            db.syncDao().ensureMeta(SyncMeta(userId = user))
            val post = id(); val channel = id()
            val body = JSONObject().put("id", post).put("chat_id", channel).put("content", "오프라인 메시지")
            SocialStore.enqueueCommand(db, user, "post_send", post, body)
            val original = db.syncDao().pending().single()
            assertEquals("social", original.entity)
            assertEquals("post_send", original.operation)
            assertEquals(body.toString(), original.body)
            db.close()
            db = Room.databaseBuilder(context, QuestDatabase::class.java, file).build()
            val restored = db.syncDao().pending().single()
            assertEquals(original.eventId, restored.eventId)
            assertEquals(original.body, restored.body)
            assertEquals(post, restored.targetId)
        } finally { db.close(); context.deleteDatabase(file) }
    }
    @Test fun socialMutationRollsBackWithItsOutboxAndCannotCrossAccount() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, QuestDatabase::class.java).build()
        try {
            db.syncDao().ensureMeta(SyncMeta(userId = user))
            val alarm = id()
            try {
                db.withTransaction {
                    SocialStore.enqueueCommand(db, user, "alarm_read", alarm, JSONObject().put("id", alarm))
                    throw IllegalStateException("interrupted_transaction")
                }
            } catch (_: IllegalStateException) { }
            assertTrue(db.syncDao().pending().isEmpty())
            try {
                SocialStore.enqueueCommand(db, id(), "alarm_read", alarm, JSONObject().put("id", alarm))
                fail("Another account must never queue into this database")
            } catch (e: IllegalStateException) { assertEquals("account_scope_mismatch", e.message) }
            assertTrue(db.syncDao().pending().isEmpty())
        } finally { db.close() }
    }
    @Test fun serverReceiptAcknowledgesOriginalOperationWithoutAwardingClientCurrency() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, QuestDatabase::class.java).build()
        try {
            db.syncDao().ensureMeta(SyncMeta(userId = user))
            val post = id(); val chat = id()
            SocialStore.enqueueCommand(db, user, "post_send", post, JSONObject().put("id", post).put("chat_id", chat).put("content", "메시지"))
            val operation = db.syncDao().pending().single()
            val serverPost = JSONObject().put("id", post).put("chat_id", chat).put("author_id", user).put("actor", "user")
                .put("type", "chat").put("content", "메시지").put("created_at", "2026-11-03T00:00:00Z")
            val receipt = JSONObject().put("id", id()).put("event_id", operation.eventId).put("user_id", user).put("sync_seq", 2)
                .put("result_status", "applied").put("cash_delta", 0).put("xp_delta", 0)
                .put("changes", JSONArray().put(JSONObject().put("entity", "post").put("id", post).put("version", 0).put("data", serverPost)))
            db.withTransaction { SyncApply.event(db, receipt, operation) }
            assertTrue(db.syncDao().pending().isEmpty())
            assertEquals("메시지", JSONObject(db.syncDao().record("post", post)!!.body).getString("content"))
            assertEquals(0L, db.syncDao().meta()!!.confirmedCash)
            db.withTransaction { SyncApply.event(db, receipt) }
            db.openHelper.readableDatabase.query("SELECT COUNT(*) FROM confirmed_events").use { rows ->
                assertTrue(rows.moveToFirst()); assertEquals(1, rows.getInt(0))
            }
        } finally { db.close() }
    }
    @Test fun rejectedServerCommandStaysVisibleForExplicitResolution() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, QuestDatabase::class.java).build()
        try {
            db.syncDao().ensureMeta(SyncMeta(userId = user))
            val chat = id()
            SocialStore.enqueueCommand(db, user, "chat_read", chat, JSONObject().put("id", chat))
            val op = db.syncDao().pending().single()
            val receipt = JSONObject().put("id", id()).put("event_id", op.eventId).put("user_id", user).put("sync_seq", 2)
                .put("result_status", "rejected").put("result", JSONObject().put("error", "not_chat_member")).put("changes", JSONArray())
            db.withTransaction { SyncApply.event(db, receipt, op) }
            val retained = db.syncDao().targetOperations("social", chat).single()
            assertEquals(op.eventId, retained.eventId)
            assertEquals("rejected", retained.state)
            assertEquals("not_chat_member", retained.error)
        } finally { db.close() }
    }
    @Test fun confirmedPlanCommandFreezesExplicitConsentAndGoalPayload() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(context, QuestDatabase::class.java).build()
        try {
            db.syncDao().ensureMeta(SyncMeta(userId = user))
            val message = id()
            val goal = JSONObject().put("id", id()).put("title", "확인한 제안").put("nodes", JSONArray())
            val body = JSONObject().put("id", message).put("confirmed", true).put("goal", goal)
            SocialStore.enqueueCommand(db, user, "plan_confirm", message, body)
            goal.put("title", "나중에 바뀐 입력")
            val queued = db.syncDao().pending().single()
            val sealed = JSONObject(queued.body)
            assertTrue(sealed.getBoolean("confirmed"))
            assertEquals("확인한 제안", sealed.getJSONObject("goal").getString("title"))
            assertEquals("plan_confirm", queued.operation)
            assertEquals(0, db.goalDao().countGoals())
        } finally { db.close() }
    }
}
