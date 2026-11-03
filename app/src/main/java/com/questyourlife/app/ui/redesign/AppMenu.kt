package com.questyourlife.app.ui.redesign

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.questyourlife.app.R

@Composable
private fun AppMenuRow(@DrawableRes icon: Int, title: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).heightIn(min = 52.dp)
        .padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(painterResource(icon), null, Modifier.size(22.dp), tint = PatchTokens.ink)
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = PatchTokens.ink)
        Icon(painterResource(R.drawable.ic_patch_chevron_right), null, Modifier.size(18.dp), tint = PatchTokens.muted)
    }
}

@Composable
fun AppMenu(onCompanion: () -> Unit, onCreate: () -> Unit, onHistory: () -> Unit,
    onNotes: () -> Unit, onAccount: () -> Unit, onTimer: () -> Unit, onTheme: () -> Unit, onSettings: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text("목표와 실천", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
    Column {
        AppMenuRow(R.drawable.ic_patch_sparkles, "패치와 목표 나누기", onCompanion)
        AppMenuRow(R.drawable.ic_patch_target, "직접 목표 만들기", onCreate)
        AppMenuRow(R.drawable.ic_patch_timer, "집중 타이머", onTimer)
    }
    HorizontalDivider(color = PatchTokens.line)
    Text("기록과 소통", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
    Column {
        AppMenuRow(R.drawable.ic_patch_history, "완료 기록", onHistory)
        AppMenuRow(R.drawable.ic_patch_mail, "새 쪽지", onNotes)
    }
    HorizontalDivider(color = PatchTokens.line)
    Text("앱 설정", style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
    Column {
        AppMenuRow(R.drawable.ic_patch_nav_inactive_my, "계정과 동기화", onAccount)
        AppMenuRow(R.drawable.ic_patch_moon, "화면 테마", onTheme)
        AppMenuRow(R.drawable.ic_patch_settings, "알림과 앱 설정", onSettings)
    }
    }
}

@Composable
fun AppearanceOptions(selected: String, onSelect: (String) -> Unit) {
    listOf("system" to "시스템 설정 따르기", "light" to "라이트", "dark" to "다크").forEach { (key, title) ->
        Row(Modifier.fillMaxWidth().clickable { onSelect(key) }.heightIn(min = 52.dp),
            verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected == key, onClick = null)
            Spacer(Modifier.width(12.dp))
            Text(title, color = PatchTokens.ink)
        }
    }
}
