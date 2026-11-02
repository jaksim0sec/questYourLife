package com.questyourlife.app.ui.redesign

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val ProfileZone = ZoneId.of("Asia/Seoul")
private val ProfileShape = RoundedCornerShape(16.dp)

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
    var showStamps by rememberSaveable { mutableStateOf(false) }
    val today = LocalDate.now(ProfileZone)
    val monday = today.minusDays((today.dayOfWeek.value - 1).toLong())
    val nextMonday = monday.plusWeeks(1)
    val weekStart = monday.atStartOfDay(ProfileZone).toInstant().toEpochMilli()
    val weekEnd = nextMonday.atStartOfDay(ProfileZone).toInstant().toEpochMilli()

    // Both tables contribute to the persisted balance, including linked reward rows.
    val rewardEvents = remember(history, steps) {
        history.mapNotNull { quest -> quest.completedAt?.let { it to quest.reward } } +
            steps.mapNotNull { step -> step.completedAt?.let { it to step.reward } }
    }
    val completions = remember(history, steps) {
        val completedDaily = history.filter { it.completedAt != null }
        val linkedSteps = completedDaily.mapNotNull { quest ->
            quest.goalId?.let { goalId -> quest.stepPosition?.let { goalId to it } }
        }.toSet()
        // A daily quest can complete its parent step: count that action only once.
        completedDaily.mapNotNull { it.completedAt } +
            steps.filter { (it.goalId to it.position) !in linkedSteps }
                .mapNotNull { it.completedAt }
    }
    val weeklyEarned = remember(rewardEvents, weekStart, weekEnd) {
        rewardEvents.filter { (timestamp, _) -> timestamp >= weekStart && timestamp < weekEnd }
            .sumOf { it.second }
    }
    val weekly = remember(completions, weekStart, weekEnd) {
        completions.filter { it >= weekStart && it < weekEnd }
    }
    val completionDates = remember(completions) {
        completions.map { Instant.ofEpochMilli(it).atZone(ProfileZone).toLocalDate() }.toSet()
    }
    val activeDays = remember(weekly) {
        weekly.map { Instant.ofEpochMilli(it).atZone(ProfileZone).toLocalDate() }.toSet()
    }
    val hasConsecutiveWeek = remember(completionDates) {
        completionDates.any { lastDay ->
            (0L..6L).all { offset -> lastDay.minusDays(offset) in completionDates }
        }
    }
    val hasCompleteChapter = remember(steps) {
        steps.groupBy { it.goalId to it.chapter }.values.any { chapter ->
            chapter.isNotEmpty() && chapter.all { it.completedAt != null }
        }
    }
    val stamps = listOf(
        ProfileStamp("첫 시작", "퀘스트나 목표 단계를 처음 완료하면 받아요.", completions.isNotEmpty()),
        ProfileStamp("꾸준한 일주일", "7일 연속으로 퀘스트나 목표 단계를 완료하면 받아요.", hasConsecutiveWeek),
        ProfileStamp("첫 챕터", "한 챕터의 목표 단계를 모두 완료하면 받아요.", hasCompleteChapter)
    )

    Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            .padding(horizontal = PatchTokens.space16)
            .padding(top = PatchTokens.space4, bottom = PatchTokens.space12),
        verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)
    ) {
        ProfileHero(model.demoClan.name, model.equipped.intValue, onDecor)

        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val stacked = maxWidth < 300.dp || LocalDensity.current.fontScale > 1.3f ||
                coins.toString().length > 6 || weeklyEarned.toString().length > 6
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                    ProfileCash("보유 캐시", coins)
                    ProfileCash("이번 주 획득", weeklyEarned)
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                    ProfileCash("보유 캐시", coins, Modifier.weight(1f))
                    ProfileCash("이번 주 획득", weeklyEarned, Modifier.weight(1f))
                }
            }
        }

        PatchPanel(Modifier.clickable(role = Role.Button, onClickLabel = "이번 주 성취 기록 보기", onClick = onHistory)) {
            ProfileHeading("이번 주의 나", showArrow = true)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text("퀘스트", fontSize = 11.sp, lineHeight = 15.sp, color = PatchTokens.muted)
                    Text("${weekly.size}개 완료", fontSize = 17.sp, lineHeight = 23.sp,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                }
                Text("${activeDays.size}일 실천", fontSize = 15.sp, lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold, color = PatchTokens.ink,
                    modifier = Modifier.padding(start = PatchTokens.space8))
            }
            Row(Modifier.fillMaxWidth()) {
                listOf("월", "화", "수", "목", "금", "토", "일").forEachIndexed { index, weekday ->
                    val date = monday.plusDays(index.toLong())
                    val completed = date in activeDays
                    val description = "${date.monthValue}월 ${date.dayOfMonth}일 ${weekday}요일, " + when {
                        completed -> "실천 기록 있음"
                        date.isAfter(today) -> "아직 오지 않은 날"
                        else -> "완료 기록 없음"
                    }
                    Column(
                        Modifier.weight(1f).clearAndSetSemantics { contentDescription = description },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Image(painterResource(R.drawable.mascot_face_stamp), null,
                            Modifier.fillMaxWidth().height(29.dp), contentScale = ContentScale.Fit,
                            colorFilter = if (completed) null else ColorFilter.tint(PatchTokens.muted.copy(alpha = .28f)))
                        Text(weekday, fontSize = 10.sp, lineHeight = 14.sp,
                            fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal,
                            color = if (date == today) PatchTokens.ink else PatchTokens.muted)
                    }
                }
            }
        }

        PatchPanel(Modifier.clickable(role = Role.Button, onClickLabel = "모은 도장과 획득 조건 보기") { showStamps = true }) {
            ProfileHeading("모은 도장", showArrow = true)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                stamps.forEach { stamp ->
                    Column(
                        Modifier.weight(1f).clearAndSetSemantics {
                            contentDescription = "${stamp.title}, ${if (stamp.earned) "획득" else "미획득"}. ${stamp.requirement}"
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Reserved for the supplied badge image. Never substitute invented art.
                        Box(Modifier.fillMaxWidth().aspectRatio(2f)
                            .background(PatchTokens.soft, RoundedCornerShape(10.dp)))
                        Text(stamp.title, fontSize = 11.sp, lineHeight = 16.sp,
                            color = PatchTokens.ink, fontWeight = FontWeight.Medium)
                        Text(if (stamp.earned) "획득" else "미획득", fontSize = 10.sp, lineHeight = 13.sp,
                            color = if (stamp.earned) PatchTokens.red else PatchTokens.muted,
                            fontWeight = if (stamp.earned) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }

        Column(Modifier.fillMaxWidth().clip(ProfileShape).background(PatchTokens.surface)) {
            ProfileLink(R.drawable.ic_patch_shirt, "꾸미기 보관함", onDecor,
                "꾸미기 보관함, 구매한 아이템 ${ownedItems.size}개")
            ProfileDivider()
            ProfileLink(R.drawable.ic_patch_chart_bars, "성취 기록", onHistory)
            ProfileDivider()
            ProfileLink(R.drawable.ic_patch_bell, "알림 설정", onSettings)
        }
        Text("목표와 기록은 이 기기에만 저장돼요.",
            fontSize = 10.sp, lineHeight = 15.sp, color = PatchTokens.muted)
    }

    if (showStamps) {
        PatchSheet("모은 도장", onDismiss = { showStamps = false }) {
            stamps.forEach { stamp ->
                Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
                    Text("${stamp.title} · ${if (stamp.earned) "획득" else "미획득"}",
                        fontSize = 16.sp, lineHeight = 23.sp, color = PatchTokens.ink,
                        fontWeight = FontWeight.Bold)
                    Text(stamp.requirement, fontSize = 14.sp, lineHeight = 21.sp, color = PatchTokens.muted)
                }
            }
            Text("이 기기에 저장된 완료 기록을 기준으로 해요. 날짜는 한국 시간 기준이에요.",
                fontSize = 12.sp, lineHeight = 18.sp, color = PatchTokens.muted)
        }
    }
}

@Composable
private fun ProfileHero(clanName: String, itemId: Int, onDecor: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val availableWidth = maxWidth
        val stacked = availableWidth < 300.dp || LocalDensity.current.fontScale > 1.4f
        if (stacked) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                ProfileAvatar(itemId, Modifier.width(128.dp))
                ProfileIdentity(clanName, onDecor, Modifier.fillMaxWidth())
            }
        } else {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
                ProfileAvatar(itemId, Modifier.width((availableWidth * .39f).coerceAtMost(136.dp)))
                ProfileIdentity(clanName, onDecor, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ProfileAvatar(itemId: Int, modifier: Modifier) {
    Box(modifier.height(120.dp), contentAlignment = Alignment.BottomCenter) {
        DressedAvatar(itemId, Modifier.fillMaxWidth().height(120.dp), "내 착용 아바타")
    }
}

@Composable
private fun ProfileIdentity(clanName: String, onDecor: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
        Text("나", fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.ExtraBold,
            color = PatchTokens.ink, modifier = Modifier.semantics { heading() })
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
            com.questyourlife.app.ui.ClanMark(Modifier.size(24.dp))
            Column {
                Text(clanName, fontSize = 12.sp, lineHeight = 17.sp, color = PatchTokens.ink)
                Text("시연용 클랜", fontSize = 10.sp, lineHeight = 14.sp, color = PatchTokens.muted)
            }
        }
        OutlinedButton(onClick = onDecor, modifier = Modifier.heightIn(min = 40.dp),
            shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, PatchTokens.muted.copy(alpha = .6f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PatchTokens.ink),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
            Icon(painterResource(R.drawable.ic_patch_shirt), null, Modifier.size(20.dp))
            Spacer(Modifier.width(PatchTokens.space4))
            Text("프로필 꾸미기", fontSize = 12.sp, lineHeight = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileCash(title: String, amount: Int, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().clip(ProfileShape).background(PatchTokens.surface)
        .padding(PatchTokens.space12), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
        Text(title, fontSize = 11.sp, lineHeight = 16.sp, color = PatchTokens.muted)
        PatchMoney(amount, compact = true)
    }
}

@Composable
private fun ProfileHeading(title: String, showArrow: Boolean = false) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.ExtraBold,
            color = PatchTokens.ink, modifier = Modifier.weight(1f).semantics { heading() })
        if (showArrow) Icon(painterResource(R.drawable.ic_patch_chevron_right), null,
            Modifier.size(18.dp), tint = PatchTokens.muted)
    }
}

@Composable
private fun ProfileLink(
    @DrawableRes icon: Int,
    title: String,
    onClick: () -> Unit,
    description: String = title
) {
    Row(Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick)
        .heightIn(min = PatchTokens.control).padding(horizontal = PatchTokens.space16, vertical = PatchTokens.space8)
        .semantics { contentDescription = description }, verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(icon), null, Modifier.size(22.dp), tint = PatchTokens.ink)
        Spacer(Modifier.width(PatchTokens.space12))
        Text(title, Modifier.weight(1f).clearAndSetSemantics { }, fontSize = 14.sp, lineHeight = 21.sp,
            color = PatchTokens.ink, fontWeight = FontWeight.Medium)
        Icon(painterResource(R.drawable.ic_patch_chevron_right), null, Modifier.size(16.dp), tint = PatchTokens.muted)
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(Modifier.padding(start = 50.dp, end = PatchTokens.space12),
        color = PatchTokens.line.copy(alpha = .45f))
}

private data class ProfileStamp(val title: String, val requirement: String, val earned: Boolean)
