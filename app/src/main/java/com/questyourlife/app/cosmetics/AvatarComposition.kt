package com.questyourlife.app.cosmetics

/** Coordinates are fractions of a square avatar canvas; independent of screen size. */
data class AvatarLayer(val itemId: Int?, val x: Float, val y: Float, val width: Float, val height: Float)
data class AvatarComposition(val layers: List<AvatarLayer>)

object AvatarComposer {
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
    fun compose(itemId: Int): AvatarComposition = AvatarComposition(
        listOfNotNull(AvatarLayer(null, 0f, .233f, 1f, .734f), adjustments[itemId]))
}
