package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.MascotArt
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/** Demo cooperation uses fixture members; only the owner's contribution reads Room state. */
@Composable
fun PatchClanPage(model: DailyQuestViewModel) {
    val history by model.history.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val clan = model.demoClan
    val zone = remember { ZoneId.of("Asia/Seoul") }
    val today = LocalDate.now(zone)
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekStart = monday.atStartOfDay(zone).toInstant().toEpochMilli()
    val weekEnd = monday.plusWeeks(1).atStartOfDay(zone).toInstant().toEpochMilli()
    val earned = remember(history, steps, weekStart, weekEnd) {
        history.sumOf { quest ->
            if (quest.completedAt?.let { it >= weekStart && it < weekEnd } == true) quest.reward else 0
        } + steps.sumOf { step ->
            if (step.completedAt?.let { it >= weekStart && it < weekEnd } == true) step.reward else 0
        }
    }
    val completed = remember(history, steps, weekStart, weekEnd) {
        val linkedCompletedSteps = history.mapNotNull { quest ->
            val goalId = quest.goalId
            val position = quest.stepPosition
            if (quest.completedAt != null && goalId != null && position != null) goalId to position else null
        }.toSet()
        history.count { it.completedAt?.let { time -> time >= weekStart && time < weekEnd } == true } +
            steps.count {
                (it.goalId to it.position) !in linkedCompletedSteps &&
                    it.completedAt?.let { time -> time >= weekStart && time < weekEnd } == true
            }
    }
    val demoCash = clan.members.sumOf { it.weeklyCash }
    val combinedCash = demoCash + earned
    val target = clan.weeklyTarget.coerceAtLeast(1)
    val progress = (combinedCash.toFloat() / target).coerceIn(0f, 1f)
    var showInfo by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(horizontal = PatchTokens.space20, vertical = PatchTokens.space8),
        verticalArrangement = Arrangement.spacedBy(PatchTokens.space20)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(PatchTokens.control).clip(RoundedCornerShape(PatchTokens.space12))
                    .background(PatchTokens.yellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Flag, null, Modifier.size(28.dp), tint = PatchTokens.ink)
            }
            Spacer(Modifier.width(PatchTokens.space12))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
                Text(clan.name, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = PatchTokens.ink,
                    modifier = Modifier.semantics { heading() })
                Text("데모 클랜 · 나와 데모 멤버 ${clan.members.size}명",
                    style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
            }
            IconButton(onClick = { showInfo = true }) {
                Icon(Icons.Rounded.Info, "데모 클랜 안내", tint = PatchTokens.muted)
            }
        }

        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.radius))
                .background(PatchTokens.ink).padding(PatchTokens.space20),
            verticalArrangement = Arrangement.spacedBy(PatchTokens.space16)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("각자 해내고,\n함께 채우자", Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                    color = PatchTokens.surface)
                Text("데모", style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold, color = PatchTokens.ink,
                    modifier = Modifier.clip(CircleShape).background(PatchTokens.yellow)
                        .padding(horizontal = PatchTokens.space12, vertical = PatchTokens.space8))
            }
            Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                Text("이번 주 공동 목표", style = MaterialTheme.typography.labelMedium,
                    color = PatchTokens.surface)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.weight(1f).height(12.dp).clip(CircleShape),
                        color = PatchTokens.yellow,
                        trackColor = PatchTokens.surface.copy(alpha = .24f)
                    )
                    Spacer(Modifier.width(PatchTokens.space12))
                    Text("$combinedCash / $target C", style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold, color = PatchTokens.surface)
                }
                Text(if (combinedCash >= target) "이번 주 연습 목표를 채웠어요!" else "내 작은 실천도 여기에 쌓여요",
                    style = MaterialTheme.typography.bodyMedium, color = PatchTokens.surface)
            }
            HorizontalDivider(color = PatchTokens.surface.copy(alpha = .2f))
            Text("데모 기여 $demoCash C + 내 실제 기록 $earned C",
                style = MaterialTheme.typography.labelMedium, color = PatchTokens.surface)
            Text("공동 보상은 준비 중이에요. 지금은 캐시나 꾸미기 보상이 지급되지 않아요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.surface.copy(alpha = .8f))
        }

        Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
            PatchSection("이번 주 함께 쌓은 기록")
            PatchSurface {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    MascotArt(Modifier.size(PatchTokens.control))
                    Spacer(Modifier.width(PatchTokens.space12))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
                        Text("나의 실천", style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                        Text("퀘스트 ${completed}개 완료", style = MaterialTheme.typography.bodySmall,
                            color = PatchTokens.muted)
                    }
                    Text("+$earned C", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = PatchTokens.red)
                }
                Text("일일 퀘스트와 목표 단계의 실제 완료 보상이에요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
            PatchSection("멤버 기여도 · 데모")
            PatchSurface {
                if (clan.members.isEmpty()) {
                    Text("아직 표시할 데모 멤버가 없어요.",
                        style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
                }
                Column {
                    clan.members.forEachIndexed { index, member ->
                        if (index > 0) HorizontalDivider(
                            Modifier.padding(vertical = PatchTokens.space12), color = PatchTokens.line)
                        ClanDemoMember(member.name, member.weeklyCash)
                    }
                }
            }
        }

        Text("월요일부터 일요일까지 · 한국 시간 기준\n데모 멤버는 가상 인물이에요. 실제 친구 활동이나 온라인 순위가 아니에요.",
            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        Spacer(Modifier.height(PatchTokens.space8))
    }

    if (showInfo) {
        PatchSheet(title = "데모 클랜 안내", onDismiss = { showInfo = false }) {
            Text("데모 멤버의 예시 기여와 이 기기에 저장된 내 완료 기록으로 함께 채우는 화면이에요.",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.ink)
            Text("이번 주 기여는 한국 시간 월요일 0시부터 계산해요. 캐시를 사용해도 이미 획득한 기여는 줄어들지 않아요.",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
            Text("실제 클랜 가입·초대·온라인 순위와 공동 보상 지급은 아직 지원하지 않아요.",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
            PatchButton(text = "확인", onClick = { showInfo = false })
        }
    }
}

@Composable
private fun ClanDemoMember(name: String, weeklyCash: Int) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(PatchTokens.background),
            contentAlignment = Alignment.Center) {
            Text(name.take(1), style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = PatchTokens.ink)
        }
        Spacer(Modifier.width(PatchTokens.space12))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
            Text(name, style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold, color = PatchTokens.ink)
            Text("데모 멤버", style = MaterialTheme.typography.labelSmall, color = PatchTokens.muted)
        }
        Text("+$weeklyCash C", style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold, color = PatchTokens.ink)
    }
}
