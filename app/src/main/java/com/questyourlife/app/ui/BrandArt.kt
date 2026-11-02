package com.questyourlife.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.questyourlife.app.R

private val ArtRed = Color(0xFFDA514D)

@Composable
fun MascotArt(modifier: Modifier = Modifier, festive: Boolean = false) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(painter = painterResource(R.drawable.mascot_sticker),
            contentDescription = "갓생패치 도장 마스코트", modifier = Modifier.matchParentSize())
        if (festive) {
            Canvas(Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                listOf(
                    Offset(.16f,.20f) to Offset(.10f,.09f),
                    Offset(.35f,.13f) to Offset(.34f,.04f),
                    Offset(.78f,.17f) to Offset(.88f,.05f),
                    Offset(.88f,.38f) to Offset(.99f,.35f)
                ).forEach { (a,b) ->
                    drawLine(ArtRed, Offset(w*a.x,h*a.y), Offset(w*b.x,h*b.y),
                        strokeWidth=w*.025f, cap=StrokeCap.Round)
                }
            }
        }
    }
}

@Composable
fun MountainArt(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w=size.width;val h=size.height
        val left = Path().apply {
            moveTo(w*.02f,h*.88f);lineTo(w*.38f,h*.26f);lineTo(w*.71f,h*.88f);close()
        }
        drawPath(left,Brush.verticalGradient(listOf(Color(0xFFEB9C97),Color(0xFFF9E0DC))))
        val right = Path().apply {
            moveTo(w*.35f,h*.92f);lineTo(w*.71f,h*.36f);lineTo(w*.98f,h*.92f);close()
        }
        drawPath(right,Brush.verticalGradient(listOf(Color(0xFFEFAFAB),Color(0xFFF7DFDB))))
        val snow = Path().apply {
            moveTo(w*.25f,h*.48f);lineTo(w*.38f,h*.26f);lineTo(w*.54f,h*.52f)
            lineTo(w*.44f,h*.45f);lineTo(w*.36f,h*.54f);close()
        }
        drawPath(snow,Color(0xFFFFF5F1))
        drawLine(Color(0xFFB74A46),Offset(w*.38f,h*.26f),Offset(w*.38f,h*.02f),w*.018f)
        val flag=Path().apply {
            moveTo(w*.38f,h*.03f);lineTo(w*.61f,h*.10f)
            lineTo(w*.38f,h*.17f);close()
        }
        drawPath(flag,ArtRed)
        drawOval(Color.White.copy(alpha=.53f),topLeft=Offset(w*.00f,h*.77f),
            size=Size(w*.51f,h*.19f))
        drawOval(Color.White.copy(alpha=.48f),topLeft=Offset(w*.55f,h*.80f),
            size=Size(w*.42f,h*.16f))
    }
}

@Composable
fun SproutArt(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w=size.width;val h=size.height
        drawOval(Color(0xFFE6EDE2),topLeft=Offset(w*.1f,h*.77f),size=Size(w*.8f,h*.18f))
        drawLine(Color(0xFF81AE73),Offset(w*.5f,h*.84f),Offset(w*.50f,h*.38f),
            w*.045f,cap=StrokeCap.Round)
        drawOval(Color(0xFF8FC27E),topLeft=Offset(w*.18f,h*.21f),size=Size(w*.35f,h*.21f))
        drawOval(Color(0xFF7FB675),topLeft=Offset(w*.48f,h*.14f),size=Size(w*.33f,h*.22f))
    }
}

@Composable
fun ChestArt(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w=size.width;val h=size.height
        drawRoundRect(Brush.verticalGradient(listOf(Color(0xFFFAC76F),Color(0xFFE6A045))),
            topLeft=Offset(w*.10f,h*.36f),size=Size(w*.80f,h*.50f),
            cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.09f))
        drawRoundRect(Brush.verticalGradient(listOf(Color(0xFFE9A159),Color(0xFFF8CF70))),
            topLeft=Offset(w*.10f,h*.24f),size=Size(w*.80f,h*.24f),
            cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.12f))
        drawRect(Color(0xFFCE6748),topLeft=Offset(w*.47f,h*.25f),
            size=Size(w*.12f,h*.61f))
        drawRoundRect(Color(0xFFFFE2A2),topLeft=Offset(w*.44f,h*.49f),
            size=Size(w*.20f,h*.15f),
            cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.04f))
    }
}

@Composable
fun HatArt(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w=size.width;val h=size.height
        val dome=Path().apply {
            moveTo(w*.20f,h*.63f)
            cubicTo(w*.23f,h*.21f,w*.64f,h*.17f,w*.78f,h*.60f)
            close()
        }
        drawPath(dome, Brush.verticalGradient(listOf(color.copy(alpha=.83f),color)))
        drawOval(Color(0xFF33302E),topLeft=Offset(w*.09f,h*.57f),size=Size(w*.82f,h*.23f))
        drawOval(color,topLeft=Offset(w*.14f,h*.58f),size=Size(w*.72f,h*.13f))
        drawLine(Color.White.copy(alpha=.5f),Offset(w*.33f,h*.39f),
            Offset(w*.49f,h*.32f),w*.033f,cap=StrokeCap.Round)
        drawCircle(Color(0xFFF4D4A8),radius=w*.048f,center=Offset(w*.55f,h*.48f))
    }
}
