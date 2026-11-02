package com.questyourlife.app.cosmetics

import org.junit.Assert.*
import org.junit.Test

class AvatarCompositionTest {
    @Test fun unsupportedItemKeepsFaceWithoutSubstitutingAnAccessory() {
        val layers = AvatarComposer.compose(1002).layers
        assertEquals(1, layers.size)
        assertNull(layers.single().itemId)
    }

    @Test fun everyRegisteredAccessoryIsComposedAboveTheSameFace() {
        val face = AvatarComposer.compose(-1).layers.single()
        listOf(0, 1, 2, 3, 4, 5, 1001, 1003, 1004).forEach { id ->
            val layers = AvatarComposer.compose(id).layers
            assertEquals(face, layers.first())
            assertEquals(id, layers.last().itemId)
            assertEquals(2, layers.size)
            assertTrue(layers.last().width > 0f && layers.last().height > 0f)
        }
    }

    @Test fun differentSilhouettesHaveIndependentCalibration() {
        val beanie = AvatarComposer.compose(0).layers.last()
        val cap = AvatarComposer.compose(1).layers.last()
        val bucket = AvatarComposer.compose(5).layers.last()
        assertNotEquals(beanie.y, cap.y)
        assertNotEquals(cap.height, bucket.height)
    }
}
