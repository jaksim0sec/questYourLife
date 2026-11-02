package com.questyourlife.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.QuestEntity

private val Background = Color(0xFF0E1015)
private val Surface = Color(0xFF1A1D25)
private val Accent = Color(0xFF438BFA)
private val Muted = Color(0xFFABB1BC)

@Composable
fun QuestHome(
    quests: List<QuestEntity>,
    onAdd: (String) -> Unit,
    onToggle: (QuestEntity) -> Unit
) {
    val done = quests.count { it.completed }
    val progress = if (quests.isEmpty()) 0f else done.toFloat() / quests.size
    var newTitle by rememberSaveable { mutableStateOf("") }
    val add = {
        val title = newTitle.trim()
        if (title.isNotEmpty()) {
            onAdd(title)
            newTitle = ""
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(primary = Accent, background = Background, surface = Surface)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {
            Text("갓생패치", color = Muted, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text("오늘의 퀘스트", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("오늘의 진행 상황", color = Muted, fontSize = 14.sp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("$done / ${quests.size}개 완료", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Text("+${done * 30} XP", color = Accent, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(7.dp),
                        color = Accent,
                        trackColor = Color(0xFF343844)
                    )
                    Text("XP는 로컬 화면 시연용이며 실제 보상이 아닙니다", color = Muted, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { if (it.length <= 120) newTitle = it },
                    label = { Text("새 퀘스트") },
                    placeholder = { Text("오늘 할 일을 입력해") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { add() })
                )
                Button(
                    onClick = { add() },
                    enabled = newTitle.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) { Text("추가") }
            }

            Spacer(Modifier.height(20.dp))
            Text("체크리스트", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            if (quests.isEmpty()) {
                Text("아직 퀘스트가 없어. 오늘 할 일을 하나 추가해 봐.", color = Muted, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(quests, key = { it.id }) { quest ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(Surface, RoundedCornerShape(16.dp))
                                .clickable { onToggle(quest) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = quest.completed,
                                onCheckedChange = { onToggle(quest) },
                                colors = CheckboxDefaults.colors(checkedColor = Accent)
                            )
                            Text(
                                text = quest.title,
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                                color = if (quest.completed) Muted else Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                textDecoration = if (quest.completed) TextDecoration.LineThrough else TextDecoration.None,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
