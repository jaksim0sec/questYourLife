package com.questyourlife.app.cosmetics

/** Coordinates are fractions of a square avatar canvas; independent of screen size. */
data class AvatarLayer(val itemId: Int?, val x: Float, val y: Float, val width: Float, val height: Float)
data class AvatarComposition(val layers: List<AvatarLayer>)

object AvatarComposer {
    private val adjustments = mapOf(
        0 to AvatarLayer(0, .105f, -.055f, .79f, .586f),
        1 to AvatarLayer(1, .10f, -.065f, .80f, .569f),
        2 to AvatarLayer(2, .075f, -.005f, .85f, .569f),
        3 to AvatarLayer(3, .085f, -.045f, .83f, .560f),
        4 to AvatarLayer(4, .13f, -.065f, .74f, .581f),
        5 to AvatarLayer(5, .10f, -.09f, .80f, .622f),
        1001 to AvatarLayer(1001, -.02f, .10f, 1.04f, .817f),
        1003 to AvatarLayer(1003, -.03f, .13f, 1.06f, .780f),
        1004 to AvatarLayer(1004, .025f, .48f, .95f, .360f)
    )

    /** Returns a render tree: face first, then the registered accessory, with per-item calibration. */
    fun compose(itemId: Int): AvatarComposition = AvatarComposition(
        listOfNotNull(AvatarLayer(null, 0f, .233f, 1f, .734f), adjustments[itemId]))
}
