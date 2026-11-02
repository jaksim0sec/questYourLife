package com.questyourlife.app.cosmetics

sealed interface CosmeticDecision {
    data class Equip(val id: Int) : CosmeticDecision
    data class Buy(val item: CosmeticItem) : CosmeticDecision
    data class Reject(val reason: Reason) : CosmeticDecision
    enum class Reason { UNKNOWN_ITEM, INSUFFICIENT_CASH }
}

object CosmeticPolicy {
    fun decide(itemId: Int, ownedIds: Set<Int>, balance: Int): CosmeticDecision {
        if (itemId == 0 || itemId in ownedIds) return CosmeticDecision.Equip(itemId)
        val item = CosmeticCatalog.find(itemId)
            ?: return CosmeticDecision.Reject(CosmeticDecision.Reason.UNKNOWN_ITEM)
        if (balance < item.price) return CosmeticDecision.Reject(CosmeticDecision.Reason.INSUFFICIENT_CASH)
        return CosmeticDecision.Buy(item)
    }
}
