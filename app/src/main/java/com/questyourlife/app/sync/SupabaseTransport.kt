package com.questyourlife.app.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI

object SupabaseConfig {
    const val URL = "https://xpqjjfcyiirospxzykqd.supabase.co"
    const val PUBLISHABLE_KEY = "sb_publishable_C8mypWDo7XBz8Lx-XnHg1w_robOEzl8"
}
class ApiFailure(val status: Int, val code: String) : Exception("HTTP $status: $code")

/** HTTPS-only, bounded bodies/timeouts. Never log requests, credentials, or token responses. */
class SupabaseTransport {
    suspend fun post(path: String, body: JSONObject, token: String? = null): JSONObject = withContext(Dispatchers.IO) {
        require(path.startsWith("/") && !path.startsWith("//"))
        val connection = URI(SupabaseConfig.URL + path).toURL().openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 12000
            connection.readTimeout = 20000
            connection.instanceFollowRedirects = false
            connection.setRequestProperty("apikey", SupabaseConfig.PUBLISHABLE_KEY)
            connection.setRequestProperty("Content-Type", "application/json")
            if (token != null) connection.setRequestProperty("Authorization", "Bearer $token")
            connection.doOutput = true
            connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val bytes = stream?.use { it.readNBytesCompat(4 * 1024 * 1024) } ?: byteArrayOf()
            val result = if (bytes.isEmpty()) JSONObject() else runCatching { JSONObject(bytes.toString(Charsets.UTF_8)) }.getOrDefault(JSONObject())
            if (status !in 200..299) throw ApiFailure(status, result.optString("error_code", result.optString("code", result.optString("error","request_failed"))))
            result
        } finally { connection.disconnect() }
    }
}
private fun java.io.InputStream.readNBytesCompat(limit: Int): ByteArray {
    val out = java.io.ByteArrayOutputStream()
    val buffer = ByteArray(8192)
    while (true) {
        val count = read(buffer)
        if (count < 0) break
        if (out.size() + count > limit) throw ApiFailure(502, "response_too_large")
        out.write(buffer, 0, count)
    }
    return out.toByteArray()
}
