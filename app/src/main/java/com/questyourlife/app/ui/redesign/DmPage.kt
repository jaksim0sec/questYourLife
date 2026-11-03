package com.questyourlife.app.ui.redesign

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.data.CompanionMessageEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import com.questyourlife.app.ui.CosmeticArtwork
import com.questyourlife.app.ui.MascotArt
import com.questyourlife.app.ui.LocalPatchDark
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Both the assistant and the demo conversations are stored by the local repository. */
@Composable
fun PatchDmPage(model: DailyQuestViewModel, onOpenThread: (String) -> Unit, onProfile: (String) -> Unit) {
    DmConversations(model, onOpenThread, onProfile)
}

/**
 * 시안 기준: 뒤로가기·아바타·이름·더보기 헤더, 중앙 저장 안내와 날짜,
 * 좌우 말풍선, 하단 + / 회색 입력창 / 원형 위쪽 화살표. 구분선은 쓰지 않는다.
 * 텍스트 저장과 로컬 계획 적용은 기존 저장소를 사용하며 파일 첨부는 준비 중이다.
 * 기기에서의 키보드·줄바꿈·색상·간격 비교는 별도 확인이 필요하다.
 */
@Composable
fun PatchThreadPage(model: DailyQuestViewModel, threadId: String, onBack: () -> Unit,
    onProfile: (String) -> Unit, onMenu: () -> Unit) {
    LaunchedEffect(threadId) {
        if (model.activeThread.value != threadId) model.openThread(threadId)
    }
    DmConversation(model, threadId, onBack, onProfile, onMenu)
}

@Composable
private fun DmConversations(model: DailyQuestViewModel, onOpenThread: (String) -> Unit, onProfile: (String) -> Unit) {
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val social by model.social.collectAsStateWithLifecycle()
    var search by rememberSaveable { mutableStateOf("") }
    val query = search.trim()
    val contacts = (if (signedIn) social?.chats.orEmpty().filter { it.type != "ai" }.map {
        com.questyourlife.app.data.DemoContact(it.id, it.name, it.preview, 0)
    } else model.contacts).filter {
        it.id != "mascot" && (query.isEmpty() || it.name.contains(query, ignoreCase = true) ||
            it.preview.contains(query, ignoreCase = true))
    }
    LaunchedEffect(signedIn) { if (signedIn) model.refreshSocial() }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("검색해보세요") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = if (search.isNotEmpty()) {
                    { IconButton(onClick = { search = "" }) {
                        Icon(Icons.Rounded.Close, contentDescription = "검색어 지우기")
                    } }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = dmFieldColors()
            )
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
        item(key = "pinned-mascot") {
            Column {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenThread("mascot") }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MascotArt(Modifier.size(52.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("마스코트", style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                            DmLabel(if (signedIn) "비공개 채널" else "로컬 도우미")
                        }
                        Text(if (signedIn) "메시지를 저장해요 · AI 답변은 준비 중" else "목표를 작은 실천으로 나눠 봐요", color = PatchTokens.muted,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Rounded.PushPin, contentDescription = "고정된 대화",
                        modifier = Modifier.size(18.dp), tint = PatchTokens.muted)
                }
            }
        }
        item {
            Text("친구 대화", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                style = MaterialTheme.typography.labelLarge, color = PatchTokens.muted)
        }
        items(contacts, key = { it.id }) { contact ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .clickable { onOpenThread(contact.id) }
                    .padding(vertical = 14.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (signedIn) {
                    val chat = social?.chats?.firstOrNull { it.id == contact.id }
                    DressedAvatar(chat?.itemId ?: 0, Modifier.size(48.dp).clickable(enabled = chat?.peerId != null) {
                        chat?.peerId?.let(onProfile)
                    }, "${contact.name} 아바타")
                } else DmFriendAvatar(contact.name, onProfile = onProfile)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(contact.name, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    Text(contact.preview, style = MaterialTheme.typography.bodyMedium,
                        color = PatchTokens.muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        if (contacts.isEmpty()) {
            item {
                Text(if (query.isEmpty()) "아직 대화가 없어요." else "검색 결과가 없어요.",
                    Modifier.padding(vertical = 24.dp), color = PatchTokens.muted,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            if (signedIn) {
                SocialNewChat(model)
                TextButton(onClick = model::refreshSocial, enabled = !model.socialLoading.value) { Text("새로고침") }
                model.socialError.value?.let { Text(it, color = PatchTokens.muted) }
                SocialRequestProblems(model)
            }
            Text(if (signedIn) "보낸 메시지는 기기에 저장한 뒤 연결되면 전송해요. AI 응답 기능은 아직 연결하지 않았어요."
                else "실제 친구에게 전송되지 않는 데모 대화예요. 보낸 메시지는 이 기기에 저장돼요.",
                Modifier.padding(vertical = 12.dp), color = PatchTokens.muted,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DmConversation(model: DailyQuestViewModel, threadId: String, onBack: () -> Unit,
    onProfile: (String) -> Unit, onMenu: () -> Unit) {
    val allMessages by model.chatMessages.collectAsStateWithLifecycle()
    val busy by model.chatBusy.collectAsStateWithLifecycle()
    val activeThread by model.activeThread.collectAsStateWithLifecycle()
    val signedIn by model.signedIn.collectAsStateWithLifecycle()
    val social by model.social.collectAsStateWithLifecycle()
    val messages = allMessages.filter { it.threadId == threadId }
    val mascot = threadId == "mascot"
    val remoteChat = social?.chats?.firstOrNull { it.id == threadId }
    val name = if (mascot) "마스코트" else if (signedIn) remoteChat?.name ?: "대화"
        else model.contacts.firstOrNull { it.id == threadId }?.name ?: "데모 친구"
    val avatarProfile: (String) -> Unit = if (signedIn) ({ _ -> remoteChat?.peerId?.let(onProfile) }) else onProfile
    val listState = rememberLazyListState()
    val keyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    var lastSeenMessage by rememberSaveable(threadId) { mutableStateOf<String?>(null) }
    var draft by rememberSaveable(threadId) { mutableStateOf("") }
    val channelReady = !signedIn || (if (mascot) social?.chats?.any { it.type == "ai" } == true else remoteChat != null)
    val canSend = draft.isNotBlank() && !busy && activeThread == threadId && channelReady
    var showAttachmentInfo by remember { mutableStateOf(false) }
    var showLocalInfo by remember { mutableStateOf(false) }
    val conversationColor = if (LocalPatchDark.current) PatchTokens.background else Color(0xFFF8F8F7)
    val inputColor = if (LocalPatchDark.current) PatchTokens.soft else Color(0xFFECECEA)
    val dayCount = messages.map { dmDate(it.createdAt) }.distinct().size
    val lastItem = (messages.size + dayCount + if (busy) 1 else 0).coerceAtLeast(0)
    val send = {
        if (canSend) {
            model.sendMessage(draft.trim()) { draft = "" }
        }
    }

    LaunchedEffect(threadId, messages.lastOrNull()?.id, busy, keyboardOpen) {
        // Include the notice, date separators and optional busy row in the scroll target.
        val lastId = messages.lastOrNull()?.id
        if (lastId != null && (lastId != lastSeenMessage || busy || keyboardOpen)) {
            listState.animateScrollToItem(lastItem)
        }
        if (lastId != null) lastSeenMessage = lastId
    }

    Column(Modifier.fillMaxSize().background(conversationColor).imePadding()) {
        Row(
            Modifier.fillMaxWidth().padding(start = 4.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "대화 목록으로",
                    tint = PatchTokens.ink)
            }
            if (mascot) MascotArt(Modifier.size(36.dp))
            else if (signedIn) DressedAvatar(remoteChat?.itemId ?: 0, Modifier.size(36.dp).clickable(enabled = remoteChat?.peerId != null) {
                remoteChat?.peerId?.let(onProfile)
            }, "$name 아바타")
            else DmFriendAvatar(name, compact = true, onProfile = onProfile)
            Text(name, modifier = Modifier.weight(1f).padding(start = 8.dp).semantics { heading() },
                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                color = PatchTokens.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            IconButton(onClick = onMenu) {
                Icon(Icons.Rounded.MoreVert, "대화 메뉴", tint = PatchTokens.ink)
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(key = "local-notice") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(if (signedIn) "비공개 대화 · 연결되면 동기화" else if (mascot) "로컬 도우미 · 기기에 저장" else "데모 대화 · 기기에 저장",
                        modifier = Modifier.clip(RoundedCornerShape(50))
                            .background(inputColor).clickable { showLocalInfo = true }
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                }
            }
            if (signedIn && model.hasOlderMessages.value) {
                item(key = "older-messages") { TextButton(onClick = model::loadOlderMessages) { Text("이전 메시지 더 보기") } }
            }
            if (messages.isEmpty()) {
                item(key = "empty-conversation") {
                    Text(if (signedIn && mascot) "메시지는 비공개로 저장돼요.\nAI 답변은 아직 지원하지 않아요."
                        else if (mascot) "이루고 싶은 목표를 말해 주세요.\n예: 하루 10분씩 책 읽기"
                        else "가볍게 인사를 건네 보세요.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
                }
            }
            messages.forEachIndexed { index, message ->
                val day = dmDate(message.createdAt)
                if (index == 0 || day != dmDate(messages[index - 1].createdAt)) {
                    item(key = "day-$day") {
                        Text(day.format(DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                    }
                }
                item(key = message.id) {
                    Box(Modifier.animateItem()) {
                        DmMessage(message, mascot, name, busy, avatarProfile,
                            remoteItem = if (signedIn) remoteChat?.itemId ?: 0 else null) { model.applyChatPlan(message.id) }
                    }
                }
            }
            if (busy) {
                item(key = "busy") {
                    Text("준비하고 있어요…", modifier = Modifier.padding(start = 40.dp),
                        color = PatchTokens.muted, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        model.chatError.value?.let { error ->
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(error, modifier = Modifier.weight(1f), color = PatchTokens.red,
                    style = MaterialTheme.typography.bodySmall)
                IconButton(onClick = { model.chatError.value = null }) {
                    Icon(Icons.Rounded.Close, contentDescription = "오류 안내 닫기", tint = PatchTokens.muted)
                }
            }
        }
        if (signedIn) {
            SocialRequestProblems(model)
            model.chatNotice.value?.let { Text(it, Modifier.padding(horizontal = 16.dp), color = PatchTokens.muted,
                style = MaterialTheme.typography.bodySmall) }
            TextButton(onClick = { model.refreshSocial(); model.openThread(threadId) }, enabled = !busy) { Text("새로고침") }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = { showAttachmentInfo = true }, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Rounded.Add, "파일 첨부 · 준비 중", Modifier.size(28.dp), tint = PatchTokens.ink)
            }
            androidx.compose.foundation.text.BasicTextField(
                value = draft, onValueChange = { draft = it.take(2000) },
                modifier = Modifier.weight(1f).animateContentSize()
                    .clip(RoundedCornerShape(26.dp)).background(inputColor)
                    .heightIn(min = 48.dp).padding(horizontal = 16.dp, vertical = 12.dp),
                maxLines = 4,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = PatchTokens.ink),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(PatchTokens.ink),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { send() }),
                decorationBox = { input ->
                    Box {
                        if (draft.isEmpty()) Text("메시지 보내기", style = MaterialTheme.typography.bodyLarge,
                            color = PatchTokens.muted)
                        input()
                    }
                })
            IconButton(onClick = send, enabled = canSend,
                modifier = Modifier.size(48.dp).padding(2.dp)
                    .background(MaterialTheme.colorScheme.inverseSurface, CircleShape)) {
                Icon(Icons.Rounded.ArrowUpward, "메시지 보내기", Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = if (canSend) 1f else .6f))
            }
        }
    }
    if (showAttachmentInfo || showLocalInfo) {
        AlertDialog(onDismissRequest = { showAttachmentInfo = false; showLocalInfo = false },
            title = { Text(if (showAttachmentInfo) "파일 첨부" else if (signedIn) "비공개 대화" else "이 기기에 저장되는 대화") },
            text = { Text(if (showAttachmentInfo) "파일 첨부는 아직 지원하지 않아요. 텍스트 메시지를 보낼 수 있어요."
                else if (signedIn) "대화 참여자만 서버에 저장된 메시지를 볼 수 있어요. 전송은 연결되면 처리되며, 실패한 요청은 계정의 동기화 상태에서 확인해 주세요. AI 응답은 아직 지원하지 않아요."
                else if (mascot) "준비된 로컬 제안으로 계획을 도와요. AI 서비스에 연결하지 않으며, 대화는 이 기기에 저장돼요."
                else "실제 친구에게 전송되지 않는 데모 대화예요. 보낸 메시지는 이 기기에 저장돼요.") },
            confirmButton = { TextButton(onClick = { showAttachmentInfo = false; showLocalInfo = false }) { Text("확인") } })
    }
}

@Composable
private fun DmMessage(
    message: CompanionMessageEntity,
    mascot: Boolean,
    name: String,
    busy: Boolean,
    onProfile: (String) -> Unit,
    remoteItem: Int? = null,
    onApply: () -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        // Reserve the timestamp at the current font scale so long bubbles cannot squeeze it.
        val timeWidth = with(LocalDensity.current) { 64.sp.toDp() }
        val bubbleWidth = (maxWidth - timeWidth - if (message.fromUser) 0.dp else 48.dp).coerceAtLeast(1.dp)
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom) {
            if (!message.fromUser) {
                Box(Modifier.align(Alignment.Top)) {
                    if (mascot) MascotArt(Modifier.size(40.dp))
                    else if (remoteItem != null) DressedAvatar(remoteItem, Modifier.size(40.dp).clickable { onProfile(name) }, "$name 아바타")
                    else DmFriendAvatar(name, compact = true, onProfile = onProfile)
                }
                Spacer(Modifier.width(8.dp))
            }
            if (message.fromUser && message.createdAt > 0L) DmMessageTime(message.createdAt, Modifier.width(timeWidth))
            Surface(
                modifier = Modifier.widthIn(max = bubbleWidth),
                shape = RoundedCornerShape(18.dp),
                color = if (message.fromUser) MaterialTheme.colorScheme.inverseSurface else PatchTokens.surface,
                contentColor = if (message.fromUser) MaterialTheme.colorScheme.inverseOnSurface else PatchTokens.ink,
                border = if (message.fromUser) null else BorderStroke(1.dp,
                    if (LocalPatchDark.current) PatchTokens.soft else Color(0xFFEAEAE8))
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(message.body, style = MaterialTheme.typography.bodyLarge)
                    if (!message.planTitle.isNullOrBlank()) {
                        HorizontalDivider(color = PatchTokens.line)
                        Text(if (remoteItem != null) "계획 초안 · 확인 후 적용" else "계획 초안 · 로컬 제안", color = PatchTokens.red,
                            style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text(message.planTitle, style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold)
                        message.planSteps.orEmpty().lineSequence().filter { it.isNotBlank() }.forEachIndexed { index, step ->
                            Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                        }
                        val applied = message.appliedGoalId != null
                        if (!message.planSteps.isNullOrBlank()) PatchButton(text = if (applied) "퀘스트에 추가했어요 ✓" else "이 계획으로 시작",
                            onClick = onApply, enabled = !applied && !busy)
                        else Text("구조화된 단계가 없어 이 초안은 아직 적용할 수 없어요.",
                            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                        if (applied) Text("퀘스트 탭에서 확인할 수 있어요.",
                            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                    }
                }
            }
            if (!message.fromUser && message.createdAt > 0L) DmMessageTime(message.createdAt, Modifier.width(timeWidth))
        }
    }
}

@Composable
private fun DmMessageTime(createdAt: Long, modifier: Modifier) {
    Text(dmTimestamp(createdAt), style = MaterialTheme.typography.labelSmall,
        color = PatchTokens.muted, modifier = modifier.padding(horizontal = 6.dp, vertical = 2.dp))
}

@Composable
private fun DmLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelSmall, color = PatchTokens.muted,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .background(PatchTokens.line.copy(alpha = .5f))
            .padding(horizontal = 7.dp, vertical = 4.dp))
}

@Composable
private fun DmFriendAvatar(name: String, compact: Boolean = false, onProfile: (String) -> Unit) {
    DressedAvatar(CosmeticArtwork.demoItem(name), Modifier.size(if (compact) 40.dp else 48.dp).clickable(onClickLabel = "$name 프로필 보기") { onProfile(name) }, "${name}의 시연 아바타")
}

@Composable
private fun dmFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = PatchTokens.ink,
    unfocusedTextColor = PatchTokens.ink,
    focusedContainerColor = PatchTokens.surface,
    unfocusedContainerColor = PatchTokens.surface,
    focusedBorderColor = PatchTokens.red,
    unfocusedBorderColor = PatchTokens.line,
    cursorColor = PatchTokens.red,
    focusedPlaceholderColor = PatchTokens.muted,
    unfocusedPlaceholderColor = PatchTokens.muted
)

private fun dmTimestamp(createdAt: Long): String =
    DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN)
        .withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(createdAt))

private fun dmDate(createdAt: Long) =
    Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
