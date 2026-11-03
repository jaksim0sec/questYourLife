package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.data.QuestDatabase
import com.questyourlife.app.sync.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun PatchAccountPage(onBack: () -> Unit, onAuthenticated: () -> Unit = {}, initialSignup: Boolean = true) {
    val context=LocalContext.current
    val account=remember { AccountRepository.get(context) }
    val session by account.state.collectAsStateWithLifecycle()
    val db=remember { QuestDatabase.get(context) }
    val meta by db.syncDao().observeMeta().collectAsStateWithLifecycle(null)
    val syncStatus by db.syncDao().observeRecord("sync_status","transport").collectAsStateWithLifecycle(null)
    val pending by db.syncDao().observePendingCount().collectAsStateWithLifecycle(0)
    val pendingCash by db.syncDao().observePendingCash().collectAsStateWithLifecycle(0L)
    val problems by db.syncDao().observeProblems().collectAsStateWithLifecycle(emptyList())
    val localGoals by db.goalDao().watchGoals().collectAsStateWithLifecycle(emptyList())
    val localQuests by db.questDao().observeAll().collectAsStateWithLifecycle(emptyList())
    val localHistory by db.dailyQuestDao().observeHistory().collectAsStateWithLifecycle(emptyList())
    val hasGuestRecords = meta?.userId == null && (localGoals.any { it.id != "example-japanese" } || localQuests.isNotEmpty() || localHistory.any { it.accepted || it.completedAt != null || it.goalId != null })
    var importGuest by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") } // Never saved to Bundle/Room/preferences.
    var signup by remember(initialSignup) { mutableStateOf(initialSignup) }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    val scope=rememberCoroutineScope()
    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState())
        .padding(horizontal=20.dp,vertical=12.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Row {
            IconButton(onClick=onBack) { Icon(painterResource(R.drawable.ic_patch_arrow_left),"뒤로가기",tint=PatchTokens.ink) }
            Text(if(session==null) "계정 연결" else "계정과 동기화",style=MaterialTheme.typography.headlineSmall,modifier=Modifier.padding(top=8.dp),color=PatchTokens.ink)
        }
        if(session==null) {
            Text(if(signup) "두 가지만 입력하면 시작할 수 있어요" else "내 기록으로 돌아오기",style=MaterialTheme.typography.titleLarge,color=PatchTokens.ink)
            OutlinedTextField(name,{ name=it },Modifier.fillMaxWidth(),label={ Text("아이디") },
                supportingText={ Text("영문·숫자·밑줄 3~24자") },singleLine=true,enabled=!busy,
                keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Ascii))
            OutlinedTextField(password,{ password=it },Modifier.fillMaxWidth(),label={ Text("비밀번호") },
                supportingText={ Text("8~128자") },singleLine=true,enabled=!busy,visualTransformation=PasswordVisualTransformation(),
                keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password))
            if (hasGuestRecords && signup) {
                Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked=importGuest,onCheckedChange={importGuest=it},enabled=!busy)
                    Column {
                        Text("이 기기 기록도 가져오기",style=MaterialTheme.typography.titleSmall,color=PatchTokens.ink)
                        Text("선택하면 새 계정에 연결해요. 기존 계정으로 로그인할 때는 기록을 자동으로 합치지 않아요.",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
                    }
                }
            }
            if (hasGuestRecords && !signup) Text("기기 기록은 그대로 남아요. 로그인하는 계정과 자동으로 합치지 않아요.",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
            PatchButton(if(busy) "연결 중…" else if(signup) "회원가입" else "로그인",enabled=!busy&&AccountInput.username(name)!=null&&AccountInput.validPassword(password),onClick={
                scope.launch {
                    busy=true; message=null
                    try { account.signIn(name,password,signup,importGuest = signup && importGuest); password=""; onAuthenticated() }
                    catch(e: CancellationException) { throw e }
                    catch(e: Exception) { message=when {
                        e is ApiFailure && e.status==429 -> "잠시 후 다시 시도해 주세요."
                        e is ApiFailure && e.status==404 -> "계정 서버 준비가 아직 완료되지 않았어요."
                        e is ApiFailure && e.status in setOf(400,401,403) -> "아이디와 비밀번호를 확인해 주세요. 가입 중이라면 다른 아이디를 사용해 보세요."
                        else -> "연결하지 못했어요. 인터넷 연결을 확인하고 다시 시도해 주세요."
                    } }
                    finally { busy=false }
                }
            })
            TextButton(onClick={signup=!signup;importGuest=false;message=null},enabled=!busy) { Text(if(signup) "이미 계정이 있어요 · 로그인" else "새 계정 만들기") }
            Text("기존 기록은 그대로 유지돼요. 서버 확정 캐쉬는 별도로 시작해요. 이메일을 등록하지 않으므로 아이디와 비밀번호를 잊지 않도록 보관해 주세요.",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
            TextButton(onClick=onBack) { Text("지금은 오프라인으로 사용하기",color=PatchTokens.muted) }
        } else {
            Text(session!!.username,style=MaterialTheme.typography.titleLarge,color=PatchTokens.ink)
            Text("서버 확정 ${meta?.confirmedCash ?: 0} C · ${meta?.confirmedXp ?: 0} XP",color=PatchTokens.ink)
            val status=syncStatus?.body?.let { org.json.JSONObject(it).optString("state") }
            Text(when(status) { "running"->"동기화 중";"idle"->"마지막 동기화 완료";"login_required"->"세션이 만료됐어요. 로그아웃 후 다시 로그인해 주세요.";"server_unavailable"->"서버 연결을 확인해 주세요.";"offline"->"연결되면 다시 동기화해요.";"protocol_error"->"동기화를 확인해야 해요. 기기 기록은 보존되어 있어요.";else->"계정에 연결했어요" },color=PatchTokens.muted)
            Text("확인 대기 $pendingCash C · 전송 대기 ${pending}건",color=PatchTokens.muted)
            Text("실천 보상은 건당 20 C, 한국 시각 하루 최대 100 C예요. 대기 금액은 서버 확인 후 달라질 수 있어요.",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
            Text("기존 기기 캐쉬 ${meta?.legacyCash ?: 0} C · 이전 구매 기록 보존",color=PatchTokens.muted)
            PatchButton("지금 동기화",{SyncScheduler.enqueue(context);message="연결 가능한 상태에서 동기화를 진행해요."})
            problems.forEach { problem ->
                PatchSurface {
                    Text(if(problem.state=="conflict") "다른 기기의 변경과 충돌했어요" else "서버에서 확인하지 못한 작업이 있어요",style=MaterialTheme.typography.titleSmall)
                    Text("기기 기록은 보존되어 있어요. ${problem.operation}",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
                    Row {
                        TextButton(onClick={scope.launch { SyncResolution.retryLocal(db,problem);SyncScheduler.enqueue(context) }}) {Text("내 변경 다시 적용")}
                        TextButton(onClick={scope.launch {SyncResolution.acceptRemote(db,problem);SyncScheduler.enqueue(context)}}) {Text("서버 내용 사용")}
                    }
                }
            }
            TextButton(onClick={scope.launch {account.signOut()}}) {Text("로그아웃",color=PatchTokens.red)}
            Text("로그아웃해도 기존 계정의 기록은 삭제하지 않아요. 다른 계정의 기록과 구분해서 보관해요.",style=MaterialTheme.typography.bodySmall,color=PatchTokens.muted)
        }
        if(message!=null) Text(message!!,color=PatchTokens.red,style=MaterialTheme.typography.bodyMedium)
    }
}
