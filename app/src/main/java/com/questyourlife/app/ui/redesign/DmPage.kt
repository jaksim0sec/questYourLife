package com.questyourlife.app.ui.redesign

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.data.CompanionMessageEntity
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import com.questyourlife.app.ui.CosmeticArtwork
import com.questyourlife.app.ui.MascotArt
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Both the assistant and the demo conversations are stored by the local repository. */
@Composable
fun PatchDmPage(model: DailyQuestViewModel, onOpenThread: (String) -> Unit) {
    DmConversations(model, onOpenThread)
}

@Composable
fun PatchThreadPage(model: DailyQuestViewModel, threadId: String, onBack: () -> Unit) {
    LaunchedEffect(threadId) {
        if (model.activeThread.value != threadId) model.openThread(threadId)
    }
    DmConversation(model, threadId, onBack)
}

@Composable
private fun DmConversations(model: DailyQuestViewModel, onOpenThread: (String) -> Unit) {
    var search by rememberSaveable { mutableStateOf("") }
    val query = search.trim()
    val contacts = model.contacts.filter {
        it.id != "mascot" && (query.isEmpty() || it.name.contains(query, ignoreCase = true) ||
            it.preview.contains(query, ignoreCase = true))
    }
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
                            DmLabel("로컬 도우미")
                        }
                        Text("목표를 작은 실천으로 나눠 봐요", color = PatchTokens.muted,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Rounded.PushPin, contentDescription = "고정된 대화",
                        modifier = Modifier.size(18.dp), tint = PatchTokens.muted)
                }
            }
        }
        item {
            Text("친구 대화 · 데모", modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
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
                DmFriendAvatar(contact.name)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(contact.name, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = PatchTokens.ink)
                    Text(contact.preview, style = MaterialTheme.typography.bodyMedium,
                        color = PatchTokens.muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                DmLabel("데모")
            }
            HorizontalDivider(color = PatchTokens.line)
        }
        if (contacts.isEmpty()) {
            item {
                Text(if (query.isEmpty()) "아직 데모 친구가 없어요." else "검색 결과가 없어요.",
                    Modifier.padding(vertical = 24.dp), color = PatchTokens.muted,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            Text("실제 친구에게 전송되지 않는 데모 대화예요. 보낸 메시지는 이 기기에 저장돼요.",
                Modifier.padding(vertical = 12.dp), color = PatchTokens.muted,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DmConversation(model: DailyQuestViewModel, threadId: String, onBack: () -> Unit) {
    val allMessages by model.chatMessages.collectAsStateWithLifecycle()
    val busy by model.chatBusy.collectAsStateWithLifecycle()
    val messages = allMessages.filter { it.threadId == threadId }
    val mascot = threadId == "mascot"
    val name = if (mascot) "마스코트" else model.contacts.firstOrNull { it.id == threadId }?.name ?: "데모 친구"
    val listState = rememberLazyListState()
    val keyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    var draft by rememberSaveable(threadId) { mutableStateOf("") }
    val canSend = draft.isNotBlank() && !busy
    val send = {
        if (draft.isNotBlank() && !busy) {
            model.sendMessage(draft.trim())
            draft = ""
        }
    }

    LaunchedEffect(threadId, messages.lastOrNull()?.id, busy, keyboardOpen) {
        // The list includes the local-service notice and an optional busy row.
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size + if (busy) 1 else 0)
    }

    Column(Modifier.fillMaxSize().imePadding()) {
        Row(
            Modifier.fillMaxWidth().padding(start = 4.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "대화 목록으로",
                    tint = PatchTokens.ink)
            }
            if (mascot) MascotArt(Modifier.size(36.dp)) else DmFriendAvatar(name, compact = true)
            Text(name, modifier = Modifier.weight(1f).semantics { heading() },
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = PatchTokens.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            DmLabel(if (mascot) "로컬" else "데모")
        }
        HorizontalDivider(color = PatchTokens.line)
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "local-notice") {
                Text(
                    if (mascot) "준비된 로컬 제안으로 계획을 도와요. AI 서비스에 연결하지 않으며, 대화는 이 기기에 저장돼요."
                    else "실제 친구에게 전송되지 않는 데모 대화예요. 보낸 메시지는 이 기기에 저장돼요.",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted
                )
            }
            if (messages.isEmpty()) {
                item(key = "empty-conversation") {
                    Text(if (mascot) "이루고 싶은 목표를 말해 주세요.\n예: 하루 10분씩 책 읽기"
                        else "가볍게 인사를 건네 보세요.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
                }
            }
            items(messages, key = { it.id }) { message ->
                Box(Modifier.animateItem()) {
                    DmMessage(message, mascot, name, busy) { model.applyChatPlan(message.id) }
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
        HorizontalDivider(color = PatchTokens.line)
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it.take(2000) },
                modifier = Modifier.weight(1f).animateContentSize().heightIn(min = 48.dp),
                placeholder = { Text(if (mascot) "목표나 고민을 말해 주세요" else "메시지 보내기") },
                minLines = 1,
                maxLines = 3,
                shape = RoundedCornerShape(24.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { send() }),
                colors = dmFieldColors(),
                trailingIcon = if (draft.isNotBlank()) ({ FilledIconButton(
                onClick = send,
                enabled = canSend,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = PatchTokens.red,
                    contentColor = PatchTokens.surface,
                    disabledContainerColor = PatchTokens.line,
                    disabledContentColor = PatchTokens.muted
                )
            ) {
                Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "메시지 보내기",
                    modifier = Modifier.size(21.dp))
            } }) else null
            )
        }
    }
}

@Composable
private fun DmMessage(
    message: CompanionMessageEntity,
    mascot: Boolean,
    name: String,
    busy: Boolean,
    onApply: () -> Unit
) {
    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top) {
        if (!message.fromUser) {
            if (mascot) MascotArt(Modifier.size(28.dp)) else DmFriendAvatar(name, compact = true)
            Spacer(Modifier.width(8.dp))
        }
        Column(Modifier.widthIn(max = 320.dp).fillMaxWidth(.86f),
            horizontalAlignment = if (message.fromUser) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (!message.fromUser) Text(name, style = MaterialTheme.typography.labelMedium,
                color = PatchTokens.muted, fontWeight = FontWeight.SemiBold)
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (message.fromUser) PatchTokens.line.copy(alpha = .55f) else PatchTokens.surface,
                contentColor = PatchTokens.ink
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(message.body, style = MaterialTheme.typography.bodyMedium)
                    if (!message.planTitle.isNullOrBlank() && !message.planSteps.isNullOrBlank()) {
                        HorizontalDivider(color = PatchTokens.line)
                        Text("계획 초안 · 로컬 제안", color = PatchTokens.red,
                            style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text(message.planTitle, style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold)
                        message.planSteps.lineSequence().filter { it.isNotBlank() }.forEachIndexed { index, step ->
                            Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                        }
                        val applied = message.appliedGoalId != null
                        PatchButton(
                            text = if (applied) "퀘스트에 추가했어요 ✓" else "이 계획으로 시작",
                            onClick = onApply,
                            enabled = !applied && !busy
                        )
                        if (applied) {
                            Text("퀘스트 탭에서 확인할 수 있어요.",
                                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                        }
                    }
                }
            }
            if (message.createdAt > 0L) {
                Text(dmTimestamp(message.createdAt), style = MaterialTheme.typography.labelSmall,
                    color = PatchTokens.muted, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

@Composable
private fun DmLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelSmall, color = PatchTokens.muted,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .background(PatchTokens.line.copy(alpha = .5f))
            .padding(horizontal = 7.dp, vertical = 4.dp))
}

@Composable
private fun DmFriendAvatar(name: String, compact: Boolean = false) {
    DressedAvatar(CosmeticArtwork.demoItem(name), Modifier.size(if (compact) 32.dp else 48.dp), "${name}의 시연 아바타")
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
    DateTimeFormatter.ofPattern("M월 d일 a h:mm", Locale.KOREAN)
        .withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(createdAt))
