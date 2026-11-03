package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.ui.DailyQuestViewModel

/** The original illustrated path rendered from real stable steps, never QuestPreviewStore. */
@Composable
fun PatchChapterPage(model: DailyQuestViewModel, bookId: String, initialChapterId: String? = null,
    onBack: () -> Unit, onMenu: () -> Unit) {
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val goal = goals.firstOrNull { it.id == bookId }
    val book = goal?.let { LiveQuestPath.from(it, steps, daily + history) }
    if (book == null) {
        Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("퀘스트북을 찾을 수 없어요", style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink)
            QuestAction("퀘스트 목록으로", onBack)
        }
        return
    }
    if (book.chapters.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            IconButton(onClick = onBack) { QuestGlyph(R.drawable.ic_patch_arrow_left, description = "뒤로가기") }
            Text(book.title, style = MaterialTheme.typography.headlineSmall, color = PatchTokens.ink)
            Text("아직 등록된 단계가 없어요", style = MaterialTheme.typography.bodyLarge, color = PatchTokens.muted)
            QuestAction("퀘스트 목록으로", onBack)
        }
        return
    }
    var chapterId by rememberSaveable(bookId, initialChapterId) {
        mutableStateOf(initialChapterId ?: book.currentChapter()?.id)
    }
    val chapter = book.chapterById(chapterId) ?: book.currentChapter() ?: book.chapters.first()
    val current = chapter.missions.firstOrNull { !it.completed }
    val actionable = current?.takeIf { it.id == book.current()?.id }
    var routeOpen by rememberSaveable(bookId) { mutableStateOf(false) }
    var selectedMissionId by rememberSaveable(bookId) { mutableStateOf<String?>(null) }
    val selectedMission = chapter.missions.firstOrNull { it.id == selectedMissionId }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                QuestGlyph(R.drawable.ic_patch_arrow_left, description = "퀘스트 목록으로")
            }
            Text(chapter.title, Modifier.weight(1f), fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
            IconButton(onClick = onMenu) { QuestGlyph(R.drawable.ic_patch_more_vertical, description = "메뉴") }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)
            .padding(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("${book.title} · CHAPTER ${chapter.number.toString().padStart(2, '0')}",
                    fontSize = 11.sp, lineHeight = 16.sp, color = PatchTokens.muted, fontWeight = FontWeight.Bold)
                Text(chapter.title, fontSize = 25.sp, lineHeight = 32.sp,
                    fontWeight = FontWeight.Black, color = PatchTokens.ink)
            }
            LiveChapterPath(chapter, actionable?.id) { selectedMissionId = it.id }
            Column(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.soft).padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(if (chapter.complete) "챕터 완주" else "현재 미션",
                    fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PatchTokens.muted)
                Text(current?.title ?: "${chapter.title} 완료", fontSize = 20.sp, lineHeight = 28.sp,
                    fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                Text(if (chapter.complete) "${chapter.done} / ${chapter.missions.size} 단계 완료"
                    else if (actionable == null) "앞 챕터를 먼저 완료해 주세요"
                    else "직접 실천한 뒤 완료를 기록해요",
                    fontSize = 12.sp, color = PatchTokens.muted)
                Spacer(Modifier.height(3.dp))
                QuestAction(when {
                    chapter.complete -> "공략 경로 보기"
                    actionable == null -> "앞 챕터부터 이어하기"
                    actionable.dailyId != null && !actionable.dailyAccepted -> "오늘의 미션 수락하기"
                    else -> "미션 상세 보기"
                }, {
                    when {
                        chapter.complete -> routeOpen = true
                        actionable == null -> routeOpen = true
                        else -> selectedMissionId = actionable.id
                    }
                })
            }
            QuestTextLink("전체 공략 경로 보기", { routeOpen = true })
            if (chapter.complete) {
                Row(Modifier.fillMaxWidth().padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    QuestChest(Modifier.size(88.dp), "완료한 챕터")
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("한 챕터를 완주했어요", fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                        Text("완료 기록은 퀘스트북에 저장돼요",
                            fontSize = 11.sp, color = PatchTokens.muted)
                    }
                }
            }
        }
    }
    if (routeOpen) LiveChapterRouteSheet(book, {
        routeOpen = false
    }) { selected ->
        chapterId = selected.id
        selectedMissionId = null
        routeOpen = false
    }
    if (selectedMission != null && selectedMission.id == book.current()?.id) {
        val needsAccept = selectedMission.dailyId != null && !selectedMission.dailyAccepted
        PatchSheet(if (needsAccept) "오늘의 미션" else "미션 완료하기",
            onDismiss = { selectedMissionId = null },
            footer = {
                QuestAction(if (needsAccept) "미션 수락하기" else "실천했어요 · 완료하기", {
                    if (needsAccept) model.accept(selectedMission.dailyId!!)
                    else model.completeStep(selectedMission.id)
                    selectedMissionId = null
                })
            }) {
            QuestPill(chapter.title, QuestYellow, QuestNight)
            Text(selectedMission.title, style = MaterialTheme.typography.headlineSmall,
                color = PatchTokens.ink)
            Text("내가 직접 실천한 뒤 완료를 눌러요.",
                style = MaterialTheme.typography.bodyLarge, color = PatchTokens.muted)
            Text(if (signedIn) "보상은 서버 기록 확인 후 확정돼요."
                else "이 단계의 기록 보상 +${selectedMission.reward} C",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
    }
}

/** Same curved, dashed and alternating visual rhythm as the supplied reference. */
@Composable
private fun LiveChapterPath(chapter: LivePathChapter, activeId: String?,
    onMission: (LivePathMission) -> Unit) {
    val scale = LocalDensity.current.fontScale.coerceAtLeast(1f)
    val nodeSpace = 76.dp * scale
    val top = 54.dp * scale
    val pathHeight = top + nodeSpace * chapter.missions.size + 140.dp
    BoxWithConstraints(Modifier.fillMaxWidth().height(pathHeight)
        .clip(QuestShape).background(QuestNight)) {
        val width = maxWidth
        Canvas(Modifier.fillMaxSize()) {
            val path = Path()
            val points = chapter.missions.indices.map { index ->
                androidx.compose.ui.geometry.Offset(size.width * if (index % 2 == 0) .18f else .62f,
                    (top + nodeSpace * index + 24.dp).toPx())
            }
            points.forEachIndexed { index, point ->
                if (index == 0) path.moveTo(point.x, point.y)
                else {
                    val before = points[index - 1]
                    path.cubicTo(before.x, before.y + nodeSpace.toPx() * .65f,
                        point.x, point.y - nodeSpace.toPx() * .65f, point.x, point.y)
                }
            }
            points.lastOrNull()?.let { last ->
                path.cubicTo(last.x, last.y + 70.dp.toPx(), size.width * .24f,
                    last.y + 20.dp.toPx(), size.width * .24f, size.height - 95.dp.toPx())
            }
            drawPath(path, QuestYellow, style = Stroke(3.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 7.dp.toPx()))))
        }
        Row(Modifier.align(Alignment.TopEnd).padding(12.dp)) {
            QuestPill("${chapter.done} / ${chapter.missions.size} 단계 완료", QuestLime, QuestNight)
        }
        chapter.missions.forEachIndexed { index, mission ->
            val done = mission.completed
            val active = mission.id == activeId
            Row(Modifier.offset(x = width * if (index % 2 == 0) .07f else .45f,
                    y = top + nodeSpace * index)
                .width(width * .5f).heightIn(min = 48.dp).clip(CircleShape)
                .background(if (active) QuestYellow else QuestNightInk)
                .clickable(enabled = active, role = Role.Button,
                    onClickLabel = "${mission.title} 열기") { onMission(mission) }
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(29.dp).clip(CircleShape)
                    .background(if (active) QuestNightInk else Color.Transparent)
                    .border(2.dp, if (done) PatchTokens.red else if (active) QuestYellow else Color(0xFF959992),
                        CircleShape), contentAlignment = Alignment.Center) {
                    if (done || !active) QuestGlyph(if (done) R.drawable.ic_patch_check else R.drawable.ic_patch_lock,
                        Modifier.size(18.dp), if (done) PatchTokens.red else Color(0xFF777D75))
                    else Box(Modifier.size(15.dp).background(QuestYellow, CircleShape))
                }
                Text("${index + 1}. ${mission.title}", Modifier.weight(1f)
                    .padding(start = 5.dp, end = 3.dp), fontSize = 12.sp, lineHeight = 17.sp,
                    fontWeight = FontWeight.ExtraBold, color = QuestNight,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
        Column(Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            QuestChest(Modifier.size(94.dp), "챕터 완주 기록")
            QuestPill("챕터 완주", QuestLime, QuestNight)
        }
        Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(10.dp)
            .clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = .09f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically) {
            QuestGlyph(R.drawable.ic_patch_chest, Modifier.padding(end = 6.dp).size(18.dp), QuestYellow)
            Text(if (chapter.complete) "완주 기록에 남았어요" else "모든 단계를 마치면 챕터 완주",
                Modifier.weight(1f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = QuestNightInk)
        }
    }
}

@Composable
private fun LiveChapterRouteSheet(book: LivePathBook, onDismiss: () -> Unit,
    onOpen: (LivePathChapter) -> Unit) {
    PatchSheet("전체 공략 경로", onDismiss) {
        Text(book.title, style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink)
        book.chapters.forEach { chapter ->
            val unlocked = book.chapters.takeWhile { it.number != chapter.number }.all { it.complete }
            Row(Modifier.fillMaxWidth().clip(QuestShape).background(PatchTokens.surface)
                .clickable(enabled = unlocked, role = Role.Button) { onOpen(chapter) }.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically) {
                QuestGlyph(if (chapter.complete) R.drawable.ic_patch_check
                    else if (unlocked) R.drawable.ic_patch_route else R.drawable.ic_patch_lock,
                    Modifier.size(26.dp), if (chapter.complete) PatchTokens.red else PatchTokens.muted)
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(chapter.title, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    Text(if (unlocked) "${chapter.done} / ${chapter.missions.size} 단계 완료"
                        else "앞 챕터 완료 후 시작", fontSize = 12.sp, color = PatchTokens.muted)
                }
            }
        }
    }
}
