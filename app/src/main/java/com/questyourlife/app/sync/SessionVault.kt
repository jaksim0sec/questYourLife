package com.questyourlife.app.sync

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONObject
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

data class AuthSession(val userId: String, val username: String, val accessToken: String, val refreshToken: String, val expiresAt: Long) {
    override fun toString() = "AuthSession([redacted])"
}

/** Only encrypted refresh/access tokens are persisted. Passwords are never passed to this class. */
class SessionVault(context: Context) {
    private val prefs = context.getSharedPreferences("qyl_session", Context.MODE_PRIVATE)
    private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getKey("qyl_session_v1", null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(KeyGenParameterSpec.Builder("qyl_session_v1", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build())
        }.generateKey()
    }
    fun load(): AuthSession? = runCatching {
        val stored = prefs.getString("sealed", null) ?: return null
        val parts = stored.split(':')
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, Base64.decode(parts[0], Base64.NO_WRAP)))
        val j = JSONObject(cipher.doFinal(Base64.decode(parts[1], Base64.NO_WRAP)).toString(Charsets.UTF_8))
        AuthSession(j.getString("id"), j.getString("username"), j.getString("access"), j.getString("refresh"), j.getLong("expires"))
    }.getOrNull()
    fun save(session: AuthSession) {
        val j = JSONObject().put("id", session.userId).put("username", session.username).put("access", session.accessToken).put("refresh", session.refreshToken).put("expires", session.expiresAt)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply { init(Cipher.ENCRYPT_MODE, key()) }
        val sealed = Base64.encodeToString(cipher.iv, Base64.NO_WRAP) + ":" + Base64.encodeToString(cipher.doFinal(j.toString().toByteArray()), Base64.NO_WRAP)
        check(prefs.edit().putString("sealed", sealed).commit()) { "session_save_failed" }
    }
    fun clear() { prefs.edit().clear().commit() }
}
