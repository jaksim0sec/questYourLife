package com.questyourlife.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.DailyQuestEntity

val Cream = Color(0xFFF9F7F2)
val TextInk = Color(0xFF22211F)
val TextGray = Color(0xFF888581)
val AccentRed = Color(0xFFD84D4A)
val LightRed = Color(0xFFF9E7E4)
val WarmLine = Color(0xFFEDE8E2)
val CoinGold = Color(0xFFE3A746)

@Composable
fun ScreenHeading(title: String, subtitle: String? = null, action: (@Composable () -> Unit)? = null) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = TextInk, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
            Spacer(Modifier.weight(1f))
            action?.invoke()
        }
        if (subtitle != null) Text(subtitle, color = TextGray, fontSize = 12.sp)
    }
}

@Composable
fun CashBadge(coins: Int, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.background(Color(0xFFF1EDE6), CircleShape)
            .then(if(onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 13.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("✦", fontSize = 18.sp, color = CoinGold)
        Spacer(Modifier.width(7.dp))
        Text("$coins C", fontSize = 13.sp, color = TextInk, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HeadSection(label: String, trailing: String = "", onClick: (() -> Unit)? = null) {
    Row(modifier=Modifier.fillMaxWidth().then(if (onClick!=null) Modifier.clickable(onClick=onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically) {
        Text(label, color=TextInk, fontWeight=FontWeight.Bold, fontSize=17.sp)
        Spacer(Modifier.weight(1f))
        Text(trailing, color=TextGray, fontSize=12.sp)
    }
}

@Composable
fun SmallBar(progress: Float, modifier: Modifier = Modifier, height: androidx.compose.ui.unit.Dp = 7.dp) {
    LinearProgressIndicator(progress = { progress.coerceIn(0f,1f) },
        modifier = modifier.fillMaxWidth().height(height),
        color = AccentRed, trackColor = Color(0xFFF1D9D6), gapSize = 0.dp,
        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
    )
}

@Composable
fun WhitePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier=modifier.fillMaxWidth()
        .background(Color.White.copy(alpha=.87f), RoundedCornerShape(21.dp))
        .padding(16.dp), content=content)
}

@Composable
fun RedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick=onClick, modifier=modifier.height(53.dp),
        colors=ButtonDefaults.buttonColors(containerColor=AccentRed),
        shape=RoundedCornerShape(16.dp)) {
        Text(text, fontWeight=FontWeight.Bold, fontSize=14.sp)
    }
}

@Composable
fun PaleTag(label: String) {
    Text(label, fontSize=10.sp, color=AccentRed, fontWeight=FontWeight.Bold,
        modifier=Modifier.background(LightRed,CircleShape).padding(horizontal=9.dp,vertical=4.dp))
}

@Composable
fun QuestTick(done: Boolean, modifier: Modifier = Modifier, locked: Boolean = false) {
    Box(modifier=modifier.size(26.dp)
        .border(if(done) 0.dp else 1.4.dp, if(locked) WarmLine else Color(0xFFB9B7B3),CircleShape)
        .background(if(done) AccentRed else Color.Transparent,CircleShape),
        contentAlignment=Alignment.Center) {
        if (done) Text("✓",color=Color.White,fontSize=15.sp,fontWeight=FontWeight.Bold)
        else if(locked) Text("⌁",color=TextGray,fontSize=15.sp)
    }
}

@Composable
fun DailyCompactRow(
    quest: DailyQuestEntity,
    onAccept: (String) -> Unit,
    onComplete: (String) -> Unit,
    onOpen: (() -> Unit)? = null
) {
    Row(Modifier.fillMaxWidth().heightIn(min=55.dp),
        verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.clickable {
            if(quest.completedAt == null) {
                if(quest.accepted) onComplete(quest.id) else onAccept(quest.id)
            }
        }) { QuestTick(quest.completedAt!=null) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).then(if(onOpen!=null) Modifier.clickable(onClick=onOpen) else Modifier),
            verticalArrangement=Arrangement.spacedBy(3.dp)) {
            Text(quest.title,fontSize=13.sp,color=TextInk,fontWeight=FontWeight.SemiBold,
                maxLines=1,overflow=TextOverflow.Ellipsis)
            Text(
                if(quest.completedAt!=null) "완료" else if(quest.accepted) "진행 중 · 누르면 완료" else "수락해서 시작하기",
                color=TextGray,fontSize=10.sp
            )
        }
        Text("+${quest.reward} C",color=CoinGold,fontSize=10.sp)
        if(onOpen!=null) {
            Spacer(Modifier.width(3.dp))
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,contentDescription="상세",
                tint=TextGray,modifier=Modifier.size(20.dp).clickable(onClick=onOpen))
        }
    }
}
