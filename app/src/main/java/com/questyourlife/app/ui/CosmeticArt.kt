package com.questyourlife.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.questyourlife.app.R

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
    fun demoItem(name: String): Int = demoItems[Math.floorMod(name.hashCode(), demoItems.size)]
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
        Box(Modifier.size(side), contentAlignment = Alignment.Center) {
            Image(painterResource(R.drawable.mascot_face_stamp), description,
                Modifier.size(side, side * .734f).offset(y = side * .10f),
                contentScale = ContentScale.Fit)
            when (itemId) {
                in 0..5 -> CosmeticImage(itemId, Modifier.align(Alignment.TopCenter)
                    .size(side * .80f, side * .55f))
                1001, 1003 -> CosmeticImage(itemId, Modifier.align(Alignment.TopCenter)
                    .offset(y = side * .12f).size(side * .98f, side * .77f))
                1004 -> CosmeticImage(itemId, Modifier.offset(y = side * .16f)
                    .size(side * .95f, side * .36f))
            }
        }
    }
}
