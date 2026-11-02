package com.questyourlife.app.companion

enum class ClanEmblemIcon(val path: String) {
    STAR("M12.96 15.84H26.64M16.38 9.916L23.22 21.764M16.38 21.764L23.22 9.916")
}

enum class ClanEmblemPalette(val flag: String, val ink: String, val pole: String) {
    LIME("#D9E779", "#151715", "#53533D")
}

/** The SVG export and native renderer consume these same paths and palette. */
data class ClanEmblem(
    val icon: ClanEmblemIcon = ClanEmblemIcon.STAR,
    val palette: ClanEmblemPalette = ClanEmblemPalette.LIME
) {
    fun toSvg(): String = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36"><path d="$POLE" fill="none" stroke="${palette.pole}" stroke-width="1.98" stroke-linecap="round"/><path d="$FLAG" fill="${palette.flag}"/><path d="${icon.path}" fill="none" stroke="${palette.ink}" stroke-width="3.24" stroke-linecap="round"/></svg>"""

    companion object {
        const val POLE = "M4.32 2.16V34.56"
        const val FLAG = "M6.12 2.88L34.2 5.04V28.8L6.12 26.28Z"
    }
}
