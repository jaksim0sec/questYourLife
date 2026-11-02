package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.MascotArt
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.text.NumberFormat
import java.util.Locale

private val ProfileZone = ZoneId.of("Asia/Seoul")

@Composable
fun PatchProfilePage(
    model: DailyQuestViewModel,
    onHistory: () -> Unit,
    onDecor: () -> Unit,
    onSettings: () -> Unit
) {
    val coins by model.coins.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val ownedItems by model.ownedItems.collectAsStateWithLifecycle()
    val sessions by model.sessions.collectAsStateWithLifecycle()
    val today = LocalDate.now(ProfileZone)
    val monday = today.minusDays((today.dayOfWeek.value - 1).toLong())
    val nextMonday = monday.plusWeeks(1)
    val rewardEvents = remember(history, steps) {
        history.mapNotNull { quest -> quest.completedAt?.let { it to quest.reward } } +
            steps.mapNotNull { step -> step.completedAt?.let { it to step.reward } }
    }
    val completions = remember(history, steps) {
        val completedDaily = history.filter { it.completedAt != null }
        val linkedSteps = completedDaily.mapNotNull { quest ->
            quest.goalId?.let { goalId -> quest.stepPosition?.let { goalId to it } }
        }.toSet()
        // A linked daily completion also marks its goal step complete: one action, one record.
        completedDaily.mapNotNull { quest -> quest.completedAt?.let { it to quest.reward } } +
            steps.filter { (it.goalId to it.position) !in linkedSteps }
                .mapNotNull { step -> step.completedAt?.let { it to step.reward } }
    }
    val weekStart = monday.atStartOfDay(ProfileZone).toInstant().toEpochMilli()
    val weekEnd = nextMonday.atStartOfDay(ProfileZone).toInstant().toEpochMilli()
    val weeklyEarned = remember(rewardEvents, weekStart, weekEnd) {
        rewardEvents.filter { (timestamp, _) -> timestamp >= weekStart && timestamp < weekEnd }
            .sumOf { it.second }
    }
    val weekly = remember(completions, monday) {
        completions.filter { (timestamp, _) ->
            val date = Instant.ofEpochMilli(timestamp).atZone(ProfileZone).toLocalDate()
            !date.isBefore(monday) && date.isBefore(nextMonday)
        }
    }
    val activeDays = remember(weekly) {
        weekly.map { (timestamp, _) ->
            Instant.ofEpochMilli(timestamp).atZone(ProfileZone).toLocalDate()
        }.toSet()
    }
    val focusMinutes = sessions.sumOf { it.durationSeconds.toLong() } / 60
    val numbers = remember { NumberFormat.getIntegerInstance(Locale.KOREA) }

    Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(horizontal = PatchTokens.space20)
            .padding(bottom = PatchTokens.space24),
        verticalArrangement = Arrangement.spacedBy(PatchTokens.space20)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = PatchTokens.space8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PatchTokens.space16)
        ) {
            MascotArt(Modifier.size(128.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                Text("이 기기의 나", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                Text("작은 실천을 쌓는 중", style = MaterialTheme.typography.bodySmall,
                    color = PatchTokens.muted)
                PatchButton("프로필 꾸미기", onClick = onDecor)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
            PatchSurface(Modifier.weight(1f)) {
                Text("보유 캐시", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                Text("${numbers.format(coins)} C", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, color = PatchTokens.ink)
            }
            PatchSurface(Modifier.weight(1f)) {
                Text("이번 주 획득", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                Text("${numbers.format(weeklyEarned)} C",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, color = PatchTokens.red)
            }
        }

        PatchSurface {
            PatchSection("이번 주의 실천")
            Row(Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text("퀘스트 ${weekly.size}개 완료", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    Text("일일 퀘스트와 목표 단계", style = MaterialTheme.typography.bodySmall,
                        color = PatchTokens.muted)
                }
                Text("${activeDays.size}일 실천", style = MaterialTheme.typography.labelLarge,
                    color = PatchTokens.red)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("월", "화", "수", "목", "금", "토", "일").forEachIndexed { index, weekday ->
                    val date = monday.plusDays(index.toLong())
                    val completed = date in activeDays
                    val description = "${date.monthValue}월 ${date.dayOfMonth}일 ${weekday}요일, " +
                        when {
                            date.isAfter(today) -> "아직 오지 않은 날"
                            completed -> "실천 기록 있음"
                            else -> "완료 기록 없음"
                        }
                    Column(
                        Modifier.clearAndSetSemantics { contentDescription = description },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)
                    ) {
                        Box(Modifier.size(32.dp).background(
                            if (completed) PatchTokens.red else PatchTokens.background, CircleShape),
                            contentAlignment = Alignment.Center) {
                            if (completed) Icon(Icons.Rounded.Check, null,
                                Modifier.size(20.dp), tint = PatchTokens.surface)
                            else Text("·", style = MaterialTheme.typography.titleMedium,
                                color = PatchTokens.muted)
                        }
                        Text(weekday, style = MaterialTheme.typography.labelSmall,
                            color = if (date == today) PatchTokens.ink else PatchTokens.muted,
                            fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
            Text(if (weekly.isEmpty()) "완료한 실천이 여기에 차곡차곡 쌓여요." else
                "${monday.monthValue}월 ${monday.dayOfMonth}일 ~ ${nextMonday.minusDays(1).monthValue}월 ${nextMonday.minusDays(1).dayOfMonth}일 · 한국 시간 기준",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }

        PatchSurface {
            PatchSection("쌓인 기록")
            Row(Modifier.fillMaxWidth().padding(top = PatchTokens.space12),
                horizontalArrangement = Arrangement.spacedBy(PatchTokens.space16)) {
                Column(Modifier.weight(1f)) {
                    Text("전체 완료", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                    Spacer(Modifier.height(PatchTokens.space4))
                    Text("${numbers.format(completions.size)}개", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                }
                Column(Modifier.weight(1f)) {
                    Text("집중 기록", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                    Spacer(Modifier.height(PatchTokens.space4))
                    Text("${numbers.format(focusMinutes)}분", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                }
            }
        }

        Column {
            ProfileLink(Icons.Rounded.Palette, "꾸미기 보관함", "구매한 아이템 ${ownedItems.size}개", onDecor)
            HorizontalDivider(color = PatchTokens.line)
            ProfileLink(Icons.Rounded.History, "성취 기록", "완료한 퀘스트와 집중 기록", onHistory)
            HorizontalDivider(color = PatchTokens.line)
            ProfileLink(Icons.Rounded.Settings, "알림 설정", "실천을 이어갈 시간 정하기", onSettings)
        }
        Text("목표와 기록은 이 기기에 저장돼요. 다른 사람에게 공개되지 않아요.",
            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
    }
}

@Composable
private fun ProfileLink(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick)
        .heightIn(min = 64.dp).padding(vertical = PatchTokens.space12),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(24.dp), tint = PatchTokens.ink)
        Spacer(Modifier.width(PatchTokens.space12))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = PatchTokens.ink,
                fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
        Icon(Icons.Rounded.ChevronRight, null, Modifier.size(24.dp), tint = PatchTokens.muted)
    }
}
