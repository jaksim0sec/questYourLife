package com.questyourlife.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.questyourlife.app.ui.BrandLanding
import com.questyourlife.app.ui.QuestHome
import com.questyourlife.app.ui.QuestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var checklistOpen by rememberSaveable { mutableStateOf(false) }

            if (checklistOpen) {
                val model: QuestViewModel = viewModel()
                val quests by model.quests.collectAsStateWithLifecycle()
                QuestHome(
                    quests = quests,
                    onAdd = model::addQuest,
                    onToggle = model::toggleQuest
                )
            } else {
                BrandLanding(onOpenChecklist = { checklistOpen = true })
            }
        }
    }
}
