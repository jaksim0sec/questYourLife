package com.questyourlife.app.ui

import android.Manifest
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay

@Composable
fun DailyPanel(quests: List<DailyQuestEntity>, history: List<DailyQuestEntity>,
    onAccept: (String) -> Unit,onComplete: (String) -> Unit,onBack: () -> Unit) {
    val days=remember(history) { history.mapNotNull { if(it.completedAt!=null) it.dateKey else null }.toSet() }
    var streak=0
    var d=LocalDate.now()
    while(days.contains(d.toString())) { streak++; d=d.minusDays(1) }
    Column(verticalArrangement=Arrangement.spacedBy(20.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
            Text("‹",fontSize=29.sp,color=TextInk,modifier=Modifier.clickable(onClick=onBack).padding(end=13.dp))
            Text("일일 퀘스트",fontSize=26.sp,fontWeight=FontWeight.Black,color=TextInk)
        }
        Row(Modifier.fillMaxWidth().background(
            Brush.horizontalGradient(listOf(LightRed,Color(0xFFFDEEE9))),RoundedCornerShape(21.dp))
            .padding(19.dp),verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                Text("🔥 ${streak}일 연속 달성 ${if(streak>0) "중!" else "도전!"}",
                    fontWeight=FontWeight.ExtraBold,color=AccentRed,fontSize=21.sp)
                Text("매일 조금씩 쌓아가는 기록",fontSize=12.sp,color=TextGray)
                Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                    (6 downTo 0).reversed().forEach {
                        val day=LocalDate.now().minusDays((6-it).toLong())
                        Column(horizontalAlignment=Alignment.CenterHorizontally) {
                            Box(Modifier.size(23.dp).background(
                                if(days.contains(day.toString())) AccentRed else Color.White.copy(alpha=.9f),CircleShape),
                                contentAlignment=Alignment.Center) {
                                Text(if(days.contains(day.toString())) "✓" else "",
                                    color=Color.White,fontSize=11.sp)
                            }
                            Spacer(Modifier.height(5.dp))
                            Text(listOf("일","월","화","수","목","금","토")[day.dayOfWeek.value%7],
                                fontSize=10.sp,color=TextGray)
                        }
                    }
                }
            }
            ChestArt(Modifier.size(76.dp))
        }
        HeadSection("◉  오늘의 일일 퀘스트",
            "${quests.count{it.completedAt!=null}} / ${quests.size}")
        Column(verticalArrangement=Arrangement.spacedBy(1.dp)) {
            quests.forEachIndexed { index,q ->
                DailyCompactRow(q,onAccept,onComplete)
                if(index<quests.lastIndex) HorizontalDivider(color=WarmLine,thickness=.6.dp)
            }
        }
        Row(Modifier.fillMaxWidth().background(
            Brush.horizontalGradient(listOf(Color(0xFFFBE9E5),Color(0xFFFFF3EE))),
            RoundedCornerShape(18.dp)).padding(18.dp),
            verticalAlignment=Alignment.CenterVertically) {
            Text("🎁",fontSize=23.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text("주간 보상",color=TextInk,fontSize=15.sp,fontWeight=FontWeight.Bold)
                Text("5일 이상 달성하면 특별한 성취 배지!",color=TextGray,fontSize=11.sp)
            }
            Spacer(Modifier.weight(1f))
            Text("›",fontSize=21.sp,color=TextInk)
        }
        Text("퀘스트 제안은 로컬에서 생성돼. 완료 버튼을 눌러야 기록돼.",
            color=TextGray,fontSize=10.sp)
    }
}

@Composable
fun CompletionPanel(title: String,reward: Int,onClose: () -> Unit,onNext: () -> Unit) {
    Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.spacedBy(20.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("퀘스트 완료!",color=TextInk,fontWeight=FontWeight.Black,fontSize=29.sp)
        MascotArt(Modifier.size(210.dp),festive=true)
        Text("+$reward C",fontSize=27.sp,fontWeight=FontWeight.Black,color=AccentRed,
            modifier=Modifier.background(Color(0xFFFFF1DA),CircleShape)
                .padding(horizontal=24.dp,vertical=10.dp))
        WhitePanel {
            Text(title,color=TextInk,fontWeight=FontWeight.Bold,fontSize=16.sp)
            Spacer(Modifier.height(6.dp))
            Text("오늘도 한 걸음 성장했어",color=TextGray,fontSize=12.sp)
        }
        WhitePanel {
            Text("✎  선택의 영향",color=TextGray,fontSize=12.sp)
            Spacer(Modifier.height(9.dp))
            Text("“작은 하루가 쌓여서 결국 목표가 되는 거야.”",fontSize=14.sp,
                lineHeight=22.sp,color=TextInk)
        }
        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick=onClose,modifier=Modifier.weight(1f).height(51.dp),
                shape=RoundedCornerShape(16.dp)) { Text("닫기",color=TextInk) }
            RedButton("다음 퀘스트 보기  ▷",onClick=onNext,modifier=Modifier.weight(1.5f))
        }
    }
}

private val HatColors=listOf(
    Color(0xFF2D2D32),Color(0xFFF3C879),Color(0xFF457DC8),
    Color(0xFF8061B3),Color(0xFF68877C),Color(0xFF61676D)
)
private val Prices=listOf(0,50,80,100,120,150)
@Composable
fun DecorPanel(coins: Int, owned: List<OwnedItemEntity>, equipped: Int,
    onBuy: (Int,Int)->Unit,onEquip: (Int)->Unit) {
    var category by remember { mutableIntStateOf(0) }
    Column(verticalArrangement=Arrangement.spacedBy(18.dp)) {
        ScreenHeading("꾸미기",action={CashBadge(coins)})
        Box(Modifier.fillMaxWidth().height(183.dp),contentAlignment=Alignment.Center) {
            MascotArt(Modifier.size(177.dp))
            HatArt(HatColors[equipped.coerceIn(0,5)],
                modifier=Modifier.size(100.dp).offset(x=(-6).dp,y=(-48).dp))
        }
        Row(Modifier.fillMaxWidth().background(Color(0xFFF0EEEB),CircleShape).padding(4.dp),
            horizontalArrangement=Arrangement.spacedBy(3.dp)) {
            listOf("모자","옷","소품","배경").forEachIndexed { index,s ->
                Box(Modifier.weight(1f).background(
                    if(category==index) Color(0xFF343433) else Color.Transparent,CircleShape)
                    .clickable { category=index }.padding(vertical=10.dp),
                    contentAlignment=Alignment.Center) {
                    Text(s,fontSize=12.sp,color=if(category==index) Color.White else TextGray)
                }
            }
        }
        if(category!=0) {
            WhitePanel {
                Text("이 꾸미기 카테고리는 준비 중이야",fontWeight=FontWeight.Bold,color=TextInk)
                Spacer(Modifier.height(6.dp))
                Text("현재는 모자를 실제로 구매하고 착용할 수 있어.",fontSize=12.sp,color=TextGray)
            }
        } else {
            val items=(0..8).toList()
            // Explicit two-dimensional grid, no unbounded nested scrolling.
            items.chunked(3).forEach { group ->
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    group.forEach { index ->
                        val available=index<=5
                        val ownedItem=index==0 || owned.any { it.itemId==index }
                        val isEquipped=index==equipped
                        Column(
                            modifier=Modifier.weight(1f).height(118.dp)
                                .border(if(isEquipped) 1.4.dp else .5.dp,
                                    if(isEquipped) AccentRed else WarmLine,RoundedCornerShape(17.dp))
                                .background(Color.White.copy(alpha=.86f),RoundedCornerShape(17.dp))
                                .clickable(enabled=available) {
                                    if(ownedItem) onEquip(index) else onBuy(index,Prices[index])
                                }.padding(8.dp),
                            horizontalAlignment=Alignment.CenterHorizontally,
                            verticalArrangement=Arrangement.SpaceBetween
                        ) {
                            Box(contentAlignment=Alignment.Center,modifier=Modifier.size(69.dp)) {
                                if(available) HatArt(HatColors[index],Modifier.fillMaxSize())
                                else Text("?",fontSize=36.sp,color=WarmLine,fontWeight=FontWeight.Black)
                            }
                            Text(
                                if(!available) "잠김" else if(isEquipped) "착용 중 ✓"
                                else if(ownedItem) "보유 · 착용" else "✦ ${Prices[index]} C",
                                fontSize=10.sp, color=if(isEquipped) AccentRed else TextGray,
                                fontWeight=FontWeight.Bold,maxLines=1
                            )
                        }
                    }
                }
            }
            Text("보상으로 획득한 캐시만 사용할 수 있어. 구매 후 자동 착용돼.",
                color=TextGray,fontSize=11.sp)
        }
    }
}

@Composable
fun HistoryPanel(
    questsHistory: List<DailyQuestEntity>,steps: List<GoalStepEntity>,
    sessions: List<FocusSessionEntity>
) {
    var range by remember { mutableIntStateOf(0) }
    var weekShift by remember { mutableIntStateOf(0) }
    val completed=questsHistory.filter{it.completedAt!=null}
    val minutes=sessions.sumOf { it.durationSeconds }/60
    val dates=completed.map{it.dateKey}.toSet()
    val anchor=LocalDate.now().minusWeeks(weekShift.toLong())
    val monday=anchor.minusDays((anchor.dayOfWeek.value-1).toLong())
    Column(verticalArrangement=Arrangement.spacedBy(19.dp)) {
        ScreenHeading("기록")
        Row(Modifier.fillMaxWidth().background(Color(0xFFF0EEEB),CircleShape).padding(4.dp)) {
            listOf("일간","주간","월간").forEachIndexed { i,label->
                Box(Modifier.weight(1f).background(
                    if(range==i) Color.White else Color.Transparent,CircleShape)
                    .clickable { range=i }.padding(vertical=10.dp),
                    contentAlignment=Alignment.Center) {
                    Text(label,fontSize=12.sp,color=if(range==i) TextInk else TextGray)
                }
            }
        }
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("‹",fontSize=26.sp,color=TextInk,
                    modifier=Modifier.clickable { weekShift++ }.padding(end=18.dp))
                Text("${monday.monthValue}월 ${monday.dayOfMonth}일 ~ ${monday.plusDays(6).monthValue}월 ${monday.plusDays(6).dayOfMonth}일",
                    modifier=Modifier.weight(1f),textAlign=TextAlign.Center,
                    color=TextInk,fontSize=12.sp,fontWeight=FontWeight.Bold)
                Text("›",fontSize=26.sp,color=if(weekShift>0) TextInk else TextGray,
                    modifier=Modifier.clickable { if(weekShift>0) weekShift-- }.padding(start=18.dp))
            }
            Spacer(Modifier.height(17.dp))
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                (0..6).forEach { i->
                    val d=monday.plusDays(i.toLong())
                    val hit=dates.contains(d.toString())
                    Column(horizontalAlignment=Alignment.CenterHorizontally,
                        verticalArrangement=Arrangement.spacedBy(8.dp)) {
                        Text(listOf("월","화","수","목","금","토","일")[i],color=TextGray,fontSize=10.sp)
                        Box(Modifier.size(32.dp).background(
                            if(d==LocalDate.now()) AccentRed else if(hit) LightRed else Cream,CircleShape),
                            contentAlignment=Alignment.Center) {
                            Text(if(hit) "✓" else "${d.dayOfMonth}",fontSize=12.sp,
                                color=if(d==LocalDate.now()) Color.White else if(hit) AccentRed else TextGray)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement=Arrangement.spacedBy(11.dp)) {
            WhitePanel(modifier=Modifier.weight(1f)) {
                Text("완료한 퀘스트",color=TextGray,fontSize=11.sp)
                Text("${completed.size + steps.count { it.completedAt!=null }}개",fontSize=26.sp,
                    fontWeight=FontWeight.Black,color=TextInk)
            }
            WhitePanel(modifier=Modifier.weight(1f)) {
                Text("집중한 시간",color=TextGray,fontSize=11.sp)
                Text("${minutes/60}시간 ${minutes%60}분",fontSize=19.sp,
                    fontWeight=FontWeight.Black,color=TextInk)
            }
        }
        WhitePanel {
            Text("활동 비중",fontSize=15.sp,color=TextInk,fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(17.dp))
            val study=completed.count {
                listOf("단어","공부","읽","책","일본어","집중").any { word->it.title.contains(word) }
            }+steps.count{it.completedAt!=null}
            val total=(completed.size+steps.count{it.completedAt!=null}).coerceAtLeast(1)
            val ratio=study.toFloat()/total
            Row(Modifier.fillMaxWidth().height(12.dp)
                .background(Color(0xFFF0E7DC),CircleShape)) {
                Box(Modifier.fillMaxWidth(ratio).fillMaxHeight().background(Color(0xFF78A4DA),CircleShape))
            }
            Spacer(Modifier.height(14.dp))
            Text("● 학습 ${study*100/total}%    ● 기타 ${(total-study)*100/total}%",
                fontSize=11.sp,color=TextGray)
            Text("개인 기기에 기록된 완료 항목 기준",fontSize=10.sp,color=TextGray)
        }
    }
}

@Composable
fun FocusPanel(onBack: () -> Unit, onFinish: (Int) -> Unit) {
    val context=LocalContext.current
    val prefs=remember { context.getSharedPreferences("focus_timer",Context.MODE_PRIVATE) }
    var seconds by remember { mutableIntStateOf(prefs.getInt("remaining",25*60)) }
    var deadline by remember { mutableLongStateOf(prefs.getLong("end_time",0L)) }
    var startedAt by remember { mutableLongStateOf(prefs.getLong("started_at",0L)) }
    var focusLength by remember { mutableIntStateOf(prefs.getInt("length",25*60)) }
    val running=deadline>0
    LaunchedEffect(deadline) {
        while(deadline>0L) {
            val rest=((deadline-System.currentTimeMillis()+999)/1000).toInt().coerceAtLeast(0)
            seconds=rest
            if(rest==0) {
                deadline=0L
                prefs.edit().putLong("end_time",0L).putInt("remaining",focusLength).apply()
                onFinish(focusLength)
                seconds=focusLength
                break
            }
            delay(500)
        }
    }
    Column(horizontalAlignment=Alignment.CenterHorizontally,
        modifier=Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(21.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text("×",fontSize=29.sp,color=TextInk,modifier=Modifier.clickable(onClick=onBack))
            Spacer(Modifier.weight(1f))
            Text("집중하기",fontSize=16.sp,fontWeight=FontWeight.Bold,color=TextInk)
            Spacer(Modifier.weight(1f))
            Text("♪",fontSize=21.sp,color=TextGray)
        }
        Spacer(Modifier.height(42.dp))
        Box(Modifier.size(292.dp),contentAlignment=Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val width=10.dp.toPx()
                val r=size.minDimension*.47f
                val top=androidx.compose.ui.geometry.Offset(size.width*.5f-r,size.height*.5f-r)
                val sizeArc=androidx.compose.ui.geometry.Size(r*2,r*2)
                drawArc(Color(0xFFF0E9E2),-90f,360f,false,top,sizeArc,style=Stroke(width,cap=StrokeCap.Round))
                drawArc(AccentRed,-90f,360f*seconds.toFloat()/focusLength.coerceAtLeast(1),
                    false,top,sizeArc,style=Stroke(width,cap=StrokeCap.Round))
            }
            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                Text("집중 타이머",color=TextGray,fontSize=13.sp)
                Spacer(Modifier.height(9.dp))
                Text("%02d:%02d".format(seconds/60,seconds%60),
                    color=TextInk,fontSize=43.sp,fontWeight=FontWeight.Black)
                Spacer(Modifier.height(10.dp))
                Text("작은 수행에 집중하는 시간",fontSize=11.sp,color=TextGray)
            }
        }
        Spacer(Modifier.height(14.dp))
        Box(Modifier.size(68.dp).background(AccentRed,CircleShape).clickable {
            if(running) {
                val rest=((deadline-System.currentTimeMillis())/1000).toInt().coerceAtLeast(0)
                seconds=rest
                deadline=0
                prefs.edit().putLong("end_time",0L).putInt("remaining",rest).apply()
            } else {
                if(seconds<=0) seconds=focusLength
                deadline=System.currentTimeMillis()+seconds*1000L
                startedAt=System.currentTimeMillis()
                prefs.edit().putLong("end_time",deadline).putInt("length",focusLength)
                    .putLong("started_at",startedAt).apply()
            }
        },contentAlignment=Alignment.Center) {
            Text(if(running) "Ⅱ" else "▶",fontSize=28.sp,color=Color.White)
        }
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) {
            listOf(15,25,45).forEach { min ->
                Column(Modifier.clickable {
                    if(!running) {
                        focusLength=min*60;seconds=min*60
                        prefs.edit().putInt("remaining",seconds).putInt("length",focusLength).apply()
                    }
                },horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("$min",fontSize=19.sp,fontWeight=FontWeight.Bold,
                        color=if(focusLength==min*60) AccentRed else TextGray)
                    Text("분 집중",fontSize=10.sp,color=TextGray)
                }
            }
        }
        Text("타이머는 경과 시간만 기록해. 퀘스트 완료는 직접 눌러야 해.",
            color=TextGray,fontSize=11.sp,textAlign=TextAlign.Center)
    }
}

@Composable
fun SettingsPanel(model: DailyQuestViewModel,onBack: () -> Unit,onClassic: () -> Unit) {
    val context=LocalContext.current
    var allowed by remember { mutableStateOf(
        (Build.VERSION.SDK_INT<33 || context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)
            && context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
    ) }
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        allowed=(Build.VERSION.SDK_INT<33 ||
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)
            && context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
    }
    Column(verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Text("‹   설정",fontWeight=FontWeight.Bold,fontSize=26.sp,color=TextInk,
            modifier=Modifier.clickable(onClick=onBack))
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("매일 퀘스트 제안",fontSize=16.sp,fontWeight=FontWeight.Bold,color=TextInk)
                    Text("매일 정한 시각에 알림",fontSize=11.sp,color=TextGray)
                }
                Switch(model.reminders.value,onCheckedChange=model::setReminders)
            }
            HorizontalDivider(color=WarmLine,modifier=Modifier.padding(vertical=14.dp))
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("제안 시각",fontSize=14.sp,color=TextInk)
                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick={
                    TimePickerDialog(context,{_,h,m->model.setTime(h,m)},
                        model.hour.intValue,model.minute.intValue,true).show()
                }) {
                    Text("%02d:%02d".format(model.hour.intValue,model.minute.intValue))
                }
            }
            if(!allowed && Build.VERSION.SDK_INT>=33) {
                Spacer(Modifier.height(13.dp))
                Text("알림 권한을 허용해야 알림창에 표시돼.",fontSize=11.sp,color=TextGray)
                TextButton(onClick={launcher.launch(Manifest.permission.POST_NOTIFICATIONS)}) {
                    Text("알림 권한 요청")
                }
            }
        }
        WhitePanel {
            Text("기존 체크리스트",color=TextInk,fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("이전에 작성한 퀘스트는 그대로 보존해.",fontSize=11.sp,color=TextGray)
            TextButton(onClick=onClassic) { Text("열기") }
        }
        Text("알림은 절전 상태에서 늦어질 수 있어. AI 생성과 서버 동기화는 아직 준비 중이야.",
            color=TextGray,fontSize=11.sp)
    }
}
