package com.questyourlife.app.sync

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AccountRepository private constructor(private val context: Context) {
    private val vault = SessionVault(context)
    private val session = MutableStateFlow(vault.load())
    val state = session.asStateFlow()
    private val mutex = Mutex()
    private val api = SupabaseTransport()
    init { session.value?.let { ProfileRegistry.get(context).resume(it.userId) } }
    suspend fun signIn(name: String, password: String, create: Boolean) = withContext(Dispatchers.IO) {
        val username = AccountInput.username(name) ?: throw IllegalArgumentException("invalid_username")
        require(AccountInput.validPassword(password)) { "invalid_password" }
        mutex.withLock {
            val response=api.post("/functions/v1/questyourlife-account",JSONObject().put("action",if(create) "signup" else "signin").put("username",username).put("password",password))
            val next = decode(response, username)
            val bootstrap=api.post("/rest/v1/rpc/questyourlife_bootstrap", JSONObject(), next.accessToken)
            // Verify backend and ownership before activating another profile or persisting its session.
            val (_,file) = ProfileRegistry.get(context).bind(next.userId,bootstrap.getJSONObject("user"))
            vault.save(next)
            session.value = next
            com.questyourlife.app.daily.DailyQuestNotifications.serialized { ProfileRegistry.get(context).activate(file) }
            SyncScheduler.enqueue(context,file,next.userId)
        }
    }
    suspend fun accessToken(userId: String, forceRefresh: Boolean = false): String = mutex.withLock {
        val current = session.value ?: throw ApiFailure(401, "login_required")
        if (current.userId != userId) throw ApiFailure(401, "account_scope_mismatch")
        if (!forceRefresh && current.expiresAt > System.currentTimeMillis() + 30000) return@withLock current.accessToken
        val next = decode(api.post("/auth/v1/token?grant_type=refresh_token", JSONObject().put("refresh_token", current.refreshToken)), current.username)
        check(next.userId == userId) { "account_scope_mismatch" }
        vault.save(next); session.value = next
        next.accessToken
    }
    suspend fun signOut() = withContext(Dispatchers.IO) {
        mutex.withLock { vault.clear(); session.value = null; com.questyourlife.app.daily.DailyQuestNotifications.serialized { ProfileRegistry.get(context).guest() } }
    }
    private fun decode(j: JSONObject, username: String) = AuthSession(j.getJSONObject("user").getString("id"), username,
        j.getString("access_token"), j.getString("refresh_token"), System.currentTimeMillis() + j.getLong("expires_in") * 1000)
    companion object {
        @Volatile private var instance: AccountRepository? = null
        fun get(context: Context): AccountRepository = instance ?: synchronized(this) {
            instance ?: AccountRepository(context.applicationContext).also { instance = it }
        }
    }
}
