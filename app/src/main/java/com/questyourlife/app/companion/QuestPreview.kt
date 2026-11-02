package com.questyourlife.app.companion

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PreviewMission(val id: String, val label: String, val action: String, val detail: String, val minutes: Int = 10)
data class PreviewChapter(val id: String, val number: Int, val title: String, val missions: List<PreviewMission>)
data class PreviewBook(val id: String, val title: String, val clan: Boolean, val chapters: List<PreviewChapter>)

/** Explicit local previews. These never write to Room, personal cash or inventory. */
object QuestPreviewCatalog {
    val books = listOf(
        PreviewBook("travel", "여행 가서 직접 주문하기", true, listOf(
            PreviewChapter("travel-1", 1, "기초 인사부터", listOf(
                PreviewMission("greeting", "기초 인사", "일본어 인사 3개 말해보기", "안녕하세요, 감사합니다, 잘 부탁합니다를 소리 내어 연습해요."))),
            PreviewChapter("travel-2", 2, "첫 주문을 성공시켜라", listOf(
                PreviewMission("menu-words", "메뉴 단어", "메뉴 속 단어 알아보기", "메뉴에서 음식과 수량을 나타내는 단어를 찾아봐요."),
                PreviewMission("menu-read", "메뉴 읽기", "메뉴 한 줄 읽어보기", "마음에 드는 메뉴를 고르고 천천히 읽어봐요."),
                PreviewMission("order-phrases", "주문 표현", "주문 표현 3개 말해보기", "메뉴를 보며 원하는 음식과 수량을 일본어로 세 번 말해 보세요.\n예시  ラーメンを一つください"),
                PreviewMission("own-order", "내 말로 주문", "내 말로 주문해보기", "메뉴 하나를 골라 도움말 없이 주문 문장을 만들어 말해보세요."))),
            PreviewChapter("travel-3", 3, "길을 묻고 답하기", listOf(
                PreviewMission("ask-way", "길 묻기", "목적지까지 가는 길 물어보기", "역과 숙소를 넣어 길을 묻는 문장을 연습해요."),
                PreviewMission("answer-way", "길 안내", "길 안내 문장 따라 말하기", "오른쪽, 왼쪽, 직진을 넣어 한 문장을 말해봐요.")))
        )),
        PreviewBook("run", "첫 5km 완주하기", false, listOf(
            PreviewChapter("run-1", 1, "걷기부터 시작", listOf(
                PreviewMission("walk", "편하게 걷기", "10분 편하게 걷기", "편한 신발을 신고 가볍게 10분 걸어요. 무리하지 않아도 괜찮아요."))),
            PreviewChapter("run-2", 2, "조금씩 달리기", listOf(
                PreviewMission("jog", "가볍게 달리기", "3분 천천히 달리기", "충분히 준비 운동한 뒤 편한 속도로 달려봐요.", 3))),
            PreviewChapter("run-3", 3, "나만의 속도로", listOf(
                PreviewMission("finish-run", "완주 연습", "걷기와 달리기 이어가기", "호흡에 맞춰 달리고, 필요하면 걸으며 나만의 속도를 찾아요.")))
        ))
    )
    val initialDone = setOf("greeting", "menu-words", "menu-read")
    fun book(id: String) = books.firstOrNull { it.id == id }
}

data class QuestPreviewState(
    val completed: Set<String> = QuestPreviewCatalog.initialDone,
    val started: Set<String> = emptySet(),
    val rewards: Map<String, Int> = emptyMap()
) {
    val earned: Int get() = (completed - QuestPreviewCatalog.initialDone).size * 20
    fun chapter(book: PreviewBook) = book.chapters.firstOrNull { c -> c.missions.any { it.id !in completed } } ?: book.chapters.last()
    fun current(book: PreviewBook) = book.chapters.flatMap { it.missions }.firstOrNull { it.id !in completed }
    fun start(id: String): QuestPreviewState = if (QuestPreviewCatalog.books.any { current(it)?.id == id })
        copy(started = started + id) else this
    fun complete(id: String): QuestPreviewState = if (id in started && QuestPreviewCatalog.books.any { current(it)?.id == id })
        copy(completed = completed + id, started = started - id) else this
    fun claim(chapterId: String, itemId: Int): QuestPreviewState {
        val chapter = QuestPreviewCatalog.books.flatMap { it.chapters }.firstOrNull { it.id == chapterId } ?: return this
        return if (itemId in setOf(0, 1001, 2) && chapterId !in rewards && chapter.missions.all { it.id in completed })
            copy(rewards = rewards + (chapterId to itemId)) else this
    }
}

class QuestPreviewStore(context: Context) {
    private val prefs = context.getSharedPreferences("patch_quest_preview", Context.MODE_PRIVATE)
    private val mutableState = MutableStateFlow(QuestPreviewState(
        prefs.getStringSet("completed", QuestPreviewCatalog.initialDone)!!.toSet(),
        prefs.getStringSet("started", emptySet())!!.toSet(),
        prefs.getStringSet("rewards", emptySet())!!.mapNotNull { value ->
            val parts = value.split('='); parts.getOrNull(1)?.toIntOrNull()?.let { parts[0] to it }
        }.toMap()))
    val state = mutableState.asStateFlow()
    private fun update(next: QuestPreviewState) {
        if (next == mutableState.value) return
        prefs.edit().putStringSet("completed", next.completed).putStringSet("started", next.started)
            .putStringSet("rewards", next.rewards.map { "${it.key}=${it.value}" }.toSet()).apply()
        mutableState.value = next
    }
    fun start(id: String) = update(mutableState.value.start(id))
    fun complete(id: String) = update(mutableState.value.complete(id))
    fun claim(chapter: String, item: Int) = update(mutableState.value.claim(chapter, item))
}
