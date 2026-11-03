package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.questyourlife.app.R
import com.questyourlife.app.ui.MascotArt

/**
 * First-run steps are UI-only until the user submits the goal in the existing create route.
 * Re-entry after process death restores the unfinished step and draft.
 */
@Composable
fun PatchOnboardingPage(onFinished: (String) -> Unit) {
    var stage by rememberSaveable { mutableStateOf("welcome") }
    var draft by rememberSaveable { mutableStateOf("") }
    var createAccount by rememberSaveable { mutableStateOf(true) }

    PatchTheme {
        if (stage == "account") {
            Box(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
                PatchAccountPage(
                    onBack = { stage = "connect" },
                    onAuthenticated = { onFinished(draft.trim()) },
                    initialSignup = createAccount
                )
            }
        } else {
            Column(
                Modifier.fillMaxSize()
                    .background(PatchTokens.background)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium, color = PatchTokens.ink)
                    Spacer(Modifier.weight(1f))
                    Text(
                        when (stage) { "welcome" -> "01 / 03"; "goal" -> "02 / 03"; else -> "03 / 03" },
                        style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted
                    )
                }
                HorizontalDivider(color = PatchTokens.line)
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    when (stage) {
                        "welcome" -> {
                            Spacer(Modifier.height(34.dp))
                            Text("오늘부터,\n하나씩 바꿔봐", style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Black, color = PatchTokens.ink)
                            Text("큰 목표도 오늘 할 수 있는 작은 퀘스트부터 시작해요.",
                                style = MaterialTheme.typography.bodyLarge, color = PatchTokens.muted)
                            Box(Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                                MascotArt(Modifier.size(176.dp))
                            }
                        }
                        "goal" -> {
                            Spacer(Modifier.height(24.dp))
                            Text("어떤 걸 이루고 싶어?", style = MaterialTheme.typography.headlineLarge, color = PatchTokens.ink)
                            Text("한 줄만 적어도 돼요. 목표는 나중에 바꾸거나 건너뛸 수 있어요.",
                                style = MaterialTheme.typography.bodyLarge, color = PatchTokens.muted)
                            OutlinedTextField(
                                value = draft, onValueChange = { if (it.length <= 90) draft = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("예: 일본어로 자연스럽게 대화하기") },
                                label = { Text("첫 목표") }, minLines = 2, maxLines = 3,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            Text("목표를 저장하기 전에 직접 확인할 수 있어요.",
                                style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                        }
                        else -> {
                            Spacer(Modifier.height(24.dp))
                            Text("기록을 계속 이어가려면", style = MaterialTheme.typography.headlineLarge, color = PatchTokens.ink)
                            Text("계정을 만들면 다른 기기에서도 이어갈 수 있어요. 지금은 기기에만 저장해도 돼요.",
                                style = MaterialTheme.typography.bodyLarge, color = PatchTokens.muted)
                            Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                MascotArt(Modifier.size(140.dp))
                            }
                        }
                    }
                }
                when (stage) {
                    "welcome" -> {
                        PatchButton("시작하기", { stage = "goal" })
                        TextButton(onClick = { stage = "connect" }, modifier = Modifier.fillMaxWidth()) {
                            Text("설명 건너뛰기", color = PatchTokens.muted)
                        }
                    }
                    "goal" -> {
                        PatchButton(if (draft.isBlank()) "나중에 목표 정하기" else "다음", { stage = "connect" })
                        TextButton(onClick = { stage = "welcome" }, modifier = Modifier.fillMaxWidth()) {
                            Text("이전", color = PatchTokens.muted)
                        }
                    }
                    else -> {
                        PatchButton("계정 만들기", { createAccount = true; stage = "account" })
                        OutlinedButton(
                            onClick = { createAccount = false; stage = "account" },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) { Text("로그인") }
                        TextButton(onClick = { onFinished(draft.trim()) }, modifier = Modifier.fillMaxWidth()) {
                            Text("지금은 기기에서 시작하기", color = PatchTokens.muted)
                        }
                    }
                }
            }
        }
    }
}
