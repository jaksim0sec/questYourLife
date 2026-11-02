package com.questyourlife.app.ui.redesign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.questyourlife.app.R
import com.questyourlife.app.cosmetics.CosmeticCatalog
import com.questyourlife.app.cosmetics.CosmeticCategory
import com.questyourlife.app.cosmetics.CosmeticItem
import com.questyourlife.app.ui.DailyQuestViewModel
import com.questyourlife.app.ui.DressedAvatar
import com.questyourlife.app.ui.CosmeticImage

private enum class DecorTab(val label: String) {
    RECOMMENDED("추천"), HATS("모자"), ACCESSORIES("소품"), OWNED("보관함")
}

/** The shell supplies safe drawing insets; this page owns its header and pinned action. */
@Composable
fun PatchDecorPage(model: DailyQuestViewModel, onBack: () -> Unit) {
    val coins by model.coins.collectAsStateWithLifecycle()
    val ownedItems by model.ownedItems.collectAsStateWithLifecycle()
    val equipped = model.equipped.intValue
    val busy by model.cosmeticBusy
    val message by model.cosmeticMessage
    var tab by rememberSaveable { mutableStateOf(DecorTab.RECOMMENDED) }
    var selectedItemId by rememberSaveable { mutableStateOf<Int?>(null) }
    var previewId by rememberSaveable { mutableStateOf(equipped) }
    var showClanReward by rememberSaveable { mutableStateOf(false) }

    // Legacy purchases keep their identity and stored price in the inventory.
    val inventory = remember(ownedItems) {
        (listOfNotNull(CosmeticCatalog.find(0)) + ownedItems.map(CosmeticCatalog::ownedItem))
            .distinctBy { it.id }
    }
    val ownedIds = remember(ownedItems) { ownedItems.map { it.itemId }.toSet() + 0 }
    val items = when (tab) {
        DecorTab.RECOMMENDED -> CosmeticCatalog.items
        DecorTab.HATS -> CosmeticCatalog.items.filter { it.category == CosmeticCategory.HAT }
        DecorTab.ACCESSORIES -> CosmeticCatalog.items.filter { it.category == CosmeticCategory.ACCESSORY }
        DecorTab.OWNED -> inventory
    }
    val selectedItem = selectedItemId?.let { id ->
        inventory.firstOrNull { it.id == id } ?: CosmeticCatalog.find(id)
    }

    Column(Modifier.fillMaxSize().background(PatchTokens.background)) {
        DecorHeader(coins, onBack)
        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = PatchTokens.space16)
                .padding(bottom = PatchTokens.space8)
        ) {
            DecorAppearancePreview(previewId)
            DecorTabs(tab) { tab = it }
            Spacer(Modifier.height(PatchTokens.space12))
            DecorClanReward { showClanReward = true }
            Spacer(Modifier.height(PatchTokens.space8))
            Column(verticalArrangement = Arrangement.spacedBy(PatchTokens.space12)) {
                items.chunked(2).forEach { row ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(PatchTokens.space12)
                    ) {
                        row.forEach { item ->
                            DecorItemTile(
                                item = item,
                                owned = item.id in ownedIds,
                                equipped = item.id == equipped,
                                selected = item.id == previewId,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    model.cosmeticMessage.value = null
                                    previewId = item.id
                                    if (item.id !in ownedIds) selectedItemId = item.id
                                }
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        Box(
            Modifier.fillMaxWidth().background(PatchTokens.background)
                .padding(horizontal = PatchTokens.space16)
                .padding(top = PatchTokens.space8, bottom = PatchTokens.space12)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            message?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted) }
            Button(
                onClick = { model.chooseCosmetic(previewId) },
                enabled = previewId in ownedIds && !busy && previewId != equipped,
                modifier = Modifier.fillMaxWidth().heightIn(min = PatchTokens.control),
                shape = RoundedCornerShape(PatchTokens.space12),
                colors = ButtonDefaults.buttonColors(containerColor = PatchTokens.red)
            ) {
                Text("적용하기", style = MaterialTheme.typography.labelLarge)
            }
            }
        }
    }

    selectedItem?.let { item ->
        DecorItemSheet(
            item = item,
            owned = item.id in ownedIds,
            equipped = item.id == equipped,
            coins = coins,
            busy = busy,
            message = message,
            onDismiss = { selectedItemId = null },
            onChoose = { model.chooseCosmetic(item.id, applyAfterPurchase = false) }
        )
    }
    if (showClanReward) {
        PatchSheet("이번 주 클랜 보상", onDismiss = { showClanReward = false }) {
            CosmeticImage(2, Modifier.fillMaxWidth().height(144.dp))
            Text("함께 열어서 받는 꾸미기", style = MaterialTheme.typography.titleMedium,
                color = PatchTokens.ink)
            Text("클랜은 데모 화면이에요. 실제 보상 받기는 준비 중이에요.",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted)
        }
    }
}

@Composable
private fun DecorHeader(coins: Int, onBack: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val stackCash = maxWidth < 320.dp || LocalDensity.current.fontScale > 1.3f ||
            coins.toString().length > 6
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().heightIn(min = 56.dp)
                    .padding(start = PatchTokens.space4, end = PatchTokens.space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(painterResource(R.drawable.ic_patch_arrow_left), "뒤로가기",
                        Modifier.size(24.dp), tint = PatchTokens.ink)
                }
                Text("꾸미기", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PatchTokens.ink,
                    modifier = Modifier.weight(1f).semantics { heading() })
                if (!stackCash) PatchMoney(coins, compact = true)
            }
            if (stackCash) PatchMoney(coins, Modifier.align(Alignment.End)
                .padding(horizontal = PatchTokens.space16).padding(bottom = PatchTokens.space8))
        }
    }
}

@Composable
private fun DecorAppearancePreview(itemId: Int) {
    BoxWithConstraints(
        Modifier.fillMaxWidth().aspectRatio(2.15f)
            .clip(RoundedCornerShape(PatchTokens.space16)).background(PatchTokens.soft)
            .semantics { contentDescription = "선택한 아바타 미리보기" },
        contentAlignment = Alignment.BottomCenter
    ) {
        val faceSize = minOf(maxWidth * .42f, 152.dp)
        DressedAvatar(itemId, Modifier.padding(bottom = PatchTokens.space12).size(faceSize))
    }
}

@Composable
private fun DecorTabs(selected: DecorTab, onSelect: (DecorTab) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth().selectableGroup()) {
            DecorTab.entries.forEach { tab ->
                val active = tab == selected
                Column(
                    Modifier.weight(1f).selectable(selected = active, role = Role.Tab,
                        onClick = { onSelect(tab) }),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.heightIn(min = PatchTokens.control), contentAlignment = Alignment.Center) {
                        Text(tab.label, style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                            color = if (active) PatchTokens.red else PatchTokens.ink)
                    }
                    Box(Modifier.fillMaxWidth().height(2.dp)
                        .clip(RoundedCornerShape(topStart = PatchTokens.space4, topEnd = PatchTokens.space4))
                        .background(if (active) PatchTokens.red else PatchTokens.background))
                }
            }
        }
        HorizontalDivider(color = PatchTokens.line)
    }
}

@Composable
private fun DecorClanReward(onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(PatchTokens.space12))
            .background(PatchTokens.yellow).clickable(role = Role.Button, onClick = onClick)
            .heightIn(min = 52.dp).padding(horizontal = PatchTokens.space16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)) {
            Text("이번 주 클랜 보상", fontSize = 15.sp,
                fontWeight = FontWeight.Bold, color = PatchTokens.ink)
            Text("함께 열어서 받는 꾸미기 · 데모", style = MaterialTheme.typography.bodySmall,
                color = PatchTokens.ink)
        }
        CosmeticImage(2, Modifier.width(64.dp).height(52.dp))
        Icon(painterResource(R.drawable.ic_patch_chevron_right), null,
            Modifier.size(20.dp), tint = PatchTokens.ink)
    }
}

@Composable
private fun DecorItemTile(
    item: CosmeticItem,
    owned: Boolean,
    equipped: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(PatchTokens.space12)
    Column(
        modifier.clip(shape).background(PatchTokens.surface)
            .then(if (selected) Modifier.border(1.5.dp, PatchTokens.red, shape) else Modifier)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics {
                stateDescription = when {
                    equipped -> "착용 중"
                    selected -> "미리보기 선택됨"
                    owned -> "보유 중"
                    else -> "구매 가능"
                }
            }
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1.8f).background(PatchTokens.soft)
        ) {
            CosmeticImage(item.id, Modifier.fillMaxSize().padding(PatchTokens.space8))
            if (equipped) {
                Text("착용 중", style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold, color = PatchTokens.surface,
                    modifier = Modifier.align(Alignment.TopEnd).padding(PatchTokens.space8)
                        .clip(RoundedCornerShape(PatchTokens.space8)).background(PatchTokens.red)
                        .padding(horizontal = PatchTokens.space8, vertical = PatchTokens.space4))
            }
        }
        Column(
            Modifier.fillMaxWidth().heightIn(min = 52.dp).padding(
                horizontal = PatchTokens.space12, vertical = PatchTokens.space8),
            verticalArrangement = Arrangement.spacedBy(PatchTokens.space4)
        ) {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, color = PatchTokens.ink)
            when {
                equipped -> Unit
                owned -> Text("보유 중",
                    style = MaterialTheme.typography.labelMedium, color = PatchTokens.muted)
                item.price == 0 -> Text("무료", style = MaterialTheme.typography.labelMedium,
                    color = PatchTokens.muted)
                else -> PatchMoney(item.price, compact = true)
            }
        }
    }
}

@Composable
private fun DecorItemSheet(
    item: CosmeticItem,
    owned: Boolean,
    equipped: Boolean,
    coins: Int,
    busy: Boolean,
    message: String?,
    onDismiss: () -> Unit,
    onChoose: () -> Unit
) {
    val affordable = owned || item.price <= coins
    PatchSheet(item.name, onDismiss = onDismiss, footer = {
        Button(onClick = onChoose, enabled = !busy && !owned && affordable,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PatchTokens.red)) {
            if (busy) {
                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
            }
            Text("구매하기", style = MaterialTheme.typography.labelLarge)
        }
    }) {
        Box(Modifier.fillMaxWidth().height(132.dp).clip(RoundedCornerShape(PatchTokens.space12))
            .background(PatchTokens.soft), contentAlignment = Alignment.Center) {
            CosmeticImage(item.id, Modifier.size(120.dp))
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(if (owned) "보유한 꾸미기" else "구매 가격",
                style = MaterialTheme.typography.bodyMedium, color = PatchTokens.muted,
                modifier = Modifier.weight(1f))
            if (owned) {
                Text(if (equipped) "착용 중" else "보유 중",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (equipped) PatchTokens.red else PatchTokens.ink)
            } else PatchMoney(item.price, compact = true)
        }
        Text("구매 후 적용하기를 누르면 프로필에 반영돼요.",
            style = MaterialTheme.typography.bodySmall, color = PatchTokens.muted)
        if (!owned) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("보유 캐시", style = MaterialTheme.typography.bodyMedium,
                    color = PatchTokens.muted, modifier = Modifier.weight(1f))
                PatchMoney(coins, compact = true)
            }
            Text(
                if (affordable) "구매 후 잔액: ${coins - item.price} C"
                else "캐시가 ${item.price.toLong() - coins.toLong()} C 부족해요.",
                style = MaterialTheme.typography.bodySmall,
                color = if (affordable) PatchTokens.muted else PatchTokens.red
            )
        }
        message?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = if (owned) PatchTokens.muted else PatchTokens.red)
        }
    }
}
