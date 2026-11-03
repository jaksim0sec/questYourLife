package com.questyourlife.app.daily

import android.content.Context
import androidx.room.withTransaction
import com.questyourlife.app.data.*
import com.questyourlife.app.sync.*
import java.util.UUID

class GoalRepository(context: Context) {
    private val app = context.applicationContext
    private val profile=ProfileRegistry.get(app).file.value
    private val db = QuestDatabase.open(app,profile)
    val dao = db.goalDao()
    private val prefs = app.getSharedPreferences("quest_main_settings", Context.MODE_PRIVATE)
    fun watchGoals() = dao.watchGoals()
    fun watchSteps() = dao.watchSteps()
    fun watchItems() = dao.watchItems()
    fun watchSessions() = dao.watchSessions()
    suspend fun seedDemoIfNew() {
        if (prefs.getBoolean("example_seeded", false)) return
        if (dao.countGoals() == 0) {
            val id = "example-japanese"
            val titles = listOf("일본어 영상 10분 보기","단어 10개 복습하기","간단한 문장 5개 말하기","일본어로 일기 쓰기","짧은 일상 대화 따라 말하기","상황별 표현 연습하기","일본어로 대화 10분 도전")
            dao.addGoal(GoalEntity(id,"일본어로 자유롭게 대화하기","일상 대화를 자연스럽게 이어갈 수 있도록 연습해요.",System.currentTimeMillis()),
                titles.mapIndexed { i,title -> GoalStepEntity(id,i,i*5/titles.size+1,title,if(i>3)30 else 20) })
        }
        prefs.edit().putBoolean("example_seeded", true).apply()
    }
    suspend fun create(title: String) {
        val name = title.trim().take(90); if(name.isBlank()) return
        val id=UUID.randomUUID().toString()
        val titles=listOf("$name 준비하기","첫 연습 기록하기","작은 결과물 만들기","진행 상황 점검하기","부족한 부분 보완하기","다시 도전하고 개선하기","최종 목표 마무리하기")
        db.withTransaction {
            dao.addGoal(GoalEntity(id,name,"직접 만든 목표 · 단계는 임시 가이드이며 AI 생성이 아닙니다.",System.currentTimeMillis()),
                titles.mapIndexed { i,text -> GoalStepEntity(id,i,i*5/titles.size+1,text,20) })
            SyncCapture.goal(db,id)
        }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId)
    }
    suspend fun complete(stableId: String): GoalCompletion? {
        val result=db.withTransaction {
            val step=dao.step(stableId) ?: return@withTransaction null
            db.syncDao().unfinishedForStep(stableId).forEach { SyncCapture.daily(db,it.id) }
            val complete=dao.completeStable(stableId)
            if(complete!=null) SyncCapture.completedStep(db,step)
            complete
        }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId); return result
    }
    suspend fun buy(itemId: Int, price: Int): Boolean = db.withTransaction {
        db.syncDao().ensureMeta(); val meta=db.syncDao().meta()!!
        if(meta.userId!=null || meta.legacyCash<price) return@withTransaction false
        val bought=dao.buyItem(itemId,price)
        if(bought) db.syncDao().setMeta(meta.copy(legacyCash=meta.legacyCash-price))
        bought
    }
    suspend fun requestPurchase(itemId: Int) {
        db.withTransaction { SyncCapture.enqueue(db,"purchase","purchase",UUID.randomUUID().toString(),"",org.json.JSONObject().put("item_id",itemId)) }
        SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId)
    }
    suspend fun setEquipped(value: Int) {
      db.withTransaction {
        db.syncDao().ensureMeta(); val meta=db.syncDao().meta()!!
        db.syncDao().setMeta(meta.copy(equipped=value))
        if(meta.userId!=null && (value==0 || dao.watchItemsSnapshot().any { it.itemId==value && it.source!="legacy" })) {
            SyncCapture.enqueue(db,"equip","user",meta.userId,"",org.json.JSONObject().put("id",meta.userId).put("item_id",value))
        }
    }
      SyncScheduler.enqueue(app,profile,db.syncDao().meta()?.userId)
    }
    suspend fun recordFocus(seconds:Int) { if(seconds>=60) dao.insertSession(FocusSessionEntity(finishedAt=System.currentTimeMillis(),durationSeconds=seconds)) }
}
