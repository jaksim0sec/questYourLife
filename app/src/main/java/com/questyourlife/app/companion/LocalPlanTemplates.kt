package com.questyourlife.app.companion

data class PlanDraft(val title: String, val actions: List<String>, val minutes: Int)

/** Versioned, curated offline templates. This is not a generative AI service. */
object LocalPlanTemplates {
    fun draft(input: String, previousTitle: String? = null): PlanDraft {
        val clean = input.trim().replace(Regex("\\s+"), " ")
        require(clean.isNotEmpty()) { "목표를 입력해 주세요" }
        val requested = Regex("(\\d+)\\s*분").find(clean)?.groupValues?.get(1)?.toIntOrNull()
        require(requested == null || requested in 1..180) { "하루 1~180분 안에서 정해 주세요" }
        val minutes = requested ?: 15
        val adjustingTime = previousTitle != null &&
            Regex("^(오늘|하루|시간|\\d+\\s*분)").containsMatchIn(clean) &&
            !Regex("일본어|운동|달리|읽기|독서|정리").containsMatchIn(clean)
        val title = (if (adjustingTime) previousTitle!! else clean).take(120)
        val actions = when {
            title.contains("일본어") -> listOf(
                "あ행 5글자를 읽고 써보기 · ${minutes}분",
                "か행 5글자를 읽고 써보기 · ${minutes}분",
                "배운 글자를 섞어 읽고 복습하기 · ${minutes}분"
            )
            Regex("달리|운동|걷기|러닝").containsMatchIn(title) -> listOf(
                "편한 속도로 걷기 · ${minutes}분",
                "걷기와 가벼운 달리기 번갈아 하기 · ${minutes}분",
                "몸 상태와 운동 기록 돌아보기 · ${minutes}분"
            )
            Regex("독서|읽기|책").containsMatchIn(title) -> listOf(
                "읽을 책을 골라 첫 부분 읽기 · ${minutes}분",
                "이어서 읽고 기억할 문장 하나 남기기 · ${minutes}분",
                "읽은 내용을 내 말로 정리하기 · ${minutes}분"
            )
            else -> listOf(
                "$title — 오늘 할 수 있는 첫 행동 정하기 · ${minutes}분",
                "정한 행동을 직접 해보고 결과 기록하기 · ${minutes}분",
                "해본 내용을 돌아보고 다음 행동 고르기 · ${minutes}분"
            )
        }
        return PlanDraft(title, actions, minutes)
    }
}
