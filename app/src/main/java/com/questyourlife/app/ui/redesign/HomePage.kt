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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.companion.QuestPreviewCatalog
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar

/** Fourth supplied reference: profile strip, shared chest, resume, popular quests, daily, clan. */
@Composable
fun PatchHomePage(model: DailyQuestViewModel, onDaily: (DailyQuestEntity) -> Unit,
    onQuests: () -> Unit, onClan: () -> Unit, onPreview: (String) -> Unit, onMy: () -> Unit) {
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    if (signedIn) {
        SocialHomePage(model, onDaily, onQuests, onClan, onMy)
        return
    }
    val balance by model.cash.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val preview by model.questPreview.state.collectAsStateWithLifecycle()
    val clan = remember { model.clanPreview() }
    val travel = QuestPreviewCatalog.books.first()
    val current = preview.current(travel)
    val fontScale = LocalDensity.current.fontScale.coerceAtLeast(1f)
    val cash = clan.cash + preview.earned
    val today = daily.firstOrNull { it.completedAt == null } ?: daily.firstOrNull()
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 18.dp, end = 18.dp,
        top = 4.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
                BoxWithConstraints(Modifier.weight(1f)) {
                    val compact = maxWidth < 240.dp || fontScale > 1.1f
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .clickable(role = Role.Button, onClick = onMy), verticalAlignment = Alignment.CenterVertically) {
                        DressedAvatar(model.equipped.intValue, Modifier.size(42.dp))
                        if (compact) Column(Modifier.weight(1f).padding(start = 4.dp)) {
                            Text("이모리", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                            Text(clan.name, fontSize = 10.sp, color = PatchTokens.muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        } else {
                            Text("이모리", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                            Spacer(Modifier.width(6.dp))
                            QuestPill(clan.name, QuestLime, QuestNight)
                        }
                    }
                }
                PatchCash(balance, compact = true)
            }
        }
        item {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BoxWithConstraints(Modifier.fillMaxWidth().height(202.dp + 300.dp * (fontScale - 1f))) {
                    Text("오늘의 공략,\n같이 한 걸음", Modifier.fillMaxWidth(if (fontScale > 1.1f) 1f else .72f).align(Alignment.TopStart),
                        fontSize = 30.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black, color = PatchTokens.ink)
                    QuestChest(Modifier.align(Alignment.BottomEnd).size(minOf(166.dp, maxWidth * .46f)), "함께 모으는 클랜 상자")
                    QuestGlyph(R.drawable.ic_patch_sparkles, Modifier.align(Alignment.CenterEnd)
                        .offset(x = (-120).dp, y = (-20).dp).size(32.dp), QuestYellow)
                    QuestFaces(Modifier.align(Alignment.BottomStart).padding(bottom = 8.dp), 64.dp)
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("우리 클랜 상자", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                    Spacer(Modifier.width(8.dp))
                    Text("$cash / ${clan.target}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                }
                QuestProgress(cash.toFloat() / clan.target)
            }
        }
        item {
            Column(Modifier.fillMaxWidth().clip(QuestShape).background(QuestNight)
                .clickable(role = Role.Button) { onPreview(travel.id) }.padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (current == null) "완주한 퀘스트 보기" else "내 미션 이어하기", color = QuestNightInk,
                        fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(23.dp), QuestNightInk)
                }
                Text(current?.let { "${it.action} · ${it.minutes}분" } ?: "모든 챕터를 완주했어요",
                    color = QuestNightInk.copy(alpha = .8f), fontSize = 11.sp, lineHeight = 17.sp,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
        item { QuestSectionHeading("지금 많이 도전하는 퀘스트", Modifier.padding(top = 6.dp)) }
        items(QuestPreviewCatalog.books.size) { index ->
            val book = QuestPreviewCatalog.books[index]
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .clickable(role = Role.Button) { onPreview(book.id) }.padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically) {
                QuestBookSymbol(book.clan)
                Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                    Text(book.title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold,
                        color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(if (book.clan) "1,284명 도전 중 · 시연" else "863명 도전 중 · 시연",
                        fontSize = 11.sp, color = PatchTokens.muted)
                }
                QuestFaces(size = 25.dp)
                QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp))
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                .background(QuestYellow.copy(alpha = .24f)).clickable(role = Role.Button) {
                    if (today != null) onDaily(today) else onQuests()
                }.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("오늘의 데일리", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        QuestGlyph(if (today?.completedAt != null) R.drawable.ic_patch_check else R.drawable.ic_patch_target,
                            Modifier.size(18.dp), if (today?.completedAt != null) PatchTokens.red else PatchTokens.ink)
                        Spacer(Modifier.width(7.dp))
                        Text(today?.title ?: "새 목표에서 미션 시작하기", fontSize = 12.sp,
                            color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
                if (today != null) QuestPill("+${today.reward} C", QuestYellow, QuestNight)
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable(role = Role.Button, onClick = onClan)
                .padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                QuestGlyph(R.drawable.ic_patch_flag, Modifier.size(22.dp), PatchTokens.red)
                Text("방과후 일본어 · 함께 도전할 멤버 모집 중", Modifier.weight(1f).padding(horizontal = 8.dp),
                    fontSize = 11.sp, color = PatchTokens.ink)
                QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp))
            }
        }
        item { Text("클랜·인기 퀘스트는 로컬 시연이에요. 내 캐쉬와 오늘의 데일리는 실제 기록이에요.",
            fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted) }
    }
}
