package com.questyourlife.app.ui

import android.Manifest
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
private fun BackTitle(text:String,onBack:()->Unit) {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
        IconButton(onClick=onBack,modifier=Modifier.size(45.dp)) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack,"뒤로가기",tint=TextInk)
        }
        Spacer(Modifier.width(4.dp))
        Text(text,fontSize=21.sp,fontWeight=FontWeight.ExtraBold,color=TextInk)
    }
}

@Composable
fun DailyPanel(quests:List<DailyQuestEntity>,history:List<DailyQuestEntity>,
    onAccept:(String)->Unit,onComplete:(String)->Unit,onBack:()->Unit
) {
    val completedDates=remember(history) { history.filter{it.completedAt!=null}.map{it.dateKey}.toSet() }
    var streak=0
    var date=LocalDate.now()
    while(completedDates.contains(date.toString())) { streak++;date=date.minusDays(1) }
    val done=quests.count{it.completedAt!=null}
    Column(verticalArrangement=Arrangement.spacedBy(18.dp)) {
        BackTitle("일일 퀘스트",onBack)
        Row(Modifier.fillMaxWidth().clip(CardShape)
            .background(Brush.horizontalGradient(listOf(Color(0xFFFAE4E0),Color(0xFFFDF0E9))))
            .padding(18.dp),verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${streak}일 연속 달성 중",fontSize=18.sp,fontWeight=FontWeight.Bold,
                    color=AccentRed)
                Spacer(Modifier.height(5.dp))
                Text("오늘도 조금씩 이어가 봐",fontSize=11.sp,color=TextGray)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    (6 downTo 0).forEach { offset ->
                        val day=LocalDate.now().minusDays(offset.toLong())
                        val isDone=completedDates.contains(day.toString())
                        Column(horizontalAlignment=Alignment.CenterHorizontally) {
                            Box(Modifier.size(25.dp).background(
                                if(isDone) AccentRed else CardWhite,CircleShape),
                                contentAlignment=Alignment.Center) {
                                if(isDone) Icon(Icons.Filled.Check,null,Modifier.size(15.dp),Color.White)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(listOf("월","화","수","목","금","토","일")
                                [day.dayOfWeek.value-1],fontSize=10.sp,color=TextGray)
                        }
                    }
                }
            }
            ChestArt(Modifier.size(62.dp))
        }
        HeadSection("오늘의 일일 퀘스트","$done / ${quests.size}")
        Column {
            quests.forEachIndexed { i,q ->
                DailyCompactRow(q,onAccept,onComplete)
                if(i!=quests.lastIndex) HorizontalDivider(color=WarmLine,thickness=.6.dp)
            }
            if(quests.isEmpty()) Text("오늘 제안된 퀘스트가 없어",fontSize=12.sp,color=TextGray)
        }
        Row(Modifier.fillMaxWidth().clip(CardShape)
            .background(Color(0xFFFBECE7)).padding(17.dp),
            verticalAlignment=Alignment.CenterVertically) {
            Text("주간 달성",fontSize=14.sp,fontWeight=FontWeight.Bold,color=TextInk)
            Spacer(Modifier.weight(1f))
            Text("이번 주 ${completedDates.count{key ->
                val day=runCatching{LocalDate.parse(key)}.getOrNull()
                day!=null && !day.isBefore(LocalDate.now().minusDays(6)) &&
                    !day.isAfter(LocalDate.now())
            }}일 기록",fontSize=11.sp,color=AccentRed)
        }
        Text("일일 퀘스트는 수락 후 직접 완료해야 보상이 기록돼.",
            fontSize=11.sp,color=TextGray,lineHeight=17.sp)
    }
}

@Composable
fun CompletionPanel(title:String,reward:Int,onClose:()->Unit,onNext:()->Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible=true }
    val scale by animateFloatAsState(if(visible) 1f else .82f,
        animationSpec=spring(dampingRatio=.68f,stiffness=370f),label="reward entrance")
    Column(Modifier.fillMaxWidth().padding(horizontal=23.dp,vertical=10.dp),
        horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Text("퀘스트 완료!",fontSize=25.sp,fontWeight=FontWeight.Black,color=TextInk)
        Box(Modifier.fillMaxWidth().height(177.dp),contentAlignment=Alignment.Center) {
            MascotArt(Modifier.size(170.dp).scale(scale),festive=true)
        }
        Row(Modifier.clip(CircleShape).background(Color(0xFFFFF1D8))
            .padding(horizontal=20.dp,vertical=8.dp),
            verticalAlignment=Alignment.CenterVertically) {
            Text("+$reward C",fontSize=23.sp,fontWeight=FontWeight.Black,color=TextInk)
        }
        Spacer(Modifier.height(2.dp))
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                QuestTick(true)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title,fontSize=14.sp,fontWeight=FontWeight.Bold,color=TextInk,
                        maxLines=2,overflow=TextOverflow.Ellipsis)
                    Text("완료한 퀘스트",fontSize=11.sp,color=TextGray)
                }
            }
        }
        WhitePanel {
            Text("선택의 영향",fontSize=13.sp,fontWeight=FontWeight.Bold,color=TextInk)
            Spacer(Modifier.height(9.dp))
            Text("작은 실천이 오늘의 기록을 바꿨어.",
                fontSize=12.sp,color=TextGray,lineHeight=17.sp)
        }
        RedButton("보상 받고 다음 퀘스트 보기",onClick=onNext,
            modifier=Modifier.fillMaxWidth())
        TextButton(onClick=onClose) { Text("닫기",fontSize=12.sp,color=TextGray) }
    }
}

private val HatColors=listOf(
    Color(0xFF303033),Color(0xFFF1CB81),Color(0xFF447CC6),
    Color(0xFF8864B6),Color(0xFF668A7C),Color(0xFF666D74)
)
private val HatPrices=listOf(0,50,80,100,120,150)

@Composable
fun DecorPanel(coins:Int,owned:List<OwnedItemEntity>,equipped:Int,
    onBuy:(Int,Int)->Unit,onEquip:(Int)->Unit
) {
    var category by remember { mutableIntStateOf(0) }
    Column(verticalArrangement=Arrangement.spacedBy(16.dp)) {
        ScreenHeading("꾸미기",action={CashBadge(coins)})
        Box(Modifier.fillMaxWidth().height(175.dp),contentAlignment=Alignment.Center) {
            MascotArt(Modifier.size(174.dp))
            HatArt(HatColors[equipped.coerceIn(0,5)],
                modifier=Modifier.size(87.dp).offset(x=(-11).dp,y=(-51).dp).rotate(-10f))
        }
        SegmentedTabs(listOf("모자","옷","소품","배경"),category,{category=it})
        if(category==0) {
            (0..8).toList().chunked(3).forEach { indexes ->
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(9.dp)) {
                    indexes.forEach { index ->
                        val unlocked=index<=5
                        val mine=index==0 || owned.any{it.itemId==index}
                        val worn=index==equipped
                        val price=HatPrices.getOrElse(index){0}
                        Column(
                            Modifier.weight(1f).height(120.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(CardWhite)
                                .border(if(worn) 1.5.dp else .7.dp,
                                    if(worn) AccentRed else WarmLine,RoundedCornerShape(17.dp))
                                .clickable(enabled=unlocked) {
                                    if(mine) onEquip(index) else onBuy(index,price)
                                }.padding(8.dp),
                            verticalArrangement=Arrangement.SpaceBetween,
                            horizontalAlignment=Alignment.CenterHorizontally
                        ) {
                            Box(Modifier.fillMaxWidth().height(73.dp),
                                contentAlignment=Alignment.Center) {
                                if(unlocked) HatArt(HatColors[index],Modifier.size(74.dp))
                                else Text("?",fontSize=39.sp,fontWeight=FontWeight.Black,
                                    color=WarmLine)
                            }
                            Text(when {
                                !unlocked->"잠김"
                                worn->"착용 중"
                                mine->"착용하기"
                                else->"${price} C"
                            },fontSize=11.sp,color=if(worn) AccentRed else TextGray,
                                fontWeight=if(worn) FontWeight.Bold else FontWeight.Medium)
                        }
                    }
                }
            }
        } else WhitePanel {
            Text("아직 준비 중인 꾸미기 항목이야",fontWeight=FontWeight.Bold,
                color=TextInk,fontSize=13.sp)
            Spacer(Modifier.height(5.dp))
            Text("현재는 모자만 구매하고 착용할 수 있어.",color=TextGray,fontSize=11.sp)
        }
    }
}

@Composable
fun HistoryPanel(questsHistory:List<DailyQuestEntity>,steps:List<GoalStepEntity>,
    sessions:List<FocusSessionEntity>
) {
    var range by remember { mutableIntStateOf(0) }
    var shift by remember(range) { mutableIntStateOf(0) }
    val anchor=when(range) {
        0->LocalDate.now().minusDays(shift.toLong())
        1->LocalDate.now().minusWeeks(shift.toLong())
        else->LocalDate.now().minusMonths(shift.toLong())
    }
    val monday=anchor.minusDays((anchor.dayOfWeek.value-1).toLong())
    val start=when(range) {
        0->anchor
        1->monday
        else->anchor.withDayOfMonth(1)
    }
    val end=when(range) {
        0->anchor.plusDays(1)
        1->monday.plusWeeks(1)
        else->start.plusMonths(1)
    }
    fun inPeriod(d:LocalDate)=!d.isBefore(start)&&d.isBefore(end)
    val all=questsHistory.filter{it.completedAt!=null}
    val today=all.filter{ runCatching{inPeriod(LocalDate.parse(it.dateKey))}.getOrDefault(false) }
    val linkedCompletions = questsHistory.filter { it.completedAt != null }.mapNotNull {
        if (it.goalId != null && it.stepPosition != null) it.goalId to it.stepPosition else null
    }.toSet()
    val doneSteps=steps.filter { (it.goalId to it.position) !in linkedCompletions }.filter {
        it.completedAt?.let{value->
            inPeriod(Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate())
        } ?: false
    }
    val minutes=sessions.filter{
        inPeriod(Instant.ofEpochMilli(it.finishedAt).atZone(ZoneId.systemDefault()).toLocalDate())
    }.sumOf{it.durationSeconds}/60
    val days=all.map{it.dateKey}.toSet()
    val count=today.size+doneSteps.size
    val learningTerms=listOf("일본어","학습","단어","문제","읽","책","공부","수학")
    val healthTerms=listOf("산책","운동","스트레칭","걷","건강")
    val titles=today.map{it.title}+doneSteps.map{it.title}
    val study=titles.count{s->learningTerms.any{s.contains(it)}}
    val health=titles.count{s->healthTerms.any{s.contains(it)}}
    val other=(titles.size-study-health).coerceAtLeast(0)
    Column(verticalArrangement=Arrangement.spacedBy(17.dp)) {
        ScreenHeading("기록")
        Row(Modifier.fillMaxWidth().clip(CircleShape).background(MutedSurface).padding(3.dp)) {
            listOf("일간","주간","월간").forEachIndexed { idx,label ->
                Box(Modifier.weight(1f).height(35.dp)
                    .clip(CircleShape)
                    .background(if(range==idx) CardWhite else Color.Transparent)
                    .clickable{range=idx},contentAlignment=Alignment.Center) {
                    Text(label,fontSize=11.sp,color=if(range==idx)TextInk else TextGray,
                        fontWeight=if(range==idx)FontWeight.Bold else FontWeight.Medium)
                }
            }
        }
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                IconButton(onClick={shift++},modifier=Modifier.size(42.dp)) {
                    Text("‹",fontSize=25.sp,color=TextInk)
                }
                Text(when(range) {
                    0->"${anchor.monthValue}월 ${anchor.dayOfMonth}일"
                    1->"${monday.monthValue}월 ${monday.dayOfMonth}일 ~ "+
                        "${monday.plusDays(6).monthValue}월 ${monday.plusDays(6).dayOfMonth}일"
                    else->"${anchor.year}년 ${anchor.monthValue}월"
                },modifier=Modifier.weight(1f),textAlign=TextAlign.Center,
                    fontSize=12.sp,fontWeight=FontWeight.Bold,color=TextInk)
                IconButton(onClick={if(shift>0)shift--},enabled=shift>0,
                    modifier=Modifier.size(42.dp)) {
                    Text("›",fontSize=25.sp,color=if(shift>0)TextInk else WarmLine)
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                (0..6).forEach{i->
                    val d=monday.plusDays(i.toLong())
                    val checked=days.contains(d.toString())
                    Column(horizontalAlignment=Alignment.CenterHorizontally) {
                        Text(listOf("월","화","수","목","금","토","일")[i],
                            fontSize=10.sp,color=TextGray)
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.size(30.dp).background(
                            if(d==LocalDate.now()&&shift==0) AccentRed
                            else if(checked) LightRed else Cream,CircleShape),
                            contentAlignment=Alignment.Center) {
                            Text(if(checked) "✓" else "${d.dayOfMonth}",
                                fontSize=11.sp,
                                color=if(d==LocalDate.now()&&shift==0)Color.White
                                    else if(checked)AccentRed else TextGray)
                        }
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
            WhitePanel(Modifier.weight(1f)) {
                Text("완료한 퀘스트",fontSize=11.sp,color=TextGray)
                Spacer(Modifier.height(9.dp))
                Text("${count}개",fontSize=24.sp,fontWeight=FontWeight.Black,color=TextInk)
            }
            WhitePanel(Modifier.weight(1f)) {
                Text("집중한 시간",fontSize=11.sp,color=TextGray)
                Spacer(Modifier.height(9.dp))
                Text("${minutes/60}시간 ${minutes%60}분",
                    fontSize=17.sp,fontWeight=FontWeight.Black,color=TextInk,
                    maxLines=1)
            }
        }
        WhitePanel {
            Text("활동 비중",fontWeight=FontWeight.Bold,fontSize=15.sp,color=TextInk)
            Spacer(Modifier.height(17.dp))
            val categories=listOf(
                Triple("학습",study,Color(0xFF79A3D5)),
                Triple("건강",health,Color(0xFF99BEAB)),
                Triple("기타",other,Color(0xFFE7C8A3))
            )
            Row(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape)
                .background(MutedSurface)) {
                categories.forEach { (_,size,color)->
                    if(size>0) Box(Modifier.weight(size.toFloat()).fillMaxHeight()
                        .background(color))
                }
            }
            Spacer(Modifier.height(16.dp))
            categories.forEach{(label,value,color)->
                Row(Modifier.fillMaxWidth().heightIn(min=25.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).background(color,CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text(label,fontSize=11.sp,color=TextGray)
                    Spacer(Modifier.weight(1f))
                    Text(if(count==0) "0%" else "${value*100/count}%",
                        fontSize=11.sp,fontWeight=FontWeight.Bold,color=TextInk)
                }
            }
            Spacer(Modifier.height(5.dp))
            Text("완료된 항목의 제목 기준으로 분류했어",fontSize=10.sp,color=TextGray)
        }
    }
}

@Composable
fun FocusPanel(onBack:()->Unit,onFinish:(Int)->Unit) {
    val context=LocalContext.current
    val prefs=remember{context.getSharedPreferences("focus_timer",Context.MODE_PRIVATE)}
    var seconds by remember { mutableIntStateOf(prefs.getInt("remaining",25*60)) }
    var deadline by remember { mutableLongStateOf(prefs.getLong("end_time",0L)) }
    var length by remember { mutableIntStateOf(prefs.getInt("length",25*60)) }
    val running=deadline>0L
    val progress by animateFloatAsState(seconds.toFloat()/length.coerceAtLeast(1),
        animationSpec=tween(350),label="focus sweep")
    LaunchedEffect(deadline) {
        while(deadline>0L) {
            val remain=((deadline-System.currentTimeMillis()+999)/1000).toInt().coerceAtLeast(0)
            seconds=remain
            if(remain==0) {
                deadline=0L
                prefs.edit().putLong("end_time",0L).putInt("remaining",length).apply()
                onFinish(length)
                seconds=length
                break
            }
            delay(500)
        }
    }
    Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally,
        verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            IconButton(onClick=onBack) { Icon(Icons.Default.Close,"닫기",tint=TextInk) }
            Spacer(Modifier.weight(1f))
            Text("집중하기",fontSize=15.sp,fontWeight=FontWeight.Bold,color=TextInk)
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(48.dp))
        }
        Spacer(Modifier.height(26.dp))
        Box(Modifier.fillMaxWidth().widthIn(max=294.dp).aspectRatio(1f),
            contentAlignment=Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke=8.dp.toPx()
                val inset=stroke/2+6.dp.toPx()
                drawArc(Color(0xFFF1EAE4),-90f,360f,false,
                    topLeft=Offset(inset,inset),
                    size=Size(size.width-2*inset,size.height-2*inset),
                    style=Stroke(stroke,cap=StrokeCap.Round))
                drawArc(AccentRed,-90f,360*progress.coerceIn(0f,1f),false,
                    topLeft=Offset(inset,inset),
                    size=Size(size.width-2*inset,size.height-2*inset),
                    style=Stroke(stroke,cap=StrokeCap.Round))
            }
            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                Text("집중 타이머",fontSize=12.sp,color=TextGray)
                Spacer(Modifier.height(10.dp))
                Text("%02d:%02d".format(seconds/60,seconds%60),
                    fontSize=43.sp,fontWeight=FontWeight.Black,color=TextInk)
                Spacer(Modifier.height(9.dp))
                Text("나에게 집중하는 시간",fontSize=11.sp,color=TextGray)
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.size(66.dp).clip(CircleShape)
            .background(AccentRed).clickable {
                if(running) {
                    val remaining=((deadline-System.currentTimeMillis()+999)/1000)
                        .toInt().coerceAtLeast(0)
                    seconds=remaining
                    deadline=0L
                    prefs.edit().putLong("end_time",0L).putInt("remaining",seconds).apply()
                } else {
                    if(seconds<=0)seconds=length
                    deadline=System.currentTimeMillis()+seconds*1000L
                    prefs.edit().putLong("end_time",deadline).putInt("length",length)
                        .putInt("remaining",seconds).apply()
                }
            },contentAlignment=Alignment.Center) {
            Icon(if(running)Icons.Filled.Pause else Icons.Filled.PlayArrow,
                if(running)"일시정지" else "시작",tint=Color.White,
                modifier=Modifier.size(31.dp))
        }
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) {
            listOf(15,25,45).forEach{minutes->
                Column(Modifier.clip(RoundedCornerShape(14.dp))
                    .clickable(enabled=!running) {
                        length=minutes*60;seconds=length
                        prefs.edit().putInt("length",length).putInt("remaining",length).apply()
                    }.padding(horizontal=12.dp,vertical=9.dp),
                    horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("$minutes",fontSize=18.sp,fontWeight=FontWeight.Bold,
                        color=if(minutes*60==length)AccentRed else TextGray)
                    Text("분",fontSize=11.sp,color=TextGray)
                }
            }
        }
        Text("타이머 완료는 기록만 남겨. 퀘스트 완료는 직접 눌러야 해.",
            fontSize=11.sp,color=TextGray,textAlign=TextAlign.Center,lineHeight=17.sp)
    }
}

@Composable
fun SettingsPanel(model:DailyQuestViewModel,onBack:()->Unit,onClassic:()->Unit) {
    val context=LocalContext.current
    var allowed by remember {
        mutableStateOf((Build.VERSION.SDK_INT<33 ||
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)
            && context.getSystemService(NotificationManager::class.java).areNotificationsEnabled())
    }
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        allowed=(Build.VERSION.SDK_INT<33 ||
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)
            && context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
    }
    Column(verticalArrangement=Arrangement.spacedBy(18.dp)) {
        BackTitle("설정",onBack)
        WhitePanel {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("매일 퀘스트 제안",fontSize=14.sp,color=TextInk,
                        fontWeight=FontWeight.Bold)
                    Text("설정한 시각에 알려줘",fontSize=11.sp,color=TextGray)
                }
                Switch(checked=model.reminders.value,onCheckedChange=model::setReminders,
                    colors=SwitchDefaults.colors(checkedTrackColor=AccentRed))
            }
            HorizontalDivider(color=WarmLine,modifier=Modifier.padding(vertical=11.dp))
            Row(verticalAlignment=Alignment.CenterVertically) {
                Text("제안 시각",fontSize=13.sp,color=TextInk)
                Spacer(Modifier.weight(1f))
                TextButton(onClick={
                    TimePickerDialog(context,{_,h,m->model.setTime(h,m)},
                        model.hour.intValue,model.minute.intValue,true).show()
                }) {
                    Text("%02d:%02d".format(model.hour.intValue,model.minute.intValue),
                        color=AccentRed,fontWeight=FontWeight.Bold)
                }
            }
            if(!allowed && Build.VERSION.SDK_INT>=33) {
                Text("알림 권한이 꺼져 있어",fontSize=11.sp,color=TextGray)
                TextButton(onClick={launcher.launch(Manifest.permission.POST_NOTIFICATIONS)}) {
                    Text("알림 권한 요청",color=AccentRed)
                }
            }
        }
        WhitePanel {
            Text("기존 체크리스트",fontSize=14.sp,
                fontWeight=FontWeight.Bold,color=TextInk)
            Spacer(Modifier.height(5.dp))
            Text("이전 데이터는 그대로 보존해.",fontSize=11.sp,color=TextGray)
            Spacer(Modifier.height(8.dp))
            TextButton(onClick=onClassic) { Text("열기",color=AccentRed) }
        }
        Text("알림은 절전 상태에서 늦게 도착할 수 있어. AI 목표 생성과 서버 동기화는 준비 중이야.",
            fontSize=11.sp,color=TextGray,lineHeight=17.sp)
    }
}
