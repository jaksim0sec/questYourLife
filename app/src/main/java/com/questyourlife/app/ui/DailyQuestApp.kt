package com.questyourlife.app.ui

import android.Manifest
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FactCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Paper = Color(0xFFF8F5EF)
private val Ink = Color(0xFF292522)
private val Muted = Color(0xFF8C847E)
private val Stamp = Color(0xFFD94B48)
private val Pink = Color(0xFFF9E7E4)
private val Line = Color(0xFFEAE4DD)
private val Gold = Color(0xFFE4A642)

private fun notificationsAllowed(context: Context): Boolean {
    val system = context.getSystemService(NotificationManager::class.java)
    return (Build.VERSION.SDK_INT < 33 ||
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) &&
        system.areNotificationsEnabled()
}

@Composable
fun DailyQuestApp(model: DailyQuestViewModel, onOpenClassicChecklist: () -> Unit) {
    val quests by model.quests.collectAsStateWithLifecycle()
    val coins by model.coins.collectAsStateWithLifecycle()
    val completed by model.completedCount.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableStateOf(0) }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Stamp, background = Paper, surface = Color.White),
    ) {
        Scaffold(
            containerColor = Paper,
            bottomBar = {
                NavigationBar(containerColor = Paper, tonalElevation = 0.dp) {
                    val titles = listOf("홈", "퀘스트", "기록", "설정")
                    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.FactCheck,
                        Icons.Outlined.CheckCircleOutline, Icons.Outlined.Settings)
                    titles.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = tab == index, onClick = { tab = index },
                            icon = { Icon(icons[index], contentDescription = title) },
                            label = { Text(title, fontSize = 11.sp) },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 22.dp, end = 22.dp, top = 22.dp, bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (tab) {
                    0 -> {
                        item {
                            TopTitle(coins)
                            Spacer(Modifier.height(24.dp))
                            Hero()
                        }
                        item { ProgressOverview(quests) }
                        item { SectionHeading("오늘의 일일 퀘스트", "${quests.count { it.completedAt != null }} / ${quests.size}") }
                        if (quests.isEmpty()) {
                            item { SimpleCard { Text("오늘의 제안을 준비하고 있어", color = Muted) } }
                        } else {
                            items(quests, key = { it.id }) { quest ->
                                QuestRow(quest, model::accept, model::complete)
                            }
                        }
                        item {
                            SimpleCard {
                                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                                    Text("메인 퀘스트", color = Stamp, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("큰 목표를 작은 단계로", color = Ink, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                                    Text("AI 목표 설계 기능은 다음 버전에서 만날 수 있어", color = Muted, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    1 -> {
                        item {
                            PageTitle("퀘스트", "제안에서 완료까지, 오늘의 한 걸음")
                            SectionHeading("오늘의 제안", "${quests.count { !it.accepted }}개 대기")
                        }
                        items(quests, key = { it.id }) { quest ->
                            QuestRow(quest, model::accept, model::complete)
                        }
                        item {
                            Text("완료는 직접 체크해야 반영돼. 제안은 하루에 한 번 갱신돼.",
                                fontSize = 12.sp, color = Muted)
                        }
                    }
                    2 -> {
                        item { PageTitle("기록", "쌓인 작은 성취를 확인해") }
                        item {
                            SimpleCard {
                                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                                    Text("지금까지의 기록", fontSize = 14.sp, color = Muted)
                                    Text("$completed개 완료", fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold, color = Ink)
                                    HorizontalDivider(color = Line)
                                    Text("획득한 캐시  $coins C", fontSize = 17.sp,
                                        color = Stamp, fontWeight = FontWeight.SemiBold)
                                    Text("이 기록은 기기에만 저장돼. 서버 랭킹과 교환 기능은 아직 없어.",
                                        fontSize = 12.sp, color = Muted)
                                }
                            }
                        }
                    }
                    else -> {
                        item { PageTitle("설정", "내 일상에 맞게 알림을 조절해") }
                        item { ReminderSettings(model) }
                        item {
                            SimpleCard {
                                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                                    Text("기존 체크리스트", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                                    Text("전에 사용하던 로컬 퀘스트도 그대로 남아 있어.",
                                        fontSize = 13.sp, color = Muted)
                                    OutlinedButton(onClick = onOpenClassicChecklist) { Text("체크리스트 열기") }
                                }
                            }
                        }
                        item {
                            Text("예약 알림은 절전 상태에서 늦어질 수 있어. 알림을 꺼도 퀘스트 기록은 유지돼.",
                                fontSize = 12.sp, color = Muted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopTitle(coins: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text("현생패치", fontSize = 25.sp, fontWeight = FontWeight.Black, color = Ink)
        Spacer(Modifier.weight(1f))
        Surface(shape = RoundedCornerShape(30.dp), color = Color(0xFFF1EAE0)) {
            Text("●  $coins C", modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                color = Gold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Hero() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)),
                color = Muted, fontSize = 12.sp)
            Text("오늘도,\n한 걸음 더", fontSize = 31.sp, lineHeight = 37.sp,
                fontWeight = FontWeight.Black, color = Ink)
            Text("작은 수행이 모여\n일상이 패치돼.", fontSize = 13.sp,
                lineHeight = 20.sp, color = Muted)
        }
        Image(
            painter = painterResource(R.drawable.launcher_stamp),
            contentDescription = "빨간 도장 마스코트",
            modifier = Modifier.size(132.dp).clip(RoundedCornerShape(20.dp))
        )
    }
}

@Composable
private fun ProgressOverview(quests: List<DailyQuestEntity>) {
    val done = quests.count { it.completedAt != null }
    val progress = if (quests.isEmpty()) 0f else done.toFloat() / quests.size.toFloat()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Pink)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("DAILY QUEST", color = Stamp, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("오늘의 진행 상황", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.weight(1f))
                Text("$done / ${quests.size}", color = Stamp, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(7.dp)
                .clip(RoundedCornerShape(50)), color = Stamp, trackColor = Color(0xFFF0CECA))
            Text("마음에 드는 제안을 수락하고 직접 완료해 봐",
                color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SectionHeading(title: String, detail: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.weight(1f))
        Text(detail, fontSize = 13.sp, color = Muted)
    }
}

@Composable
private fun PageTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontWeight = FontWeight.Black, fontSize = 27.sp, color = Ink)
        Text(subtitle, color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SimpleCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) { content() }
    }
}

@Composable
private fun QuestRow(
    quest: DailyQuestEntity,
    onAccept: (String) -> Unit,
    onComplete: (String) -> Unit
) {
    val done = quest.completedAt != null
    SimpleCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(50))
                    .background(if (done) Stamp else if (quest.accepted) Pink else Paper),
                contentAlignment = Alignment.Center
            ) {
                Text(if (done) "✓" else if (quest.accepted) "◉" else "+",
                    color = if (done) Color.White else Stamp,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp, end = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(quest.title, color = Ink, fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(if (done) "완료했어" else if (quest.accepted) "진행 중" else quest.detail,
                    color = Muted, fontSize = 11.sp, maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text("+${quest.reward} C", color = Gold, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold)
            }
            if (done) {
                Text("완료", color = Stamp, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            } else {
                Button(
                    onClick = { if (quest.accepted) onComplete(quest.id) else onAccept(quest.id) },
                    modifier = Modifier.height(39.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (quest.accepted) Stamp else Pink,
                        contentColor = if (quest.accepted) Color.White else Stamp
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp)
                ) { Text(if (quest.accepted) "완료" else "수락", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun ReminderSettings(model: DailyQuestViewModel) {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(notificationsAllowed(context)) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted = notificationsAllowed(context) }

    SimpleCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("매일 퀘스트 제안", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("알림 예약", fontWeight = FontWeight.SemiBold, color = Ink)
                    Text("설정한 시각 이후 오늘의 제안 알림", fontSize = 12.sp, color = Muted)
                }
                Switch(checked = model.reminders.value, onCheckedChange = model::setReminders)
            }
            HorizontalDivider(color = Line)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("제안 시각", fontWeight = FontWeight.SemiBold, color = Ink)
                    Text("기본 오전 7시", color = Muted, fontSize = 12.sp)
                }
                OutlinedButton(onClick = {
                    TimePickerDialog(context, { _, hour, minute ->
                        model.setTime(hour, minute)
                    }, model.hour.intValue, model.minute.intValue, true).show()
                }) {
                    Text("%02d:%02d".format(model.hour.intValue, model.minute.intValue))
                }
            }
            if (!granted && Build.VERSION.SDK_INT >= 33) {
                Text("알림 권한이 꺼져 있어. 예약한 퀘스트가 알림창에는 나타나지 않아.",
                    color = Muted, fontSize = 12.sp)
                Button(onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                    colors = ButtonDefaults.buttonColors(containerColor = Stamp)) {
                    Text("알림 권한 허용")
                }
            } else if (granted) {
                Text("알림 표시 가능 · Android 절전 상태에 따라 시각이 늦어질 수 있어.",
                    color = Muted, fontSize = 12.sp)
            }
        }
    }
}
