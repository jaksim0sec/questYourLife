package com.questyourlife.app.companion

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class ClanInvitationTest {
    private val clanId = "00000000-0000-0000-0000-000000000011"
    private val token = "a".repeat(64)
    private fun response(status: String = "applied") = JSONObject()
        .put("receipts", JSONArray().put(JSONObject().put("result_status", status)))
        .put("invitation", JSONObject().put("clan_id", clanId).put("token", token)
            .put("expires_at", "2026-11-10T09:00:00Z"))

    @Test fun acceptedInvitationExposesTokenExactlyOnce() {
        assertEquals(ClanInvitation(token, clanId, "2026-11-10T09:00:00Z"),
            ClanInvitation.parse(response(), clanId))
    }

    @Test fun rejectedReceiptNeverExposesSecret() {
        assertThrows(IllegalArgumentException::class.java) {
            ClanInvitation.parse(response("rejected"), clanId)
        }
    }

    @Test fun missingDeliveryIsNotPretendedToBeSuccessful() {
        assertThrows(IllegalArgumentException::class.java) {
            ClanInvitation.parse(response().apply { remove("invitation") }, clanId)
        }
    }

    @Test fun tokenForDifferentClanCannotBeShared() {
        assertThrows(IllegalArgumentException::class.java) {
            ClanInvitation.parse(response(), "00000000-0000-0000-0000-000000000022")
        }
    }
}
