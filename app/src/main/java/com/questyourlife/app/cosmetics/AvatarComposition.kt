package com.questyourlife.app.cosmetics

/** Normalized square-canvas coordinates shared by every use of an equipment ID. */
data class AvatarLayer(val itemId: Int?, val x: Float, val y: Float, val width: Float, val height: Float)
data class AvatarComposition(val layers: List<AvatarLayer>)

object AvatarComposer {
    private val face = AvatarLayer(null, 0f, .233f, 1f, .734f)

    // Equipment placement only: never special-case a friend's name or a screen.
    // The navy cap (2) is the unchanged baseline for the other forward caps.
    private val equipment = mapOf(
        0 to AvatarLayer(0, .075f, .012f, .85f, .612f),    // Beanie: lowered band
        1 to AvatarLayer(1, .065f, .020f, .87f, .582f),   // Cream cap: navy size / lower brim
        2 to AvatarLayer(2, .065f, .015f, .87f, .582f),   // Navy cap: reference, unchanged
        3 to AvatarLayer(3, .065f, .020f, .87f, .582f),   // Charcoal cap: same fit
        4 to AvatarLayer(4, .105f, -.020f, .78f, .612f),  // Backward cap: preserve fit
        5 to AvatarLayer(5, .0675f, -.018f, .865f, .656f), // Bucket hat: lower brim
        1001 to AvatarLayer(1001, .030f, .175f, .94f, .730f), // Cream headphones
        1003 to AvatarLayer(1003, .030f, .175f, .94f, .730f), // Black headphones
        1004 to AvatarLayer(1004, .025f, .480f, .95f, .360f) // Earbuds: unchanged
    )

    /** Every owner of the same item gets identical geometry at any avatar size. */
    fun compose(itemId: Int): AvatarComposition =
        AvatarComposition(listOfNotNull(face, equipment[itemId]))
}
