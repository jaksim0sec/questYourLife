package com.questyourlife.app.companion

import com.questyourlife.app.data.*
import java.util.UUID
import androidx.room.withTransaction
import com.questyourlife.app.sync.SyncCapture

/** Guest-only local suggestions and clearly labeled conversation previews. Never uploads demo messages. */
class LocalCompanionGateway(private val db: QuestDatabase) : CompanionGateway {
    private val dao=db.companionDao()
    override fun watchMessages(thread: String) = dao.watch(thread)
    override fun contacts() = listOf(
        DemoContact("demo-night", "밤산책", "오늘도 하나씩, 같이 해보자", 220),
        DemoContact("demo-mochi", "모찌", "이번 주도 차근차근!", 160),
        DemoContact("demo-haru", "하루", "작은 완료부터 시작해요", 140)
    )
    override fun nearbyClanRanks() = LocalClanPreview.nearby(clanPreview())
    override fun clanPreview(previous: Boolean) = LocalClanPreview.week(previous)
    override fun demoClan() = DemoClan("느슨한 갓생단", contacts(), 1000)
    override suspend fun openThread(thread: String) {
        if (thread.isBlank()) return
        val body = if (thread == "mascot")
            "어떤 일상을 바꾸고 싶어? 목표와 하루에 쓸 수 있는 시간을 알려줘. 지금은 준비된 로컬 제안으로 함께 계획해요."
        else "친구 대화 체험 공간이에요. 여기에 보낸 메시지는 이 기기에만 저장되고 실제 친구에게 전달되지 않아요."
        dao.insert(CompanionMessageEntity("welcome-$thread", thread, body, false, System.currentTimeMillis()))
    }
    override suspend fun send(thread: String, text: String) {
        require(thread == "mascot" || contacts().any { it.id == thread })
        val clean = text.trim().take(2000)
        require(clean.isNotBlank())
        openThread(thread)
        val time = System.currentTimeMillis()
        dao.insert(CompanionMessageEntity(UUID.randomUUID().toString(), thread, clean, true, time))
        if (thread != "mascot") return // No fabricated live friend response.
        if (clean in listOf("안녕", "안녕하세요", "고마워", "고마워요")) {
            dao.insert(CompanionMessageEntity(UUID.randomUUID().toString(), thread, "함께 조금씩 바꿔보자. 만들고 싶은 목표를 이야기해줘!", false, time + 1))
            return
        }
        val plan = LocalPlanTemplates.draft(clean, dao.latestPlanTitle(thread))
        dao.insert(CompanionMessageEntity(UUID.randomUUID().toString(), thread,
            "하루 ${plan.minutes}분으로 시작하는 제안이야. 아래 내용을 확인하고 마음에 들면 퀘스트에 추가해줘.",
            false, time + 1, plan.title, plan.actions.joinToString("\n")))
    }
    override suspend fun applyPlan(messageId: String) = db.withTransaction {
        dao.applyPlan(messageId)?.also { SyncCapture.goal(db,it); db.syncDao().allDaily().filter { q -> q.goalId==it }.forEach { q -> SyncCapture.daily(db,q.id) } }
    }
}
