package com.questyourlife.app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.*

@Composable
fun HomePanel(
    coins: Int,
    goals: List<GoalEntity>,
    steps: List<GoalStepEntity>,
    daily: List<DailyQuestEntity>,
    onGoal: (String) -> Unit,
    onDaily: () -> Unit,
    onAccept: (String) -> Unit,
    onComplete: (String) -> Unit,
    onCreateGoal: () -> Unit,
    onSettings: () -> Unit
) {
    val goal = goals.firstOrNull()
    val progressSteps = steps.filter { it.goalId == goal?.id }
    val done = progressSteps.count { it.completedAt != null }
    val date = remember { java.time.LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("M월 d일 EEEE",java.util.Locale.KOREAN)) }
    Column(verticalArrangement=Arrangement.spacedBy(17.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text("현생패치",fontWeight=FontWeight.Black,fontSize=26.sp,color=TextInk)
            Spacer(Modifier.weight(1f))
            CashBadge(coins)
            IconButton(onClick=onSettings,modifier=Modifier.size(32.dp)) {
                Icon(Icons.Outlined.Settings,contentDescription="설정",tint=TextGray,
                    modifier=Modifier.size(18.dp))
            }
        }
        Row(verticalAlignment=Alignment.CenterVertically,modifier=Modifier.height(132.dp)) {
            Column(Modifier.weight(1f)) {
                Text(date,color=TextGray,fontSize=12.sp)
                Spacer(Modifier.height(13.dp))
                Text("오늘도,\n한 걸음 더",fontWeight=FontWeight.Black,
                    fontSize=31.sp,lineHeight=38.sp,color=TextInk)
                Spacer(Modifier.height(8.dp))
                Text("작은 수행이 모여\n일상이 패치돼.",fontSize=12.sp,
                    lineHeight=18.sp,color=TextGray)
            }
            MascotArt(Modifier.size(128.dp),festive=true)
        }
        Column(
            modifier=Modifier.fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFFFBE9E7),Color(0xFFFCF1EE))),
                    RoundedCornerShape(21.dp))
                .clickable { if(goal==null) onCreateGoal() else onGoal(goal.id) }
                .padding(16.dp)
        ) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                PaleTag(if(goal == null) "메인 퀘스트" else if(goal.id=="example-japanese") "예시 메인 퀘스트" else "메인 퀘스트 · 챕터 ${if(done==0) 1 else ((done*5)/maxOf(progressSteps.size,1)+1).coerceAtMost(5)}")
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,null,tint=TextGray,modifier=Modifier.size(20.dp))
            }
            Row(Modifier.fillMaxWidth().height(82.dp),verticalAlignment=Alignment.CenterVertically) {
                Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text(goal?.title ?: "새로운 목표를 만들어봐",fontWeight=FontWeight.Bold,
                        fontSize=18.sp,lineHeight=23.sp,color=TextInk,maxLines=2,
                        overflow=TextOverflow.Ellipsis)
                    Text(if(goal==null) "목표를 직접 추가할 수 있어" else "$done / ${progressSteps.size} 단계",
                        color=TextGray,fontSize=12.sp)
                }
                MountainArt(Modifier.size(90.dp))
            }
            Row(verticalAlignment=Alignment.CenterVertically) {
                SmallBar(if(progressSteps.isEmpty()) 0f else done.toFloat()/progressSteps.size,
                    Modifier.weight(1f))
                Spacer(Modifier.width(10.dp))
                Text(if(progressSteps.isEmpty()) "0%" else "${done*100/progressSteps.size}%",
                    fontSize=12.sp,color=AccentRed)
            }
        }
        Spacer(Modifier.height(1.dp))
        HeadSection("◉  오늘의 일일 퀘스트",
            "${daily.count { it.completedAt!=null }} / ${daily.size}",onDaily)
        Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(1.dp)) {
            daily.forEachIndexed { index, item ->
                DailyCompactRow(item,onAccept,onComplete)
                if(index != daily.lastIndex)
                    HorizontalDivider(color=WarmLine,thickness=.6.dp)
            }
        }
        if(daily.isEmpty()) Text("오늘의 퀘스트를 준비하고 있어",color=TextGray,fontSize=13.sp)
    }
}

@Composable
fun QuestListPanel(
    coins: Int,
    goals: List<GoalEntity>,
    steps: List<GoalStepEntity>,
    daily: List<DailyQuestEntity>,
    onGoal: (String) -> Unit,
    onDaily: () -> Unit,
    onCreate: () -> Unit
) {
    var filter by remember { mutableIntStateOf(0) }
    Column(verticalArrangement=Arrangement.spacedBy(17.dp)) {
        ScreenHeading("퀘스트",action={
            TextButton(onClick=onCreate) {
                Icon(Icons.Outlined.Add,null,modifier=Modifier.size(18.dp))
                Text("목표 추가",fontSize=12.sp,color=TextInk)
            }
        })
        Row(horizontalArrangement=Arrangement.spacedBy(7.dp),modifier=Modifier.fillMaxWidth()) {
            listOf("메인 퀘스트","일일 퀘스트","완료").forEachIndexed { i,s ->
                Box(Modifier.weight(1f).background(
                    if(filter==i) Color(0xFF343433) else Color(0xFFF0EEEA),CircleShape)
                    .clickable { filter=i }.padding(vertical=11.dp),
                    contentAlignment=Alignment.Center) {
                    Text(s,fontSize=11.sp,color=if(filter==i) Color.White else TextGray,
                        fontWeight=FontWeight.SemiBold)
                }
            }
        }
        if(filter == 0 || filter == 2) {
            goals.filter { g ->
                filter==0 || steps.filter { it.goalId==g.id }.let { s->s.isNotEmpty() && s.all { it.completedAt!=null } }
            }.forEachIndexed { i,goal ->
                val gSteps=steps.filter { it.goalId == goal.id }
                val completed=gSteps.count { it.completedAt!=null }
                Row(Modifier.fillMaxWidth().height(123.dp)
                    .background(
                        when(i%3){0->Color(0xFFFAE8E6);1->Color(0xFFF3F3EA);else->Color(0xFFF6F2EF)},
                        RoundedCornerShape(20.dp))
                    .clickable { onGoal(goal.id) }.padding(17.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    if(i%2 == 0) MountainArt(Modifier.size(75.dp))
                    else SproutArt(Modifier.size(75.dp))
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        PaleTag(if(completed==gSteps.size && gSteps.isNotEmpty()) "완료"
                            else if(completed>0) "진행 중" else "대기 중")
                        Text(goal.title,fontWeight=FontWeight.Bold,color=TextInk,
                            fontSize=14.sp,maxLines=2,overflow=TextOverflow.Ellipsis)
                        Text("챕터 ${(completed*5/maxOf(gSteps.size,1)+1).coerceAtMost(5)} · 작은 단계부터",
                            fontSize=10.sp,color=TextGray)
                        SmallBar(if(gSteps.isEmpty()) 0f else completed.toFloat()/gSteps.size)
                    }
                    Text("$completed / ${gSteps.size}",fontSize=11.sp,color=TextGray)
                }
            }
        }
        if(filter == 1) {
            Box(Modifier.fillMaxWidth().background(LightRed,RoundedCornerShape(20.dp))
                .clickable(onClick=onDaily).padding(20.dp)) {
                Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text("매일 쌓는 작은 성취",fontSize=16.sp,fontWeight=FontWeight.Bold,color=TextInk)
                    Text("오늘의 제안 ${daily.size}개 · 수락하고 완료하기",fontSize=12.sp,color=TextGray)
                    Text("일일 퀘스트 보러 가기  ›",color=AccentRed,fontWeight=FontWeight.Bold,fontSize=12.sp)
                }
            }
        } else if(filter==0) {
            Row(Modifier.fillMaxWidth().background(Color(0xFFF6ECF5),
                RoundedCornerShape(17.dp)).clickable(onClick=onCreate).padding(17.dp),
                verticalAlignment=Alignment.CenterVertically) {
                Text("✧",fontSize=24.sp,color=Color(0xFFB58AB7))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("새 목표 만들기",fontWeight=FontWeight.Bold,color=TextInk,fontSize=14.sp)
                    Text("내 목표를 작은 퀘스트로 시작해 봐",color=TextGray,fontSize=11.sp)
                }
                Spacer(Modifier.weight(1f))
                Text("›",color=TextGray,fontSize=24.sp)
            }
        }
        if(filter==2 && goals.none { g ->
                steps.filter { it.goalId==g.id }.let { it.isNotEmpty() && it.all { step -> step.completedAt!=null } }
            }) Text("완료한 메인 퀘스트가 아직 없어",color=TextGray,fontSize=13.sp)
    }
}

@Composable
fun GoalDetailPanel(
    goal: GoalEntity,
    steps: List<GoalStepEntity>,
    onBack: () -> Unit,
    onComplete: (String,Int) -> Unit,
    onTimer: () -> Unit
) {
    val done=steps.count { it.completedAt!=null }
    val chapter=(done*5/maxOf(steps.size,1)+1).coerceAtMost(5)
    val chapterTasks=steps.filter { it.chapter==chapter }
    Column(verticalArrangement=Arrangement.spacedBy(19.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text("‹",fontSize=31.sp,color=TextInk,
                modifier=Modifier.clickable(onClick=onBack).padding(end=17.dp))
            Spacer(Modifier.weight(1f))
            Text("•••",color=TextInk,fontSize=17.sp)
        }
        Row(verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                PaleTag("메인 퀘스트")
                Text(goal.title,fontSize=24.sp,lineHeight=29.sp,fontWeight=FontWeight.ExtraBold,color=TextInk)
                Text(goal.description,fontSize=12.sp,lineHeight=18.sp,color=TextGray)
            }
            MountainArt(Modifier.size(102.dp))
        }
        Row(verticalAlignment=Alignment.CenterVertically) {
            Text("$done / ${steps.size} 단계",fontSize=12.sp,color=TextInk)
            Spacer(Modifier.weight(1f))
            Text("${if(steps.isEmpty()) 0 else done*100/steps.size}%",
                color=AccentRed,fontSize=12.sp,fontWeight=FontWeight.Bold)
        }
        SmallBar(if(steps.isEmpty()) 0f else done.toFloat()/steps.size)
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            for(i in 1..5) {
                if(i>1) HorizontalDivider(color=WarmLine,modifier=Modifier.weight(.35f))
                Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally) {
                    Box(Modifier.size(30.dp).background(
                        if(i<chapter) AccentRed else if(i==chapter) AccentRed else WarmLine,
                        CircleShape),contentAlignment=Alignment.Center) {
                        Text(if(i<chapter) "✓" else "$i",
                            color=if(i<=chapter) Color.White else TextGray,
                            fontSize=12.sp,fontWeight=FontWeight.Bold)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("챕터 $i",fontSize=10.sp,color=if(i==chapter) AccentRed else TextGray)
                }
            }
        }
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("챕터 $chapter · ${when(chapter){1->"첫걸음";2->"듣기 감각 기르기";3->"반복 연습";4->"실전 도전";else->"마무리"}}",
                    fontWeight=FontWeight.Bold,fontSize=15.sp,color=TextInk)
                Spacer(Modifier.weight(1f))
                Text("${chapterTasks.count{it.completedAt!=null}} / ${chapterTasks.size} 완료",
                    color=TextGray,fontSize=10.sp)
            }
            Spacer(Modifier.height(13.dp))
            steps.forEach { step ->
                val locked=steps.any { it.position < step.position && it.completedAt==null }
                Row(Modifier.fillMaxWidth().heightIn(min=56.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.clickable {
                        if(!locked && step.completedAt==null) onComplete(step.goalId,step.position)
                    }) { QuestTick(step.completedAt!=null,locked=locked) }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(2.dp)) {
                        Text("#${step.position+1} ${step.title}",fontSize=12.sp,
                            color=if(locked) TextGray else TextInk,
                            fontWeight=FontWeight.SemiBold)
                        if(locked) Text("이전 단계 완료 후 해금",fontSize=10.sp,color=TextGray)
                    }
                    Text("+${step.reward} C",fontSize=10.sp,color=CoinGold)
                    Spacer(Modifier.width(5.dp))
                    Icon(if(locked) Icons.Outlined.Lock else Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription=null,tint=TextGray,modifier=Modifier.size(16.dp))
                }
            }
        }
        RedButton(if(done==steps.size) "챕터 완료!" else "집중하며 퀘스트 수행하기  ▷",
            onClick=onTimer,modifier=Modifier.fillMaxWidth())
    }
}

@Composable
fun NewGoalPanel(onBack: () -> Unit,onCreate: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    Column(verticalArrangement=Arrangement.spacedBy(19.dp)) {
        Text("‹",fontSize=28.sp,modifier=Modifier.clickable(onClick=onBack),color=TextInk)
        ScreenHeading("새 목표 만들기","작은 단계부터 하나씩 시작해 봐")
        WhitePanel {
            Text("이루고 싶은 목표",color=TextInk,fontSize=15.sp,fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value=title,onValueChange={ if(it.length<=90) title=it },
                placeholder={Text("예: 일본어로 자유롭게 대화하기")},
                modifier=Modifier.fillMaxWidth(),singleLine=false,maxLines=3,
                shape=RoundedCornerShape(12.dp))
            Spacer(Modifier.height(7.dp))
            Text("현재는 로컬 7단계 가이드로 시작해. Gemini 생성은 아직 연결 전이야.",
                color=TextGray,fontSize=11.sp,lineHeight=16.sp)
        }
        RedButton("목표 시작하기",onClick={if(title.isNotBlank()) onCreate(title)},
            modifier=Modifier.fillMaxWidth())
    }
}
