package com.questyourlife.app.companion

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class SocialPlanDraftTest {
    private val message = "10000000-0000-0000-0000-000000000001"
    private fun draft() = JSONObject().put("title", "원래 제안 제목").put("description", "하루 10분")
        .put("id", "draft-root").put("nodes", JSONArray()
            .put(JSONObject().put("id", "chapter").put("head", "draft-root").put("type", "chap").put("name", "원래 챕터").put("position", 1))
            .put(JSONObject().put("id", "main").put("head", "chapter").put("type", "main").put("name", "원래 단계").put("position", 2))
            .put(JSONObject().put("id", "daily").put("head", "main").put("type", "daily").put("name", "원래 실천").put("position", 1)
                .put("data", JSONObject().put("required_count", 2).put("estimated_minutes", 10)
                    .put("criteria", JSONObject().put("method", "self_check").put("description", "다섯 글자 읽기")))))
    @Test fun confirmationMapsOriginalTreeToFreshStableIdsAndPreservesCriteria() {
        val original = draft()
        val before = original.toString()
        val first = SocialPlanDraft.goal(message, original)
        val retry = SocialPlanDraft.goal(message, original)
        assertEquals(first.toString(), retry.toString())
        assertEquals(before, original.toString())
        assertEquals("원래 제안 제목", first.getString("title"))
        assertNotEquals(first.getString("id"), first.getString("join_id"))
        val nodes = first.getJSONArray("nodes")
        assertEquals(first.getString("id"), nodes.getJSONObject(0).getString("head"))
        assertEquals(nodes.getJSONObject(0).getString("id"), nodes.getJSONObject(1).getString("head"))
        assertEquals(nodes.getJSONObject(1).getString("id"), nodes.getJSONObject(2).getString("head"))
        assertEquals(original.getJSONArray("nodes").getJSONObject(2).getJSONObject("data").toString(),
            nodes.getJSONObject(2).getJSONObject("data").toString())
        assertNotEquals(first.getString("id"), SocialPlanDraft.goal("another-message", original).getString("id"))
    }
    @Test fun titleOnlyDraftIsExplicitlyUnsupportedAndNeverCreatesInventedSteps() {
        val proposal = JSONObject().put("title", "제목만 있는 초안")
        assertNull(SocialPlanDraft.previewSteps(message, proposal))
        val row = JSONObject().put("id", message).put("chat_id", "ai-channel").put("actor", "assistant")
            .put("payload", JSONObject().put("plan_draft", proposal))
        val mapped = SocialSnapshot.message(row, "owner")
        assertEquals("제목만 있는 초안", mapped.planTitle)
        assertNull(mapped.planSteps)
    }
    @Test fun userPayloadCannotImpersonateAnAssistantPlan() {
        val row = JSONObject().put("id", message).put("chat_id", "ai-channel").put("actor", "user").put("author_id", "owner")
            .put("payload", JSONObject().put("plan_draft", draft()))
        val mapped = SocialSnapshot.message(row, "owner")
        assertNull(mapped.planTitle)
        assertNull(mapped.planSteps)
    }
    @Test fun invalidParentIsNotRepairedByFabricatingNodes() {
        val invalid = draft()
        invalid.getJSONArray("nodes").getJSONObject(2).put("head", "chapter")
        assertNull(SocialPlanDraft.previewSteps(message, invalid))
    }
    @Test fun appliedReceiptMarksProposalAsAppliedWithoutRunningAClientGoalMutation() {
        val row = JSONObject().put("id", message).put("chat_id", "ai-channel").put("actor", "assistant")
            .put("payload", JSONObject().put("plan_draft", draft()).put("applied_goal_id", "confirmed-goal"))
        val mapped = SocialSnapshot.message(row, "owner")
        assertEquals("confirmed-goal", mapped.appliedGoalId)
        assertEquals("원래 챕터\n원래 단계\n원래 실천", mapped.planSteps)
    }
}
