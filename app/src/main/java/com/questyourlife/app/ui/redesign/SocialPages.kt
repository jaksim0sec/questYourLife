package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.companion.*
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.sync.AccountRepository
import com.questyourlife.app.sync.ProfileRegistry
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

/** Shared by surfaces whose existing navigation signature doesn't pass the ViewModel. */
internal class SocialAccess(val store: SocialStore, val snapshot: SocialSnapshot?, val signedIn: Boolean,
    val error: String?, val problems: Int, val run: (suspend () -> Unit) -> Unit)

@Composable
internal fun rememberSocialAccess(): SocialAccess {
    val context = LocalContext.current.applicationContext
    val file by ProfileRegistry.get(context).file.collectAsStateWithLifecycle()
    val store = remember(context, file) { SocialStore(context) }
    val account by AccountRepository.get(context).state.collectAsStateWithLifecycle()
    val snapshot by remember(store) { store.snapshot }.collectAsStateWithLifecycle(initialValue = null)
    val problems by remember(store) { store.problems }.collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    var error by remember(store) { mutableStateOf<String?>(null) }
    val run: (suspend () -> Unit) -> Unit = { action -> scope.launch {
        try { action(); error = null }
        catch (e: CancellationException) { throw e }
        catch (_: Exception) { error = "연결하지 못했어요. 저장된 내용을 표시해요. 다시 시도해 주세요." }
    }; Unit }
    LaunchedEffect(store, account?.userId) { if (account != null) run { store.refresh() } }
    return SocialAccess(store, snapshot, account != null, error, problems.size, run)
}

@Composable
internal fun SignedInClanPage(model: DailyQuestViewModel, onProfile: (String) -> Unit) {
    val snapshot by model.social.collectAsStateWithLifecycle()
    val clan = snapshot?.clan
    var name by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var clanId by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { model.refreshSocial() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(clan?.name ?: "우리 클랜", Modifier.weight(1f), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = model::refreshSocial, enabled = !model.socialLoading.value) { Text("새로고침") }
        }
        model.socialError.value?.let { Text(it, color = PatchTokens.muted) }
        SocialRequestProblems(model)
        if (snapshot?.loaded != true) Text("클랜 정보를 불러오는 중이에요. 연결 상태를 확인해 주세요.", color = PatchTokens.muted)
        else if (clan == null) {
            PatchPanel {
                Text("아직 참여한 클랜이 없어요.", fontWeight = FontWeight.Bold)
                OutlinedTextField(name, { name = it.take(80) }, label = { Text("새 클랜 이름") }, modifier = Modifier.fillMaxWidth())
                PatchButton("클랜 만들기", {
                    val id = UUID.randomUUID().toString()
                    model.socialCommand("clan_create", id, JSONObject().put("id", id).put("name", name.trim()))
                }, enabled = name.isNotBlank())
            }
            PatchPanel {
                OutlinedTextField(clanId, { clanId = it }, label = { Text("초대받은 클랜 ID") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(token, { token = it }, label = { Text("초대 코드") }, modifier = Modifier.fillMaxWidth())
                PatchButton("초대로 참여하기", {
                    model.socialCommand("clan_join", clanId.trim(), JSONObject().put("id", clanId.trim()).put("token", token.trim()))
                }, enabled = token.isNotBlank() && validSocialUuid(clanId.trim()))
            }
        } else {
            PatchPanel {
                Text("각자 해내고, 같이 한 걸음", fontSize = 25.sp, fontWeight = FontWeight.Black)
                Text("멤버 ${clan.members.size}명", color = PatchTokens.muted)
                clan.weeklyScore?.let { Text("이번 주 기여도 $it", fontWeight = FontWeight.Bold) }
                clan.rank?.let { Text("클랜 주간 순위 ${it}위") }
                Text("기여도는 한국 시간 월요일부터의 서버 확정 완료 기록을 기준으로 해요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
            PatchPanel {
                Text("우리 클랜의 멤버들", fontWeight = FontWeight.Bold)
                clan.members.forEach { member ->
                    Row(Modifier.fillMaxWidth().clickable { onProfile(member.id) }.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        DressedAvatar(member.itemId, Modifier.size(52.dp), "${member.name} 아바타")
                        Text(member.name, Modifier.padding(start = 12.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
            TextButton(onClick = { model.socialCommand("clan_leave", clan.id, JSONObject().put("id", clan.id)) }) {
                Text("클랜 나가기")
            }
        }
        Text("요청은 기기에 저장한 뒤 서버에서 확인해요. 확정된 변경은 새로고침하면 표시돼요.",
            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
    }
}

@Composable
internal fun SocialProfileContent(access: SocialAccess, profileId: String) {
    LaunchedEffect(access.store, profileId) { access.run { access.store.fetchProfile(profileId) } }
    val profile = access.snapshot?.profiles?.firstOrNull { it.id == profileId }
    var requested by remember(profileId) { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (profile == null) {
            Text("공개 프로필을 볼 수 없어요.", color = PatchTokens.muted)
            Text("공개 여부와 계정 연결 상태를 확인해 주세요.", color = PatchTokens.muted)
        } else {
            DressedAvatar(profile.itemId, Modifier.size(136.dp), "${profile.name} 아바타")
            Text(profile.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            access.snapshot?.clans?.firstOrNull { clan -> clan.members.any { it.id == profile.id } }?.let { Text(it.name) }
            if (profile.id != access.snapshot?.userId) {
                val direct = access.snapshot?.chats?.firstOrNull { it.type == "direct" && it.peerId == profile.id }
                Button(onClick = { access.run {
                    val id = direct?.id ?: UUID.randomUUID().toString()
                    access.store.command(if (direct == null) "chat_open_direct" else "friend_request", id,
                        JSONObject().put("id", id).put("user_id", profile.id))
                    access.store.schedule()
                    requested = true
                } }) { Text(if (direct == null) "대화 만들기" else "친구 요청 보내기") }
                if (requested) Text("요청을 기기에 저장했어요. 연결되면 서버에서 확인해요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
            Text("상대가 공개한 프로필만 표시해요.", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
        access.error?.let { Text(it, color = PatchTokens.red) }
        if (access.problems > 0) Text("확인하지 못한 요청이 있어요. 계정의 동기화 상태를 확인해 주세요.", color = PatchTokens.red)
        TextButton(onClick = { access.run { access.store.refresh() } }) { Text("새로고침") }
    }
}

private fun validSocialUuid(value: String) = runCatching { UUID.fromString(value).toString() == value.lowercase() }.getOrDefault(false)

@Composable
internal fun SocialNewChat(model: DailyQuestViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    TextButton(onClick = { expanded = !expanded }) { Text("새 대화") }
    if (expanded) {
        val ids = users.split(',').map { it.trim() }.filter { it.isNotBlank() }.distinct()
        OutlinedTextField(users, { users = it }, label = { Text("상대 계정 ID · 여러 명은 쉼표로 구분") },
            modifier = Modifier.fillMaxWidth())
        if (ids.size > 1) OutlinedTextField(name, { name = it.take(80) }, label = { Text("그룹 이름") },
            modifier = Modifier.fillMaxWidth())
        PatchButton("대화 만들기", {
            val id = UUID.randomUUID().toString()
            val data = JSONObject().put("id", id)
            if (ids.size == 1) data.put("user_id", ids.single())
            else data.put("user_ids", org.json.JSONArray(ids)).put("name", name.trim())
            model.socialCommand(if (ids.size == 1) "chat_open_direct" else "chat_create_group", id, data)
            expanded = false
        }, enabled = ids.isNotEmpty() && ids.size <= 49 && ids.all(::validSocialUuid) && (ids.size == 1 || name.isNotBlank()))
    }
}

@Composable
internal fun SocialNotificationCenter(access: SocialAccess, quests: List<DailyQuestEntity>,
    onDismiss: () -> Unit, onSettings: () -> Unit, onProfile: (String) -> Unit, onClan: () -> Unit,
    onQuest: (String) -> Unit, onAccept: (String) -> Unit, onReject: (String) -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    val all = access.snapshot?.alarms.orEmpty()
    val alerts = all.filter { tab == 0 || it.type.startsWith("clan") == (tab == 2) }
    PatchSheet("알림", onDismiss, initialHeight = 540.dp, startExpanded = true,
        footer = { TextButton(onClick = { access.run { access.store.markRead(all.filter { !it.read }.map { it.id }.toSet()) } },
            enabled = all.any { !it.read }, modifier = Modifier.fillMaxWidth()) { Text("모두 읽음으로 표시") } }) {
        Row(Modifier.fillMaxWidth()) {
            listOf("전체", "친구", "클랜").forEachIndexed { index, title ->
                TextButton(onClick = { tab = index }, modifier = Modifier.weight(1f)) {
                    Text(title, color = if (tab == index) PatchTokens.ink else PatchTokens.muted)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { access.run { access.store.refresh() } }) { Text("새로고침") }
            TextButton(onClick = onSettings) { Text("알림 설정") }
        }
        access.error?.let { Text(it, color = PatchTokens.red) }
        if (access.problems > 0) Text("읽음 상태 등 확인하지 못한 요청이 있어요. 계정의 동기화 상태를 확인해 주세요.", color = PatchTokens.red)
        if (access.snapshot?.loaded != true) Text("저장된 알림을 불러오는 중이에요.", color = PatchTokens.muted)
        else if (alerts.isEmpty()) Text("새 알림이 없어요.", color = PatchTokens.muted)
        alerts.forEach { alarm ->
            Row(Modifier.fillMaxWidth().clickable {
                access.run { access.store.markRead(setOf(alarm.id)) }
                when {
                    alarm.questId != null -> onQuest(alarm.questId)
                    alarm.profileId != null -> onProfile(alarm.profileId)
                    alarm.type.startsWith("clan") -> onClan()
                }
            }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!alarm.read) Text("●", color = PatchTokens.red, style = MaterialTheme.typography.labelSmall)
                val profile = access.snapshot?.profiles?.firstOrNull { it.id == alarm.profileId }
                DressedAvatar(profile?.itemId ?: 0, Modifier.size(48.dp), profile?.name ?: "알림")
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(alarm.title, fontWeight = FontWeight.Bold)
                    if (alarm.detail.isNotBlank()) Text(alarm.detail, style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                }
            }
            HorizontalDivider(color = PatchTokens.line)
        }
        if (tab == 0) QuestInbox(quests, onOpen = onQuest, onAccept = onAccept, onReject = onReject, onSettings = null)
    }
}

@Composable
internal fun SocialHomePage(model: DailyQuestViewModel, onDaily: (DailyQuestEntity) -> Unit,
    onQuests: () -> Unit, onClan: () -> Unit, onMy: () -> Unit) {
    val social by model.social.collectAsStateWithLifecycle()
    val profile by model.ownProfile.collectAsStateWithLifecycle()
    val balance by model.cash.collectAsStateWithLifecycle()
    val goals by model.goals.collectAsStateWithLifecycle()
    val daily by model.quests.collectAsStateWithLifecycle()
    val clan = social?.clan
    val today = daily.firstOrNull { it.completedAt == null && !it.dismissed } ?: daily.firstOrNull()
    LaunchedEffect(Unit) { model.refreshSocial() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f).clickable(onClick = onMy), verticalAlignment = Alignment.CenterVertically) {
                DressedAvatar(model.equipped.intValue, Modifier.size(42.dp), "내 아바타")
                Column(Modifier.padding(start = 8.dp)) {
                    Text(profile?.name ?: "나", fontWeight = FontWeight.ExtraBold)
                    Text(clan?.name ?: "참여한 클랜 없음", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                }
            }
            PatchCash(balance, compact = true)
        }
        Box(Modifier.fillMaxWidth().heightIn(min = 190.dp)) {
            Text("오늘의 공략,\n같이 한 걸음", Modifier.align(Alignment.TopStart), fontSize = 30.sp,
                lineHeight = 35.sp, fontWeight = FontWeight.Black, color = PatchTokens.ink)
            QuestChest(Modifier.align(Alignment.BottomEnd).size(142.dp), "클랜 공동 상자")
        }
        Row(Modifier.fillMaxWidth().clickable(onClick = onClan).padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(clan?.name ?: "우리 클랜 만나기", fontWeight = FontWeight.Bold)
                Text(clan?.weeklyScore?.let { "이번 주 기여도 $it" } ?: "완료 기록으로 함께 도전해요.",
                    style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
            }
            Text("보기", color = PatchTokens.red)
        }
        Column(Modifier.fillMaxWidth().background(QuestNight, RoundedCornerShape(16.dp))
            .clickable(onClick = onQuests).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("내 미션 이어하기", color = QuestNightInk, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(goals.firstOrNull()?.title ?: "새 목표를 시작해 보세요", color = QuestNightInk,
                style = MaterialTheme.typography.bodySmall)
        }
        if (social?.popularBooks?.isNotEmpty() == true) {
            Text("지금 많이 도전하는 퀘스트", fontWeight = FontWeight.Bold)
            social?.popularBooks.orEmpty().take(3).forEach { book ->
                Row(Modifier.fillMaxWidth().clickable(onClick = onQuests).padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(book.name, fontWeight = FontWeight.Bold)
                        Text("${book.participants}명 도전 중", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                    }
                    Text("보기", color = PatchTokens.red)
                }
            }
        }
        PatchPanel {
            Text("오늘의 데일리", fontWeight = FontWeight.Bold)
            Text(today?.title ?: "오늘의 미션을 확인해 보세요", color = PatchTokens.muted)
            TextButton(onClick = { if (today == null) onQuests() else onDaily(today) }) { Text("확인하기") }
        }
        TextButton(onClick = model::refreshSocial, enabled = !model.socialLoading.value) { Text("새로고침") }
        model.socialError.value?.let { Text(it, color = PatchTokens.muted, style = MaterialTheme.typography.bodySmall) }
        SocialRequestProblems(model)
    }
}

@Composable
internal fun SocialRequestProblems(model: DailyQuestViewModel) {
    val problems by model.socialProblems.collectAsStateWithLifecycle()
    if (problems.isNotEmpty()) Text("서버에서 확인하지 못한 요청 ${problems.size}개가 있어요. 계정의 동기화 상태에서 확인해 주세요.",
        style = MaterialTheme.typography.bodySmall, color = PatchTokens.red)
}
