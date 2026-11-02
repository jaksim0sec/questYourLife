package com.questyourlife.app.companion

/** Social preview only; these values never enter the personal currency ledger. */
data class ClanPreviewMember(val name: String, val cash: Int, val isSelf: Boolean = false)
data class ClanPreview(
    val name: String, val memberCount: Int, val contributorCount: Int,
    val cash: Int, val target: Int, val rank: Int, val rise: Int,
    val members: List<ClanPreviewMember>
)

data class ClanRankEntry(val rank: Int, val name: String, val cash: Int)

object LocalClanPreview {
    fun nearby(clan: ClanPreview) = listOf(
        ClanRankEntry(clan.rank - 2, "모닝 패치", clan.cash + 140),
        ClanRankEntry(clan.rank - 1, "하루 한 칸", clan.cash + 60),
        ClanRankEntry(clan.rank, clan.name, clan.cash)
    )
    fun week(previous: Boolean = false) = ClanPreview(
        "느슨한 갓생단", 8, if (previous) 8 else 6,
        if (previous) 1040 else 780, 1000, if (previous) 15 else 12,
        if (previous) 1 else 3,
        if (previous) listOf(
            ClanPreviewMember("밤산책", 280), ClanPreviewMember("이모리", 240, true),
            ClanPreviewMember("모찌", 200)
        ) else listOf(
            ClanPreviewMember("밤산책", 220), ClanPreviewMember("이모리", 180, true),
            ClanPreviewMember("모찌", 160)
        )
    )
}
