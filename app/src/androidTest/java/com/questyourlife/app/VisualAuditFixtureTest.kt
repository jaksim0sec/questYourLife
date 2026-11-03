package com.questyourlife.app

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.sync.AuthSession
import com.questyourlife.app.sync.ProfileRegistry
import com.questyourlife.app.sync.RemoteRecord
import com.questyourlife.app.sync.SessionVault
import com.questyourlife.app.sync.SyncMeta
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Isolated Android emulator screenshot fixture, not a shipping/demo user data source.
 * There are no production user IDs, credentials or server writes here.
 */
@RunWith(AndroidJUnit4::class)
class VisualAuditFixtureTest {
    @Test fun seedPrivateEmulatorVisualState() = runBlocking {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val owner = "00000000-0000-0000-0000-000000000001"
        val clanId = "00000000-0000-0000-0000-000000000051"
        val names = listOf("이모리", "반선생", "모찌", "하루", "다온", "초록", "팽이", "단비")
        val members = JSONObject()
        val publicProfiles = JSONArray()
        val scores = JSONArray()
        names.forEachIndexed { index, name ->
            val id = "00000000-0000-0000-0000-${(index + 1).toString().padStart(12, '0')}"
            members.put(id, JSONObject().put("role", if (index == 0) "owner" else "member")
                .put("joined_at", "2026-11-01T00:00:00Z").put("left_at", JSONObject.NULL))
            publicProfiles.put(JSONObject().put("id", id).put("name", name)
                .put("equipped", JSONObject().put("item_id",
                    listOf(1001, 1003, 0, 3, 2, 0, 1001, 2)[index])))
            scores.put(JSONObject().put("user_id", id).put("score",
                listOf(200, 160, 140, 100, 80, 60, 30, 10)[index]))
        }
        val clan = JSONObject().put("id", clanId).put("owner_id", owner)
            .put("name", "느슨한 갓생단").put("members", members)
            .put("chest_policy", JSONObject().put("threshold", 1000).put("reward", 50))
            .put("chests", JSONObject())
        val other = JSONObject().put("id", "00000000-0000-0000-0000-000000000052")
            .put("name", "모닝 피치").put("owner_id", "00000000-0000-0000-0000-000000000042")
            .put("members", JSONObject())
        val response = JSONObject().put("public_profiles", publicProfiles)
            .put("clans", JSONArray().put(clan).put(other))
            .put("clan_member_scores", scores)
            .put("clan_ranking", JSONArray()
                .put(JSONObject().put("clan_id", clanId).put("rank", 12).put("score", 780))
                .put(JSONObject().put("clan_id", other.getString("id"))
                    .put("rank", 13).put("score", 740)))
        val file = "quests-user-$owner.db"
        val db = QuestDatabase.open(context, file)
        db.syncDao().ensureMeta(SyncMeta(userId = owner))
        db.syncDao().setRecord(RemoteRecord("social_snapshot", owner, body = response.toString()))
        db.syncDao().setRecord(RemoteRecord("user", owner,
            body = JSONObject().put("name", "이모리").put("id", owner).toString()))
        ProfileRegistry.get(context).activate(file)
        context.getSharedPreferences("qyl_profiles", Context.MODE_PRIVATE).edit()
            .putString("user:$owner", file).putString("active", file).commit()
        context.getSharedPreferences("qyl_onboarding", Context.MODE_PRIVATE).edit()
            .putBoolean("finished", true).commit()
        SessionVault(context).save(AuthSession(owner, "visual_audit", "fixture-not-a-token",
            "fixture-not-a-refresh-token", Long.MAX_VALUE))
    }
}
