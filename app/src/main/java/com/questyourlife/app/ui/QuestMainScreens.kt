package com.questyourlife.app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomePanel(
    coins:Int,goals:List<GoalEntity>,steps:List<GoalStepEntity>,
    daily:List<DailyQuestEntity>,onGoal:(String)->Unit,onDaily:()->Unit,
    onAccept:(String)->Unit,onComplete:(String)->Unit,
    onCreateGoal:()->Unit,onSettings:()->Unit
) {
    val goal=goals.firstOrNull()
    val relevant=steps.filter{it.goalId==goal?.id}
    val completed=relevant.count{it.completedAt!=null}
    val ratio=if(relevant.isEmpty()) 0f else completed.toFloat()/relevant.size
    val date=remember { LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일 EEEE",Locale.KOREAN)) }
    Column(verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text("갓생패치",fontSize=23.sp,fontWeight=FontWeight.Black,color=TextInk)
            Spacer(Modifier.weight(1f))
            CashBadge(coins)
            IconButton(onClick=onSettings,modifier=Modifier.size(44.dp)) {
                Icon(Icons.Outlined.Settings,"설정",tint=TextGray,modifier=Modifier.size(19.dp))
            }
        }
        Spacer(Modifier.height(2.dp))
        Row(Modifier.fillMaxWidth().heightIn(min=144.dp),verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                Text(date,fontSize=11.sp,color=TextGray)
                Text("오늘도,\n한 걸음 더",fontSize=27.sp,lineHeight=32.sp,
                    fontWeight=FontWeight.Black,letterSpacing=(-.6).sp,color=TextInk)
                Text("작은 수행이 모여\n일상이 패치돼.",fontSize=11.sp,
                    lineHeight=16.sp,color=TextGray)
            }
            MascotArt(Modifier.size(128.dp),festive=true)
        }
        Column(Modifier.fillMaxWidth()
            .clip(CardShape)
            .background(Brush.linearGradient(listOf(Color(0xFFFAE7E4),Color(0xFFFDF2EE))))
            .clickable { if(goal==null) onCreateGoal() else onGoal(goal.id) }
            .padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically) {
                PaleTag(if(goal==null) "메인 퀘스트" else if(goal.id=="example-japanese")
                    "예시 메인 퀘스트" else "메인 퀘스트 · 챕터 "+
                        (relevant.firstOrNull{it.completedAt==null}?.chapter ?: 5))
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,"목표 상세",
                    tint=TextInk,modifier=Modifier.size(20.dp))
            }
            Row(verticalAlignment=Alignment.CenterVertically) {
                Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                    Text(goal?.title ?: "첫 목표를 만들어봐",fontSize=16.sp,
                        fontWeight=FontWeight.Bold,lineHeight=21.sp,color=TextInk,
                        maxLines=2,overflow=TextOverflow.Ellipsis)
                    Text(if(goal==null) "지금 바로 시작할 수 있어" else
                        "${completed} / ${relevant.size} 단계",fontSize=11.sp,color=TextGray)
                }
                MountainArt(Modifier.size(85.dp))
            }
            Row(verticalAlignment=Alignment.CenterVertically) {
                SmallBar(ratio,Modifier.weight(1f),height=7.dp)
                Spacer(Modifier.width(10.dp))
                Text("${(ratio*100).toInt()}%",fontSize=11.sp,color=AccentRed,
                    fontWeight=FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(3.dp))
        HeadSection("오늘의 일일 퀘스트",
            "${daily.count{it.completedAt!=null}} / ${daily.size}",onDaily)
        Column(Modifier.fillMaxWidth()) {
            daily.forEachIndexed { i,q ->
                DailyCompactRow(q,onAccept,onComplete,onOpen=onDaily)
                if(i<daily.lastIndex) HorizontalDivider(color=WarmLine,thickness=.6.dp)
            }
            if(daily.isEmpty()) Text("오늘의 퀘스트가 아직 없어",color=TextGray,
                fontSize=12.sp,modifier=Modifier.padding(vertical=14.dp))
        }
    }
}

@Composable
fun QuestListPanel(
    coins:Int,goals:List<GoalEntity>,steps:List<GoalStepEntity>,daily:List<DailyQuestEntity>,
    onGoal:(String)->Unit,onDaily:()->Unit,onCreate:()->Unit,showHeading:Boolean=true
) {
    var tab by remember { mutableIntStateOf(0) }
    Column(verticalArrangement=Arrangement.spacedBy(17.dp)) {
        if(showHeading) {
            ScreenHeading("퀘스트",action={
                TextButton(onClick=onCreate) {
                    Icon(Icons.Outlined.Add,null,modifier=Modifier.size(16.dp),tint=AccentRed)
                    Text("목표 추가",fontSize=12.sp,color=TextInk)
                }
            })
        } else {
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
                TextButton(onClick=onCreate) {
                    Icon(Icons.Rounded.Add,null,modifier=Modifier.size(20.dp),tint=AccentRed)
                    Spacer(Modifier.width(4.dp))
                    Text("목표 만들기",color=TextInk)
                }
            }
        }
        SegmentedTabs(listOf("메인 퀘스트","일일 퀘스트","완료"),tab,{tab=it})
        if(tab==0 || tab==2) {
            val visible=goals.filter { g ->
                val list=steps.filter{it.goalId==g.id}
                if(tab==0) list.any{it.completedAt==null} || list.isEmpty()
                else list.isNotEmpty() && list.all{it.completedAt!=null}
            }
            visible.forEachIndexed { index,g ->
                val current=steps.filter{it.goalId==g.id}
                val done=current.count{it.completedAt!=null}
                val ratio=if(current.isEmpty()) 0f else done.toFloat()/current.size
                val bg=when(index%3) {
                    0->LightRed
                    1->GrassSurface
                    else->MutedSurface
                }
                Row(Modifier.fillMaxWidth().clip(CardShape).background(bg)
                    .clickable {onGoal(g.id)}.padding(15.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(66.dp),contentAlignment=Alignment.Center) {
                        if(index%2==0) MountainArt(Modifier.fillMaxSize())
                        else SproutArt(Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                        Text(if(done==current.size && current.isNotEmpty()) "완료"
                            else if(done>0) "진행 중" else "대기 중",
                            fontSize=10.sp,color=if(done>0) AccentRed else TextGray,
                            fontWeight=FontWeight.Bold)
                        Text(g.title,fontSize=13.sp,lineHeight=18.sp,color=TextInk,
                            fontWeight=FontWeight.Bold,maxLines=2,overflow=TextOverflow.Ellipsis)
                        Text("챕터 ${current.firstOrNull{it.completedAt==null}?.chapter ?: 5} · 단계별 도전",
                            fontSize=10.sp,color=TextGray)
                        SmallBar(ratio,Modifier.fillMaxWidth(),height=6.dp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("${done} / ${current.size}",fontSize=10.sp,color=TextGray)
                }
            }
            if(visible.isEmpty()) WhitePanel {
                Text(if(tab==2) "완료한 메인 퀘스트가 없어" else "아직 목표가 없어",
                    fontSize=13.sp,color=TextGray)
            }
        }
        if(tab==1) {
            Column(Modifier.fillMaxWidth().clip(CardShape)
                .background(LightRed).clickable(onClick=onDaily).padding(18.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Text("오늘의 제안",fontSize=16.sp,fontWeight=FontWeight.Bold,color=TextInk)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,"일일 퀘스트",
                        tint=TextInk,modifier=Modifier.size(19.dp))
                }
                Spacer(Modifier.height(5.dp))
                Text("${daily.size}개의 일일 퀘스트 · 수락하고 시작하기",
                    color=TextGray,fontSize=12.sp)
                Spacer(Modifier.height(12.dp))
                daily.take(3).forEach { q ->
                    Row(verticalAlignment=Alignment.CenterVertically,
                        modifier=Modifier.padding(vertical=4.dp)) {
                        QuestTick(q.completedAt!=null)
                        Spacer(Modifier.width(9.dp))
                        Text(q.title,color=TextInk,fontSize=12.sp)
                    }
                }
            }
        }
        if(tab==0) {
            Row(Modifier.fillMaxWidth().clip(CardShape)
                .background(LilacSurface).clickable(onClick=onCreate).padding(17.dp),
                verticalAlignment=Alignment.CenterVertically) {
                Text("✦",fontSize=25.sp,color=Color(0xFFA781AD))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("새 목표 만들기",fontSize=14.sp,fontWeight=FontWeight.Bold,color=TextInk)
                    Spacer(Modifier.height(3.dp))
                    Text("내 목표를 작은 퀘스트로 시작해 봐",fontSize=11.sp,color=TextGray)
                }
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,null,
                    tint=TextGray,modifier=Modifier.size(19.dp))
            }
        }
    }
}

@Composable
fun GoalDetailPanel(goal:GoalEntity,steps:List<GoalStepEntity>,
    onBack:()->Unit,onComplete:(String,Int)->Unit,onTimer:()->Unit
) {
    val sorted=steps.sortedBy{it.position}
    val done=sorted.count{it.completedAt!=null}
    val chapter=sorted.firstOrNull{it.completedAt==null}?.chapter
        ?: sorted.maxOfOrNull{it.chapter} ?: 1
    val active=sorted.filter{it.chapter==chapter}
    val ratio=if(sorted.isEmpty()) 0f else done.toFloat()/sorted.size
    val totalChapters=maxOf(5,sorted.maxOfOrNull{it.chapter} ?: 5)
    Column(verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            IconButton(onClick=onBack,modifier=Modifier.size(45.dp)) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack,"뒤로가기",tint=TextInk)
            }
            Spacer(Modifier.weight(1f))
            Text("메인 퀘스트",fontSize=11.sp,color=AccentRed,
                modifier=Modifier.clip(CircleShape).background(LightRed)
                    .padding(horizontal=10.dp,vertical=5.dp))
        }
        Row(verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(goal.title,fontSize=22.sp,lineHeight=27.sp,color=TextInk,
                    fontWeight=FontWeight.ExtraBold)
                Text(goal.description,fontSize=11.sp,color=TextGray,lineHeight=17.sp)
            }
            MountainArt(Modifier.size(91.dp))
        }
        Text("${done} / ${sorted.size} 단계",fontSize=12.sp,color=TextInk)
        SmallBar(ratio,height=7.dp)
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.Top) {
            (1..totalChapters).forEach { number ->
                if(number>1) {
                    Box(Modifier.weight(.33f).padding(top=13.dp).height(2.dp)
                        .background(if(number<=chapter) AccentRed.copy(alpha=.55f) else WarmLine))
                }
                Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally) {
                    Box(Modifier.size(28.dp).background(
                        if(number<=chapter) AccentRed else MutedSurface,CircleShape),
                        contentAlignment=Alignment.Center) {
                        Text(if(number<chapter) "✓" else "$number",fontSize=11.sp,
                            fontWeight=FontWeight.Bold,
                            color=if(number<=chapter) MaterialTheme.colorScheme.onPrimary else TextGray)
                    }
                    Spacer(Modifier.height(5.dp))
                    Text("챕터 $number",fontSize=9.sp,color=if(number==chapter) AccentRed else TextGray,
                        maxLines=1)
                }
            }
        }
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("챕터 $chapter",fontSize=15.sp,fontWeight=FontWeight.Bold,color=TextInk)
                Spacer(Modifier.weight(1f))
                Text("${active.count{it.completedAt!=null}} / ${active.size} 완료",
                    color=TextGray,fontSize=11.sp)
            }
            Spacer(Modifier.height(12.dp))
            active.forEachIndexed { i,step ->
                val locked=sorted.any{it.position<step.position && it.completedAt==null}
                Row(Modifier.fillMaxWidth().heightIn(min=56.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).clip(CircleShape).clickable(
                        enabled=!locked && step.completedAt==null,
                        onClick={onComplete(step.goalId,step.position)}),
                        contentAlignment=Alignment.Center) {
                        QuestTick(step.completedAt!=null,locked=locked)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("#${step.position+1} ${step.title}",fontSize=12.sp,
                            fontWeight=FontWeight.Medium,
                            color=if(locked) TextGray else TextInk,
                            maxLines=2,overflow=TextOverflow.Ellipsis)
                        if(locked) Text("이전 단계 완료 후 해금",fontSize=10.sp,color=TextGray)
                    }
                    Text("+${step.reward} C",fontSize=10.sp,color=CoinGold)
                    Spacer(Modifier.width(7.dp))
                    Icon(if(locked) Icons.Outlined.Lock else
                        Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription=null,tint=TextGray,modifier=Modifier.size(17.dp))
                }
                if(i<active.lastIndex) HorizontalDivider(color=WarmLine.copy(alpha=.7f),thickness=.5.dp)
            }
            if(active.isEmpty()) Text("이 챕터에는 퀘스트가 없어",color=TextGray,fontSize=12.sp)
        }
        RedButton("집중 타이머 열기",onClick=onTimer,modifier=Modifier.fillMaxWidth())
    }
}

@Composable
fun NewGoalPanel(onBack:()->Unit,onCreate:(String)->Unit) {
    var title by remember { mutableStateOf("") }
    Column(verticalArrangement=Arrangement.spacedBy(21.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
            IconButton(onClick=onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack,"뒤로가기",tint=TextInk)
            }
            Text("새 목표",fontSize=19.sp,fontWeight=FontWeight.Bold,color=TextInk)
        }
        Text("지금부터 이뤄볼 목표",fontSize=24.sp,color=TextInk,fontWeight=FontWeight.ExtraBold)
        WhitePanel {
            Text("어떤 목표를 이루고 싶어?",fontSize=14.sp,color=TextInk,
                fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value=title,onValueChange={if(it.length<=90)title=it},
                modifier=Modifier.fillMaxWidth(),maxLines=3,
                placeholder={Text("예: 일본어로 자연스럽게 대화하기",fontSize=13.sp)},
                shape=RoundedCornerShape(14.dp),
                colors=OutlinedTextFieldDefaults.colors(
                    focusedBorderColor=AccentRed,unfocusedBorderColor=WarmLine))
            Spacer(Modifier.height(10.dp))
            Text("현재는 로컬에서 7단계 계획을 만들어. AI 자동 생성은 아직 연결 전이야.",
                fontSize=11.sp,lineHeight=16.sp,color=TextGray)
        }
        RedButton("목표 시작하기",onClick={if(title.isNotBlank())onCreate(title.trim())},
            modifier=Modifier.fillMaxWidth())
    }
}
