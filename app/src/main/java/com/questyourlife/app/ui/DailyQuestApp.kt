package com.questyourlife.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    PatchTab("dm", "쪽지", R.drawable.ic_patch_nav_active_dm, R.drawable.ic_patch_nav_inactive_dm),
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
                    if (isSelected) PatchTokens.red else if (LocalPatchDark.current) PatchTokens.muted else Color(0xFF222222).copy(alpha = 0.55f),
                    animationSpec = tween(160), label = "${tab.route} tab color")
                Column(Modifier.weight(1f).fillMaxHeight()
                    .clickable(role = Role.Tab) { onSelect(tab.route) }
                    .semantics { selected = isSelected },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.5.dp, Alignment.CenterVertically)) {
                    val isDark = LocalPatchDark.current
                    val darkActiveIcon = when (tab.route) {
                        "home" -> R.drawable.ic_patch_nav_active_home_dark
                        "quests" -> R.drawable.ic_patch_nav_active_quest_dark
                        "dm" -> R.drawable.ic_patch_nav_active_dm_dark
                        "clan" -> R.drawable.ic_patch_nav_active_clan_dark
                        else -> R.drawable.ic_patch_nav_active_my_dark
                    }
                    val iconScale = remember { androidx.compose.animation.core.Animatable(1f) }
                    LaunchedEffect(isSelected) {
                        if (isSelected) {
                            iconScale.animateTo(1.045f, tween(90))
                            iconScale.animateTo(1f, tween(140))
                        } else iconScale.animateTo(1f, tween(100))
                    }
                    Crossfade(isSelected, animationSpec = tween(160), label = "tab icon") { selected ->
                        Icon(painterResource(if (selected) { if (isDark) darkActiveIcon else tab.activeIcon } else tab.inactiveIcon),
                            null, tint = if (!selected && isDark) PatchTokens.muted else Color.Unspecified, modifier = Modifier.size(27.dp)
                                .graphicsLayer { scaleX = iconScale.value; scaleY = iconScale.value }
                                .alpha(if (selected || isDark) 1f else 0.55f))
                    }
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
            PatchHeaderActions(onNotifications, onMenu)
        }
    }
}

@Composable
fun DailyQuestApp(model: DailyQuestViewModel, onOpenClassicChecklist: () -> Unit) {
    val daily by model.quests.collectAsStateWithLifecycle()
    val history by model.history.collectAsStateWithLifecycle()
    val goals by model.goals.collectAsStateWithLifecycle()
    val steps by model.steps.collectAsStateWithLifecycle()
    val cash by model.cash.collectAsStateWithLifecycle()
    val sessions by model.sessions.collectAsStateWithLifecycle()
    var screen by rememberSaveable { mutableStateOf("home") }
    var returnTab by rememberSaveable { mutableStateOf("home") }
    var focusReturn by rememberSaveable { mutableStateOf("home") }
    var selectedThreadId by rememberSaveable { mutableStateOf("mascot") }
    var profileName by rememberSaveable { mutableStateOf("") }
    var profileReturn by rememberSaveable { mutableStateOf("clan") }
    var previewBookId by rememberSaveable { mutableStateOf("travel") }
    var previewChapterId by rememberSaveable { mutableStateOf<String?>(null) }
    var chapterReturn by rememberSaveable { mutableStateOf("quests") }
    var goalId by rememberSaveable { mutableStateOf("") }
    var sheet by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedDailyId by rememberSaveable { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val themePrefs = remember(context) { context.getSharedPreferences("patch_appearance", android.content.Context.MODE_PRIVATE) }
    var appearance by rememberSaveable { mutableStateOf(themePrefs.getString("mode", "system") ?: "system") }
    val dark = appearance == "dark" || (appearance == "system" && androidx.compose.foundation.isSystemInDarkTheme())
    val pageStates = rememberSaveableStateHolder()
    val reward = model.lastReward.value
    val topTab = patchTabs.any { it.route == screen }
    val selectedDaily = (daily + history).firstOrNull { it.id == selectedDailyId }
    val navigate: (String) -> Unit = { route ->
        if (topTab) returnTab = screen
        if (route == "focus") focusReturn = screen
        sheet = null
        selectedDailyId = null
        screen = route
    }
    val back: () -> Unit = {
        screen = when (screen) {
            "person" -> profileReturn
            "thread" -> "dm"
            "detail" -> "quests"
            "chapter" -> chapterReturn
            "focus" -> focusReturn
            "create", "legacyQuests" -> "quests"
            else -> returnTab
        }
    }
    val openThread: (String) -> Unit = { id ->
        selectedThreadId = id
        model.openThread(id)
        navigate("thread")
    }
    val openProfile: (String) -> Unit = { name ->
        profileName = name
        if (screen != "person") profileReturn = screen
        navigate("person")
    }
    val openPreview: (String, String?) -> Unit = { id, chapter ->
        previewBookId = id
        previewChapterId = chapter
        chapterReturn = screen
        navigate("chapter")
    }
    val openCompanion: () -> Unit = { openThread("mascot") }
    BackHandler(enabled = !topTab && reward == null && sheet == null) { back() }

    PatchTheme(dark = dark) {
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
                AnimatedContent(screen, modifier = Modifier.fillMaxSize(), transitionSpec = {
                    (fadeIn(tween(200)) + slideInHorizontally(tween(220)) { it / 16 }) togetherWith
                        fadeOut(tween(100))
                }, label = "page transition") { page ->
                val stateKey = when (page) {
                    "thread" -> "thread:$selectedThreadId"
                    "person" -> "person:$profileName"
                    "chapter" -> "chapter:$previewBookId:${previewChapterId.orEmpty()}"
                    else -> page
                }
                pageStates.SaveableStateProvider(stateKey) {
                when (page) {
                    "home" -> PatchHomePage(model,
                        onDaily = { selectedDailyId = it.id; sheet = "daily" },
                        onQuests = { navigate("quests") }, onClan = { navigate("clan") },
                        onPreview = { openPreview(it, null) }, onMy = { navigate("my") })
                    "quests" -> PatchQuestsPage(model, onPreview = { openPreview(it, null) },
                        onChapter = { book, chapter -> openPreview(book, chapter) },
                        onGoal = { goalId = it; navigate("detail") },
                        onDaily = { selectedDailyId = it.id; sheet = "daily" }, onCreate = { navigate("create") })
                    "chapter" -> PatchChapterPage(model, previewBookId, initialChapterId = previewChapterId, onBack = back, onMenu = { sheet = "menu" })
                    "dm" -> PatchDmPage(model, onOpenThread = openThread, onProfile = openProfile)
                    "thread" -> PatchThreadPage(model, selectedThreadId, onBack = back,
                        onProfile = openProfile, onMenu = { sheet = "menu" })
                    "clan" -> PatchClanPage(model, onProfile = openProfile)
                    "person" -> PatchPersonProfilePage(profileName, model.demoClan.name, back,
                        onNotifications = { sheet = "notifications" }, onMenu = { sheet = "menu" })
                    "decor" -> PatchDecorPage(model, onBack = back)
                    "my" -> PatchProfilePage(model,
                        onHistory = { navigate("history") }, onDecor = { navigate("decor") },
                        onSettings = { navigate("settings") })
                    else -> LazyColumn(Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = PatchTokens.space20, end = PatchTokens.space20,
                            top = PatchTokens.space8, bottom = PatchTokens.space24),
                        verticalArrangement = Arrangement.spacedBy(PatchTokens.space16)) {
                        item {
                            when (page) {
                                "legacyQuests" -> QuestListPanel(cash, goals, steps, daily,
                                    onGoal = { goalId = it; navigate("detail") },
                                    onDaily = { navigate("daily") }, onCreate = { navigate("create") },
                                    showHeading = page == "legacyQuests")
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
        }
        }

        if (sheet == "recipients") PatchSheet("새 쪽지", onDismiss = { sheet = null }) {
            model.contacts.forEach { contact ->
                TextButton(onClick = { openThread(contact.id) },
                    modifier = Modifier.fillMaxWidth()) { Text(contact.name) }
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
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CashArtwork(Modifier.size(80.dp), pile = true)
                    Text("+${reward.amount} C", color = PatchTokens.red,
                        style = MaterialTheme.typography.headlineLarge)
                }
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
            "notifications" -> {
                val declined = history.filter { it.dismissed && !it.accepted && it.dateKey == com.questyourlife.app.daily.DailyQuestRepository.today() }
                NotificationCenter((daily + declined).distinctBy { it.id },
                    onDismiss = { sheet = null }, onSettings = { navigate("settings") },
                    onProfile = openProfile, onClan = { navigate("clan") },
                    onQuest = { selectedDailyId = it; sheet = "daily" },
                    onAccept = model::accept, onReject = model::rejectQuest)
            }
            "appearance" -> PatchSheet("화면 테마", onDismiss = { sheet = null }, initialHeight = 240.dp, startExpanded = true) {
                AppearanceOptions(appearance) { mode ->
                    appearance = mode
                    themePrefs.edit().putString("mode", mode).apply()
                }
            }
            "menu" -> PatchSheet("메뉴", onDismiss = { sheet = null }, initialHeight = 620.dp, startExpanded = true) {
                AppMenu(openCompanion, { navigate("create") }, { navigate("history") },
                    { sheet = "recipients" }, { navigate("focus") }, { sheet = "appearance" }, { navigate("settings") })
            }
        }
    }
}
