package com.questyourlife.app.ui.redesign

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.R
import com.questyourlife.app.ui.ClanMark
import com.questyourlife.app.ui.CosmeticArtwork
import com.questyourlife.app.ui.DressedAvatar

@Composable
fun PatchHeaderActions(onNotifications: () -> Unit, onMenu: () -> Unit) {
    Row(Modifier.width(96.dp), horizontalArrangement = Arrangement.End) {
    IconButton(onClick = onNotifications) {
        Icon(painterResource(R.drawable.ic_patch_header_alarm), "알림", tint = PatchTokens.ink)
    }
    IconButton(onClick = onMenu) {
        Icon(painterResource(R.drawable.ic_patch_header_more), "더보기", tint = PatchTokens.ink)
    }
    }
}

/** Other people's values are explicitly fixtures, never the current user's private ledger. */
@Composable
fun PatchPersonProfilePage(name: String, clanName: String, onBack: () -> Unit,
    onNotifications: () -> Unit, onMenu: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember(context) { context.getSharedPreferences("quest_social_preview", Context.MODE_PRIVATE) }
    var requested by remember(name) { mutableStateOf(prefs.getBoolean("request:$name", false)) }
    val completed = 8 + Math.floorMod(name.hashCode(), 9)
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(painterResource(R.drawable.ic_patch_arrow_left), "뒤로가기") }
            Text("프로필", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
            PatchHeaderActions(onNotifications, onMenu)
        }
        Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DressedAvatar(CosmeticArtwork.demoItem(name), Modifier.size(136.dp), "$name 프로필")
                Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClanMark(Modifier.size(24.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(clanName, style = MaterialTheme.typography.bodyMedium)
                }
                OutlinedButton(onClick = {
                    requested = !requested
                    prefs.edit().putBoolean("request:$name", requested).apply()
                }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(if (requested) "친구 요청 취소" else "친구 추가")
                }
                Text(if (requested) "친구 요청을 이 기기에 저장했어요." else "친구 요청을 시연해 볼 수 있어요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
            LevelProgress(com.questyourlife.app.progression.UserLevel.fromXp(completed * 20L))
            PatchPanel {
                Text("이번 주의 $name", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${completed}개 완료", fontWeight = FontWeight.Bold)
                    Text("4일 실천", color = PatchTokens.muted)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("월", "화", "수", "목", "금", "토", "일").forEachIndexed { index, day ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(painterResource(R.drawable.mascot_face_stamp), null,
                                Modifier.size(26.dp).alpha(if (index in listOf(0, 1, 2, 4)) 1f else .24f))
                            Text(day, fontSize = 10.sp, color = PatchTokens.muted)
                        }
                    }
                }
            }
            PatchPanel {
                Text("모은 도장", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("첫 시작", "꾸준한 일주일", "첫 챕터").forEach { title ->
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.fillMaxWidth().aspectRatio(2f)
                                .background(PatchTokens.soft, RoundedCornerShape(10.dp)))
                            Text(title, fontSize = 11.sp)
                        }
                    }
                }
            }
            Text("시연용 프로필과 기록이에요. 친구 요청은 실제 상대에게 전달되지 않아요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
    }
}
