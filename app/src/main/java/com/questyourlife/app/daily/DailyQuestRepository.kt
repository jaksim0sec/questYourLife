package com.questyourlife.app.daily

import android.content.Context
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.data.QuestDatabase
import java.time.LocalDate

class DailyQuestRepository(context: Context) {
    private val dao = QuestDatabase.get(context).dailyQuestDao()

    companion object {
        fun today(): String = LocalDate.now().toString()
    }

    // Offline starter suggestions. Replaced with reviewed Gemini suggestions later,
    // without changing the proposal/accept/complete contract.
    suspend fun ensureToday(): String {
        val day = today()
        if (dao.countForDay(day) == 0) {
            val templates = listOf(
                Triple("단어 10개 복습하기", "작게 시작하는 학습", 15),
                Triple("25분 집중하기", "방해 없이 내 목표에 집중", 20),
                Triple("10분 가볍게 걷기", "일상에서 움직이기", 15),
                Triple("오늘 할 일 하나 정리하기", "가장 중요한 일부터", 10),
                Triple("책 5쪽 읽기", "부담 없는 독서", 15),
                Triple("하루 돌아보기", "오늘 잘한 일 한 가지 기록", 10),
                Triple("책상 5분 정리하기", "시작하기 좋은 환경 만들기", 10)
            )
            val offset = LocalDate.parse(day).dayOfYear % templates.size
            val generated = (0 until 3).map { index ->
                val template = templates[(offset + index) % templates.size]
                DailyQuestEntity(
                    id = "$day-${index + 1}",
                    dateKey = day,
                    title = template.first,
                    detail = template.second,
                    reward = template.third
                )
            }
            dao.insertAll(generated)
        }
        return day
    }

    fun observeDay(dateKey: String) = dao.observeDay(dateKey)
    fun observeCoins() = dao.observeEarnedCoins()
    fun observeCompletionCount() = dao.observeCompletedCount()

    suspend fun get(id: String) = dao.find(id)

    suspend fun accept(id: String): DailyQuestEntity? {
        val today = today()
        if (dao.accept(id, today) == 0) return null
        return dao.find(id)
    }

    suspend fun complete(id: String): DailyQuestEntity? {
        val today = today()
        val existing = dao.find(id) ?: return null
        if (existing.dateKey != today) return null
        if (dao.complete(id, today, System.currentTimeMillis()) == 0) return null
        return dao.find(id)
    }
}
