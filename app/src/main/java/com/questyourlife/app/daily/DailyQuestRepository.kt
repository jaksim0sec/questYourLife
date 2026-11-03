package com.questyourlife.app.daily

import android.content.Context
import androidx.room.withTransaction
import com.questyourlife.app.sync.*
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.data.QuestDatabase
import java.time.LocalDate

class DailyQuestRepository(context: Context, private val profile: String = ProfileRegistry.get(context).file.value) {
    private val app=context.applicationContext
    private val db=QuestDatabase.open(context,profile)
    private val dao=db.dailyQuestDao()
    companion object { fun today(): String=LocalDate.now().toString() }
    suspend fun ensureToday(): String = db.withTransaction {
        val day=today()
        dao.carryUnfinished(day)
        val existing = dao.snapshotDay(day)
        val next = db.goalDao().nextCompanionSteps()
            .filter { step -> existing.none { it.goalId == step.goalId } }
            .take((3 - existing.size).coerceAtLeast(0))
        if (next.isNotEmpty()) {
            dao.insertAll(next.map { step ->
                DailyQuestEntity("${step.goalId}-${step.position}-$day", day, step.title,
                    "내 목표와 이어지는 오늘의 실행", 20,
                    goalId = step.goalId, stepPosition = step.position)
            })
        }
        if(dao.countForDay(day)==0) {
            val templates=listOf(
                Triple("단어 10개 복습하기","작게 시작하는 학습",15),
                Triple("일본어 영상 10분 보기","꾸준히 듣기 연습",20),
                Triple("산책 20분 하기","가볍게 움직이기",20),
                Triple("책 5쪽 읽기","부담 없는 독서",15),
                Triple("25분 집중하기","방해 없는 집중",20),
                Triple("오늘의 계획 정리하기","작은 하루를 관리하기",10),
                Triple("간단한 스트레칭하기","쉬어가는 한 걸음",10)
            )
            val offset=LocalDate.parse(day).dayOfYear%templates.size
            dao.insertAll((0 until 3).map { index->
                val item=templates[(offset+index)%templates.size]
                DailyQuestEntity(id="$day-${index+1}",dateKey=day,
                    title=item.first,detail=item.second,reward=item.third)
            })
        }
        dao.snapshotDay(day).forEach { SyncCapture.daily(db,it.id) }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId)
        day
    }
    suspend fun defer(id: String) = mutate(id) { dao.defer(id, LocalDate.now().plusDays(1).toString()) }
    suspend fun abandon(id: String) = mutate(id) { dao.abandon(id) }
    suspend fun reject(id: String) = mutate(id) { dao.reject(id, today()) }
    fun observeDay(dateKey:String)=dao.observeDay(dateKey)
    fun observeHistory()=dao.observeHistory()
    suspend fun snapshotDay(dateKey:String)=dao.snapshotDay(dateKey)
    fun observeCash()=dao.observeEarnedCash()
    fun observeCompletionCount()=dao.observeCompletedCount()
    suspend fun get(id:String)=dao.find(id)
    private suspend fun mutate(id: String, action: suspend () -> Int): Int {
        val changed=db.withTransaction { val count=action(); if(count>0) SyncCapture.daily(db,id); count }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId); return changed
    }
    suspend fun accept(id:String):DailyQuestEntity? {
        if(mutate(id) { dao.accept(id,today()) }==0) return null
        return dao.find(id)
    }
    suspend fun complete(id:String):DailyQuestEntity? {
        val result=db.withTransaction {
            val day=today(); dao.carryUnfinished(day)
            // Existing v4 accepted rows may be completed by an old notification before ensureToday.
            SyncCapture.daily(db,id)
            if(dao.completeAndAdvance(id,day,System.currentTimeMillis())==0) null
            else { SyncCapture.completedDaily(db,id); dao.find(id) }
        }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId); return result
    }
}
