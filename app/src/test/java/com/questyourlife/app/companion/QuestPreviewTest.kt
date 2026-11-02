package com.questyourlife.app.companion

import org.junit.Assert.*
import org.junit.Test

class QuestPreviewTest {
    private val travel = QuestPreviewCatalog.book("travel")!!
    @Test fun lockedMissionsCannotStartOrComplete() {
        val state = QuestPreviewState()
        assertEquals(state, state.start("own-order"))
        assertEquals(state, state.complete("order-phrases"))
        assertEquals(state, state.start("unknown"))
        assertEquals(0, state.earned)
    }
    @Test fun completionIsIdempotentAndUnlocksOnlyTheNextMission() {
        val state = QuestPreviewState().start("order-phrases").complete("order-phrases")
        assertEquals(20, state.earned)
        assertEquals("own-order", state.current(travel)?.id)
        assertEquals(state, state.complete("order-phrases"))
        assertEquals(state, state.start("ask-way"))
        assertEquals("travel-2", state.chapter(travel).id)
    }
    @Test fun chapterClearUnlocksNextChapterAndAllowsOneReward() {
        var state = QuestPreviewState()
        assertEquals(state, state.claim("travel-2", 1001))
        state = state.start("order-phrases").complete("order-phrases")
            .start("own-order").complete("own-order")
        assertEquals("travel-3", state.chapter(travel).id)
        assertEquals(40, state.earned)
        assertEquals(state, state.claim("travel-2", 999))
        state = state.claim("travel-2", 1001)
        assertEquals(1001, state.rewards["travel-2"])
        assertEquals(state, state.claim("travel-2", 2))
        assertEquals(state, state.claim("missing", 2))
    }
    @Test fun booksProgressIndependentlyAndCanBeFullyCompleted() {
        var state = QuestPreviewState()
        val initialTravel = state.current(travel)
        val run = QuestPreviewCatalog.book("run")!!
        run.chapters.flatMap { it.missions }.forEach { mission -> state = state.start(mission.id).complete(mission.id) }
        assertNull(state.current(run))
        assertEquals(initialTravel, state.current(travel))
        assertEquals(60, state.earned)
        assertEquals(state, state.start("walk").complete("walk"))
    }
}
