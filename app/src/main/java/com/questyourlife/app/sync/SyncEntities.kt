package com.questyourlife.app.sync

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity(tableName = "sync_meta")
data class SyncMeta(@PrimaryKey val id: Int = 1, val userId: String? = null, val cursor: Long = 0,
    val epoch: String? = null, val confirmedCash: Long = 0, val confirmedXp: Long = 0,
    val legacyCash: Long = 0, val legacyXp: Long = 0, val appearance: String = "system", val prefsImported: Boolean = false, val equipped: Int = 0)

@Entity(tableName = "sync_outbox", indices = [Index(value = ["eventId"], unique = true), Index(value = ["state", "sequence"])])
data class SyncOperation(@PrimaryKey(autoGenerate = true) val sequence: Long = 0,
    val eventId: String = UUID.randomUUID().toString(), val operation: String, val entity: String,
    val targetId: String, val localId: String, val body: String, val baseVersion: Long = 0,
    val sealedRequest: String? = null, val state: String = "queued", val leaseUntil: Long = 0,
    val attempts: Int = 0, val error: String? = null, val conflictSnapshot: String? = null)

@Entity(tableName = "sync_remote_records", primaryKeys = ["entity", "id"])
data class RemoteRecord(val entity: String, val id: String, val version: Long = 0,
    val body: String = "{}", val localHash: String? = null, val localId: String? = null, val dirty: Boolean = false, val deleted: Boolean = false)

@Entity(tableName = "confirmed_events", indices = [Index(value = ["eventId"], unique = true)])
data class ConfirmedEvent(@PrimaryKey val id: String, val eventId: String, val sequence: Long,
    val cashDelta: Long, val xpDelta: Long, val status: String, val body: String)

@Dao
interface SyncDao {
    @Query("SELECT * FROM sync_meta WHERE id=1") fun observeMeta(): Flow<SyncMeta?>
    @Query("SELECT * FROM sync_meta WHERE id=1") suspend fun meta(): SyncMeta?
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun ensureMeta(meta: SyncMeta = SyncMeta())
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun setMeta(meta: SyncMeta)
    @Insert suspend fun enqueue(operation: SyncOperation): Long
    @Query("UPDATE sync_outbox SET state='queued',leaseUntil=0 WHERE state='in_flight'") suspend fun recoverLeases()
    @Query("SELECT * FROM sync_outbox WHERE state IN ('queued','in_flight') ORDER BY sequence LIMIT :limit") suspend fun pending(limit: Int = 25): List<SyncOperation>
    @Query("SELECT * FROM sync_outbox WHERE state IN ('conflict','rejected') ORDER BY sequence") fun observeProblems(): Flow<List<SyncOperation>>
    @Query("SELECT q.* FROM sync_outbox q WHERE q.state IN ('queued','in_flight') AND q.sequence>:after AND NOT EXISTS (SELECT 1 FROM sync_outbox b WHERE b.entity=q.entity AND b.targetId=q.targetId AND b.state IN ('conflict','rejected')) ORDER BY q.sequence LIMIT 25") suspend fun runnable(after: Long): List<SyncOperation>
    @Query("UPDATE sync_outbox SET baseVersion=:version WHERE entity='user' AND targetId=:id AND sequence>:after AND baseVersion=:expected AND sealedRequest IS NULL AND state='queued'") suspend fun advanceOwnUser(id: String,after: Long,expected: Long,version: Long)
    @Query("SELECT COUNT(*) FROM sync_outbox WHERE state IN ('queued','in_flight')") fun observePendingCount(): Flow<Int>
    @Query("SELECT COUNT(*)*20 FROM sync_outbox WHERE operation IN ('daily_complete','step_complete') AND state IN ('queued','in_flight')") fun observePendingCash(): Flow<Long>
    @Query("SELECT COUNT(*) FROM sync_outbox WHERE state IN ('queued','in_flight','conflict','rejected') AND entity=:entity AND targetId=:id AND sequence<>:except") suspend fun hasPending(entity: String,id: String,except: Long = -1): Int
    @Query("SELECT * FROM sync_remote_records WHERE entity=:entity AND id=:id") suspend fun record(entity: String,id: String): RemoteRecord?
    @Query("SELECT * FROM sync_remote_records WHERE entity=:entity") suspend fun records(entity: String): List<RemoteRecord>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun setRecord(record: RemoteRecord)
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun recordEvent(event: ConfirmedEvent): Long
    @Query("SELECT COUNT(*) FROM sync_outbox WHERE operation='settings_patch'") suspend fun pendingSettings(): Int
    @Query("UPDATE owned_items SET source=CASE WHEN source='legacy' THEN 'legacy_confirmed' ELSE 'confirmed' END WHERE itemId=:id") suspend fun confirmItem(id: Int)
    @Update suspend fun updateOperation(operation: SyncOperation)
    @Query("DELETE FROM sync_outbox WHERE sequence=:sequence") suspend fun acknowledge(sequence: Long)
    @Query("UPDATE sync_outbox SET baseVersion=:version WHERE entity=:entity AND targetId=:id AND sequence>:after AND sealedRequest IS NULL AND state='queued'") suspend fun successorVersions(entity: String,id: String,after: Long,version: Long)
    @Query("SELECT COUNT(*) FROM sync_outbox WHERE entity=:entity AND targetId=:id AND state IN ('conflict','rejected')") suspend fun blocked(entity: String,id: String): Int
    @Query("SELECT * FROM sync_outbox WHERE entity=:entity AND targetId=:id ORDER BY sequence") suspend fun targetOperations(entity: String,id: String): List<SyncOperation>
    @Query("SELECT * FROM quests") suspend fun allQuick(): List<com.questyourlife.app.data.QuestEntity>
    @Query("SELECT * FROM quests WHERE syncId=:id") suspend fun quick(id: String): com.questyourlife.app.data.QuestEntity?
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun putQuick(quest: com.questyourlife.app.data.QuestEntity)
    @Query("SELECT COUNT(*) FROM sync_outbox WHERE operation=:operation") suspend fun pendingOperation(operation: String): Int
    @Query("SELECT * FROM sync_remote_records WHERE entity=:entity AND id=:id") fun observeRecord(entity: String,id: String): Flow<RemoteRecord?>
    @Query("SELECT * FROM daily_quests WHERE id=:id") suspend fun daily(id: String): com.questyourlife.app.data.DailyQuestEntity?
    @Query("SELECT * FROM daily_quests WHERE syncId=:id") suspend fun dailyBySync(id: String): com.questyourlife.app.data.DailyQuestEntity?
    @Query("SELECT * FROM daily_quests WHERE stepSyncId=:id AND completedAt IS NULL") suspend fun unfinishedForStep(id: String): List<com.questyourlife.app.data.DailyQuestEntity>
    @Query("SELECT * FROM daily_quests") suspend fun allDaily(): List<com.questyourlife.app.data.DailyQuestEntity>
    @Query("SELECT * FROM goals WHERE id=:id") suspend fun goal(id: String): com.questyourlife.app.data.GoalEntity?
    @Query("SELECT * FROM goals WHERE syncId=:id") suspend fun goalBySync(id: String): com.questyourlife.app.data.GoalEntity?
    @Query("SELECT * FROM goals WHERE deletedAt IS NULL") suspend fun allGoals(): List<com.questyourlife.app.data.GoalEntity>
    @Query("SELECT * FROM stable_goal_steps WHERE syncId=:id") suspend fun stepBySync(id: String): com.questyourlife.app.data.GoalStepEntity?
    @Query("UPDATE daily_quests SET stepSyncId=:step WHERE id=:id") suspend fun linkStep(id: String, step: String)
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun putGoal(goal: com.questyourlife.app.data.GoalEntity)
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun putSteps(steps: List<com.questyourlife.app.data.GoalStepEntity>)
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun putDaily(daily: com.questyourlife.app.data.DailyQuestEntity)
}
