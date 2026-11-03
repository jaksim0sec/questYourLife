package com.questyourlife.app.ui

import android.animation.ValueAnimator
import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.questyourlife.app.R
import com.questyourlife.app.brand.BrandColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Opening reference: solid brand red, white front-facing stamp centered, name at the bottom.
 * SrcIn whitening retains the original PNG alpha and stamp texture; no image is rewritten.
 * One subtle entrance, then home. A saved deadline prevents rotation from extending the intro.
 */
@Composable
fun BrandOpening(expiresAt: Long, onFinished: () -> Unit) {
    val animate = remember { ValueAnimator.areAnimatorsEnabled() }
    val scale = remember { Animatable(if (animate) .94f else 1f) }
    val opacity = remember { Animatable(if (animate) .6f else 1f) }
    val finished by rememberUpdatedState(onFinished)
    LaunchedEffect(expiresAt) {
        val remaining = (expiresAt - SystemClock.elapsedRealtime()).coerceAtLeast(0L)
        if (animate && remaining > 0L) {
            val duration = minOf(420L, remaining).toInt()
            coroutineScope {
                launch { scale.animateTo(1f, tween(duration, easing = FastOutSlowInEasing)) }
                launch { opacity.animateTo(1f, tween(duration)) }
                delay(remaining)
            }
        }
        finished()
    }
    BoxWithConstraints(Modifier.fillMaxSize().background(Color(BrandColor.RED))) {
        val logoWidth = minOf(maxWidth * .44f, 180.dp)
        Image(painterResource(R.drawable.mascot_face_stamp), "갓생패치 도장 마스코트",
            modifier = Modifier.align(Alignment.Center).offset(y = 8.dp)
                .width(logoWidth).aspectRatio(1453f / 1075f)
                .graphicsLayer { scaleX = scale.value; scaleY = scale.value; alpha = opacity.value },
            colorFilter = ColorFilter.tint(Color.White), contentScale = ContentScale.Fit)
        Text("갓생패치", Modifier.align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.safeDrawing).padding(bottom = 32.dp)
            .graphicsLayer { alpha = opacity.value },
            color = Color.White, fontSize = 26.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold)
    }
}
