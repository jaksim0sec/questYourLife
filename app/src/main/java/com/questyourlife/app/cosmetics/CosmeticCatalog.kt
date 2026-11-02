package com.questyourlife.app.cosmetics

import com.questyourlife.app.data.OwnedItemEntity

enum class CosmeticCategory { HAT, ACCESSORY }
data class CosmeticItem(val id: Int, val name: String, val category: CosmeticCategory, val price: Int)

/** IDs 0..5 belong to existing purchases; newly introduced items use a separate range. */
object CosmeticCatalog {
    val items = listOf(
        CosmeticItem(0, "골지 비니", CosmeticCategory.HAT, 0),
        CosmeticItem(1001, "크림 헤드폰", CosmeticCategory.ACCESSORY, 120),
        CosmeticItem(2, "블루 캡", CosmeticCategory.HAT, 80),
        CosmeticItem(1002, "라임 별", CosmeticCategory.ACCESSORY, 60),
        CosmeticItem(1, "크림 캡", CosmeticCategory.HAT, 50),
        CosmeticItem(3, "차콜 캡", CosmeticCategory.HAT, 100),
        CosmeticItem(4, "백워드 캡", CosmeticCategory.HAT, 120),
        CosmeticItem(5, "버킷 햇", CosmeticCategory.HAT, 150),
        CosmeticItem(1003, "블랙 헤드폰", CosmeticCategory.ACCESSORY, 140),
        CosmeticItem(1004, "화이트 이어버드", CosmeticCategory.ACCESSORY, 90)
    )
    fun find(id: Int): CosmeticItem? = items.firstOrNull { it.id == id }
    fun ownedItem(record: OwnedItemEntity): CosmeticItem =
        find(record.itemId)?.copy(price = record.price)
            ?: CosmeticItem(record.itemId, "기존 모자 ${record.itemId}", CosmeticCategory.HAT, record.price)
}
