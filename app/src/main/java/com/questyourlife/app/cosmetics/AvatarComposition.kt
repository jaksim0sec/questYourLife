package com.questyourlife.app.cosmetics

/** Coordinates are fractions of a square avatar canvas; independent of screen size. */
data class AvatarLayer(val itemId: Int?, val x: Float, val y: Float, val width: Float, val height: Float)
data class AvatarComposition(val layers: List<AvatarLayer>)

object AvatarComposer {
    // Pixel offsets are measured against the approximately 100 px avatar in the latest member sheet.
    private data class Adjustment(val itemId: Int, val dx: Float, val dy: Float, val scale: Float)
    private val corrections = mapOf(
        1003 to Adjustment(1003, 3f, -7f, .95f),
        0 to Adjustment(0, -1f, -5f, 1.02f),
        1 to Adjustment(1, -4f, -6f, 1f),
        1001 to Adjustment(1001, 1f, -9f, .92f),
        5 to Adjustment(5, 2f, -4f, 1.03f),
        4 to Adjustment(4, -1f, 1f, 1f)
    )
    private val adjustments = mapOf(
        0 to AvatarLayer(0, .095f, -.015f, .81f, .601f),
        1 to AvatarLayer(1, .075f, -.020f, .85f, .605f),
        2 to AvatarLayer(2, .065f, .015f, .87f, .582f),
        3 to AvatarLayer(3, .075f, -.010f, .85f, .573f),
        4 to AvatarLayer(4, .11f, -.025f, .78f, .612f),
        5 to AvatarLayer(5, .09f, -.055f, .82f, .638f),
        1001 to AvatarLayer(1001, -.02f, .145f, 1.04f, .817f),
        1003 to AvatarLayer(1003, -.02f, .155f, 1.04f, .765f),
        1004 to AvatarLayer(1004, .025f, .48f, .95f, .360f)
    )

    /** Returns a render tree: face first, then the registered accessory, with per-item calibration. */
    fun compose(itemId: Int, alternateCreamCap: Boolean = false): AvatarComposition {
        val original = adjustments[itemId]
        val adjustment = if (itemId == 1 && alternateCreamCap)
            Adjustment(1, -5f, -6f, 1f) else corrections[itemId]
        val accessory = if (original != null && adjustment != null) original.copy(
            x = original.x + adjustment.dx / 100f + original.width * (1f - adjustment.scale) / 2f,
            y = original.y + adjustment.dy / 100f + original.height * (1f - adjustment.scale) / 2f,
            width = original.width * adjustment.scale,
            height = original.height * adjustment.scale
        ) else original
        return AvatarComposition(listOfNotNull(AvatarLayer(null, 0f, .233f, 1f, .734f), accessory))
    }
}
