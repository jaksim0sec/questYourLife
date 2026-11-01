package com.questyourlife.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private data class TabItem(
    val route: String, val title: String,
    val activeIcon: ImageVector, val inactiveIcon: ImageVector
)

private val tabs = listOf(
    TabItem("home","홈",Icons.Filled.Home,Icons.Outlined.Home),
    TabItem("quests","퀘스트",Icons.Filled.CheckCircle,Icons.Outlined.FactCheck),
    TabItem("history","기록",Icons.Filled.CheckCircle,Icons.Outlined.CheckCircleOutline),
    TabItem("decor","꾸미기",Icons.Filled.Face,Icons.Outlined.Face)
)

@Composable
private fun AppBottomNav(active:String,onSelect:(String)->Unit) {
    Column(Modifier.fillMaxWidth().background(CardWhite)) {
        HorizontalDivider(color=WarmLine.copy(alpha=.8f),thickness=.5.dp)
        Row(Modifier.fillMaxWidth().height(65.dp).padding(horizontal=10.dp),
            verticalAlignment=Alignment.CenterVertically) {
            tabs.forEach { tab->
                val activeTab=active==tab.route
                val tint by animateColorAsState(if(activeTab) AccentRed else TextGray,
                    animationSpec=tween(170),label="nav tint")
                val iconSize by animateFloatAsState(if(activeTab) 1f else .95f,
                    animationSpec=tween(170),label="nav scale")
                Column(Modifier.weight(1f).fillMaxHeight()
                    .clickable(role=Role.Tab) { onSelect(tab.route) }
                    .semantics { selected=activeTab },
                    horizontalAlignment=Alignment.CenterHorizontally,
                    verticalArrangement=Arrangement.Center) {
                    Icon(if(activeTab)tab.activeIcon else tab.inactiveIcon,
                        contentDescription=null,tint=tint,
                        modifier=Modifier.size(23.dp).scale(iconSize))
                    Spacer(Modifier.height(5.dp))
                    Text(tab.title,fontSize=10.sp,
                        fontWeight=if(activeTab)FontWeight.Bold else FontWeight.Medium,
                        color=tint)
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    var goalId by rememberSaveable { mutableStateOf("example-japanese") }
    val reward=model.lastReward.value
    val topTab=tabs.any { it.route==screen }
    val back: () -> Unit = {
        screen=when(screen) {
            "daily" -> "home"
            "detail" -> "quests"
            "focus" -> "detail"
            "settings" -> "home"
            "create" -> "quests"
            else -> "home"
        }
    }
    BackHandler(enabled = !topTab && reward == null) { back() }

    MaterialTheme(colorScheme=lightColorScheme(
        primary=AccentRed,background=Cream,surface=CardWhite,
        onBackground=TextInk,onSurface=TextInk
    )) {
        Scaffold(
            containerColor=Cream,
            bottomBar={ if(topTab) AppBottomNav(screen) { screen=it } }
        ) { padding ->
            AnimatedContent(
                targetState=screen,
                modifier=Modifier.fillMaxSize().padding(padding),
                transitionSpec={
                    (fadeIn(tween(190)) +
                        slideInHorizontally(tween(210), initialOffsetX={ it / 22 }))
                        .togetherWith(fadeOut(tween(95)))
                },
                label="page transition"
            ) { target ->
                LazyColumn(modifier=Modifier.fillMaxSize(),
                    contentPadding=PaddingValues(start=20.dp,end=20.dp,top=16.dp,bottom=28.dp),
                    verticalArrangement=Arrangement.spacedBy(16.dp)) {
                    item(key=target) {
                        when(target) {
                            "home" -> HomePanel(coins,goals,steps,daily,
                                onGoal={ goalId=it;screen="detail" },
                                onDaily={ screen="daily" },
                                onAccept=model::accept,onComplete=model::complete,
                                onCreateGoal={screen="create"},onSettings={screen="settings"})
                            "quests" -> QuestListPanel(coins,goals,steps,daily,
                                onGoal={goalId=it;screen="detail"},
                                onDaily={screen="daily"},
                                onCreate={screen="create"})
                            "detail" -> {
                                val goal=goals.firstOrNull{it.id==goalId}
                                if(goal!=null) GoalDetailPanel(goal,steps.filter{it.goalId==goalId},
                                    onBack=back, onComplete=model::completeStep,
                                    onTimer={screen="focus"})
                                else Text("목표를 찾지 못했어",color=TextGray)
                            }
                            "daily" -> DailyPanel(daily,history,model::accept,model::complete,back)
                            "history" -> HistoryPanel(history,steps,sessions)
                            "decor" -> DecorPanel(coins,owned,model.equipped.intValue,
                                model::buyItem,model::equipItem)
                            "focus" -> FocusPanel(back,model::finishFocus)
                            "settings" -> SettingsPanel(model,back,onOpenClassicChecklist)
                            "create" -> NewGoalPanel(back,{
                                model.addGoal(it);screen="quests"
                            })
                        }
                    }
                }
            }
        }
        if(reward!=null) {
            val sheetState=rememberModalBottomSheetState(skipPartiallyExpanded=true)
            ModalBottomSheet(
                onDismissRequest=model::clearReward,
                sheetState=sheetState,
                containerColor=Cream,
                dragHandle={BottomSheetDefaults.DragHandle(color=Color(0xFFC3C0BB))}
            ) {
                CompletionPanel(
                    title=reward.title,reward=reward.amount,
                    onClose=model::clearReward,
                    onNext={
                        model.clearReward()
                        screen=if(reward.isGoal) "detail" else "daily"
                    }
                )
            }
        }
    }
}
