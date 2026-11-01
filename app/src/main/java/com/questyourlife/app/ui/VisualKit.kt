package com.questyourlife.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.data.DailyQuestEntity

// Reference-inspired design system, owned independently from the persistent model.
val Cream = Color(0xFFFCFAF6)
val CardWhite = Color(0xFFFFFEFC)
val TextInk = Color(0xFF252321)
val TextGray = Color(0xFF77736E)
val AccentRed = Color(0xFFD64E4B)
val LightRed = Color(0xFFF9E8E5)
val WarmLine = Color(0xFFE9E5DF)
val MutedSurface = Color(0xFFF3F0EB)
val CoinGold = Color(0xFFBE8529)
val InkSoft = Color(0xFF363432)
val LilacSurface = Color(0xFFF7EEF7)
val GrassSurface = Color(0xFFF0F5ED)
val CardShape = RoundedCornerShape(21.dp)

@Composable
fun ScreenHeading(title: String, subtitle: String? = null, action: (@Composable () -> Unit)? = null) {
    Column(Modifier.fillMaxWidth(), verticalArrangement=Arrangement.spacedBy(5.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text(title,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,color=TextInk,letterSpacing=(-.6).sp)
            Spacer(Modifier.weight(1f))
            action?.invoke()
        }
        if(subtitle!=null) Text(subtitle,fontSize=12.sp,color=TextGray)
    }
}

@Composable
fun CashBadge(coins: Int, onClick: (() -> Unit)? = null) {
    Row(
        Modifier.clip(CircleShape).background(MutedSurface)
            .then(if(onClick!=null) Modifier.clickable(role=Role.Button,onClick=onClick) else Modifier)
            .padding(horizontal=12.dp,vertical=8.dp),
        verticalAlignment=Alignment.CenterVertically
    ) {
        Box(Modifier.size(17.dp).background(Color(0xFFFFD577),CircleShape),
            contentAlignment=Alignment.Center) {
            Icon(Icons.Filled.Star,null,Modifier.size(11.dp),tint=Color.White)
        }
        Spacer(Modifier.width(6.dp))
        Text("${coins} C",fontSize=12.sp,fontWeight=FontWeight.Bold,color=TextInk)
    }
}

@Composable
fun SegmentedTabs(labels: List<String>, selected: Int, onSelect: (Int) -> Unit,
                  modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().clip(CircleShape).background(MutedSurface).padding(3.dp),
        horizontalArrangement=Arrangement.spacedBy(3.dp)) {
        labels.forEachIndexed { index, title ->
            val bg by animateColorAsState(if(index==selected) InkSoft else Color.Transparent,
                animationSpec=tween(180),label="tab background")
            Box(Modifier.weight(1f).height(37.dp).clip(CircleShape)
                .background(bg).clickable(role=Role.Tab,onClick={onSelect(index)}),
                contentAlignment=Alignment.Center) {
                Text(title,fontSize=11.sp,
                    fontWeight=if(index==selected) FontWeight.Bold else FontWeight.Medium,
                    color=if(index==selected) Color.White else TextGray,maxLines=1)
            }
        }
    }
}

@Composable
fun HeadSection(label: String,trailing: String="",onClick: (() -> Unit)?=null) {
    Row(Modifier.fillMaxWidth().heightIn(min=42.dp)
        .then(if(onClick!=null) Modifier.clickable(role=Role.Button,onClick=onClick) else Modifier),
        verticalAlignment=Alignment.CenterVertically) {
        Text(label,fontSize=15.sp,fontWeight=FontWeight.Bold,color=TextInk)
        Spacer(Modifier.weight(1f))
        Text(trailing,fontSize=12.sp,color=TextGray)
    }
}

@Composable
fun SmallBar(progress: Float,modifier: Modifier=Modifier,height: androidx.compose.ui.unit.Dp=7.dp) {
    val value by animateFloatAsState(
        progress.coerceIn(0f,1f),animationSpec=tween(350,easing=FastOutSlowInEasing),label="progress")
    Box(modifier.fillMaxWidth().height(height).clip(CircleShape)
        .background(Color(0xFFEED7D3))) {
        if(value>0f) Box(Modifier.fillMaxWidth(value).fillMaxHeight()
            .clip(CircleShape).background(AccentRed))
    }
}

@Composable
fun WhitePanel(modifier: Modifier=Modifier,content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth()
        .clip(CardShape).background(CardWhite)
        .border(.6.dp,WarmLine.copy(alpha=.7f),CardShape)
        .padding(16.dp),content=content)
}

@Composable
fun RedButton(text: String,onClick: () -> Unit,modifier: Modifier=Modifier) {
    Button(onClick=onClick,modifier=modifier.heightIn(min=50.dp),
        colors=ButtonDefaults.buttonColors(containerColor=AccentRed),
        shape=RoundedCornerShape(16.dp),
        contentPadding=PaddingValues(horizontal=18.dp,vertical=13.dp)) {
        Text(text,fontSize=14.sp,fontWeight=FontWeight.Bold)
    }
}

@Composable
fun PaleTag(label: String) {
    Text(label,color=AccentRed,fontSize=10.sp,fontWeight=FontWeight.SemiBold,
        modifier=Modifier.clip(CircleShape).background(LightRed)
            .padding(horizontal=9.dp,vertical=4.dp))
}

@Composable
fun QuestTick(done: Boolean,modifier: Modifier=Modifier,locked: Boolean=false) {
    val bg by animateColorAsState(if(done) AccentRed else Color.Transparent,
        animationSpec=tween(190),label="complete badge")
    Box(modifier.size(25.dp).clip(CircleShape).background(bg)
        .border(if(done) 0.dp else 1.3.dp,
            if(locked) WarmLine else Color(0xFFB6B2AD),CircleShape),
        contentAlignment=Alignment.Center) {
        if(done) Icon(Icons.Filled.Check,null,Modifier.size(17.dp),tint=Color.White)
    }
}

@Composable
fun DailyCompactRow(
    quest: DailyQuestEntity,
    onAccept: (String) -> Unit,
    onComplete: (String) -> Unit,
    onOpen: (() -> Unit)? = null
) {
    val done=quest.completedAt!=null
    val action={ if(!done) { if(quest.accepted) onComplete(quest.id) else onAccept(quest.id) } }
    val description=if(done) "완료" else if(quest.accepted) "완료하기" else "수락하기"
    Row(Modifier.fillMaxWidth().heightIn(min=60.dp),verticalAlignment=Alignment.CenterVertically) {
        Box(Modifier.size(45.dp).clip(CircleShape).clickable(enabled=!done,
            role=Role.Button,onClick=action)
            .semantics { contentDescription=quest.title+", "+description },
            contentAlignment=Alignment.Center) { QuestTick(done) }
        Column(Modifier.weight(1f).then(if(onOpen!=null)
            Modifier.clickable(onClick=onOpen) else Modifier),
            verticalArrangement=Arrangement.spacedBy(2.dp)) {
            Text(quest.title,fontSize=12.sp,color=TextInk,fontWeight=FontWeight.SemiBold,
                maxLines=1,overflow=TextOverflow.Ellipsis)
            Text(if(done) "완료" else if(quest.accepted) "진행 중" else "수락해서 시작하기",
                fontSize=10.sp,color=TextGray,maxLines=1)
        }
        Text("+${quest.reward} C",fontSize=10.sp,color=CoinGold)
        if(onOpen!=null) IconButton(onClick=onOpen,modifier=Modifier.size(40.dp)) {
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight,"상세",tint=TextGray,
                modifier=Modifier.size(18.dp))
        }
    }
}
