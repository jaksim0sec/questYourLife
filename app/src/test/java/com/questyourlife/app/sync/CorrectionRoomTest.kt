package com.questyourlife.app.sync

import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import com.questyourlife.app.data.QuestDatabase
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[28],manifest=Config.NONE)
class CorrectionRoomTest {
    @Test fun sharedBookUsesIndependentParticipationAndEditsOnlyOverlay() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),QuestDatabase::class.java).build()
        try {
            val user=UUID.randomUUID().toString();val author=UUID.randomUUID().toString()
            val root=UUID.randomUUID().toString();val join=UUID.randomUUID().toString()
            val chapter=UUID.randomUUID().toString();val main=UUID.randomUUID().toString()
            db.syncDao().ensureMeta(SyncMeta(userId=user))
            val tree=JSONArray().put(JSONObject().put("id",root).put("logical_id",root).put("type","book").put("name","Shared")
                .put("head",JSONObject.NULL).put("data",JSONObject()))
                .put(JSONObject().put("id",chapter).put("logical_id",chapter).put("type","chap").put("head",root).put("sort_order",1).put("name","Chapter"))
                .put(JSONObject().put("id",main).put("logical_id",main).put("type","main").put("head",chapter).put("sort_order",0).put("name","My overlay"))
            val book=JSONObject().put("id",root).put("logical_id",root).put("book_id",root).put("type","book").put("name","Shared")
                .put("owner_id",author).put("publication_status","published").put("data",JSONObject())
            val participation=JSONObject().put("id",join).put("book_id",root).put("user_id",user).put("status","active")
                .put("edit_version",2).put("effective_tree",tree).put("overrides",JSONObject("""{"patch":{},"added":[],"hidden":[]}"""))
            fun change(entity:String,id:String,body:JSONObject)=JSONObject().put("entity",entity).put("id",id).put("version",2).put("data",body)
            db.withTransaction { SyncApply.snapshot(db,JSONArray().put(change("questbook",root,book)).put(change("quest_join",join,participation))) }
            assertEquals(join,SyncCapture.participation(db,root))
            assertEquals("My overlay",db.goalDao().getSteps(root).single().title)
            db.withTransaction { SyncCapture.goal(db,root) }
            assertTrue(db.syncDao().pending().toString(),db.syncDao().pending().isEmpty())
            db.syncDao().putSteps(listOf(db.goalDao().getSteps(root).single().copy(title="Only my main changed")))
            db.withTransaction { SyncCapture.goal(db,root) }
            val operation=db.syncDao().pending().single()
            assertEquals("join_edit",operation.operation);assertEquals(join,operation.targetId)
            assertEquals(2L,JSONObject(operation.body).getLong("edit_version"))
            val patch=JSONObject(operation.body).getJSONObject("overrides").getJSONObject("patch")
            assertEquals(1,patch.length());assertTrue(patch.has(main))
            db.syncDao().acknowledge(operation.sequence)
            db.syncDao().record("quest_join",join)!!.also { db.syncDao().setRecord(it.copy(dirty=false)) }
            val dailyId=UUID.randomUUID().toString()
            val daily=JSONObject().put("id",dailyId).put("user_id",user).put("join_id",join).put("source_id",main)
                .put("scheduled_date","2026-11-03").put("content_snapshot",JSONObject().put("title","Historical criteria"))
                .put("status","completed").put("completed_at","2026-11-03T01:00:00Z")
            db.withTransaction { SyncApply.snapshot(db,JSONArray().put(change("daily",dailyId,daily))) }
            assertNotNull(db.goalDao().getSteps(root).single().completedAt)
            daily.put("status","accepted").put("completed_at",JSONObject.NULL)
            db.withTransaction { SyncApply.snapshot(db,JSONArray().put(change("daily",dailyId,daily).put("version",3))) }
            assertNull(db.goalDao().getSteps(root).single().completedAt)
            val visible=JSONArray().put(tree.getJSONObject(0)).put(tree.getJSONObject(1))
            participation.put("effective_tree",visible)
            db.withTransaction { SyncApply.snapshot(db,JSONArray().put(change("quest_join",join,participation).put("version",4))) }
            assertTrue(db.goalDao().getSteps(root).isEmpty())
            assertEquals("Historical criteria",db.syncDao().dailyBySync(dailyId)!!.title)
            assertEquals(1,db.goalDao().getAllSteps(root).size)
            val nextEdition=UUID.randomUUID().toString()
            val revised=JSONObject(book.toString()).put("id",nextEdition).put("book_id",nextEdition)
            participation.put("book_id",nextEdition)
            db.withTransaction { SyncApply.snapshot(db,JSONArray().put(change("questbook",nextEdition,revised))
                .put(change("quest_join",join,participation).put("version",5))) }
            assertEquals(1,db.goalDao().countGoals())
            assertEquals(root,db.syncDao().goalBySync(nextEdition)!!.id)
            assertEquals("Historical criteria",db.syncDao().dailyBySync(dailyId)!!.title)
        } finally { db.close() }
    }
}
