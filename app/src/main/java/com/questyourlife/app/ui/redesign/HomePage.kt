package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Toll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.MascotArt
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun PatchHomePage(
    model: DailyQuestViewModel,
    onDaily: (DailyQuestEntity) -> Unit,
    onQuests: () -> Unit,
    onClan: () -> Unit,
    onCompanion: () -> Unit
) {
    val level by model.userLevel.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val coins by model.coins.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val done = daily.count { it.completedAt != null }
    val weekStart = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays((LocalDate.now(ZoneId.of("Asia/Seoul")).dayOfWeek.value - 1).toLong()).toString()
    val weekDaily = history.filter { it.completedAt != null && it.dateKey >= weekStart }.sumOf { it.reward }
    val weekEpoch = LocalDate.parse(weekStart).atStartOfDay(java.time.ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()
    val weekStep = steps.filter { (it.completedAt ?: 0L) >= weekEpoch }.sumOf { it.reward }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = PatchTokens.space20, end = PatchTokens.space20,
            top = PatchTokens.space8, bottom = PatchTokens.space24),
        verticalArrangement = Arrangement.spacedBy(PatchTokens.space12)
    ) {
        item { LevelProgress(level) }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.space12))
                .background(PatchTokens.soft).padding(PatchTokens.space12),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Toll, null, Modifier.size(20.dp), tint = PatchTokens.ink)
                Spacer(Modifier.width(PatchTokens.space8))
                Text("내 캐시 ", style = MaterialTheme.typography.bodySmall)
                Text("$coins C", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.weight(1f))
                Text("이번 주 +${weekDaily + weekStep} C", style = MaterialTheme.typography.labelMedium,
                    color = PatchTokens.muted)
            }
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.radius))
                .background(Color(0xFF252B25)).padding(PatchTokens.space20),
                verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                    Text("TODAY'S PATCH", color = Color.White.copy(alpha = .7f),
                        style = MaterialTheme.typography.labelMedium)
                    Text("오늘의 작은 완료가\n내일의 나를 바꿔요", color = Color.White,
                        style = MaterialTheme.typography.titleLarge)
                    Text(if (done == 0) "가볍게 하나부터 시작해 봐요" else "오늘 ${done}개의 작은 약속을 지켰어요",
                        color = Color.White.copy(alpha = .75f), style = MaterialTheme.typography.bodySmall)
                }
                MascotArt(Modifier.size(80.dp))
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    PatchSection("오늘의 퀘스트")
                    Spacer(Modifier.weight(1f))
                    Text("$done / ${daily.size} 완료", color = PatchTokens.muted,
                        style = MaterialTheme.typography.labelMedium)
                }
                LinearProgressIndicator(progress = { if (daily.isEmpty()) 0f else done.toFloat() / daily.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = PatchTokens.red, trackColor = PatchTokens.line)
            }
        }
        if (daily.isEmpty()) item {
            PatchSurface {
                Text("오늘 남은 제안이 없어요", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = model::refresh) { Text("다시 확인") }
            }
        }
        items(daily, key = { it.id }) { quest ->
            PatchDailyRow(quest, onOpen = { onDaily(quest) }, onAction = {
                if (quest.accepted) model.complete(quest.id) else model.accept(quest.id)
            })
        }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.space16))
                .background(PatchTokens.yellow.copy(alpha = .4f)).padding(PatchTokens.space16),
                verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
                    Text("지금 가능한 한 걸음", style = MaterialTheme.typography.titleSmall)
                    Text("패치와 목표를 작게 나눠 봐요", style = MaterialTheme.typography.bodySmall,
                        color = PatchTokens.muted)
                }
                TextButton(onClick = onCompanion) { Text("쪽지 열기", color = PatchTokens.red) }
            }
        }
        item {
            PatchSurface(Modifier.clickable(role = Role.Button, onClick = onClan)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Groups, null, tint = PatchTokens.muted)
                    Spacer(Modifier.width(PatchTokens.space8))
                    Text("함께하는 연습", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Text("데모", style = MaterialTheme.typography.labelSmall, color = PatchTokens.muted)
                    Icon(Icons.Rounded.ChevronRight, null, tint = PatchTokens.muted)
                }
                Text("내 기록은 실제로, 클랜 멤버는 데모로 둘러봐요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
        }
        item {
            TextButton(onClick = onQuests, modifier = Modifier.fillMaxWidth().heightIn(min = PatchTokens.control)) {
                Text("내 목표와 퀘스트 보기", color = PatchTokens.ink)
                Icon(Icons.Rounded.ChevronRight, null, tint = PatchTokens.muted)
            }
        }
    }
}

@Composable
private fun PatchDailyRow(quest: DailyQuestEntity, onOpen: () -> Unit, onAction: () -> Unit) {
    val done = quest.completedAt != null
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.space16))
        .background(PatchTokens.surface).clickable(role = Role.Button, onClick = onOpen)
        .padding(end = PatchTokens.space12, top = PatchTokens.space4, bottom = PatchTokens.space4),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onAction, enabled = !done, modifier = Modifier.size(52.dp)) {
            Icon(if (done) Icons.Rounded.Check else Icons.Rounded.RadioButtonUnchecked,
                if (done) "${quest.title}, 완료됨" else if (quest.accepted) "${quest.title}, 완료하기" else "${quest.title}, 수락하기",
                tint = if (done) PatchTokens.red else PatchTokens.muted)
        }
        Column(Modifier.weight(1f).padding(vertical = PatchTokens.space8),
            verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
            Text(quest.title, style = MaterialTheme.typography.titleSmall,
                color = if (done) PatchTokens.muted else PatchTokens.ink,
                maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(if (done) "완료했어요" else if (quest.accepted) "진행 중 · 체크하면 완료" else "수락하고 시작하기",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
        Spacer(Modifier.width(PatchTokens.space8))
        Text("+${quest.reward} C", style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold, color = if (done) PatchTokens.muted else PatchTokens.ink,
            modifier = Modifier.clip(CircleShape).background(if (done) PatchTokens.soft else PatchTokens.yellow)
                .padding(horizontal = PatchTokens.space8, vertical = PatchTokens.space4))
    }
}
