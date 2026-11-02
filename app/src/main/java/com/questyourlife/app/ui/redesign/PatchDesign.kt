package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import com.questyourlife.app.R
import java.text.NumberFormat
import java.util.Locale
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

/** Flat panel used by the new reference pages; content owns its structure. */
@Composable
fun PatchPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White)
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
}

@Composable
fun PatchMoney(amount: Int, modifier: Modifier = Modifier) {
    val formatted = remember(amount) { NumberFormat.getIntegerInstance(Locale.KOREA).format(amount) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(22.dp).clip(CircleShape).background(Color(0xFFFFE5A0)),
            contentAlignment = Alignment.Center) {
            Icon(painterResource(R.drawable.ic_patch_coin), null, Modifier.size(20.dp), tint = Color(0xFFBA8606))
        }
        Text("$formatted C", fontSize = 20.sp, lineHeight = 26.sp,
            fontWeight = FontWeight.Bold, color = PatchTokens.ink)
    }
}

/** Native sheet anchors/nested scrolling own all drag, fling and interruption behavior. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatchSheet(title: String, onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    val close: () -> Unit = {
        scope.launch {
            sheetState.hide()
            if (!sheetState.isVisible) onDismiss()
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PatchTokens.background,
        shape = RoundedCornerShape(topStart = PatchTokens.space24, topEnd = PatchTokens.space24),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier.clickable(role = Role.Button) {
                    scope.launch {
                        if (sheetState.currentValue == SheetValue.Expanded && sheetState.hasPartiallyExpandedState)
                            sheetState.partialExpand()
                        else sheetState.expand()
                    }
                }.semantics {
                    contentDescription = if (sheetState.currentValue == SheetValue.Expanded) "시트 반으로 줄이기" else "시트 전체 펼치기"
                }, color = PatchTokens.muted.copy(alpha = .4f))
        }
    ) {
        // Tall measurement ensures both partial and expanded anchors exist, even for short content.
        Column(Modifier.fillMaxWidth().fillMaxHeight(.92f).imePadding()) {
            Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = PatchTokens.ink,
                    modifier = Modifier.weight(1f))
                IconButton(onClick = close) {
                    Icon(painterResource(R.drawable.ic_patch_close), "닫기", tint = PatchTokens.muted)
                }
            }
            Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
        }
    }
}
