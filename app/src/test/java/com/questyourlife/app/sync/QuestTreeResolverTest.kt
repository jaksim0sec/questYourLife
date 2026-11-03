package com.questyourlife.app.sync

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class QuestTreeResolverTest {
    private fun base() = JSONArray("""[
      {"id":"r1","logical_id":"book","type":"book","head":null,"tags":["study"]},
      {"id":"c1","logical_id":"chap","type":"chap","head":"r1","tags":null},
      {"id":"m1","logical_id":"main","type":"main","head":"c1","name":"original","tags":[]},
      {"id":"d1","logical_id":"daily","type":"daily","head":"m1","name":"read"}
    ]""")
    @Test fun oneTitlePatchKeepsSharedIdentityAndDescendants() {
        val base=base()
        val result=QuestTreeResolver.resolve(base,JSONObject("""{"patch":{"main":{"name":"mine"}}}"""))
        assertEquals(4,result.length())
        val main=(0 until result.length()).map(result::getJSONObject).single { it.getString("logical_id")=="main" }
        assertEquals("main",main.getString("id"));assertEquals("m1",main.getString("physical_id"));assertEquals("mine",main.getString("name"))
        assertEquals("original",base.getJSONObject(2).getString("name"))
        val daily=(0 until result.length()).map(result::getJSONObject).single { it.getString("logical_id")=="daily" }
        assertEquals("main",daily.getString("head"));assertEquals(0,daily.getJSONArray("tags").length())
    }
    @Test fun additionsAndHiddenBranchesNeverDuplicatePhysicalDefinitions() {
        val result=QuestTreeResolver.resolve(base(),JSONObject("""{"added":[{"id":"custom","head":"chap","type":"main","name":"extra"}],"hidden":["main"]}"""))
        assertEquals(setOf("book","chap","custom"),(0 until result.length()).map { result.getJSONObject(it).getString("logical_id") }.toSet())
        assertEquals("study",(0 until result.length()).map(result::getJSONObject).single { it.getString("logical_id")=="custom" }.getJSONArray("tags").getString(0))
    }
    @Test fun editionsResolveSameLogicalParentWithoutCopying() {
        val version=base().toString().replace("r1","r2").replace("c1","c2").replace("m1","m2").replace("d1","d2")
        val overlay=JSONObject("""{"patch":{"main":{"name":"mine"}}}""")
        assertEquals(QuestTreeResolver.resolve(base(),overlay).length(),QuestTreeResolver.resolve(JSONArray(version),overlay).length())
    }
    @Test(expected=IllegalArgumentException::class) fun invalidReparentIsRejected() {
        QuestTreeResolver.resolve(base(),JSONObject("""{"patch":{"main":{"head":"book"}}}"""))
    }
    @Test(expected=IllegalArgumentException::class) fun changingIdentityIsRejected() {
        QuestTreeResolver.resolve(base(),JSONObject("""{"patch":{"main":{"owner_id":"attacker"}}}"""))
    }
}
