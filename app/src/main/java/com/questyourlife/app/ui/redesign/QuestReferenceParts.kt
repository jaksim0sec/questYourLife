package com.questyourlife.app.ui.redesign

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.R
import com.questyourlife.app.ui.DressedAvatar

internal val QuestYellow = Color(0xFFFFDF43)
internal val QuestLime = Color(0xFFE4EF83)
internal val QuestNight = Color(0xFF1D231D)
internal val QuestNightInk = Color(0xFFF9F8F1)
internal val QuestShape = RoundedCornerShape(18.dp)

@Composable
internal fun QuestGlyph(@DrawableRes icon: Int, modifier: Modifier = Modifier.size(22.dp),
    color: Color = PatchTokens.ink, description: String? = null) {
    Icon(painterResource(icon), description, modifier, tint = color)
}

@Composable
internal fun QuestPill(text: String, color: Color = PatchTokens.soft, ink: Color = PatchTokens.ink) {
    Text(text, Modifier.clip(RoundedCornerShape(9.dp)).background(color)
        .padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, lineHeight = 15.sp,
        fontWeight = FontWeight.Bold, color = ink)
}

@Composable
internal fun QuestProgress(value: Float, modifier: Modifier = Modifier, color: Color = QuestYellow) {
    LinearProgressIndicator(progress = { value.coerceIn(0f, 1f) },
        modifier = modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
        color = color, trackColor = PatchTokens.line.copy(alpha = .4f),
        gapSize = 0.dp, drawStopIndicator = {})
}

@Composable
internal fun QuestAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    yellow: Boolean = true, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 50.dp), shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (yellow) QuestYellow else QuestNight,
            contentColor = if (yellow) QuestNight else QuestNightInk)) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
internal fun QuestChest(modifier: Modifier, description: String? = null) {
    Image(painterResource(R.drawable.box_gold), description, modifier)
}

@Composable
internal fun QuestFaces(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 44.dp) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
        listOf(1003, 0, 1).forEach { DressedAvatar(it, Modifier.size(size)) }
    }
}

@Composable
internal fun QuestSectionHeading(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier, color = PatchTokens.ink, fontSize = 18.sp, lineHeight = 26.sp,
        fontWeight = FontWeight.ExtraBold)
}

@Composable
internal fun QuestBookSymbol(travel: Boolean, modifier: Modifier = Modifier) {
    Box(modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
        .background(if (travel) QuestYellow.copy(alpha = .38f) else Color(0xFFD9EAF9)),
        contentAlignment = Alignment.Center) {
        QuestGlyph(if (travel) R.drawable.ic_patch_speech else R.drawable.ic_patch_shoe,
            Modifier.size(27.dp), color = QuestNight)
    }
}

@Composable
internal fun QuestTextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().heightIn(min = 48.dp).clip(RoundedCornerShape(10.dp))
        .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink)
        Spacer(Modifier.width(6.dp))
        QuestGlyph(R.drawable.ic_patch_chevron_right, Modifier.size(18.dp))
    }
}
