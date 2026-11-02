package com.questyourlife.app.ui.redesign

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.companion.ClanPreview
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.R
import com.questyourlife.app.ui.DressedAvatar
import com.questyourlife.app.ui.CosmeticArtwork

/** Measurements belong to this reference surface, not the discarded generic cards. */
private object ClanStyle {
    val paper = Color(0xFFFAF9F6)
    val card = Color.White
    val ink = Color(0xFF151715)
    val muted = Color(0xFF5C5E5A)
    val lime = Color(0xFFD9E779)
    val yellow = Color(0xFFFFDF58)
    val soft = Color(0xFFF0EFEB)
    val line = Color(0xFFD0D0CB)
    val red = Color(0xFFFF3B30)
    val radius = RoundedCornerShape(16.dp)
}

@Composable
fun PatchClanPage(model: DailyQuestViewModel, onProfile: (String) -> Unit) {
    var previous by rememberSaveable { mutableStateOf(false) }
    val clan = remember { model.clanPreview() }
    val contributionWeek = remember(previous) { model.clanPreview(previous) }
    var periodMenu by remember { mutableStateOf(false) }
    var sheet by rememberSaveable { mutableStateOf<String?>(null) }
    var copied by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fontScale = LocalDensity.current.fontScale
    BoxWithConstraints(Modifier.fillMaxSize()) {
    // Preserve the reference rhythm while keeping the rank panel reachable on shorter phones.
    val compact = maxHeight < 650.dp && fontScale <= 1.05f
    val rowHeight = if (compact) 48.dp else 54.dp


    Column(Modifier.fillMaxSize().background(ClanStyle.paper)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp).padding(top = 2.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().heightIn(min = if (compact) 52.dp else 58.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            ClanFlag(Modifier.size(44.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(clan.name, color = ClanStyle.ink, fontSize = 20.sp,
                    lineHeight = 25.sp, fontWeight = FontWeight.Black,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() })
                Text("멤버 ${clan.memberCount}명", color = ClanStyle.muted,
                    fontSize = 12.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = { copied = false; sheet = "invite" },
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ClanStyle.line),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ClanStyle.ink)) {
                Icon(painterResource(R.drawable.ic_patch_invite), null, Modifier.size(19.dp), tint = Color.Unspecified)
                Spacer(Modifier.width(4.dp))
                Text("초대하기", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        ClanChestPanel(clan, compact)
        Column(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.card)
            .clickable(role = Role.Button, onClickLabel = "클랜 멤버 전체 보기") { sheet = "members" }
            .padding(horizontal = 14.dp, vertical = if (compact) 10.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ClanHeading("우리 클랜의 멤버들", Modifier.weight(1f))
                Text("${clan.memberCount}명", color = ClanStyle.muted,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val faceSize = minOf(if (compact) 64.dp else 68.dp,
                    ((maxWidth - 18.dp) / 4).coerceAtLeast(0.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    clan.members.take(4).forEach { member ->
                        ClanFace(Modifier.size(faceSize).clickable(onClickLabel = "${member.name} 프로필 보기") { onProfile(member.name) }, member.name)
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.card)
            .padding(horizontal = 8.dp).padding(top = 4.dp, bottom = 8.dp)) {
            Row(Modifier.fillMaxWidth().padding(start = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                ClanHeading("멤버 기여도", Modifier.weight(1f))
                Box {
                    OutlinedButton(onClick = { periodMenu = true },
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ClanStyle.line),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        modifier = Modifier.height(30.dp).defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)) {
                        Text(if (previous) "지난 주" else "이번 주", fontSize = 12.sp, color = ClanStyle.muted, fontWeight = FontWeight.SemiBold)
                        Icon(Icons.Rounded.KeyboardArrowDown, null, Modifier.size(16.dp), tint = ClanStyle.muted)
                    }
                    DropdownMenu(expanded = periodMenu, onDismissRequest = { periodMenu = false }) {
                        DropdownMenuItem(text = { Text("이번 주") }, onClick = { previous = false; periodMenu = false })
                        DropdownMenuItem(text = { Text("지난 주") }, onClick = { previous = true; periodMenu = false })
                    }
                }
            }
            contributionWeek.members.take(3).forEachIndexed { index, member ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .background(if (member.isSelf) ClanStyle.soft else ClanStyle.card)
                    .heightIn(min = rowHeight).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", Modifier.width(26.dp), fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = ClanStyle.ink)
                    ClanFace(Modifier.size(46.dp).clickable(onClickLabel = "${member.name} 프로필 보기") { onProfile(member.name) }, member.name)
                    Spacer(Modifier.width(12.dp))
                    Text(member.name, Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = ClanStyle.ink)
                    Text("+${member.cash} C", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,
                        color = ClanStyle.ink, maxLines = 1)
                }
            }
        }
        Row(Modifier.fillMaxWidth().clip(ClanStyle.radius).background(ClanStyle.soft)
            .clickable(role = Role.Button) { sheet = "rank" }
            .padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("클랜 주간 순위", fontSize = 12.sp, color = ClanStyle.muted, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${clan.rank}위", fontSize = 26.sp, lineHeight = 32.sp,
                        fontWeight = FontWeight.ExtraBold, color = ClanStyle.ink)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Rounded.ArrowUpward, null, Modifier.size(18.dp), tint = ClanStyle.red)
                    Text("${clan.rise}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ClanStyle.red)
                }
            }
            Text("비슷한 규모의 클랜과\n비교해요", fontSize = 12.sp, lineHeight = 18.sp, color = ClanStyle.muted, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Rounded.ChevronRight, null, Modifier.size(18.dp), tint = ClanStyle.muted)
        }
    }
    } // viewport
    if (sheet != null) {
        PatchSheet(when (sheet) { "invite" -> "클랜 초대"; "members" -> "우리 클랜의 멤버들"; else -> "클랜 주간 순위" }, { sheet = null }) {
            Text("시연용 클랜이에요. 멤버·기여도·순위는 준비된 예시이며 내 캐시에는 반영되지 않아요.",
                style = MaterialTheme.typography.bodyMedium)
            if (sheet == "members") {
                clan.members.take(10).chunked(5).forEach { members ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        members.forEach { member ->
                            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                ClanFace(Modifier.fillMaxWidth().aspectRatio(1f).clickable { sheet = null; onProfile(member.name) }, member.name)
                                Text(member.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                    maxLines = 1, overflow = TextOverflow.Ellipsis, color = ClanStyle.ink)
                            }
                        }
                        repeat(5 - members.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
                PatchButton("확인", { sheet = null })
            } else if (sheet == "invite") {
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
private fun ClanChestPanel(clan: ClanPreview, compact: Boolean) {
    val fontScale = LocalDensity.current.fontScale
    val panelHeight = (if (compact) 170.dp else 178.dp) * fontScale.coerceAtLeast(1f)
    BoxWithConstraints(Modifier.fillMaxWidth().heightIn(min = panelHeight)
        .clip(ClanStyle.radius).background(ClanStyle.ink)) {
        val artWidth = (maxWidth * .38f).coerceAtMost(148.dp)
        val textWidth = maxWidth - artWidth - 20.dp
        val multilineCaption = fontScale > 1.1f || maxWidth < 330.dp
        val wrapProgress = textWidth - 16.dp < 55.dp + 78.dp * fontScale
        val barWidth = (textWidth - 16.dp - 78.dp * fontScale).coerceIn(48.dp, 104.dp)
        Image(painterResource(R.drawable.box_gold), "이번 주 공동 보상 상자",
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 12.dp)
                .width(artWidth).height(130.dp), contentScale = ContentScale.Fit)
        Text("${clan.contributorCount}명 기여 중", Modifier.align(Alignment.TopEnd)
            .padding(top = 12.dp, end = 12.dp).clip(RoundedCornerShape(10.dp))
            .background(ClanStyle.lime).padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, color = ClanStyle.ink)
        Column(Modifier.width(textWidth)
            .padding(start = 16.dp, top = 16.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("각자 해내고,\n같이 열자", fontSize = 25.sp, lineHeight = 31.sp,
                fontWeight = FontWeight.Black, color = Color.White)
            Text("이번 주 공동 상자", fontSize = 12.sp, lineHeight = 18.sp, color = Color(0xFFE2E2DE), fontWeight = FontWeight.Bold)
            if (wrapProgress) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ClanProgress(clan, Modifier.width(barWidth))
                    ClanProgressLabel(clan)
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClanProgress(clan, Modifier.width(barWidth))
                    Spacer(Modifier.width(7.dp))
                    ClanProgressLabel(clan)
                }
            }
            Text("보상  3가지 꾸미기 중 1개 선택", fontSize = 11.sp, lineHeight = 17.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFFF1F1ED), maxLines = if (multilineCaption) 2 else 1)
        }
    }
}

@Composable
private fun ClanProgress(clan: ClanPreview, modifier: Modifier = Modifier) {
    val fraction by androidx.compose.animation.core.animateFloatAsState(
        if (clan.target > 0) (clan.cash.toFloat() / clan.target).coerceIn(0f, 1f) else 0f,
        androidx.compose.animation.core.tween(260), label = "clan progress")
    Box(modifier.height(12.dp).clip(CircleShape).background(Color(0xFF62635F))
        .semantics { progressBarRangeInfo = androidx.compose.ui.semantics.ProgressBarRangeInfo(fraction, 0f..1f) }) {
        if (fraction > 0f) Box(Modifier.fillMaxHeight().fillMaxWidth(fraction)
            .clip(CircleShape).background(ClanStyle.yellow))
    }
}

@Composable
private fun ClanProgressLabel(clan: ClanPreview) {
    Text("${clan.cash} / ${clan.target}", fontSize = 12.sp, lineHeight = 16.sp,
        fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
}

@Composable
private fun ClanFace(modifier: Modifier = Modifier, name: String? = null) {
    DressedAvatar(CosmeticArtwork.demoItem(name.orEmpty()), modifier,
        name?.let { "${it}의 시연 아바타" } ?: "시연 아바타")
}

@Composable
private fun ClanHeading(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier.semantics { heading() }, fontSize = 16.sp, lineHeight = 22.sp,
        fontWeight = FontWeight.Black, color = ClanStyle.ink)
}

@Composable
private fun ClanFlag(modifier: Modifier = Modifier) = com.questyourlife.app.ui.ClanMark(modifier)
