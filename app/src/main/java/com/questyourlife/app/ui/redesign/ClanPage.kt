package com.questyourlife.app.ui.redesign

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.companion.ClanPreview
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.MascotArt
import kotlin.math.cos
import kotlin.math.sin

/** Measurements belong to this reference surface, not the discarded generic cards. */
private object ClanStyle {
    val paper = Color(0xFFFAF9F6)
    val card = Color.White
    val ink = Color(0xFF151715)
    val muted = Color(0xFF777976)
    val lime = Color(0xFFD9E779)
    val yellow = Color(0xFFFFDF58)
    val soft = Color(0xFFF0EFEB)
    val line = Color(0xFFD0D0CB)
    val red = Color(0xFFFF3B30)
    val radius = RoundedCornerShape(16.dp)
}

@Composable
fun PatchClanPage(model: DailyQuestViewModel) {
    var previous by rememberSaveable { mutableStateOf(false) }
    val clan = remember { model.clanPreview() }
    val contributionWeek = remember(previous) { model.clanPreview(previous) }
    var periodMenu by remember { mutableStateOf(false) }
    var sheet by rememberSaveable { mutableStateOf<String?>(null) }
    var copied by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().background(ClanStyle.paper)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp).padding(top = 2.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().heightIn(min = 58.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            ClanFlag(Modifier.size(44.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(clan.name, color = ClanStyle.ink, fontSize = 20.sp,
                    lineHeight = 25.sp, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() })
                Text("멤버 ${clan.memberCount}명 · 예시", color = ClanStyle.muted,
                    fontSize = 12.sp, lineHeight = 18.sp)
            }
            OutlinedButton(onClick = { copied = false; sheet = "invite" },
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                modifier = Modifier.heightIn(min = 40.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ClanStyle.line),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ClanStyle.ink)) {
                Icon(Icons.Rounded.PersonAdd, null, Modifier.size(19.dp))
                Spacer(Modifier.width(4.dp))
                Text("초대하기", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        ClanChestPanel(clan)
        Column(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.card)
            .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ClanHeading("이번 주 함께 쌓은 기록")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                repeat(4) { ClanFace(Modifier.size(58.dp)) }
            }
        }
        Column(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.card)
            .padding(horizontal = 8.dp).padding(top = 8.dp, bottom = 8.dp)) {
            Row(Modifier.fillMaxWidth().padding(start = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                ClanHeading("멤버 기여도", Modifier.weight(1f))
                Box {
                    OutlinedButton(onClick = { periodMenu = true },
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ClanStyle.line),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        modifier = Modifier.heightIn(min = 40.dp)) {
                        Text(if (previous) "지난 주" else "이번 주", fontSize = 12.sp, color = ClanStyle.muted)
                        Icon(Icons.Rounded.KeyboardArrowDown, null, Modifier.size(16.dp), tint = ClanStyle.muted)
                    }
                    DropdownMenu(expanded = periodMenu, onDismissRequest = { periodMenu = false }) {
                        DropdownMenuItem(text = { Text("이번 주") }, onClick = { previous = false; periodMenu = false })
                        DropdownMenuItem(text = { Text("지난 주") }, onClick = { previous = true; periodMenu = false })
                    }
                }
            }
            contributionWeek.members.forEachIndexed { index, member ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .background(if (member.isSelf) ClanStyle.soft else ClanStyle.card)
                    .heightIn(min = 52.dp).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", Modifier.width(26.dp), fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = ClanStyle.ink)
                    ClanFace(Modifier.size(42.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(member.name, Modifier.weight(1f), fontSize = 15.sp, color = ClanStyle.ink)
                    Text("+${member.cash} C", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = ClanStyle.ink, maxLines = 1)
                }
            }
        }
        Row(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.soft)
            .clickable(role = Role.Button) { sheet = "rank" }
            .padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("클랜 주간 순위", fontSize = 12.sp, color = ClanStyle.muted)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${clan.rank}위", fontSize = 26.sp, lineHeight = 32.sp,
                        fontWeight = FontWeight.ExtraBold, color = ClanStyle.ink)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Rounded.ArrowUpward, null, Modifier.size(18.dp), tint = ClanStyle.red)
                    Text("${clan.rise}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ClanStyle.red)
                }
            }
            Text("비슷한 규모의 클랜과\n비교해요", fontSize = 12.sp, lineHeight = 18.sp, color = ClanStyle.muted)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Rounded.ChevronRight, null, Modifier.size(18.dp), tint = ClanStyle.muted)
        }
    }
    if (sheet != null) {
        PatchSheet(if (sheet == "invite") "클랜 초대" else "클랜 주간 순위", { sheet = null }) {
            Text("시연용 클랜이에요. 멤버·기여도·순위는 준비된 예시이며 내 캐시에는 반영되지 않아요.",
                style = MaterialTheme.typography.bodyMedium)
            if (sheet == "invite") {
                Text("테스트용 초대 코드", style = MaterialTheme.typography.labelLarge)
                Text("PATCH-DEMO", style = MaterialTheme.typography.titleLarge)
                Text("실제 가입 링크는 서버 연결 후 제공돼요.", style = MaterialTheme.typography.bodyMedium)
                PatchButton(if (copied) "코드를 복사했어요" else "예시 코드 복사", {
                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                        .setPrimaryClip(ClipData.newPlainText("클랜 예시 코드", "PATCH-DEMO"))
                    copied = true
                })
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "갓생패치 클랜 예시 코드: PATCH-DEMO. 실제 가입 기능은 아직 연결되지 않았어요.")
                    }
                    context.startActivity(Intent.createChooser(intent, "클랜 예시 코드 공유"))
                }) { Text("예시 코드 공유") }
            } else {
                Text("이번 주 ${clan.rank}위 · ${clan.rise}단계 상승",
                    style = MaterialTheme.typography.titleLarge)
                Text("동일한 규모의 클랜을 비교하는 화면의 예시예요. 온라인 순위는 아직 연결되지 않았어요.")
                model.nearbyClans.forEach { entry ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("${entry.rank}위", Modifier.width(48.dp), fontWeight = FontWeight.Bold)
                        Text(entry.name, Modifier.weight(1f))
                        Text("${entry.cash} C", fontWeight = FontWeight.SemiBold)
                    }
                }
                PatchButton("확인", { sheet = null })
            }
        }
    }
}

@Composable
private fun ClanChestPanel(clan: ClanPreview) {
    BoxWithConstraints(Modifier.fillMaxWidth().heightIn(min = 174.dp).clip(ClanStyle.radius).background(ClanStyle.ink)) {
        // Empty art slot is intentional: the user deferred the golden chest asset.
        val artWidth = (maxWidth * .37f).coerceAtMost(142.dp)
        val textWidth = maxWidth - 30.dp - artWidth
        Spacer(Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 8.dp)
            .width(artWidth).height(126.dp))
        Text("${clan.contributorCount}명 기여 중", Modifier.align(Alignment.TopEnd)
            .padding(top = 12.dp, end = 12.dp).clip(RoundedCornerShape(10.dp))
            .background(ClanStyle.lime).padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ClanStyle.ink)
        Column(Modifier.width(textWidth + 16.dp).padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("각자 해내고,\n같이 열자", fontSize = 25.sp, lineHeight = 31.sp,
                fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text("이번 주 공동 상자", fontSize = 12.sp, color = Color(0xFFDEDEDA))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(progress = { (clan.cash.toFloat() / clan.target).coerceIn(0f, 1f) },
                    modifier = Modifier.width(100.dp).height(12.dp).clip(CircleShape),
                    color = ClanStyle.yellow, trackColor = Color(0xFF62635F), gapSize = 0.dp,
                    drawStopIndicator = {})
                Spacer(Modifier.width(6.dp))
                Text("${clan.cash} / ${clan.target}", fontSize = 11.sp, color = Color.White, maxLines = 1)
            }
            Text("보상  3가지 꾸미기 중 1개 선택", fontSize = 10.sp, lineHeight = 17.sp,
                color = Color(0xFFDEDEDA), maxLines = 1)
        }
    }
}

@Composable
private fun ClanFace(modifier: Modifier = Modifier) {
    Box(modifier.clip(CircleShape).background(ClanStyle.paper), contentAlignment = Alignment.Center) {
        MascotArt(Modifier.fillMaxSize().padding(3.dp))
    }
}

@Composable
private fun ClanHeading(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier.semantics { heading() }, fontSize = 16.sp, lineHeight = 22.sp,
        fontWeight = FontWeight.Bold, color = ClanStyle.ink)
}

@Composable
private fun ClanFlag(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width; val h = size.height
        drawLine(Color(0xFF53533D), Offset(w * .12f, h * .06f), Offset(w * .12f, h * .96f), w * .055f, StrokeCap.Round)
        val flag = Path().apply {
            moveTo(w * .17f, h * .08f); lineTo(w * .95f, h * .14f)
            lineTo(w * .95f, h * .80f); lineTo(w * .17f, h * .73f); close()
        }
        drawPath(flag, ClanStyle.lime)
        repeat(6) { n ->
            val a = n * Math.PI / 3
            drawLine(ClanStyle.ink, Offset(w * .55f, h * .44f),
                Offset(w * .55f + cos(a).toFloat() * w * .19f,
                    h * .44f + sin(a).toFloat() * h * .19f),
                w * .09f, StrokeCap.Round)
        }
    }
}
