package com.questyourlife.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.questyourlife.app.R
import com.questyourlife.app.cosmetics.AvatarComposer

/** Shared temporary artwork registry. IDs match persisted inventory, never grid positions. */
object CosmeticArtwork {
    fun resource(id: Int): Int? = when (id) {
        0 -> R.drawable.cosmetic_beanie
        1 -> R.drawable.cosmetic_cream_cap
        2 -> R.drawable.cosmetic_blue_cap
        3 -> R.drawable.cosmetic_charcoal_cap
        4 -> R.drawable.cosmetic_backward_cap
        5 -> R.drawable.cosmetic_bucket_hat
        1001 -> R.drawable.cosmetic_cream_headphones
        1003 -> R.drawable.cosmetic_black_headphones
        1004 -> R.drawable.cosmetic_white_airpods_pair
        else -> null
    }
    private val demoItems = listOf(0, 1001, 2, 1, 3, 4, 5, 1003, 1004)
    fun demoItem(name: String): Int = when (name) {
        "밤산책" -> 1003
        "이모리" -> 0
        "모찌", "하루" -> 1
        "다온" -> 2
        else -> demoItems[Math.floorMod(name.hashCode(), demoItems.size)]
    }
}

@Composable
fun CosmeticImage(id: Int, modifier: Modifier = Modifier) {
    CosmeticArtwork.resource(id)?.let {
        Image(painterResource(it), null, modifier, contentScale = ContentScale.Fit)
    }
}

/** One coordinate system keeps the face and accessories aligned at every avatar size. */
@Composable
fun DressedAvatar(itemId: Int, modifier: Modifier = Modifier, description: String? = null) {
    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val side = minOf(maxWidth, maxHeight)
        val composition = androidx.compose.runtime.remember(itemId) { AvatarComposer.compose(itemId) }
        Box(Modifier.size(side)) {
            composition.layers.forEach { layer ->
                val placement = Modifier.offset(x = side * layer.x, y = side * layer.y)
                    .wrapContentSize(Alignment.TopStart, unbounded = true)
                    .size(side * layer.width, side * layer.height)
                if (layer.itemId == null) {
                    Image(painterResource(R.drawable.mascot_face_stamp), description, placement,
                        contentScale = ContentScale.Fit)
                } else CosmeticImage(layer.itemId, placement)
            }
        }
    }
}
