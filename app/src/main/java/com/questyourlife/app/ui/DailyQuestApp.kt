package com.questyourlife.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    val mainScreens=listOf("home","quests","history","decor")
    val topTab=screen in mainScreens
    val back: () -> Unit = {
        screen=when(screen) {
            "daily" -> "home"
            "detail" -> "quests"
            "focus" -> "detail"
            "settings" -> "home"
            "create" -> "quests"
            "completed" -> "home"
            else -> "home"
        }
    }
    BackHandler(enabled = !topTab) { back() }
    LaunchedEffect(reward?.nonce) {
        if(reward!=null) screen="completed"
    }

    MaterialTheme(colorScheme=lightColorScheme(primary=AccentRed,background=Cream,
        surface=Color.White),typography=Typography()) {
        Scaffold(
            containerColor=Cream,
            bottomBar={
                if(topTab) {
                    NavigationBar(containerColor=Color(0xFFFFFCF8),tonalElevation=0.dp,
                        modifier=Modifier.height(76.dp)) {
                        val icons=listOf(Icons.Outlined.Home,Icons.Outlined.FactCheck,
                            Icons.Outlined.CheckCircleOutline,Icons.Outlined.Face)
                        listOf("홈","퀘스트","기록","꾸미기").forEachIndexed { i,label ->
                            val target=mainScreens[i]
                            NavigationBarItem(
                                selected=screen==target,
                                onClick={screen=target},
                                icon={Icon(icons[i],contentDescription=label,
                                    tint=if(screen==target) AccentRed else TextGray,
                                    modifier=Modifier.size(22.dp))},
                                label={Text(label,color=if(screen==target) AccentRed else TextGray,
                                    fontSize=10.sp,fontWeight=if(screen==target) FontWeight.Bold else FontWeight.Normal)},
                                alwaysShowLabel=true,
                                colors=NavigationBarItemDefaults.colors(
                                    indicatorColor=Color.Transparent,
                                    selectedIconColor=AccentRed,
                                    unselectedIconColor=TextGray
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(modifier=Modifier.fillMaxSize(),
                    contentPadding=PaddingValues(start=22.dp,end=22.dp,top=20.dp,bottom=30.dp),
                    verticalArrangement=Arrangement.spacedBy(20.dp)) {
                    item {
                        when(screen) {
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
                                    onBack=back,
                                    onComplete=model::completeStep,
                                    onTimer={screen="focus"})
                                else Text("목표를 찾지 못했어",color=TextGray)
                            }
                            "daily" -> DailyPanel(daily,history,model::accept,model::complete,back)
                            "history" -> HistoryPanel(history,steps,sessions)
                            "decor" -> DecorPanel(coins,owned,model.equipped.intValue,
                                model::buyItem,model::equipItem)
                            "focus" -> FocusPanel(back,model::finishFocus)
                            "completed" -> {
                                if(reward!=null) CompletionPanel(reward.title,reward.amount,
                                    onClose={model.clearReward();screen="home"},
                                    onNext={model.clearReward();screen=if(reward.isGoal) "detail" else "daily"})
                                else CompletionPanel("퀘스트 완료",0,onClose=back,onNext=back)
                            }
                            "settings" -> SettingsPanel(model,back,onOpenClassicChecklist)
                            "create" -> NewGoalPanel(back,{
                                model.addGoal(it)
                                screen="quests"
                            })
                        }
                    }
                }

            }
        }
    }
}
