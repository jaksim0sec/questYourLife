package com.questyourlife.app.companion

import com.questyourlife.app.data.CompanionMessageEntity
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

data class SocialProfile(val id: String, val name: String, val itemId: Int)
data class SocialPublicBook(val id: String, val logicalId: String, val name: String, val participants: Int)
data class SocialAlarm(val id: String, val type: String, val title: String, val detail: String,
    val read: Boolean, val profileId: String?, val questId: String?, val createdAt: Long)
data class SocialChat(val id: String, val type: String, val name: String, val peerId: String?,
    val itemId: Int, val preview: String)
data class SocialClan(val id: String, val name: String, val members: List<SocialProfile>,
    val ownerId: String, val weeklyScore: Long?, val rank: Int?, val target: Long?)
data class SocialChestRight(val clanId: String, val clanName: String, val weekStart: String, val reward: Long)
data class SocialClanRank(val id: String, val name: String?, val score: Long, val rank: Int)
data class SocialMemberScore(val userId: String, val score: Long)

/** Only the RPC's public profile projection is mapped. Private user rows never become contacts. */
data class SocialSnapshot(val userId: String, val profiles: List<SocialProfile> = emptyList(),
    val alarms: List<SocialAlarm> = emptyList(), val chats: List<SocialChat> = emptyList(),
    val messages: List<CompanionMessageEntity> = emptyList(), val clans: List<SocialClan> = emptyList(),
    val loaded: Boolean = false, val popularBooks: List<SocialPublicBook> = emptyList(),
    val chestRights: List<SocialChestRight> = emptyList(),
    val clanRanking: List<SocialClanRank> = emptyList(),
    val memberScores: List<SocialMemberScore> = emptyList()) {
    val clan: SocialClan? get() = clans.firstOrNull()
    companion object {
        fun parse(userId: String, body: JSONObject): SocialSnapshot {
            val profiles = body.optJSONArray("public_profiles").objects().map { row ->
                SocialProfile(row.getString("id"), row.optString("name", "사용자"),
                    row.optJSONObject("equipped")?.optInt("item_id") ?: 0)
            }
            val byId = profiles.associateBy { it.id }
            val chatRows = body.optJSONArray("chats").objects().filter { row ->
                val members = row.optJSONObject("members") ?: return@filter false
                val own = members.optJSONObject(userId) ?: return@filter false
                own.isNull("left_at") && (row.optString("type") != "ai" || members.length() == 1) && row.isNull("deleted_at")
            }
            val allowed = chatRows.associateBy { it.getString("id") }
            val posts = body.optJSONArray("posts").objects().filter {
                it.optString("chat_id") in allowed && it.isNull("deleted_at") && it.optString("type", "chat") == "chat"
            }
            val chats = chatRows.map { row ->
                val id = row.getString("id")
                val peer = row.getJSONObject("members").keys().asSequence().firstOrNull { it != userId }
                val profile = byId[peer]
                SocialChat(id, row.getString("type"), if (row.getString("type") == "ai") "마스코트"
                    else row.optString("name").takeUnless { it.isBlank() || it == "null" } ?: profile?.name ?: "대화",
                    peer, profile?.itemId ?: 0,
                    posts.filter { it.optString("chat_id") == id }.maxByOrNull { it.optString("created_at") }?.optString("content") ?: "")
            }
            val alarms = body.optJSONArray("alarms").objects().filter { it.optString("user_id", userId) == userId }.map { row ->
                val payload = row.optJSONObject("payload") ?: JSONObject()
                SocialAlarm(row.getString("id"), row.optString("type"), payload.optString("title", "새 알림"),
                    payload.optString("body", payload.optString("text")), row.optInt("read") == 1,
                    payload.optionalString("user_id") ?: payload.optionalString("profile_id"),
                    payload.optionalString("daily_id"), time(row.optString("created_at")))
            }
            val rankings = body.optJSONArray("clan_ranking").objects()
            val clanRows = body.optJSONArray("clans").objects()
            val chestRights = clanRows.flatMap { row ->
                val chests = row.optJSONObject("chests") ?: return@flatMap emptyList()
                chests.keys().asSequence().mapNotNull { week ->
                    val entry = chests.optJSONObject(week) ?: return@mapNotNull null
                    val eligible = entry.optJSONObject("eligible")?.has(userId) == true
                    val reward = entry.optLong("reward")
                    if (!eligible || reward <= 0L) null else
                        SocialChestRight(row.getString("id"), row.getString("name"), week, reward)
                }.toList()
            }
            val clansById = clanRows.associateBy { it.optString("id") }
            val visibleRanking = rankings.mapNotNull { rank ->
                val clanId = rank.optString("clan_id", rank.optString("id"))
                if (clanId.isBlank() || !rank.has("score") || !rank.has("rank")) null
                else SocialClanRank(clanId, clansById[clanId]?.optString("name"), rank.optLong("score"), rank.optInt("rank"))
            }.sortedWith(compareBy({ it.rank }, { it.id }))
            val memberScores = body.optJSONArray("clan_member_scores").objects().mapNotNull { row ->
                val id = row.optString("user_id")
                if (id.isBlank() || !row.has("score")) null else SocialMemberScore(id, row.optLong("score"))
            }
            val clans = clanRows.filter { row ->
                val own = row.optJSONObject("members")?.optJSONObject(userId)
                own != null && own.isNull("left_at") && row.isNull("deleted_at")
            }.map { row ->
                val members = row.getJSONObject("members")
                val id = row.getString("id")
                val rank = rankings.firstOrNull { it.optString("id", it.optString("clan_id")) == id }
                SocialClan(id, row.getString("name"), members.keys().asSequence().filter {
                    members.optJSONObject(it)?.isNull("left_at") == true
                }.map { byId[it] ?: SocialProfile(it, "비공개 프로필", 0) }.toList(), row.getString("owner_id"),
                    rank?.let { if (it.has("score")) it.optLong("score") else null },
                    rank?.let { if (it.has("rank")) it.optInt("rank") else null },
                    row.optJSONObject("chest_policy")?.optLong("threshold")?.takeIf { it > 0 })
            }
            val popular = body.optJSONArray("popular_books").objects().mapNotNull { entry ->
                val book = entry.optJSONObject("book") ?: return@mapNotNull null
                SocialPublicBook(book.getString("id"), book.getString("logical_id"), book.getString("name"), entry.optInt("active_participants"))
            }
            return SocialSnapshot(userId, profiles, alarms, chats,
                posts.map { message(it, userId) }.sortedWith(compareBy({ it.createdAt }, { it.id })), clans, true, popular, chestRights, visibleRanking, memberScores)
        }
        fun message(row: JSONObject, userId: String): CompanionMessageEntity {
            val payload = row.optJSONObject("payload")
            val draft = if (row.optString("actor") == "assistant") payload?.optJSONObject("plan_draft") else null
            return CompanionMessageEntity(row.getString("id"), row.getString("chat_id"), row.optString("content"),
                row.optString("actor") == "user" && row.optString("author_id") == userId,
                time(row.optString("created_at")),
                draft?.let { it.optionalString("title") ?: it.optJSONObject("goal")?.optionalString("title") ?: "계획 초안" },
                draft?.let { SocialPlanDraft.previewSteps(row.getString("id"), it) },
                payload?.optionalString("applied_goal_id"))
        }
        fun time(value: String): Long = runCatching { Instant.parse(value).toEpochMilli() }.getOrDefault(0)
    }
}

internal fun JSONArray?.objects(): List<JSONObject> = if (this == null) emptyList() else
    (0 until length()).mapNotNull { optJSONObject(it) }
internal fun JSONObject.optionalString(key: String): String? = optString(key).takeUnless { it.isBlank() || it == "null" }
