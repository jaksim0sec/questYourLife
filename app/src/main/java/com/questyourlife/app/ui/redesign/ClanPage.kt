package com.questyourlife.app.ui.redesign

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.companion.SocialChestRight
import com.questyourlife.app.companion.SocialClan
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import org.json.JSONObject
import java.util.UUID

/** Guest and signed-in clan views share a single visual hierarchy and truthful values. */
@Composable
fun PatchClanPage(model: DailyQuestViewModel, onProfile: (String) -> Unit, onAccount: () -> Unit) {
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val social by model.social.collectAsStateWithLifecycle()
    val clan = social?.clan
    val rights = social?.chestRights.orEmpty()
    val rankRows = social?.clanRanking.orEmpty()
    val contributions = social?.memberScores.orEmpty().filter { score -> clan?.members?.any { it.id == score.userId } == true }.sortedByDescending { it.score }
    val context = LocalContext.current
    val compact = LocalConfiguration.current.screenHeightDp < 700 && LocalDensity.current.fontScale <= 1.05f
    var sheet by rememberSaveable { mutableStateOf<String?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var joiningId by rememberSaveable { mutableStateOf("") }
    var joiningToken by remember { mutableStateOf("") }
    val invite = model.clanInvitation.value
    LaunchedEffect(signedIn) { if (signedIn) model.refreshSocial() }
    LaunchedEffect(sheet) { if (sheet != "invite") model.clearClanInvitation() }

    Column(Modifier.fillMaxSize().background(PatchTokens.background)
        .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().heightIn(min = if (compact) 52.dp else 58.dp).padding(horizontal = 7.dp),
            verticalAlignment = Alignment.CenterVertically) {
            com.questyourlife.app.ui.ClanMark(Modifier.size(44.dp))
            Column(Modifier.weight(1f).padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(clan?.name ?: "우리만의 클랜", fontSize = 20.sp,
                    fontWeight = FontWeight.Black, color = PatchTokens.ink, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.semantics { heading() })
                Text(if (clan != null) "멤버 ${clan.members.size}명" else "친구들과 함께 목표를 이어가요",
                    fontSize = 12.sp, color = PatchTokens.muted)
            }
            if (clan != null && social?.userId == clan.ownerId) {
                OutlinedButton(onClick = { model.clearClanInvitation(); sheet = "invite" },
                    modifier = Modifier.heightIn(min = 44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)) {
                    QuestGlyph(R.drawable.ic_patch_invite, Modifier.size(19.dp), PatchTokens.ink)
                    Spacer(Modifier.width(4.dp))
                    Text("초대하기", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        ClanLiveChestPanel(clan, contributions.takeIf { it.isNotEmpty() }?.count { it.score > 0 })

        if (!signedIn) {
            ClanLandingCard("클랜을 만들고 함께 도전해요",
                "계정을 연결하면 클랜을 만들거나 초대 코드를 사용할 수 있어요.",
                "계정 연결하기", onAccount)
        } else if (social?.loaded != true && clan == null) {
            ClanLandingCard("클랜 정보를 확인하고 있어요",
                "연결이 안 되면 마지막으로 저장한 정보를 사용해요.",
                "다시 불러오기", model::refreshSocial)
        } else if (clan == null) {
            ClanLandingCard("이제 우리 클랜을 시작할 차례",
                "같은 목표가 아니어도 각자 실천한 기록을 모을 수 있어요.",
                "클랜 만들기", { sheet = "create" })
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .background(PatchTokens.surface).clickable(role = Role.Button) { sheet = "join" }
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                QuestGlyph(R.drawable.ic_patch_invite, Modifier.size(28.dp), PatchTokens.red)
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text("초대받은 클랜에 들어가기", fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    Text("친구가 보내준 클랜 ID와 초대 코드를 입력해요",
                        fontSize = 11.sp, color = PatchTokens.muted)
                }
                QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(20.dp))
            }
        } else {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(PatchTokens.surface)
                .clickable(role = Role.Button) { sheet = "members" }.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("우리 클랜의 멤버들", Modifier.weight(1f), fontWeight = FontWeight.Black,
                        fontSize = 16.sp, color = PatchTokens.ink)
                    Text("${clan.members.size}명", color = PatchTokens.muted, fontSize = 12.sp)
                }
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val face = minOf(if (compact) 64.dp else 68.dp,
                        ((maxWidth - 18.dp) / 4).coerceAtLeast(0.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        clan.members.take(4).forEach { member ->
                            DressedAvatar(member.itemId, Modifier.size(face)
                                .clickable(role = Role.Button) { onProfile(member.id) },
                                "${member.name} 프로필 보기")
                        }
                    }
                }
            }
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(PatchTokens.surface).padding(horizontal = 8.dp, vertical = if (compact) 6.dp else 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(Modifier.fillMaxWidth().padding(start = 6.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("멤버 기여도", Modifier.weight(1f), fontSize = 16.sp,
                        fontWeight = FontWeight.Black, color = PatchTokens.ink)
                    Text("이번 주", fontSize = 12.sp, color = PatchTokens.muted)
                }
                if (contributions.isEmpty()) {
                    Text("이번 주 멤버 기여는 서버 기록 확인 후 표시돼요",
                        Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                        fontSize = 12.sp, color = PatchTokens.muted)
                } else contributions.take(3).forEachIndexed { index, score ->
                    val member = clan.members.firstOrNull { it.id == score.userId }
                    if (member != null) {
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .background(if (member.id == social?.userId) PatchTokens.soft else PatchTokens.surface)
                            .heightIn(min = if (compact) 48.dp else 54.dp)
                            .clickable(role = Role.Button) { onProfile(member.id) }
                            .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text("${index + 1}", Modifier.width(27.dp), fontSize = 16.sp,
                                fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                            DressedAvatar(member.itemId, Modifier.size(46.dp), "${member.name} 아바타")
                            Spacer(Modifier.width(12.dp))
                            Text(member.name, Modifier.weight(1f), fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink,
                                maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("${score.score}점", fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(PatchTokens.soft)
                .clickable(role = Role.Button) { sheet = "rank" }
                .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("클랜 주간 순위", fontSize = 12.sp,
                        color = PatchTokens.muted, fontWeight = FontWeight.Bold)
                    Text(clan.rank?.let { "${it}위" } ?: "집계 전",
                        fontSize = 26.sp, lineHeight = 31.sp,
                        fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                }
                Text("이번 주 서버 기준\n클랜 순위 보기", fontSize = 12.sp,
                    lineHeight = 18.sp, fontWeight = FontWeight.Bold, color = PatchTokens.muted)
                Spacer(Modifier.width(6.dp))
                QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp), PatchTokens.muted)
            }
            TextButton(onClick = { sheet = "leave" }, modifier = Modifier.align(Alignment.End)) {
                Text("클랜 나가기", color = PatchTokens.muted)
            }
        }

        if (rights.isNotEmpty()) {
            Text("지난 주 공동 상자", fontSize = 17.sp, fontWeight = FontWeight.Black,
                color = PatchTokens.ink)
            rights.forEach { right ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                    .background(PatchTokens.surface).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    QuestChest(Modifier.size(68.dp), "클랜 주간 보상")
                    Column(Modifier.weight(1f).padding(start = 10.dp)) {
                        Text(right.clanName, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                        Text("${right.weekStart} 주간 · 서버 보상 ${right.reward} C",
                            fontSize = 11.sp, color = PatchTokens.muted)
                    }
                    TextButton(onClick = {
                        model.socialCommand("clan_chest_claim", right.clanId,
                            JSONObject().put("id", right.clanId).put("week_start", right.weekStart))
                    }) { Text("수령 요청", fontSize = 12.sp, color = PatchTokens.red) }
                }
            }
            Text("수령 요청은 저장 후 서버에서 확인해요. 이미 받은 상자는 중복 지급되지 않아요.",
                fontSize = 11.sp, color = PatchTokens.muted)
        }
        if (signedIn) {
            model.socialError.value?.let { Text(it, color = PatchTokens.red,
                style = MaterialTheme.typography.bodySmall) }
            TextButton(onClick = model::refreshSocial, enabled = !model.socialLoading.value,
                modifier = Modifier.align(Alignment.End)) {
                Text(if (model.socialLoading.value) "새로고침 중" else "정보 새로고침",
                    color = PatchTokens.muted)
            }
        }
    }

    when (sheet) {
        "create" -> PatchSheet("클랜 만들기", { sheet = null },
            footer = { PatchButton("이 이름으로 만들기", {
                val id = UUID.randomUUID().toString()
                model.socialCommand("clan_create", id,
                    JSONObject().put("id", id).put("name", name.trim()))
                name = ""
                sheet = null
            }, enabled = name.trim().isNotEmpty()) }) {
            Text("같이 도전할 클랜의 이름을 정해요",
                style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink)
            Text("클랜당 최대 50명이 함께할 수 있어요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            OutlinedTextField(name, { name = it.take(80) }, label = { Text("클랜 이름") },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
        }
        "join" -> PatchSheet("초대받은 클랜에 참여", { sheet = null },
            footer = { PatchButton("클랜에 참여하기", {
                val id = joiningId.trim()
                model.socialCommand("clan_join", id,
                    JSONObject().put("id", id).put("token", joiningToken.trim()))
                joiningId = ""
                joiningToken = ""
                sheet = null
            }, enabled = validClanId(joiningId.trim()) && joiningToken.trim().length in 32..128) }) {
            Text("친구가 전달한 초대 정보를 입력해요",
                style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink)
            OutlinedTextField(joiningId, { joiningId = it.take(64) }, modifier = Modifier.fillMaxWidth(),
                label = { Text("클랜 ID") }, singleLine = true)
            OutlinedTextField(joiningToken, { joiningToken = it.take(128) }, modifier = Modifier.fillMaxWidth(),
                label = { Text("초대 코드") }, singleLine = true)
            Text("초대 코드는 일회성이며 서버 확인 후 가입이 완료돼요.",
                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
        "invite" -> PatchSheet("클랜 초대", {
            model.clearClanInvitation()
            sheet = null
        }, footer = {
            if (invite == null) PatchButton(if (model.clanInvitationBusy.value) "초대 코드 생성 중" else "초대 코드 만들기", {
                clan?.let { model.requestClanInvitation(it.id) }
            }, enabled = clan != null && !model.clanInvitationBusy.value)
            else PatchButton("클랜 ID와 코드 복사", {
                val value = "${invite.clanId}\n${invite.token}"
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(ClipData.newPlainText("클랜 초대", value))
            })
        }) {
            Text("친구에게 초대 정보를 전달하세요", style = MaterialTheme.typography.titleLarge,
                color = PatchTokens.ink)
            Text("생성한 초대 코드는 이 화면에서 한 번만 확인할 수 있어요.",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
            if (invite != null) {
                Text("클랜 ID", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                Text(invite.clanId, style = MaterialTheme.typography.bodyMedium, color = PatchTokens.ink)
                Text("초대 코드", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                Text(invite.token, style = MaterialTheme.typography.bodyMedium, color = PatchTokens.ink)
                Text("사용 기한: ${invite.expiresAt}", fontSize = 11.sp, color = PatchTokens.muted)
                TextButton(onClick = {
                    val share = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "클랜 ID: ${invite.clanId}\n초대 코드: ${invite.token}")
                    }
                    context.startActivity(Intent.createChooser(share, "클랜 초대 공유"))
                }) { Text("공유하기", color = PatchTokens.red) }
            }
            model.clanInvitationError.value?.let {
                Text(it, color = PatchTokens.red, style = MaterialTheme.typography.bodySmall)
            }
        }
        "members" -> PatchSheet("우리 클랜의 멤버들", { sheet = null },
            startExpanded = true, footer = { PatchButton("확인", { sheet = null }) }) {
            clan?.members.orEmpty().chunked(5).forEach { group ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    group.forEach { member ->
                        Column(Modifier.weight(1f).clickable(role = Role.Button) {
                            sheet = null
                            onProfile(member.id)
                        }, horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            DressedAvatar(member.itemId, Modifier.fillMaxWidth().aspectRatio(1f),
                                "${member.name} 아바타")
                            Text(member.name, fontSize = 11.sp,
                                fontWeight = FontWeight.Bold, maxLines = 1,
                                overflow = TextOverflow.Ellipsis, color = PatchTokens.ink)
                        }
                    }
                    repeat(5 - group.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        "rank" -> PatchSheet("클랜 주간 순위", { sheet = null },
            footer = { PatchButton("확인", { sheet = null }) }) {
            Text(clan?.rank?.let { "이번 주 ${it}위 · 서버 확정 점수 기준" }
                ?: "아직 순위가 집계되지 않았어요",
                style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink)
            Text("동일한 기간의 클랜 기록을 비교해요",
                fontSize = 12.sp, color = PatchTokens.muted)
            if (rankRows.isEmpty()) Text("순위 데이터가 없어요. 연결 상태를 확인해 주세요.",
                color = PatchTokens.muted)
            rankRows.forEach { entry ->
                Row(Modifier.fillMaxWidth().padding(vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("${entry.rank}위", Modifier.width(55.dp),
                        fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
                    Text(entry.name ?: "비공개 클랜", Modifier.weight(1f),
                        color = PatchTokens.ink, maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    Text("${entry.score}점", fontWeight = FontWeight.Bold,
                        color = PatchTokens.ink)
                }
            }
        }
        "leave" -> PatchSheet("클랜 나가기", { sheet = null },
            footer = { PatchButton("클랜 나가기", {
                clan?.let { model.socialCommand("clan_leave", it.id, JSONObject().put("id", it.id)) }
                sheet = null
            }) }) {
            Text("다른 멤버가 있는 클랜의 소유자라면 먼저 소유권을 이전해야 해요.",
                color = PatchTokens.ink, style = MaterialTheme.typography.bodyLarge)
            Text("진행 기록과 지난 주 정당한 상자 권리는 보존돼요.",
                color = PatchTokens.muted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun validClanId(value: String): Boolean =
    runCatching { UUID.fromString(value).toString().equals(value, ignoreCase = true) }.getOrDefault(false)

@Composable
private fun ClanLandingCard(title: String, detail: String, cta: String, onClick: () -> Unit) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
        .background(PatchTokens.surface).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = PatchTokens.ink)
        Text(detail, fontSize = 13.sp, color = PatchTokens.muted)
        QuestAction(cta, onClick)
    }
}

@Composable
private fun ClanLiveChestPanel(clan: SocialClan?, contributorCount: Int?) {
    val scale = LocalDensity.current.fontScale.coerceAtLeast(1f)
    val target = clan?.target
    val score = clan?.weeklyScore
    BoxWithConstraints(Modifier.fillMaxWidth().heightIn(min = 178.dp * scale)
        .clip(RoundedCornerShape(16.dp)).background(Color(0xFF191F19))) {
        val artWidth = (maxWidth * .38f).coerceAtMost(148.dp)
        val textWidth = maxWidth - artWidth - 20.dp
        val narrow = textWidth < 188.dp || scale > 1.1f
        Image(painterResource(R.drawable.box_gold), "클랜 공동 상자",
            Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 12.dp)
                .width(artWidth).height(130.dp), contentScale = ContentScale.Fit)
        contributorCount?.let { count ->
            Text("${count}명 기여 중", Modifier.align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
                .clip(RoundedCornerShape(10.dp)).background(Color(0xFFD9E779))
                .padding(horizontal = 10.dp, vertical = 6.dp),
                fontSize = 12.sp, lineHeight = 16.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFF151715))
        }
        Column(Modifier.width(textWidth).padding(start = 16.dp, top = 16.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("각자 해내고,\n같이 열자",
                fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Black,
                color = Color.White)
            Text("이번 주 공동 상자", fontSize = 12.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFFE2E2DE))
            if (score != null && target != null && target > 0) {
                val fraction = (score.toFloat() / target.toFloat()).coerceIn(0f, 1f)
                if (narrow) Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ClanReferenceProgress(fraction, Modifier.width((textWidth - 16.dp).coerceAtLeast(48.dp)))
                    Text("${score} / ${target}", fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                } else Row(verticalAlignment = Alignment.CenterVertically) {
                    ClanReferenceProgress(fraction,
                        Modifier.width((textWidth - 96.dp).coerceAtLeast(48.dp)))
                    Spacer(Modifier.width(8.dp))
                    Text("${score} / ${target}", fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else Text(if (clan == null) "함께 시작할 클랜을 찾아보세요" else "서버 주간 기록을 확인 중이에요",
                fontSize = 11.sp, color = Color(0xFFE2E2DE), maxLines = 2)
            Text("완료한 퀘스트를 함께 모아요", fontSize = 11.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFFE2E2DE),
                maxLines = if (narrow) 2 else 1)
        }
    }
}

@Composable
private fun ClanReferenceProgress(fraction: Float, modifier: Modifier = Modifier) {
    val value by androidx.compose.animation.core.animateFloatAsState(
        fraction, androidx.compose.animation.core.tween(260), label = "clan progress")
    Box(modifier.height(12.dp).clip(CircleShape).background(Color(0xFF62635F))) {
        if (value > 0f) Box(Modifier.fillMaxHeight().fillMaxWidth(value)
            .clip(CircleShape).background(Color(0xFFFFDF58)))
    }
}
