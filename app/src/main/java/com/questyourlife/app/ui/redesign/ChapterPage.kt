package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.companion.*
import com.questyourlife.app.ui.CosmeticImage
import com.questyourlife.app.ui.DailyQuestViewModel

/** Chapter path and all mission/reward actions operate exclusively on the persisted preview. */
@Composable
fun PatchChapterPage(model: DailyQuestViewModel, bookId: String, initialChapterId: String? = null, onBack: () -> Unit, onMenu: () -> Unit) {
    val state by model.questPreview.state.collectAsStateWithLifecycle()
    val book = QuestPreviewCatalog.book(bookId) ?: return
    var chapterId by rememberSaveable(bookId, initialChapterId) {
        mutableStateOf(book.chapters.firstOrNull { it.id == initialChapterId }?.id ?: state.chapter(book).id)
    }
    val chapter = book.chapters.firstOrNull { it.id == chapterId } ?: state.chapter(book)
    val done = chapter.missions.count { it.id in state.completed }
    val current = chapter.missions.firstOrNull { it.id !in state.completed }
    var route by rememberSaveable { mutableStateOf(false) }
    var missionOpen by rememberSaveable { mutableStateOf(false) }
    var completedMission by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedItem by rememberSaveable { mutableIntStateOf(1001) }
    val chapterComplete = done == chapter.missions.size
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { QuestGlyph(R.drawable.ic_patch_arrow_left, description = "퀘스트 목록으로") }
            Text(chapter.title.takeIf { it.length <= 10 } ?: "챕터 ${chapter.number}", Modifier.weight(1f),
                fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
            IconButton(onClick = onMenu) { QuestGlyph(R.drawable.ic_patch_more_vertical, description = "메뉴") }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)
            .padding(top = 8.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("${book.title} · CHAPTER ${chapter.number.toString().padStart(2, '0')}",
                    fontSize = 11.sp, lineHeight = 16.sp, color = PatchTokens.muted, fontWeight = FontWeight.Bold)
                Text(chapter.title, fontSize = 25.sp, lineHeight = 32.sp, fontWeight = FontWeight.Black, color = PatchTokens.ink)
            }
            ChapterPath(state, chapter) { mission ->
                if (mission.id == current?.id) missionOpen = true
            }
            Column(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.soft).padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(if (chapterComplete) "챕터 클리어!" else "현재 미션", fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, color = PatchTokens.muted)
                Text(current?.action ?: "${chapter.title} 완료", fontSize = 20.sp, lineHeight = 28.sp,
                    fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                Text(current?.let { "${it.minutes}분 · +20 C 시연 보상" } ?: "함께 한 걸음, 내 보상 하나",
                    fontSize = 12.sp, color = PatchTokens.muted)
                Spacer(Modifier.height(2.dp))
                QuestAction(if (chapterComplete) "챕터 보상 보기" else if (current?.id in state.started) "미션 이어하기" else "미션 시작하기",
                    { if (chapterComplete) completedMission = chapter.missions.last().id else missionOpen = true })
            }
            QuestTextLink("전체 공략 경로 보기", { route = true })
            Text("시연 진행은 이 기기에 저장돼요. 실제 캐시·아이템 보관함과는 별개예요.",
                fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted)
        }
    }
    if (route) QuestRouteSheet(state, book, { route = false }) { selected -> chapterId = selected.id; route = false }
    if (missionOpen && current != null) {
        val started = current.id in state.started
        PatchSheet(if (started) "오늘의 미션 · 진행 중" else "오늘의 미션", { missionOpen = false },
            footer = {
                QuestAction(if (started) "실천했어요 · 완료하기" else "미션 시작하기", {
                    if (started) {
                        model.questPreview.complete(current.id)
                        completedMission = current.id
                        missionOpen = false
                    } else model.questPreview.start(current.id)
                })
            }) {
            QuestPill("챕터 ${chapter.number} · ${current.minutes}분", QuestYellow, QuestNight)
            Text(current.action, style = MaterialTheme.typography.headlineSmall)
            Text(current.detail, style = MaterialTheme.typography.bodyLarge, color = PatchTokens.ink)
            Text("직접 실천한 뒤 완료해 주세요. 제한 시간이 있는 타이머가 아니에요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            QuestPill("+20 C · 시연 보상", QuestYellow, QuestNight)
            TextButton(onClick = { missionOpen = false }) { Text("나중에 하기", color = PatchTokens.muted) }
        }
    }
    if (completedMission != null) {
        val claimed = state.rewards[chapter.id]
        val next = book.chapters.dropWhile { it.id != chapter.id }.drop(1).firstOrNull()
        PatchSheet(if (chapterComplete) "챕터 클리어!" else "미션 클리어!", { completedMission = null },
            startExpanded = true, footer = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (chapterComplete) {
                        QuestAction(if (claimed == null) "이 아이템 받기" else "시연 보관함에 저장했어요", {
                            model.questPreview.claim(chapter.id, selectedItem)
                        }, enabled = claimed == null)
                        QuestTextLink(if (next == null) "퀘스트 목록으로" else "다음 챕터 보기", {
                            completedMission = null
                            if (next != null) chapterId = next.id else onBack()
                        })
                    } else QuestAction("다음 미션 보기", { completedMission = null; missionOpen = true })
                }
            }) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                QuestChest(Modifier.size(150.dp), "미션 완료 보상 상자")
            }
            Text(if (chapterComplete) "${chapter.title}\n상자를 열었어요" else "한 걸음 더 나아갔어요!",
                style = MaterialTheme.typography.headlineSmall)
            Text(if (chapterComplete) "마음에 드는 꾸미기 하나를 골라요." else "+20 C 시연 보상 · 이 챕터 $done / ${chapter.missions.size} 완료",
                color = PatchTokens.muted, style = MaterialTheme.typography.bodyMedium)
            if (chapterComplete) {
                Row(Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "골지 비니", 1001 to "크림 헤드폰", 2 to "블루 캡").forEach { (item, title) ->
                        val selected = (claimed ?: selectedItem) == item
                        Column(Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                            .background(PatchTokens.surface).border(if (selected) 2.dp else 1.dp,
                                if (selected) PatchTokens.red else PatchTokens.line, RoundedCornerShape(14.dp))
                            .selectable(selected = selected, enabled = claimed == null, role = Role.RadioButton) { selectedItem = item }
                            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            CosmeticImage(item, Modifier.fillMaxWidth().height(72.dp))
                            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                            if (selected) QuestGlyph(R.drawable.ic_patch_check, Modifier.size(18.dp), PatchTokens.red)
                            else Spacer(Modifier.height(18.dp))
                        }
                    }
                }
            }
            Text("시연 보상이에요. 실제 내 캐시와 꾸미기 보관함에는 합산되지 않아요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
    }
}

@Composable
private fun ChapterPath(state: QuestPreviewState, chapter: PreviewChapter, onMission: (PreviewMission) -> Unit) {
    val scale = LocalDensity.current.fontScale.coerceAtLeast(1f)
    val nodeSpace = 76.dp * scale
    val top = 54.dp * scale
    val pathHeight = top + nodeSpace * chapter.missions.size + 140.dp
    val current = chapter.missions.firstOrNull { it.id !in state.completed }
    BoxWithConstraints(Modifier.fillMaxWidth().height(pathHeight).clip(QuestShape).background(QuestNight)) {
        val contentWidth = maxWidth
        Canvas(Modifier.fillMaxSize()) {
            val path = Path()
            val points = chapter.missions.indices.map { index ->
                androidx.compose.ui.geometry.Offset(size.width * if (index % 2 == 0) .18f else .62f,
                    (top + nodeSpace * index + 24.dp).toPx())
            }
            points.forEachIndexed { index, point ->
                if (index == 0) path.moveTo(point.x, point.y)
                else {
                    val previous = points[index - 1]
                    path.cubicTo(previous.x, previous.y + nodeSpace.toPx() * .65f,
                        point.x, point.y - nodeSpace.toPx() * .65f, point.x, point.y)
                }
            }
            points.lastOrNull()?.let { last ->
                path.cubicTo(last.x, last.y + 70.dp.toPx(), size.width * .24f, last.y + 20.dp.toPx(),
                    size.width * .24f, size.height - 95.dp.toPx())
            }
            drawPath(path, QuestYellow, style = Stroke(3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 7.dp.toPx()))))
        }
        Row(Modifier.align(Alignment.TopEnd).padding(12.dp)) {
            QuestPill("${chapter.missions.count { it.id in state.completed }} / ${chapter.missions.size} 단계 완료", QuestLime, QuestNight)
        }
        chapter.missions.forEachIndexed { index, mission ->
            val done = mission.id in state.completed
            val active = mission.id == current?.id
            Row(Modifier.offset(x = contentWidth * if (index % 2 == 0) .07f else .45f,
                y = top + nodeSpace * index).width(contentWidth * .5f).heightIn(min = 48.dp)
                .clip(CircleShape).background(if (active) QuestYellow else QuestNightInk)
                .clickable(enabled = active, role = Role.Button, onClickLabel = "${mission.action} 열기") { onMission(mission) }
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(29.dp).clip(CircleShape).background(if (active) QuestNightInk else Color.Transparent)
                    .border(2.dp, if (done) Color(0xFFFF4E42) else if (active) QuestYellow else Color(0xFF959992), CircleShape),
                    contentAlignment = Alignment.Center) {
                    if (done || !active) QuestGlyph(if (done) R.drawable.ic_patch_check else R.drawable.ic_patch_lock,
                        Modifier.size(18.dp), if (done) Color(0xFFFF4E42) else Color(0xFF777D75))
                    else Box(Modifier.size(15.dp).background(QuestYellow, CircleShape))
                }
                Text("${index + 1}. ${mission.label}", Modifier.weight(1f).padding(start = 4.dp, end = 2.dp),
                    fontSize = 12.sp, lineHeight = 17.sp, fontWeight = FontWeight.ExtraBold, color = QuestNight)
            }
        }
        Column(Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            QuestChest(Modifier.size(94.dp), "챕터 완료 보상")
            QuestPill("챕터 보상", QuestLime, QuestNight)
        }
        Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(10.dp)
            .clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = .09f)).padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            QuestGlyph(R.drawable.ic_patch_chest, Modifier.padding(end = 6.dp).size(18.dp), QuestYellow)
            Text("다음 보상  |  ${if (chapter.number == 2) "주문 연습 상자" else "챕터 완료 상자"}", Modifier.weight(1f),
                fontSize = 11.sp, fontWeight = FontWeight.Bold, color = QuestNightInk)
            CosmeticImage(1003, Modifier.size(30.dp))
        }
    }
}
