package com.questyourlife.app.sync

import android.app.NotificationManager
import android.content.Context
import androidx.room.withTransaction
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/** The original quests.db is preserved; other identities never share its rows. */
class ProfileRegistry private constructor(private val context: Context) {
    private val prefs = context.getSharedPreferences("qyl_profiles", Context.MODE_PRIVATE)
    private val mutableFile = MutableStateFlow(prefs.getString("active", "quests.db")!!)
    val file = mutableFile.asStateFlow()
    suspend fun bind(userId: String, profile: org.json.JSONObject? = null, importGuest: Boolean = false): Pair<QuestDatabase,String> {
        UUID.fromString(userId)
        val active = QuestDatabase.open(context, file.value)
        val bound = active.syncDao().meta()?.userId
        val known = prefs.getString("user:$userId", null)
        if (importGuest) {
            check(known == null) { "existing_account_requires_explicit_merge" }
            check(bound == null) { "guest_import_requires_unbound_profile" }
        }
        // Never attach anonymous data to a different account without an explicit choice.
        val next = known ?: when {
            bound == userId -> file.value
            importGuest -> file.value
            else -> "quests-user-$userId.db"
        }
        val db = QuestDatabase.open(context, next)
        db.withTransaction {
            db.syncDao().ensureMeta()
            val meta = db.syncDao().meta()!!
            check(meta.userId == null || meta.userId == userId) { "account_scope_mismatch" }
            db.syncDao().setMeta(meta.copy(userId = userId))
            if(profile!=null) {
                check(profile.getString("id")==userId) { "profile_identity_mismatch" }
                SyncApply.snapshot(db,org.json.JSONArray().put(org.json.JSONObject().put("entity","user").put("id",userId)
                    .put("version",profile.getLong("version")).put("deleted",false).put("data",profile)))
            }
            if (meta.userId == null) {
                SyncCapture.bootstrap(db)
                val pending=db.syncDao().pending(100000).map { op ->
                    if(op.operation in setOf("settings_patch","equip")) {
                        val body=org.json.JSONObject(op.body).put("id",userId)
                        op.copy(targetId=userId,body=body.toString(),baseVersion=if(profile?.optLong("sync_seq")==1L) profile.getLong("version") else 0)
                    } else op
                }
                check(pending.all { it.sealedRequest==null }) { "unbound_sealed_operation" }
                val ordered=pending.sortedWith(compareBy<SyncOperation> { when {
                    it.operation=="goal_put" -> 0
                    it.operation=="daily_put" && org.json.JSONObject(it.body).optBoolean("legacy") -> 1
                    it.operation=="daily_put" -> 2
                    it.operation in setOf("daily_complete","step_complete") -> 3
                    else -> 4
                } }.thenBy { it.sequence })
                pending.forEach { db.syncDao().acknowledge(it.sequence) }
                ordered.forEach { db.syncDao().enqueue(it.copy(sequence=0)) }
            }
        }
        val edit = prefs.edit().putString("user:$userId", next)
        if (importGuest) {
            if (prefs.getString("last_guest", null) == next) edit.remove("last_guest")
        } else if (bound == null && file.value != next) {
            // A guest who skips import must be able to open the original records again.
            edit.putString("last_guest", file.value)
        }
        edit.apply()
        return db to next
    }
    fun activate(file: String) {
        prefs.edit().putString("active",file).apply()
        context.getSystemService(NotificationManager::class.java).cancelAll()
        mutableFile.value=file
    }
    fun resume(userId: String) { prefs.getString("user:$userId",null)?.let { if(it!=file.value) activate(it) } }
    fun guest() {
        // Only unbound profiles can be remembered here. Importing a guest clears this pointer.
        val next = prefs.getString("last_guest", null) ?: "quests-guest-${UUID.randomUUID()}.db"
        prefs.edit().putString("active", next).putString("last_guest", next).apply()
        context.getSystemService(NotificationManager::class.java).cancelAll()
        mutableFile.value = next
    }
    companion object {
        @Volatile private var instance: ProfileRegistry? = null
        fun get(context: Context): ProfileRegistry = instance ?: synchronized(this) {
            instance ?: ProfileRegistry(context.applicationContext).also { instance = it }
        }
    }
}
