package com.questyourlife.app.companion

import org.json.JSONObject

/**
 * Secret delivery for clan_invite. The raw capability exists only in the outer RPC response,
 * never in an event receipt, snapshot, Outbox entry or persistent app preferences.
 */
data class ClanInvitation(val token: String, val clanId: String, val expiresAt: String) {
    companion object {
        fun parse(response: JSONObject, requestedClanId: String): ClanInvitation {
            val receipts = requireNotNull(response.optJSONArray("receipts")) { "missing_invitation_receipt" }
            require(receipts.length() == 1 && receipts.getJSONObject(0).optString("result_status") == "applied") {
                "invitation_rejected"
            }
            val delivered = requireNotNull(response.optJSONObject("invitation")) { "invitation_not_delivered" }
            val id = delivered.optString("clan_id")
            val secret = delivered.optString("token")
            val expiry = delivered.optString("expires_at")
            require(id == requestedClanId && secret.length in 32..128 && expiry.isNotBlank()) {
                "invalid_invitation_delivery"
            }
            return ClanInvitation(secret, id, expiry)
        }
    }
}
