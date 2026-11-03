package com.questyourlife.app.companion

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class SocialSnapshotTest {
    private val owner = "00000000-0000-0000-0000-000000000001"
    private val peer = "00000000-0000-0000-0000-000000000002"
    private fun chat(id: String, type: String, vararg users: String) = JSONObject().put("id", id).put("type", type)
        .put("members", JSONObject().also { members -> users.forEach { members.put(it, JSONObject().put("role", "member")) } })
    private fun post(id: String, chat: String) = JSONObject().put("id", id).put("chat_id", chat).put("type", "chat")
        .put("author_id", owner).put("actor", "user").put("content", "비공개 메시지").put("created_at", "2026-11-03T00:00:00Z")

    @Test fun unrelatedAiAndNonmemberPostsAreNeverProjected() {
        val body = JSONObject().put("chats", JSONArray()
            .put(chat("mine", "ai", owner)).put(chat("other", "ai", peer)).put(chat("invalid-ai", "ai", owner, peer)))
            .put("posts", JSONArray().put(post("visible", "mine")).put(post("secret", "other")).put(post("invalid", "invalid-ai")))
        val projected = SocialSnapshot.parse(owner, body)
        assertEquals(listOf("mine"), projected.chats.map { it.id })
        assertEquals(listOf("visible"), projected.messages.map { it.id })
        assertTrue(projected.messages.single().fromUser)
    }
    @Test fun leftAndDeletedChannelsCannotRevealCachedHistory() {
        val left = chat("left", "direct", owner, peer).apply {
            getJSONObject("members").getJSONObject(owner).put("left_at", "2026-11-02T00:00:00Z")
        }
        val deleted = chat("deleted", "group", owner, peer).put("deleted_at", "2026-11-02T00:00:00Z")
        val projected = SocialSnapshot.parse(owner, JSONObject().put("chats", JSONArray().put(left).put(deleted))
            .put("posts", JSONArray().put(post("old-left", "left")).put(post("old-deleted", "deleted"))))
        assertTrue(projected.chats.isEmpty())
        assertTrue(projected.messages.isEmpty())
    }
    @Test fun absentPublicProjectionDoesNotFallBackToPrivateUserOrFixtures() {
        val body = JSONObject().put("user", JSONObject().put("id", peer).put("name", "민감한 이름").put("cash", 9999))
            .put("clans", JSONArray().put(JSONObject().put("id", "clan").put("name", "클랜").put("owner_id", owner)
                .put("members", JSONObject().put(owner, JSONObject()).put(peer, JSONObject()))))
        val projected = SocialSnapshot.parse(owner, body)
        assertTrue(projected.profiles.isEmpty())
        assertEquals("비공개 프로필", projected.clan!!.members.first { it.id == peer }.name)
        assertTrue(projected.chats.isEmpty())
        assertTrue(projected.alarms.isEmpty())
        assertNull(projected.clan!!.weeklyScore)
        assertNull(projected.clan!!.rank)
    }
    @Test fun inboxDropsOtherRecipientsAndUsesOnlyPublicOutfits() {
        val alarms = JSONArray().put(JSONObject().put("id", "mine").put("user_id", owner).put("read", 1)
            .put("payload", JSONObject().put("title", "알림").put("user_id", peer)))
            .put(JSONObject().put("id", "other").put("user_id", peer))
        val projected = SocialSnapshot.parse(owner, JSONObject().put("alarms", alarms)
            .put("public_profiles", JSONArray().put(JSONObject().put("id", peer).put("name", "공개 이름")
                .put("equipped", JSONObject().put("item_id", 1001)))))
        assertEquals(listOf("mine"), projected.alarms.map { it.id })
        assertTrue(projected.alarms.single().read)
        assertEquals(1001, projected.profiles.single().itemId)
    }
    @Test fun popularityComesFromServerJoinAggregate() {
        val projected = SocialSnapshot.parse(owner, JSONObject().put("popular_books", JSONArray().put(JSONObject()
            .put("active_participants", 2).put("book", JSONObject().put("id", "edition-2").put("logical_id", "stable-book").put("name", "공개 책")))))
        assertEquals(SocialPublicBook("edition-2", "stable-book", "공개 책", 2), projected.popularBooks.single())
        assertTrue(SocialSnapshot.parse(owner, JSONObject()).popularBooks.isEmpty())
    }
    @Test fun receiptTombstoneCannotBeOverwrittenByOlderSnapshot() {
        val original = post("post", "chat")
        val deleted = JSONObject(original.toString()).put("deleted_at", "2026-11-03T01:00:00Z").put("content", "[deleted]")
        val merged = SocialStore.mergePosts(listOf(deleted), listOf(original))
        assertEquals("[deleted]", merged.getJSONObject(0).getString("content"))
    }
    @Test fun policyTargetComesFromServerChestPolicy() {
        val clan = JSONObject().put("id", "my-clan").put("name", "실제 클랜").put("owner_id", owner)
            .put("members", JSONObject().put(owner, JSONObject()))
            .put("chest_policy", JSONObject().put("threshold", 100).put("reward", 50))
            .put("chests", JSONObject())
        val snapshot = SocialSnapshot.parse(owner, JSONObject().put("clans", JSONArray().put(clan)))
        assertEquals(100L, snapshot.clan!!.target)
    }

    @Test fun formerMemberCanSeeSettledChestRight() {
        val settled = JSONObject().put("reward", 50).put("week_start", "2026-10-26")
            .put("eligible", JSONObject().put(owner, JSONObject().put("eligible_at", "2026-11-02T00:00:00Z")))
        val clan = JSONObject().put("id", "former-clan").put("name", "예전 클랜")
            .put("owner_id", peer).put("members", JSONObject())
            .put("chests", JSONObject().put("2026-10-26", settled))
        val snap = SocialSnapshot.parse(owner, JSONObject().put("clans", JSONArray().put(clan)))
        assertNull(snap.clan)
        assertEquals(listOf(SocialChestRight("former-clan", "예전 클랜", "2026-10-26", 50L)),
            snap.chestRights)
    }

    @Test fun unrelatedMemberCannotSeeAnotherPersonsRights() {
        val settled = JSONObject().put("reward", 50).put("week_start", "2026-10-26")
            .put("eligible", JSONObject().put(peer, JSONObject()))
        val clan = JSONObject().put("id", "foreign-clan").put("name", "공개 클랜")
            .put("owner_id", peer).put("members", JSONObject())
            .put("chests", JSONObject().put("2026-10-26", settled))
        assertTrue(SocialSnapshot.parse(owner, JSONObject().put("clans", JSONArray().put(clan)))
            .chestRights.isEmpty())
    }

}
