package com.questyourlife.app.sync

import java.util.Locale
import java.util.UUID

data class CashBalances(val legacy: Long, val pending: Long, val confirmed: Long) {
    fun canSpend(price: Long) = price >= 0 && price <= confirmed
}
object AccountInput {
    fun username(value: String): String? = value.trim().lowercase(Locale.ROOT).takeIf { it.matches(Regex("[a-z0-9_]{3,24}")) }
    fun validPassword(value: String) = value.length in 8..128
}
object StableIds {
    fun child(parent: String, key: String): String = UUID.nameUUIDFromBytes("questyourlife:$parent:$key".toByteArray(Charsets.UTF_8)).toString()
}
