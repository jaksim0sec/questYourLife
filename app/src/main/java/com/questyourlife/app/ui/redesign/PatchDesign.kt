package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.ui.*

/** Shared values for the five main pages and their sheets. */
object PatchTokens {
    val background = Cream
    val surface = CardWhite
    val ink = TextInk
    val muted = TextGray
    val red = AccentRed
    val yellow = Color(0xFFFFE489)
    val line = WarmLine
    val soft = MutedSurface
    val redSoft = LightRed
    val space4 = 4.dp
    val space8 = 8.dp
    val space12 = 12.dp
    val space16 = 16.dp
    val space20 = 20.dp
    val space24 = 24.dp
    val space32 = 32.dp
    val radius = 20.dp
    val control = 48.dp
}

@Composable
fun PatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PatchTokens.red, onPrimary = Color.White,
            background = PatchTokens.background, onBackground = PatchTokens.ink,
            surface = PatchTokens.surface, onSurface = PatchTokens.ink,
            surfaceVariant = PatchTokens.soft, onSurfaceVariant = PatchTokens.muted,
            outline = PatchTokens.line, secondary = PatchTokens.ink
        ),
        typography = Typography(
            headlineLarge = TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.ExtraBold),
            headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
            titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold),
            titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold),
            titleSmall = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
            bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
            bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
            bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 18.sp),
            labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold),
            labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium),
            labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
        ), content = content
    )
}

@Composable
fun PatchSurface(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(PatchTokens.radius)
    Column(
        modifier.fillMaxWidth().clip(shape).background(PatchTokens.surface)
            .border(1.dp, PatchTokens.line.copy(alpha = .65f), shape)
            .padding(PatchTokens.space16),
        verticalArrangement = Arrangement.spacedBy(PatchTokens.space12),
        content = content
    )
}

@Composable
fun PatchSection(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, color = PatchTokens.ink,
        modifier = Modifier.padding(top = PatchTokens.space8, bottom = PatchTokens.space4))
}

@Composable
fun PatchButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled,
        modifier = Modifier.fillMaxWidth().heightIn(min = PatchTokens.control),
        shape = RoundedCornerShape(PatchTokens.space12),
        colors = ButtonDefaults.buttonColors(containerColor = PatchTokens.red)) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** All overlays share the same handle, close target, padding and keyboard treatment. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatchSheet(title: String, onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = PatchTokens.background,
        shape = RoundedCornerShape(topStart = PatchTokens.space24, topEnd = PatchTokens.space24),
        dragHandle = { BottomSheetDefaults.DragHandle(color = PatchTokens.muted.copy(alpha = .4f)) }
    ) {
        Column(Modifier.fillMaxWidth().imePadding().verticalScroll(rememberScrollState())
            .padding(start = PatchTokens.space20, end = PatchTokens.space20, bottom = PatchTokens.space24),
            verticalArrangement = Arrangement.spacedBy(PatchTokens.space16)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink,
                    modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, "닫기", tint = PatchTokens.muted)
                }
            }
            content()
        }
    }
}
