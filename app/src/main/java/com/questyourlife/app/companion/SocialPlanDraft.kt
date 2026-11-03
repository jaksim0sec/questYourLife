package com.questyourlife.app.companion

import com.questyourlife.app.sync.QuestTreeResolver
import com.questyourlife.app.sync.StableIds
import org.json.JSONArray
import org.json.JSONObject

/** Maps a structured proposal into a fresh personal tree. It never invents missing chapters or actions. */
internal object SocialPlanDraft {
    fun goal(messageId: String, draft: JSONObject): JSONObject {
        val source = draft.optJSONObject("goal") ?: draft
        val raw = source.optJSONArray("nodes")?.objects() ?: error("plan_draft_unsupported")
        require(raw.isNotEmpty() && raw.size <= 100) { "plan_draft_unsupported" }
        val roots = raw.filter { it.optString("type") == "book" }
        require(roots.size <= 1) { "plan_draft_unsupported" }
        val nodes = raw.filter { it.optString("type") != "book" }
        require(nodes.isNotEmpty()) { "plan_draft_unsupported" }
        val title = source.optionalString("title") ?: roots.singleOrNull()?.optionalString("name")
            ?: error("plan_draft_unsupported")
        require(title.length <= 300) { "plan_draft_unsupported" }
        val rootSource = source.optionalString("id") ?: roots.singleOrNull()?.optionalString("id") ?: run {
            val ids = nodes.map { it.getString("id") }.toSet()
            nodes.map { it.getString("head") }.filter { it !in ids }.distinct().singleOrNull()
        } ?: error("plan_draft_unsupported")
        val root = StableIds.child(messageId, "confirmed-plan-root")
        val idMap = linkedMapOf(rootSource to root)
        nodes.forEach { node ->
            val id = node.getString("id")
            require(id.isNotBlank() && id !in idMap) { "plan_draft_unsupported" }
            idMap[id] = StableIds.child(root, "node:$id")
        }
        val copied = JSONArray()
        val validation = JSONArray().put(JSONObject().put("id", root).put("type", "book").put("name", title).put("head", JSONObject.NULL))
        nodes.forEach { node ->
            require(node.optString("type") in setOf("chap", "main", "daily")) { "plan_draft_unsupported" }
            val name = node.getString("name")
            require(name.isNotBlank() && name.length <= 300) { "plan_draft_unsupported" }
            val parent = idMap[node.getString("head")] ?: error("plan_draft_unsupported")
            val mapped = JSONObject().put("id", idMap.getValue(node.getString("id"))).put("head", parent)
                .put("type", node.getString("type")).put("name", name)
                .put("position", node.optInt("sort_order", node.optInt("position")))
            if (node.has("data")) {
                require(node.optJSONObject("data") != null) { "plan_draft_unsupported" }
                mapped.put("data", JSONObject(node.getJSONObject("data").toString()))
            }
            if (node.has("tags")) {
                require(node.isNull("tags") || node.optJSONArray("tags") != null) { "plan_draft_unsupported" }
                mapped.put("tags", node.get("tags"))
            }
            copied.put(mapped)
            validation.put(mapped)
        }
        QuestTreeResolver.resolve(validation)
        return JSONObject().put("id", root).put("join_id", StableIds.child(root, "participation"))
            .put("title", title).put("description", source.optString("description", "")).put("nodes", copied).also { goal ->
                if (source.has("tags")) goal.put("tags", source.get("tags"))
                if (source.has("data")) {
                    require(source.optJSONObject("data") != null) { "plan_draft_unsupported" }
                    goal.put("data", JSONObject(source.getJSONObject("data").toString()))
                }
            }
    }
    fun previewSteps(messageId: String, draft: JSONObject): String? = runCatching {
        goal(messageId, draft).getJSONArray("nodes").objects().sortedWith(compareBy(
            { listOf("chap", "main", "daily").indexOf(it.getString("type")) }, { it.optInt("position") }
        )).joinToString("\n") { it.getString("name") }
    }.getOrNull()
}
