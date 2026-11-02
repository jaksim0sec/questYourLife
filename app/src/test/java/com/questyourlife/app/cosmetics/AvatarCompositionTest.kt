package com.questyourlife.app.cosmetics

import org.junit.Assert.*
import org.junit.Test

class AvatarCompositionTest {
    @Test fun accessoryCorrectionsKeepFaceAndReferenceCapUnchanged() {
        val referenceFace = AvatarComposer.compose(-1).layers.single()
        listOf(0, 1, 2, 4, 5, 1001, 1003).forEach {
            assertEquals(referenceFace, AvatarComposer.compose(it).layers.first())
        }
        val blue = AvatarComposer.compose(2).layers.last()
        assertEquals(.065f, blue.x, .00001f)
        assertEquals(.015f, blue.y, .00001f)
        assertEquals(.87f, blue.width, .00001f)
        val cap = AvatarComposer.compose(1).layers.last()
        val alternate = AvatarComposer.compose(1, alternateCreamCap = true).layers.last()
        assertEquals(-1f / 100f, alternate.x - cap.x, .00001f)
        assertEquals(cap.y, alternate.y, .00001f)
        assertEquals(cap.width, alternate.width, .00001f)
        assertEquals(.81f * 1.02f, AvatarComposer.compose(0).layers.last().width, .00001f)
        assertEquals(1.04f * .92f, AvatarComposer.compose(1001).layers.last().width, .00001f)
    }

    @Test fun eachAccessoryMovesAndScalesFromItsPreviousCenter() {
        data class Case(val id: Int, val x: Float, val y: Float, val w: Float, val h: Float,
            val dx: Float, val dy: Float, val scale: Float, val alternate: Boolean = false)
        val cases = listOf(
            Case(1003, -.02f, .155f, 1.04f, .765f, 3f, -7f, .95f),
            Case(0, .095f, -.015f, .81f, .601f, -1f, -5f, 1.02f),
            Case(1, .075f, -.020f, .85f, .605f, -4f, -6f, 1f),
            Case(1, .075f, -.020f, .85f, .605f, -5f, -6f, 1f, true),
            Case(2, .065f, .015f, .87f, .582f, 0f, 0f, 1f),
            Case(1001, -.02f, .145f, 1.04f, .817f, 1f, -9f, .92f),
            Case(5, .09f, -.055f, .82f, .638f, 2f, -4f, 1.03f),
            Case(4, .11f, -.025f, .78f, .612f, -1f, 1f, 1f))
        val face = AvatarComposer.compose(-1).layers.single()
        cases.forEach { c ->
            val layers = AvatarComposer.compose(c.id, c.alternate).layers
            assertEquals(face, layers.first())
            val layer = layers.last()
            assertEquals(c.w * c.scale, layer.width, .00001f)
            assertEquals(c.h * c.scale, layer.height, .00001f)
            assertEquals(c.dx / 100f, layer.x + layer.width / 2f - c.x - c.w / 2f, .00001f)
            assertEquals(c.dy / 100f, layer.y + layer.height / 2f - c.y - c.h / 2f, .00001f)
        }
    }
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
