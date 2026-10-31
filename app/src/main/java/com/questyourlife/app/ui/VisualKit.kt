package com.questyourlife.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.DailyQuestEntity

// Design tokens shared across all screens. Keep the paper surface quieter than the stamp.
val Cream = Color(0xFFFBF9F5)
val TextInk = Color(0xFF252321)
val TextGray = Color(0xFF77736F)
val AccentRed = Color(0xFFD64B49)
val LightRed = Color(0xFFF8E7E4)
val WarmLine = Color(0xFFEAE6E0)
val CoinGold = Color(0xFFCE993F)
val MutedSurface = Color(0xFFF2EFEA)
val CardWhite = Color(0xFFFFFEFC)

val EntryMotion = tween<Float>(durationMillis = 220, easing = FastOutSlowInEasing)

@Composable
fun ScreenHeading(title: String, subtitle: String? = null, action: (@Composable () -> Unit)? = null) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = TextInk, fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp, letterSpacing = (-0.5).sp)
            Spacer(Modifier.weight(1f))
            action?.invoke()
        }
        if (subtitle != null) Text(subtitle, color = TextGray, fontSize = 12.sp)
    }
}

@Composable
fun CashBadge(coins: Int, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.clip(CircleShape).background(MutedSurface)
            .then(if(onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick)
                  else Modifier)
            .padding(horizontal = 13.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(15.dp).background(Color(0xFFF2C45F), CircleShape),
            contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Check, contentDescription = null,
                tint = Color.White, modifier = Modifier.size(10.dp))
        }
        Spacer(Modifier.width(6.dp))
        Text("$coins C", fontSize = 12.sp, color = TextInk, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HeadSection(label: String, trailing: String = "", onClick: (() -> Unit)? = null) {
    Row(modifier=Modifier.fillMaxWidth().heightIn(min = 36.dp)
        .then(if(onClick!=null) Modifier.clickable(role = Role.Button, onClick=onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically) {
        Text(label, color=TextInk, fontWeight=FontWeight.Bold, fontSize=15.sp)
        Spacer(Modifier.weight(1f))
        Text(trailing, color=TextGray, fontSize=11.sp)
    }
}

@Composable
fun SmallBar(progress: Float, modifier: Modifier = Modifier,
             height: androidx.compose.ui.unit.Dp = 6.dp) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f,1f),
        animationSpec = tween(340, easing = FastOutSlowInEasing), label = "progress"
    )
    LinearProgressIndicator(progress = { animated },
        modifier = modifier.fillMaxWidth().height(height).clip(CircleShape),
        color = AccentRed, trackColor = Color(0xFFF0D9D5), gapSize = 0.dp,
        strokeCap = StrokeCap.Round
    )
}

@Composable
fun WhitePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier=modifier.fillMaxWidth()
        .background(CardWhite, RoundedCornerShape(19.dp))
        .border(.5.dp, WarmLine.copy(alpha=.48f), RoundedCornerShape(19.dp))
        .padding(16.dp), content=content)
}

@Composable
fun RedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick=onClick, modifier=modifier.height(49.dp),
        colors=ButtonDefaults.buttonColors(containerColor=AccentRed),
        elevation=ButtonDefaults.buttonElevation(defaultElevation=1.dp, pressedElevation=0.dp),
        shape=RoundedCornerShape(14.dp)) {
        Text(text, fontWeight=FontWeight.SemiBold, fontSize=13.sp)
    }
}

@Composable
fun PaleTag(label: String) {
    Text(label, fontSize=10.sp, color=AccentRed, fontWeight=FontWeight.SemiBold,
        modifier=Modifier.background(LightRed, CircleShape).padding(horizontal=8.dp,vertical=3.dp))
}

@Composable
fun QuestTick(done: Boolean, modifier: Modifier = Modifier, locked: Boolean = false) {
    val color by animateColorAsState(if(done) AccentRed else Color.Transparent,
        animationSpec=tween(200),label="tick")
    Box(modifier=modifier.size(23.dp)
        .border(if(done) 0.dp else 1.3.dp, if(locked) WarmLine else Color(0xFFB7B5B1),CircleShape)
        .background(color,CircleShape),
        contentAlignment=Alignment.Center) {
        if (done) Icon(Icons.Default.Check,contentDescription=null,
            tint=Color.White,modifier=Modifier.size(15.dp))
    }
}

@Composable
fun DailyCompactRow(
    quest: DailyQuestEntity,
    onAccept: (String) -> Unit,
    onComplete: (String) -> Unit,
    onOpen: (() -> Unit)? = null
) {
    val done = quest.completedAt != null
    val action = { if(!done) { if(quest.accepted) onComplete(quest.id) else onAccept(quest.id) } }
    val label = when {
        done -> "완료됨"
        quest.accepted -> "완료하기"
        else -> "수락하기"
    }
    Row(Modifier.fillMaxWidth().heightIn(min=53.dp),
        verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.size(43.dp).clip(CircleShape)
            .clickable(enabled = !done, role = Role.Button, onClick=action)
            .semantics { contentDescription = "${quest.title}, $label" },
            contentAlignment=Alignment.Center) {
            QuestTick(done)
        }
        Column(Modifier.weight(1f).then(if(onOpen!=null) Modifier.clickable(
            role = Role.Button, onClick=onOpen) else Modifier),
            verticalArrangement=Arrangement.spacedBy(2.dp)) {
            Text(quest.title,fontSize=12.sp,color=TextInk,fontWeight=FontWeight.SemiBold,
                maxLines=1,overflow=TextOverflow.Ellipsis)
            Text(
                if(done) "완료" else if(quest.accepted) "진행 중 · 완료는 직접 누르기"
                else "수락하고 시작하기",
                color=TextGray,fontSize=10.sp,
                maxLines=1,overflow=TextOverflow.Ellipsis
            )
        }
        Text("+${quest.reward} C",color=CoinGold,fontSize=10.sp)
        if(onOpen!=null) {
            IconButton(onClick=onOpen, modifier=Modifier.size(38.dp)) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription="퀘스트 상세",tint=TextGray,modifier=Modifier.size(20.dp))
            }
        }
    }
}
