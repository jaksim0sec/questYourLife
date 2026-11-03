package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DailyQuestViewModel

/** Book cards, chapter timeline and daily history all share persisted Room identities. */
@Composable
fun PatchQuestsPage(model: DailyQuestViewModel, onPreview: (String) -> Unit,
    onChapter: (String, String) -> Unit, onGoal: (String) -> Unit,
    onDaily: (DailyQuestEntity) -> Unit, onCreate: () -> Unit) {
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val social by model.social.collectAsStateWithLifecycle()
    val books = LiveQuestPath.visibleBooks(goals, steps, daily + history)
    val popular = if (signedIn) social?.popularBooks.orEmpty() else emptyList()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf("메인", "일일", "완료").forEachIndexed { index, title ->
                Column(Modifier.weight(1f).heightIn(min = 48.dp).clickable(role = Role.Tab) { tab = index }
                    .semantics { selected = tab == index }.padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(title, fontSize = 14.sp, fontWeight = if (tab == index) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (tab == index) PatchTokens.ink else PatchTokens.muted)
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.width(48.dp).height(3.dp).background(if (tab == index) PatchTokens.red else Color.Transparent))
                }
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onCreate, shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.heightIn(min = 48.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PatchTokens.line)) {
                QuestGlyph(R.drawable.ic_patch_plus, Modifier.size(16.dp))
                Text("새 목표", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 18.dp), color = PatchTokens.line.copy(alpha = .6f))
        LazyColumn(Modifier.weight(1f),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (tab != 1) {
                val visible = books.filter { book ->
                    book.total > 0 && if (tab == 2) book.done == book.total else book.done < book.total
                } + if (tab == 0) books.filter { it.total == 0 } else emptyList()
                if (visible.isNotEmpty()) item {
                    QuestSectionHeading(if (tab == 2) "완주한 퀘스트북" else "내 퀘스트북")
                }
                items(visible, key = { "book-${it.id}" }) { book ->
                    val chapter = book.currentChapter()
                    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                            .clickable(role = Role.Button) { onPreview(book.id) }
                            .padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                            QuestBookSymbol(false)
                            Column(Modifier.weight(1f).padding(horizontal = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(book.title, fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.ExtraBold,
                                    color = PatchTokens.ink, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(chapter?.title ?: "단계 준비 중", Modifier.weight(1f),
                                        fontSize = 11.sp, color = PatchTokens.muted, maxLines = 1,
                                        overflow = TextOverflow.Ellipsis)
                                    Text(if (book.total == 0) "0단계" else "${(book.progress * 100).toInt()}%",
                                        fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                                    QuestProgress(book.progress, Modifier.width(65.dp).padding(start = 5.dp))
                                }
                            }
                            QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(19.dp))
                        }
                        if (chapter != null) {
                            val chapterActive = book.current()?.id
                            chapter.missions.forEachIndexed { index, mission ->
                                val active = mission.id == chapterActive
                                LiveQuestTimeline(mission.title,
                                    if (mission.completed) "클리어" else if (active) "이번 단계" else "앞선 단계 완료 후 시작",
                                    mission.completed, active, index == chapter.missions.lastIndex) {
                                    onChapter(book.id, chapter.id)
                                }
                            }
                        }
                        HorizontalDivider(Modifier.padding(top = 10.dp),
                            color = PatchTokens.line.copy(alpha = .65f))
                    }
                }
                if (visible.isEmpty() && (tab == 0 || tab == 2)) item {
                    Column(Modifier.fillMaxWidth().padding(vertical = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuestGlyph(R.drawable.ic_patch_target, Modifier.size(42.dp), PatchTokens.red)
                        Text(if (tab == 0) "이제 첫 퀘스트북을 만들어 봐요" else "첫 완주를 기다리고 있어요",
                            style = MaterialTheme.typography.titleMedium, color = PatchTokens.ink)
                        if (tab == 0) QuestAction("새 목표 만들기", onCreate)
                        else QuestTextLink("진행 중인 퀘스트 보기", { tab = 0 })
                    }
                }
            }
            if (tab == 0 && popular.isNotEmpty()) {
                item { QuestSectionHeading("지금 많이 도전하는 퀘스트") }
                items(popular, key = { "public-${it.id}" }) { candidate ->
                    val already = goals.any { it.syncId == candidate.id }
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .background(PatchTokens.surface).padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        QuestBookSymbol(false)
                        Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                            Text(candidate.name, fontWeight = FontWeight.Bold, color = PatchTokens.ink,
                                maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text("${candidate.participants}명 참여", fontSize = 11.sp, color = PatchTokens.muted)
                        }
                        TextButton(
                            onClick = { model.joinPublicBook(candidate.id) },
                            enabled = !already && model.joiningBook.value == null
                        ) { Text(if (already) "참여 중" else if (model.joiningBook.value == candidate.id)
                            "저장 중" else "참여하기", color = PatchTokens.red) }
                    }
                }
                model.socialError.value?.let { message ->
                    item { Text(message, color = PatchTokens.muted, fontSize = 11.sp) }
                }
            }
            val visibleDaily = if (tab == 1) daily.filter { !it.dismissed }
                else if (tab == 2) history.filter { it.completedAt != null }.distinctBy { it.id }
                else emptyList()
            items(visibleDaily, key = { "daily-${it.id}" }) { quest ->
                Row(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.surface)
                    .clickable(role = Role.Button) { onDaily(quest) }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    QuestGlyph(if (quest.completedAt != null) R.drawable.ic_patch_check else R.drawable.ic_patch_timer,
                        Modifier.size(25.dp), PatchTokens.red)
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(quest.title, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                            color = PatchTokens.ink)
                        Text(if (quest.completedAt != null) "${quest.dateKey} · 완료"
                            else if (quest.accepted) "진행 중" else "수락하고 시작하기",
                            fontSize = 11.sp, color = PatchTokens.muted)
                    }
                    QuestPill(if (quest.completedAt != null) "완료" else "+${quest.reward} C",
                        QuestYellow, QuestNight)
                }
            }
            if ((tab == 1 || tab == 2) && visibleDaily.isEmpty() && (tab == 1 || books.none { it.total > 0 && it.done == it.total })) {
                item {
                    Column(Modifier.fillMaxWidth().padding(vertical = 26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuestGlyph(R.drawable.ic_patch_check, Modifier.size(37.dp), PatchTokens.muted)
                        Text(if (tab == 1) "오늘 도착한 일일 퀘스트가 없어요" else "완료 기록은 여기에 모여요",
                            color = PatchTokens.muted)
                        QuestTextLink("퀘스트북 보기", { tab = 0 })
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveQuestTimeline(title: String, subtitle: String, done: Boolean, active: Boolean,
    last: Boolean, onOpen: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = if (active) 66.dp else 52.dp)
        .clip(RoundedCornerShape(8.dp))
        .clickable(enabled = active || done, role = Role.Button, onClick = onOpen),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(40.dp).height(if (active) 66.dp else 52.dp),
            contentAlignment = Alignment.Center) {
            if (!last) Box(Modifier.align(Alignment.BottomCenter).width(1.dp)
                .height(20.dp).background(PatchTokens.line))
            Box(Modifier.size(18.dp).clip(CircleShape)
                .background(when { done -> PatchTokens.red; active -> QuestYellow; else -> PatchTokens.background })
                .border(if (done) 0.dp else 1.8.dp,
                    if (active) QuestNight else PatchTokens.muted, CircleShape),
                contentAlignment = Alignment.Center) {
                if (done) QuestGlyph(R.drawable.ic_patch_check, Modifier.size(13.dp), Color.White)
            }
        }
        Column(Modifier.weight(1f).padding(vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, fontSize = 14.sp,
                fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (!done && !active) PatchTokens.muted else PatchTokens.ink)
            if (!done) Text(subtitle, fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted)
        }
        QuestPill(when { done -> "클리어"; active -> "진행"; else -> "잠김" },
            if (active) QuestYellow else PatchTokens.soft,
            if (active) QuestNight else PatchTokens.muted)
    }
}
