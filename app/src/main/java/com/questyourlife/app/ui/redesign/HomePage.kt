package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar

/** The same reference-layout homepage for guest and signed-in accounts. Only facts change. */
@Composable
fun PatchHomePage(model: DailyQuestViewModel, onDaily: (DailyQuestEntity) -> Unit,
    onQuests: () -> Unit, onClan: () -> Unit, onPreview: (String) -> Unit, onMy: () -> Unit) {
    val balance by model.cash.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val social by model.social.collectAsStateWithLifecycle()
    val profile by model.ownProfile.collectAsStateWithLifecycle()
    val clan = social?.clan
    val popular = social?.popularBooks.orEmpty().take(3)
    val featured = LiveQuestPath.featured(goals, steps, daily)
    val next = featured?.current()
    val today = daily.firstOrNull { !it.dismissed && it.completedAt == null }
        ?: daily.firstOrNull { !it.dismissed }
    val fontScale = LocalDensity.current.fontScale.coerceAtLeast(1f)

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 18.dp, end = 18.dp,
        top = 4.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(Modifier.fillMaxWidth().heightIn(min = 50.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                    .clickable(role = Role.Button, onClick = onMy), verticalAlignment = Alignment.CenterVertically) {
                    DressedAvatar(model.equipped.intValue, Modifier.size(42.dp), "내 아바타")
                    Column(Modifier.padding(start = 9.dp)) {
                        Text(profile?.name ?: "나", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,
                            color = PatchTokens.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(clan?.name ?: "오늘도 한 걸음", fontSize = 11.sp, color = PatchTokens.muted,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                PatchCash(balance, compact = true)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BoxWithConstraints(Modifier.fillMaxWidth().height(196.dp + 200.dp * (fontScale - 1f))) {
                    Text("오늘의 공략,\n같이 한 걸음", Modifier.fillMaxWidth(if (fontScale > 1.1f) 1f else .75f)
                        .align(Alignment.TopStart), fontSize = 30.sp, lineHeight = 35.sp,
                        fontWeight = FontWeight.Black, color = PatchTokens.ink)
                    QuestChest(Modifier.align(Alignment.BottomEnd).size(minOf(156.dp, maxWidth * .44f)),
                        if (clan == null) "클랜에 참여하면 함께 도전할 상자" else "클랜 공동 상자")
                    if (clan != null) Row(Modifier.align(Alignment.BottomStart).padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                        clan.members.take(4).forEach { member ->
                            DressedAvatar(member.itemId, Modifier.size(43.dp), member.name)
                        }
                    } else QuestGlyph(R.drawable.ic_patch_sparkles,
                        Modifier.align(Alignment.BottomStart).padding(start = 8.dp, bottom = 28.dp)
                            .size(34.dp), QuestYellow)
                }
                Row(Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClan)
                    .padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(clan?.let { "우리 클랜 · ${it.name}" } ?: "클랜에서 함께 도전하기",
                            fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                        Text(if (clan == null) "초대받거나 직접 클랜을 만들 수 있어요"
                            else "이번 주 서버 기록을 함께 모아요", fontSize = 11.sp, color = PatchTokens.muted)
                    }
                    QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(20.dp))
                }
                if (clan?.weeklyScore != null && clan.target != null && clan.target > 0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("공동 목표 진행률", fontSize = 11.sp, color = PatchTokens.muted)
                        Text("${clan.weeklyScore} / ${clan.target}", fontSize = 11.sp,
                            fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    }
                    QuestProgress((clan.weeklyScore.toFloat() / clan.target.toFloat()).coerceIn(0f, 1f))
                }
            }
        }
        item {
            Column(Modifier.fillMaxWidth().clip(QuestShape).background(QuestNight)
                .clickable(role = Role.Button) {
                    if (featured == null) onQuests() else onPreview(featured.id)
                }.padding(vertical = 15.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (featured == null) "첫 목표 시작하기"
                        else if (next == null && featured.total > 0) "완주한 퀘스트 보기" else "내 미션 이어하기",
                        color = QuestNightInk, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(23.dp), QuestNightInk)
                }
                Text(next?.title ?: featured?.title ?: "목표를 만들고 오늘의 퀘스트를 시작해요",
                    color = QuestNightInk.copy(alpha = .85f), fontSize = 12.sp,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (featured != null && featured.total > 0)
                    QuestProgress(featured.progress, Modifier.fillMaxWidth(.75f))
            }
        }
        if (popular.isNotEmpty()) {
            item { QuestSectionHeading("지금 많이 도전하는 퀘스트", Modifier.padding(top = 6.dp)) }
            items(popular.size) { index ->
                val book = popular[index]
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .clickable(role = Role.Button, onClick = onQuests).padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    QuestBookSymbol(false)
                    Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                        Text(book.name, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold,
                            color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text("${book.participants}명 참여", fontSize = 11.sp, color = PatchTokens.muted)
                    }
                    QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp))
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                .background(QuestYellow.copy(alpha = .24f)).clickable(role = Role.Button) {
                    if (today == null) onQuests() else onDaily(today)
                }.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("오늘의 데일리", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        QuestGlyph(if (today?.completedAt != null) R.drawable.ic_patch_check else R.drawable.ic_patch_target,
                            Modifier.size(18.dp), if (today?.completedAt != null) PatchTokens.red else PatchTokens.ink)
                        Spacer(Modifier.width(7.dp))
                        Text(today?.title ?: "오늘 할 퀘스트를 만들어 봐요", fontSize = 12.sp,
                            color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
                if (today != null) QuestPill(if (today.completedAt != null) "완료" else "+${today.reward} C",
                    QuestYellow, QuestNight)
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                .clickable(role = Role.Button, onClick = onClan)
                .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                QuestGlyph(R.drawable.ic_patch_flag, Modifier.size(22.dp), PatchTokens.red)
                Text(clan?.let { "${it.name} · 멤버 ${it.members.size}명" } ?: "클랜에서 친구와 같이 도전하기",
                    Modifier.weight(1f).padding(horizontal = 8.dp), fontSize = 12.sp,
                    color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp))
            }
        }
    }
}
