package com.questyourlife.app.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.questyourlife.app.companion.ClanEmblem

@Composable
fun ClanMark(modifier: Modifier = Modifier, emblem: ClanEmblem = ClanEmblem()) {
    val vector = remember(emblem) {
        fun color(hex: String) = Color(android.graphics.Color.parseColor(hex))
        ImageVector.Builder("Clan emblem", 36.dp, 36.dp, 36f, 36f).apply {
            addPath(PathParser().parsePathString(ClanEmblem.POLE).toNodes(),
                stroke = SolidColor(color(emblem.palette.pole)), strokeLineWidth = 1.98f,
                strokeLineCap = StrokeCap.Round)
            addPath(PathParser().parsePathString(ClanEmblem.FLAG).toNodes(),
                fill = SolidColor(color(emblem.palette.flag)))
            addPath(PathParser().parsePathString(emblem.icon.path).toNodes(),
                stroke = SolidColor(color(emblem.palette.ink)), strokeLineWidth = 3.24f,
                strokeLineCap = StrokeCap.Round)
        }.build()
    }
    Icon(vector, "클랜 표식", modifier, tint = Color.Unspecified)
}
