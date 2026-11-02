package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.DailyQuestEntity
import com.questyourlife.app.progression.UserLevel

@Composable
fun LevelProgress(level: UserLevel, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("🌱 Lv.${level.level}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text("${level.currentXp} / 100 XP", style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        }
        LinearProgressIndicator(progress = { level.fraction }, modifier = Modifier.fillMaxWidth().height(4.dp),
            color = PatchTokens.red, trackColor = PatchTokens.line, gapSize = 0.dp, drawStopIndicator = {})
    }
}

@Composable
fun QuestInbox(quests: List<DailyQuestEntity>, onOpen: (String) -> Unit,
    onAccept: (String) -> Unit, onReject: (String) -> Unit, onSettings: (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("📬 일일 퀘스트", Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
        if (onSettings != null) TextButton(onClick = onSettings) { Text("알림 설정") }
    }
    if (quests.isEmpty()) Text("📭 새 제안이 아직 없어요.", color = PatchTokens.muted)
    quests.sortedBy { when { it.dismissed -> 3; it.completedAt != null -> 2; it.accepted -> 1; else -> 0 } }.forEach { quest ->
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(when { quest.dismissed -> "🙅"; quest.completedAt != null -> "🎉"; quest.accepted -> "🏃"; else -> "📬" }, fontSize = 26.sp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(when { quest.dismissed -> "거절한 제안"; quest.completedAt != null -> "실천 완료"; quest.accepted -> "진행 중"; else -> "오늘 도착한 제안" },
                    style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                Text(quest.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge,
                    modifier = if (!quest.dismissed) Modifier.clickable { onOpen(quest.id) } else Modifier)
                Text(when {
                    quest.dismissed -> "이번 제안은 건너뛰었어요."
                    quest.completedAt != null -> "+${quest.reward} C · +${quest.reward} XP"
                    quest.accepted -> "수락했어요. 실천 후 완료해 주세요."
                    else -> "${quest.detail} · +${quest.reward} C · +${quest.reward} XP"
                }, style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
                if (!quest.dismissed && quest.completedAt == null && !quest.accepted) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onAccept(quest.id) }) { Text("✅ 수락") }
                        OutlinedButton(onClick = { onReject(quest.id) }) { Text("🙅 거절") }
                    }
                } else if (quest.accepted && quest.completedAt == null && !quest.dismissed) {
                    TextButton(onClick = { onOpen(quest.id) }) { Text("이어서 실천하기") }
                }
            }
        }
        HorizontalDivider(color = PatchTokens.line)
    }
}
