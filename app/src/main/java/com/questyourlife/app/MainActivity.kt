package com.questyourlife.app

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.questyourlife.app.brand.BrandColor
import com.questyourlife.app.ui.BrandOpening
import com.questyourlife.app.ui.DailyQuestApp
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.QuestHome
import com.questyourlife.app.ui.QuestViewModel

class MainActivity : ComponentActivity() {
    private var dailyModel: DailyQuestViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            var classic by rememberSaveable { mutableStateOf(false) }
            var opening by rememberSaveable { mutableStateOf(savedInstanceState == null) }
            val openingDeadline by rememberSaveable { mutableStateOf(SystemClock.elapsedRealtime() + 1000L) }
            val daily: DailyQuestViewModel = viewModel()
            dailyModel = daily
            BackHandler(enabled = classic && !opening) { classic = false }

            // The opening uses white system icons; the app theme takes over on entry.
            if (opening) SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
            // Cover the native window artwork so crossfade cannot expose a second stamp.
            Box(Modifier.fillMaxSize().background(Color(BrandColor.RED))) {
                Crossfade(opening, animationSpec = tween(180), label = "opening to app") { showOpening ->
                    when {
                        showOpening -> BrandOpening(openingDeadline) { opening = false }
                        classic -> {
                            val model: QuestViewModel = viewModel()
                            val quests by model.quests.collectAsStateWithLifecycle()
                            QuestHome(quests = quests, onAdd = model::addQuest, onToggle = model::toggleQuest)
                        }
                        else -> DailyQuestApp(daily, onOpenClassicChecklist = { classic = true })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dailyModel?.refresh()
    }
}
