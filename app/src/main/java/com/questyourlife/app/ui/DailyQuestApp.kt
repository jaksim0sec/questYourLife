package com.questyourlife.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.questyourlife.app.R
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.ui.redesign.*

private data class PatchTab(
    val route: String, val title: String,
    val activeIcon: Int, val inactiveIcon: Int
)

private val patchTabs = listOf(
    PatchTab("home", "홈", R.drawable.ic_patch_nav_active_home, R.drawable.ic_patch_nav_inactive_home),
    PatchTab("quests", "퀘스트", R.drawable.ic_patch_nav_active_quest, R.drawable.ic_patch_nav_inactive_quest),
    PatchTab("dm", "DM", R.drawable.ic_patch_nav_active_dm, R.drawable.ic_patch_nav_inactive_dm),
    PatchTab("clan", "클랜", R.drawable.ic_patch_nav_active_clan, R.drawable.ic_patch_nav_inactive_clan),
    PatchTab("my", "마이", R.drawable.ic_patch_nav_active_my, R.drawable.ic_patch_nav_inactive_my)
)

@Composable
private fun PatchBottomNav(active: String, onSelect: (String) -> Unit) {
    Column(Modifier.fillMaxWidth().background(PatchTokens.background)) {
        HorizontalDivider(color = PatchTokens.line)
        Row(Modifier.fillMaxWidth().height(58.dp).padding(horizontal = PatchTokens.space8),
            verticalAlignment = Alignment.CenterVertically) {
            patchTabs.forEach { tab ->
                val isSelected = active == tab.route
                val tint by animateColorAsState(
                    if (isSelected) Color(0xFFFF3B30) else Color(0xFF222222).copy(alpha = 0.55f),
                    animationSpec = tween(160), label = "${tab.route} tab color")
                Column(Modifier.weight(1f).fillMaxHeight()
                    .clickable(role = Role.Tab) { onSelect(tab.route) }
                    .semantics { selected = isSelected },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)) {
                    Icon(painterResource(if (isSelected) tab.activeIcon else tab.inactiveIcon),
                        null, tint = Color.Unspecified, modifier = Modifier.size(25.dp).alpha(if (isSelected) 1f else 0.55f))
                    Text(tab.title, color = tint, fontSize = 11.sp, lineHeight = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun PatchTopBar(title: String, onNotifications: () -> Unit, onMenu: () -> Unit) {
    Column(Modifier.background(PatchTokens.background).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().heightIn(min = 60.dp)
            .padding(start = PatchTokens.space20, end = PatchTokens.space8),
            verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black,
                color = PatchTokens.ink, modifier = Modifier.weight(1f))
            IconButton(onClick = onNotifications) {
                Icon(painterResource(R.drawable.ic_patch_header_alarm), "알림", tint = Color.Unspecified)
            }
            IconButton(onClick = onMenu) {
                Icon(painterResource(R.drawable.ic_patch_header_more), "메뉴", tint = Color.Unspecified)
            }
        }
    }
}

@Composable
private fun MenuRow(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = PatchTokens.control)
        .clickable(role = Role.Button, onClick = onClick), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = PatchTokens.muted, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(PatchTokens.space12))
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(Icons.Rounded.ChevronRight, null, tint = PatchTokens.muted)
    }
}

@Composable
fun DailyQuestApp(model: DailyQuestViewModel, onOpenClassicChecklist: () -> Unit) {
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val coins by model.coins.collectAsStateWithLifecycle()
    val owned by model.ownedItems.collectAsStateWithLifecycle()
    val sessions by model.sessions.collectAsStateWithLifecycle()
    var screen by rememberSaveable { mutableStateOf("home") }
    var returnTab by rememberSaveable { mutableStateOf("home") }
    var focusReturn by rememberSaveable { mutableStateOf("home") }
    var goalId by rememberSaveable { mutableStateOf("") }
    var sheet by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedDailyId by rememberSaveable { mutableStateOf<String?>(null) }
    val reward = model.lastReward.value
    val topTab = patchTabs.any { it.route == screen }
    val selectedDaily = daily.firstOrNull { it.id == selectedDailyId }
    val navigate: (String) -> Unit = { route ->
        if (topTab) returnTab = screen
        if (route == "focus") focusReturn = screen
        sheet = null
        selectedDailyId = null
        screen = route
    }
    val back: () -> Unit = {
        screen = when (screen) {
            "detail" -> "quests"
            "focus" -> focusReturn
            "create", "legacyQuests" -> "quests"
            else -> returnTab
        }
    }
    val openCompanion: () -> Unit = {
        model.openThread("mascot")
        navigate("dm")
    }
    BackHandler(enabled = !topTab && reward == null && sheet == null) { back() }

    PatchTheme {
        Scaffold(
            containerColor = PatchTokens.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = { if (topTab) PatchTopBar(
                patchTabs.first { it.route == screen }.title,
                onNotifications = { sheet = "notifications" }, onMenu = { sheet = "menu" }) },
            bottomBar = { if (topTab) PatchBottomNav(screen) {
                screen = it
                selectedDailyId = null
                sheet = null
            } }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                when (screen) {
                    "home" -> PatchHomePage(model,
                        onDaily = { selectedDailyId = it.id; sheet = "daily" },
                        onQuests = { navigate("quests") }, onClan = { navigate("clan") },
                        onCompanion = openCompanion)
                    "dm" -> PatchDmPage(model)
                    "clan" -> PatchClanPage(model)
                    "decor" -> PatchDecorPage(model, onBack = back)
                    "my" -> PatchProfilePage(model,
                        onHistory = { navigate("history") }, onDecor = { navigate("decor") },
                        onSettings = { navigate("settings") })
                    else -> LazyColumn(Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = PatchTokens.space20, end = PatchTokens.space20,
                            top = PatchTokens.space8, bottom = PatchTokens.space24),
                        verticalArrangement = Arrangement.spacedBy(PatchTokens.space16)) {
                        item {
                            when (screen) {
                                "quests", "legacyQuests" -> QuestListPanel(coins, goals, steps, daily,
                                    onGoal = { goalId = it; navigate("detail") },
                                    onDaily = { navigate("daily") }, onCreate = { navigate("create") },
                                    showHeading = screen == "legacyQuests")
                                "detail" -> {
                                    val goal = goals.firstOrNull { it.id == goalId }
                                    if (goal != null) GoalDetailPanel(goal, steps.filter { it.goalId == goalId },
                                        onBack = back, onComplete = model::completeStep,
                                        onTimer = { navigate("focus") })
                                    else {
                                        Text("목표를 찾을 수 없어요", color = PatchTokens.muted)
                                        PatchButton("퀘스트로 돌아가기", { navigate("quests") })
                                    }
                                }
                                "daily" -> DailyPanel(daily, history, model::accept, model::complete, back)
                                "history" -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
                                        TextButton(onClick = back) { Text("돌아가기") }
                                        HistoryPanel(history, steps, sessions)
                                    }
                                }
                                "focus" -> FocusPanel(back, model::finishFocus)
                                "settings" -> SettingsPanel(model, back, onOpenClassicChecklist)
                                "create" -> NewGoalPanel(back, {
                                    model.addGoal(it)
                                    navigate("quests")
                                })
                            }
                        }
                    }
                }
            }
        }

        if (reward != null) {
            PatchSheet("퀘스트 완료", onDismiss = model::clearReward) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(PatchTokens.space16)) {
                    MascotArt(Modifier.size(80.dp), festive = true)
                    Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space8)) {
                        Text("한 걸음 해냈어요!", style = MaterialTheme.typography.titleLarge)
                        Text(reward.title, style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
                    }
                }
                Text("+${reward.amount} C", color = PatchTokens.red,
                    style = MaterialTheme.typography.headlineLarge)
                Text("보상과 완료 기록이 이 기기에 저장됐어요.", style = MaterialTheme.typography.bodySmall,
                    color = PatchTokens.muted)
                PatchButton("좋아요", model::clearReward)
            }
        } else when (sheet) {
            "daily" -> if (selectedDaily != null) {
                PatchSheet("퀘스트 상세", onDismiss = { sheet = null }) {
                    Text(selectedDaily.title, style = MaterialTheme.typography.titleLarge)
                    Text(if (selectedDaily.completedAt != null) "완료" else if (selectedDaily.accepted) "진행 중" else "오늘의 제안",
                        style = MaterialTheme.typography.labelMedium, color = PatchTokens.red)
                    Text(selectedDaily.detail, style = MaterialTheme.typography.bodyLarge)
                    Text("완료 보상 +${selectedDaily.reward} C", style = MaterialTheme.typography.titleSmall)
                    PatchSurface {
                        Text("내가 확인하고 완료해요", style = MaterialTheme.typography.titleSmall)
                        Text("직접 실천한 뒤 완료 버튼을 눌러 주세요.", style = MaterialTheme.typography.bodySmall,
                            color = PatchTokens.muted)
                    }
                    PatchButton(if (selectedDaily.completedAt != null) "완료했어요" else if (selectedDaily.accepted) "완료하기" else "수락하고 시작하기",
                        enabled = selectedDaily.completedAt == null, onClick = {
                            sheet = null
                            if (selectedDaily.accepted) model.complete(selectedDaily.id) else model.accept(selectedDaily.id)
                        })
                    if (selectedDaily.completedAt == null) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            TextButton(onClick = { model.deferQuest(selectedDaily.id); sheet = null }) { Text("내일로 미루기") }
                            TextButton(onClick = { model.abandonQuest(selectedDaily.id); sheet = null }) { Text("이번 퀘스트 그만하기") }
                        }
                    }
                }
            }
            "notifications" -> PatchSheet("알림", onDismiss = { sheet = null }) {
                Text("이 기기의 오늘 퀘스트", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                if (daily.isEmpty()) Text("아직 오늘의 제안이 없어요", color = PatchTokens.muted)
                daily.forEach { quest ->
                    MenuRow(quest.title, if (quest.completedAt == null) Icons.Rounded.NotificationsNone else Icons.Rounded.CheckCircle) {
                        selectedDailyId = quest.id
                        sheet = "daily"
                    }
                    Text(if (quest.completedAt != null) "완료 · +${quest.reward} C" else if (quest.accepted) "수락했어요 · 이어서 완료하기" else "제안이 준비됐어요 · 수락하고 시작하기",
                        style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                }
                TextButton(onClick = { navigate("settings") }) { Text("알림 설정") }
            }
            "menu" -> PatchSheet("메뉴", onDismiss = { sheet = null }) {
                MenuRow("패치와 목표 나누기", Icons.Rounded.ChatBubbleOutline, openCompanion)
                MenuRow("직접 목표 만들기", Icons.Rounded.Add) { navigate("create") }
                MenuRow("완료 기록", Icons.Rounded.History) { navigate("history") }
                MenuRow("꾸미기", Icons.Rounded.Palette) { navigate("decor") }
                MenuRow("집중 타이머", Icons.Rounded.Timer) { navigate("focus") }
                MenuRow("설정", Icons.Rounded.Settings) { navigate("settings") }
                MenuRow("기본 체크리스트", Icons.Rounded.Checklist) { sheet = null; onOpenClassicChecklist() }
                Text("내 기록과 대화는 이 기기에 저장돼요. 친구와 클랜은 데모예요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
        }
    }
}
