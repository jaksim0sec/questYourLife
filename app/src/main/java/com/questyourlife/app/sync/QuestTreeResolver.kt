package com.questyourlife.app.sync

import org.json.JSONArray
import org.json.JSONObject

/** Pure resolver for cached definitions and offline overlays; SQL validates the same contract. */
object QuestTreeResolver {
    fun resolve(base: JSONArray, overrides: JSONObject = JSONObject()): JSONArray {
        val nodes = linkedMapOf<String, JSONObject>()
        val physical = mutableMapOf<String, String>()
        for (i in 0 until base.length()) {
            val raw = base.getJSONObject(i)
            val logical = raw.optString("logical_id", raw.getString("id"))
            require(logical !in nodes) { "duplicate_logical_id" }
            physical[raw.getString("id")] = logical
            nodes[logical] = JSONObject(raw.toString()).put("logical_id", logical)
                .put("physical_id", raw.opt("physical_id") ?: raw.getString("id")).put("id", logical)
        }
        nodes.values.forEach { node ->
            val parent = node.optString("head").takeUnless { it.isEmpty() || it == "null" }
            node.put("head", parent?.let { physical[it] ?: it } ?: JSONObject.NULL)
        }
        val added = overrides.optJSONArray("added") ?: JSONArray()
        for (i in 0 until added.length()) {
            val raw = added.getJSONObject(i); val id = raw.getString("id")
            require(id !in nodes && raw.getString("type") != "book") { "invalid_added_node" }
            nodes[id] = JSONObject(raw.toString()).put("logical_id", id)
        }
        val patch = overrides.optJSONObject("patch") ?: JSONObject()
        patch.keys().forEach { id ->
            val node = requireNotNull(nodes[id]) { "patch_missing_node" }
            val fields = patch.getJSONObject(id)
            fields.keys().forEach { key ->
                require(key in setOf("name", "head", "tags", "data", "sort_order")) { "immutable_node_field" }
                if(key=="data") {
                    val merged=JSONObject((node.optJSONObject("data") ?: JSONObject()).toString())
                    fields.getJSONObject(key).keys().forEach { field -> merged.put(field,fields.getJSONObject(key).get(field)) }
                    node.put(key,merged)
                } else node.put(key, fields.get(key))
            }
        }
        val hidden = overrides.optJSONArray("hidden") ?: JSONArray()
        val removed = (0 until hidden.length()).map { hidden.getString(it) }.toMutableSet()
        require(removed.all { it in nodes }) { "hidden_missing_node" }
        require(nodes.values.count { it.getString("type") == "book" } == 1) { "single_root_required" }
        val root = nodes.values.single { it.getString("type") == "book" }
        require(root.getString("logical_id") !in removed && root.isNull("head")) { "invalid_root" }
        // Validate before hiding so a malformed hidden branch cannot smuggle invalid overlays.
        val resolved = mutableSetOf<String>()
        fun visit(id: String, path: Set<String>) {
            if (id in resolved) return
            require(id !in path) { "quest_cycle" }
            val node = requireNotNull(nodes[id]) { "missing_parent" }
            val type = node.getString("type")
            val parentId = node.optString("head").takeUnless { it.isEmpty() || it == "null" }
            if (type != "book") {
                val parent = requireNotNull(nodes[parentId]) { "missing_parent" }
                val expected = mapOf("chap" to "book", "main" to "chap", "daily" to "main")[type]
                require(expected != null && parent.getString("type") == expected) { "invalid_parent_type" }
                visit(parentId!!, path + id)
                if (parentId in removed) removed.add(id)
                if (!node.has("tags") || node.isNull("tags")) node.put("tags", parent.optJSONArray("tags") ?: JSONArray())
            } else if (!node.has("tags") || node.isNull("tags")) node.put("tags", JSONArray())
            resolved.add(id)
        }
        nodes.keys.forEach { visit(it, emptySet()) }
        return JSONArray().also { result ->
            nodes.values.filter { it.getString("logical_id") !in removed && it.isNull("deleted_at") }
                .sortedWith(compareBy<JSONObject>({ it.optInt("sort_order") }, { it.getString("logical_id") }))
                .forEach(result::put)
        }
    }
}
