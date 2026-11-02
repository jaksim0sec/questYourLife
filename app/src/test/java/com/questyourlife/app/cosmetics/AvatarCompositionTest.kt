package com.questyourlife.app.cosmetics

import org.junit.Assert.*
import org.junit.Test

class AvatarCompositionTest {
    private fun equipment(id: Int): AvatarLayer = AvatarComposer.compose(id).layers.last()

    @Test fun faceIsIdenticalForAllItems() {
        val baseFace = AvatarComposer.compose(-1).layers.single()
        listOf(0, 1, 2, 3, 4, 5, 1001, 1003, 1004).forEach { id ->
            val layers = AvatarComposer.compose(id).layers
            assertEquals(2, layers.size)
            assertEquals(baseFace, layers.first())
            assertEquals(id, layers.last().itemId)
            assertTrue(layers.last().width > 0 && layers.last().height > 0)
        }
    }

    @Test fun navyReferenceNeverMovesAndForwardCapsShareItsSize() {
        val navy = equipment(2)
        assertEquals(.065f, navy.x, .00001f)
        assertEquals(.015f, navy.y, .00001f)
        assertEquals(.87f, navy.width, .00001f)
        assertEquals(.582f, navy.height, .00001f)
        listOf(1, 3).forEach {
            val cap = equipment(it)
            assertEquals(navy.x, cap.x, .00001f)
            assertEquals(navy.width, cap.width, .00001f)
            assertEquals(navy.height, cap.height, .00001f)
            assertEquals(navy.y + .005f, cap.y, .00001f)
        }
    }

    @Test fun knitAndBucketHatsAreLowerAndHaveIndependentFits() {
        val beanie = equipment(0)
        val bucket = equipment(5)
        assertEquals(.012f, beanie.y, .00001f)
        assertEquals(.612f, beanie.height, .00001f)
        assertEquals(-.018f, bucket.y, .00001f)
        assertEquals(.656f, bucket.height, .00001f)
        assertNotEquals(beanie.width, bucket.width)
    }

    @Test fun bothHeadphonesHaveCenteredTighterPlacement() {
        val cream = equipment(1001)
        val black = equipment(1003)
        assertEquals(.030f, cream.x, .00001f)
        assertEquals(.175f, cream.y, .00001f)
        assertEquals(.94f, cream.width, .00001f)
        assertEquals(.730f, cream.height, .00001f)
        assertEquals(.5f, cream.x + cream.width / 2f, .00001f)
        assertEquals(cream.x, black.x, .00001f)
        assertEquals(cream.y, black.y, .00001f)
        assertEquals(cream.width, black.width, .00001f)
        assertEquals(cream.height, black.height, .00001f)
    }

    @Test fun backwardsCapAndEarbudsKeepTheirExistingPosition() {
        assertEquals(AvatarLayer(4, .105f, -.020f, .78f, .612f), equipment(4))
        assertEquals(AvatarLayer(1004, .025f, .480f, .95f, .360f), equipment(1004))
    }

    @Test fun unsupportedItemsDoNotReceiveInventedAccessories() {
        val layers = AvatarComposer.compose(1002).layers
        assertEquals(1, layers.size)
        assertNull(layers.single().itemId)
    }
}
