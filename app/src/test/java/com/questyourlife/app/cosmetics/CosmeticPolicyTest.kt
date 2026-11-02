package com.questyourlife.app.cosmetics

import org.junit.Assert.*
import org.junit.Test

class CosmeticPolicyTest {
    @Test fun ownedLegacyItemCanBeEquippedWithoutBuyingAgain() {
        assertEquals(CosmeticDecision.Equip(4), CosmeticPolicy.decide(4, setOf(4), 0))
    }
    @Test fun unknownUnownedItemCannotBePurchased() {
        assertEquals(CosmeticDecision.Reject(CosmeticDecision.Reason.UNKNOWN_ITEM),
            CosmeticPolicy.decide(444, emptySet(), 9999))
    }
    @Test fun freeDefaultNeverDebitsCash() {
        assertEquals(CosmeticDecision.Equip(0), CosmeticPolicy.decide(0, emptySet(), 0))
    }
    @Test fun insufficientCashDoesNotReturnAPurchase() {
        assertEquals(CosmeticDecision.Reject(CosmeticDecision.Reason.INSUFFICIENT_CASH),
            CosmeticPolicy.decide(1001, emptySet(), 119))
    }
    @Test fun purchaseUsesCatalogPriceAndOwnedItemsStayFree() {
        val action = CosmeticPolicy.decide(1001, emptySet(), 120)
        assertTrue(action is CosmeticDecision.Buy)
        assertEquals(120, (action as CosmeticDecision.Buy).item.price)
        assertEquals(CosmeticDecision.Equip(1001), CosmeticPolicy.decide(1001, setOf(1001), 0))
    }
}
