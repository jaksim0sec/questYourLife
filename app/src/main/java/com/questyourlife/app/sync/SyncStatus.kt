package com.questyourlife.app.sync

import androidx.room.withTransaction
import com.questyourlife.app.data.QuestDatabase
import org.json.JSONObject

object SyncStatus {
    suspend fun set(db: QuestDatabase,state: String) = db.withTransaction {
        db.syncDao().setRecord(RemoteRecord("sync_status","transport",body=JSONObject().put("state",state).put("at",System.currentTimeMillis()).toString()))
    }
}
