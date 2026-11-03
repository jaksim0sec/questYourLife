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
import com.questyourlife.app.companion.*
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import androidx.compose.ui.platform.LocalContext
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.sync.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID
import androidx.room.withTransaction

@Composable
fun PatchQuestsPage(model: DailyQuestViewModel, onPreview: (String) -> Unit,
    onChapter: (String, String) -> Unit, onGoal: (String) -> Unit, onDaily: (DailyQuestEntity) -> Unit, onCreate: () -> Unit) {
    val state by model.questPreview.state.collectAsStateWithLifecycle()
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val meta by model.syncMeta.collectAsStateWithLifecycle()
    val context=LocalContext.current
    val db=remember(context) { QuestDatabase.get(context) }
    val server by remember(db,meta?.userId) { db.syncDao().observeRecord("social_snapshot",meta?.userId ?: "guest") }
        .collectAsStateWithLifecycle(initialValue=null)
    val popular=server?.body?.let { JSONObject(it).optJSONArray("popular_books") }
    val actions=rememberCoroutineScope()
    var joining by remember { mutableStateOf(false) }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var scope by rememberSaveable { mutableStateOf("전체") }
    var filterMenu by remember { mutableStateOf(false) }
    var route by rememberSaveable { mutableStateOf(false) }
    val previewBooks = QuestPreviewCatalog.books.filter { book -> !signedIn &&
        (scope == "전체" || (scope == "클랜") == book.clan) &&
            if (tab == 2) state.current(book) == null else state.current(book) != null
    }
    val personal = goals.filter { goal ->
        val relevant = steps.filter { it.goalId == goal.id && it.deletedAt==null }
        scope != "클랜" && if (tab == 2) relevant.isNotEmpty() && relevant.all { it.completedAt != null }
        else relevant.isEmpty() || relevant.any { it.completedAt == null }
    }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf("메인", "일일", "완료").forEachIndexed { index, title ->
                Column(Modifier.weight(1f).clickable(role = Role.Tab) { tab = index }
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
                modifier = Modifier.heightIn(min = 36.dp), border = androidx.compose.foundation.BorderStroke(1.dp, PatchTokens.line)) {
                QuestGlyph(R.drawable.ic_patch_plus, Modifier.size(15.dp))
                Text("새 목표", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
            }
        }
        HorizontalDivider(Modifier.padding(horizontal = 18.dp), color = PatchTokens.line.copy(alpha = .6f))
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(18.dp, 10.dp, 18.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (tab != 1) item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box {
                        Row(Modifier.clip(CircleShape).border(1.dp, PatchTokens.line, CircleShape)
                            .clickable(role = Role.Button) { filterMenu = true }.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(scope, fontSize = 12.sp, color = PatchTokens.ink)
                            QuestGlyph(R.drawable.ic_patch_chevron_down, Modifier.size(16.dp))
                        }
                        DropdownMenu(filterMenu, { filterMenu = false }) {
                            listOf("전체", "개인", "클랜").forEach { filter ->
                                DropdownMenuItem(text = { Text(filter) }, onClick = { scope = filter; filterMenu = false })
                            }
                        }
                    }
                    listOf("개인", "클랜").forEach { filter ->
                        Text(filter, Modifier.clip(CircleShape).clickable(role = Role.Tab) { scope = filter }
                            .semantics { selected = scope == filter }.padding(horizontal = 8.dp, vertical = 8.dp),
                            fontSize = 12.sp, color = if (scope == filter) PatchTokens.red else PatchTokens.muted,
                            fontWeight = if (scope == filter) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
            if (tab != 1) {
                items(previewBooks, key = { "preview-${it.id}" }) { book ->
                    val chapter = state.chapter(book)
                    val all = book.chapters.flatMap { it.missions }
                    val done = all.count { it.id in state.completed }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .clickable(role = Role.Button) { onPreview(book.id) }, verticalAlignment = Alignment.CenterVertically) {
                            QuestBookSymbol(book.clan)
                            Column(Modifier.weight(1f).padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(book.title, fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.ExtraBold,
                                    color = PatchTokens.ink)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("챕터 ${chapter.number} · ${chapter.title}", Modifier.weight(1f),
                                        fontSize = 10.sp, color = PatchTokens.muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Spacer(Modifier.width(8.dp))
                                    Text("${done * 100 / all.size}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                                    QuestProgress(done.toFloat() / all.size, Modifier.width(60.dp).padding(start = 5.dp))
                                }
                            }
                            QuestPill(if (book.clan) "클랜" else "개인", if (book.clan) QuestLime else PatchTokens.soft,
                                if (book.clan) QuestNight else PatchTokens.muted)
                        }
                        chapter.missions.forEachIndexed { index, mission ->
                            val complete = mission.id in state.completed
                            val active = state.current(book)?.id == mission.id
                            QuestTimeline(if (mission.id == "order-phrases") "주문 문장 익히기" else mission.action,
                                when { complete -> "클리어"; active -> "오늘  ${mission.action} · ${mission.minutes}분"; else -> "앞선 미션 완료 후 시작" },
                                complete, active, index == chapter.missions.lastIndex) { onPreview(book.id) }
                        }
                    }
                    HorizontalDivider(Modifier.padding(top = 14.dp), color = PatchTokens.line.copy(alpha = .55f))
                }
                if (personal.isNotEmpty()) item { QuestSectionHeading("내 목표") }
                items(personal, key = { "goal-${it.id}" }) { goal ->
                    val relevant = steps.filter { it.goalId == goal.id && it.deletedAt==null }
                    val done = relevant.count { it.completedAt != null }
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable(role = Role.Button) { onGoal(goal.id) }
                        .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        QuestGlyph(R.drawable.ic_patch_target, Modifier.size(32.dp), PatchTokens.red)
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(goal.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                            Text("${if (goal.id == "example-japanese") "로컬 예시 · " else ""}$done / ${relevant.size} 단계 완료", fontSize = 11.sp, color = PatchTokens.muted)
                            QuestProgress(if (relevant.isEmpty()) 0f else done.toFloat() / relevant.size, color = PatchTokens.red)
                        }
                        QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(20.dp))
                    }
                }
            }
            if(signedIn && tab==0 && popular!=null && scope!="클랜") {
                item { QuestSectionHeading("인기 목표") }
                items(popular.length(), key={ "public-${popular.getJSONObject(it).getString("logical_id")}" }) { index ->
                    val entry=popular.getJSONObject(index)
                    val book=entry.optJSONObject("book")
                    if(book!=null) Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(book.getString("name"),fontWeight=FontWeight.Bold,color=PatchTokens.ink)
                            Text("${entry.optLong("active_participants")}명 참여",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
                        }
                        TextButton(enabled=!joining,onClick={ actions.launch {
                            joining=true
                            try {
                                val id=UUID.randomUUID().toString()
                                db.withTransaction { SyncCapture.enqueue(db,"book_join","quest_join",id,"",JSONObject().put("id",id).put("book_id",book.getString("id"))) }
                                SyncScheduler.enqueue(context)
                            } finally { joining=false }
                        } }) { Text("참여하기") }
                    }
                }
            }
            val visibleDaily = if (tab == 1) daily else if (tab == 2) history.filter { it.completedAt != null }.distinctBy { it.id } else emptyList()
            items(visibleDaily, key = { "daily-${it.id}" }) { quest ->
                Row(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.surface)
                    .clickable(role = Role.Button) { onDaily(quest) }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    QuestGlyph(if (quest.completedAt != null) R.drawable.ic_patch_check else R.drawable.ic_patch_timer,
                        Modifier.size(25.dp), PatchTokens.red)
                    Column(Modifier.weight(1f).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(quest.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                        Text(if (quest.completedAt != null) "${quest.dateKey} · 완료" else if (quest.accepted) "진행 중" else "수락하고 시작하기",
                            fontSize = 11.sp, color = PatchTokens.muted)
                    }
                    QuestPill("+${quest.reward} C", QuestYellow, QuestNight)
                }
            }
            if ((tab == 1 && visibleDaily.isEmpty()) || (tab == 2 && previewBooks.isEmpty() && personal.isEmpty() && visibleDaily.isEmpty())) item {
                Column(Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    QuestGlyph(R.drawable.ic_patch_check, Modifier.size(36.dp), PatchTokens.muted)
                    Spacer(Modifier.height(12.dp))
                    Text(if (tab == 1) "오늘 도착한 일일 미션이 없어요" else "첫 완료를 기다리고 있어요", color = PatchTokens.muted)
                    QuestTextLink("메인 퀘스트 보기", { tab = 0 })
                }
            }
            if (tab == 0) item {
                ChapterJourney(state, QuestPreviewCatalog.books.first(), onOpen = { route = true })
                QuestTextLink("전체 공략 경로 보기", { route = true })
            }
            item { Text("예시 퀘스트는 로컬 시연이며 시연 보상은 내 캐쉬·보관함에 합산되지 않아요.",
                fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted) }
        }
    }
    if (route) QuestRouteSheet(state, QuestPreviewCatalog.books.first(), { route = false }) { selected ->
        route = false; onChapter(QuestPreviewCatalog.books.first().id, selected.id)
    }
}

@Composable
private fun QuestTimeline(title: String, subtitle: String, done: Boolean, active: Boolean,
    last: Boolean, onOpen: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = if (active) 66.dp else 52.dp)
        .clip(RoundedCornerShape(8.dp)).clickable(enabled = active || done, role = Role.Button, onClick = onOpen),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(40.dp).height(if (active) 66.dp else 52.dp), contentAlignment = Alignment.Center) {
            if (!last) Box(Modifier.align(Alignment.BottomCenter).width(1.dp).height(20.dp).background(PatchTokens.line))
            Box(Modifier.size(18.dp).clip(CircleShape)
                .background(when { done -> PatchTokens.red; active -> QuestYellow; else -> PatchTokens.background })
                .border(if (done) 0.dp else 1.8.dp, if (active) QuestNight else PatchTokens.muted, CircleShape),
                contentAlignment = Alignment.Center) {
                if (done) QuestGlyph(R.drawable.ic_patch_check, Modifier.size(13.dp), Color.White)
            }
        }
        Column(Modifier.weight(1f).padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Medium,
                color = if (!done && !active) PatchTokens.muted else PatchTokens.ink)
            if (!done) Text(subtitle, fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted)
        }
        QuestPill(when { done -> "클리어"; active -> "진행"; else -> "잠김" },
            if (active) QuestYellow else PatchTokens.soft, if (active) QuestNight else PatchTokens.muted)
    }
}

@Composable
internal fun ChapterJourney(state: QuestPreviewState, book: PreviewBook, onOpen: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(QuestShape).clickable(role = Role.Button, onClick = onOpen)
        .padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        book.chapters.forEachIndexed { index, chapter ->
            if (index > 0) Box(Modifier.weight(.4f).height(1.dp).background(PatchTokens.line))
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                val done = chapter.missions.all { it.id in state.completed }
                val active = state.chapter(book).id == chapter.id && !done
                QuestChest(Modifier.size(44.dp).then(if (!done && !active) Modifier.background(PatchTokens.soft, CircleShape) else Modifier))
                Text(if (done) "챕터 ${chapter.number} 완료" else if (active) "챕터 ${chapter.number} 진행 중" else "목표 완주",
                    fontSize = 10.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
            }
        }
    }
}

@Composable
internal fun QuestRouteSheet(state: QuestPreviewState, book: PreviewBook, onDismiss: () -> Unit,
    onOpen: (PreviewChapter) -> Unit) {
    PatchSheet("전체 공략 경로", onDismiss) {
        Text(book.title, style = MaterialTheme.typography.titleLarge)
        book.chapters.forEach { chapter ->
            val done = chapter.missions.count { it.id in state.completed }
            val unlocked = book.chapters.takeWhile { it.id != chapter.id }.all { c -> c.missions.all { it.id in state.completed } }
            Row(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.surface)
                .clickable(enabled = unlocked, role = Role.Button) { onOpen(chapter) }.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically) {
                QuestGlyph(if (done == chapter.missions.size) R.drawable.ic_patch_check else if (unlocked) R.drawable.ic_patch_route else R.drawable.ic_patch_lock,
                    Modifier.size(26.dp), if (done == chapter.missions.size) PatchTokens.red else PatchTokens.muted)
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text("챕터 ${chapter.number} · ${chapter.title}", fontWeight = FontWeight.Bold)
                    Text(if (unlocked) "$done / ${chapter.missions.size} 미션 완료" else "앞 챕터 완료 후 시작", style = MaterialTheme.typography.bodySmall,
                        color = PatchTokens.muted)
                }
            }
        }
    }
}
