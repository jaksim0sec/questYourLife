package com.questyourlife.app.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.questyourlife.app.R

/** Original cash artwork: preserve transparency, proportions and colors in both themes. */
@Composable
fun CashArtwork(modifier: Modifier = Modifier, pile: Boolean = false, description: String? = null) {
    Image(painterResource(if (pile) R.drawable.pile_of_cash else R.drawable.cash),
        description, modifier, contentScale = ContentScale.Fit)
}
