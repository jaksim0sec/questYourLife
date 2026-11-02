package com.questyourlife.app.ui.redesign

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.ui.DressedAvatar
import com.questyourlife.app.ui.CosmeticArtwork

private data class PreviewAlert(val id: String, val clan: Boolean, val name: String?,
    val title: String, val detail: String, val time: String)

private val previewAlerts = listOf(
    PreviewAlert("preview-chapter", false, "밤산책", "밤산책님이 공동 챕터를\n완료했어요", "느슨한 갓생단 · 첫 주말 도전", "10분 전"),
    PreviewAlert("preview-join", true, "모찌", "모찌님이 클랜에 합류했어요", "느슨한 갓생단", "35분 전"),
    PreviewAlert("preview-chest", true, null, "함께 모은 클랜 상자가\n완성됐어요", "보상을 확인해 보세요", "1시간 전"),
    PreviewAlert("preview-challenge", false, "하루", "하루님이 함께하는 도전에\n참여했어요", "여행 일본어 · 공동 목표", "2시간 전")
)

@Composable
fun NotificationCenter(quests: List<DailyQuestEntity>, onDismiss: () -> Unit, onSettings: () -> Unit,
    onProfile: (String) -> Unit, onClan: () -> Unit, onQuest: (String) -> Unit,
    onAccept: (String) -> Unit, onReject: (String) -> Unit) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    val prefs = remember(context) { context.getSharedPreferences("patch_inbox_read", Context.MODE_PRIVATE) }
    var read by remember { mutableStateOf(prefs.getStringSet("ids", emptySet())!!.toSet()) }
    val markRead: (Set<String>) -> Unit = { ids ->
        read = read + ids
        prefs.edit().putStringSet("ids", read).apply()
    }
    val alerts = previewAlerts.filter { tab == 0 || it.clan == (tab == 2) }
    val allIds = previewAlerts.map { it.id }.toSet() + quests.map { it.id }
    PatchSheet("알림", onDismiss, initialHeight = 540.dp, startExpanded = true,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp), contentSpacing = 8.dp,
        headerActions = {
            IconButton(onClick = onSettings) { Icon(painterResource(R.drawable.ic_patch_settings), "알림 설정", tint = PatchTokens.muted) }
        }, headerContent = {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    listOf("전체", "친구", "클랜").forEachIndexed { index, title ->
                        Column(Modifier.weight(1f)) {
                            TextButton(onClick = { tab = index }, modifier = Modifier.fillMaxWidth()) {
                                Text(title, color = if (tab == index) PatchTokens.ink else PatchTokens.muted,
                                    fontWeight = if (tab == index) FontWeight.Bold else FontWeight.Medium)
                            }
                            Box(Modifier.fillMaxWidth().height(2.dp)
                                .background(if (tab == index) PatchTokens.ink else PatchTokens.line))
                        }
                    }
                }
            }
        }, footer = {
            TextButton(onClick = { markRead(allIds) }, enabled = (allIds - read).isNotEmpty(),
                modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp)) {
                Text(if ((allIds - read).isEmpty()) "모두 읽었어요" else "모두 읽음으로 표시", color = PatchTokens.muted)
            }
        }) {
        Text("오늘", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PatchTokens.muted)
        Column {
            alerts.forEach { alert ->
                Row(Modifier.fillMaxWidth().clickable(role = Role.Button) {
                    markRead(setOf(alert.id))
                    if (alert.name != null) onProfile(alert.name) else onClan()
                }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.width(6.dp), contentAlignment = Alignment.Center) {
                        if (alert.id !in read) Box(Modifier.size(5.dp).background(PatchTokens.red, CircleShape)
                            .semantics { contentDescription = "읽지 않은 알림" })
                    }
                    if (alert.name != null) DressedAvatar(CosmeticArtwork.demoItem(alert.name), Modifier.size(48.dp),
                        "${alert.name}의 알림")
                    else Image(painterResource(R.drawable.box_gold), "공동 상자", Modifier.size(48.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(alert.title, fontSize = 14.sp, lineHeight = 19.sp,
                            fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                        Text(alert.detail, fontSize = 11.sp, lineHeight = 15.sp, color = PatchTokens.muted)
                    }
                    Column(Modifier.align(Alignment.Top).padding(top = 2.dp), horizontalAlignment = Alignment.End) {
                        Text(alert.time, fontSize = 10.sp, color = PatchTokens.muted)
                        if (alert.name == null) Icon(painterResource(R.drawable.ic_patch_chevron_right), null,
                            Modifier.padding(top = 8.dp).size(18.dp), tint = PatchTokens.muted)
                    }
                }
                HorizontalDivider(Modifier.padding(start = 14.dp), color = PatchTokens.line)
            }
        }
        Text("친구·클랜 활동은 시연용 알림이에요.", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        if (tab == 0) {
            QuestInbox(quests, onOpen = { markRead(setOf(it)); onQuest(it) },
                onAccept = { markRead(setOf(it)); onAccept(it) }, onReject = { markRead(setOf(it)); onReject(it) },
                onSettings = null)
        }
    }
}
