package com.questyourlife.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.questyourlife.app.ui.DailyQuestApp
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.QuestHome
import com.questyourlife.app.ui.QuestViewModel

class MainActivity : ComponentActivity() {
    private var dailyModel: DailyQuestViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var classic by rememberSaveable { mutableStateOf(false) }
            BackHandler(enabled = classic) { classic = false }

            if (classic) {
                val model: QuestViewModel = viewModel()
                val quests by model.quests.collectAsStateWithLifecycle()
                QuestHome(
                    quests = quests,
                    onAdd = model::addQuest,
                    onToggle = model::toggleQuest
                )
            } else {
                val model: DailyQuestViewModel = viewModel()
                dailyModel = model
                DailyQuestApp(model, onOpenClassicChecklist = { classic = true })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dailyModel?.refresh()
    }
}
