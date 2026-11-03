package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.questyourlife.app.R
import com.questyourlife.app.data.DailyQuestEntity

/** A guest only sees genuine local quest notifications, never invented clan activity. */
@Composable
fun NotificationCenter(quests: List<DailyQuestEntity>, onDismiss: () -> Unit, onSettings: () -> Unit,
    onProfile: (String) -> Unit, onClan: () -> Unit, onQuest: (String) -> Unit,
    onAccept: (String) -> Unit, onReject: (String) -> Unit) {
    val social = rememberSocialAccess()
    if (social.signedIn) {
        SocialNotificationCenter(social, quests, onDismiss, onSettings,
            onProfile, onClan, onQuest, onAccept, onReject)
        return
    }
    PatchSheet("알림", onDismiss, initialHeight = 540.dp, startExpanded = true,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        contentSpacing = 12.dp,
        headerActions = {
            IconButton(onClick = onSettings) {
                Icon(painterResource(R.drawable.ic_patch_settings), "알림 설정",
                    tint = PatchTokens.muted)
            }
        }) {
        QuestInbox(quests, onOpen = onQuest, onAccept = onAccept, onReject = onReject)
    }
}
