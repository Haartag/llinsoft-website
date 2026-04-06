package llinsoft.site.components.utils

/**
 * Shared SVG generation utility for creating dynamic borders and overlays
 *
 * Centralizes SVG encoding and caching logic used across components:
 * - ProjectCard
 * - PrimaryButton, SecondaryButton, IconButton
 * - GalleryThumbnail
 * - SegmentedControl
 */
object SvgGenerator {
    private val cache = mutableMapOf<String, String>()

    /**
     * Encodes SVG string for use in data URI
     *
     * @param svg Raw SVG markup
     * @return URL-encoded SVG safe for data URI
     */
    private fun encodeSvg(svg: String): String {
        return svg
            .replace("%", "%25")
            .replace("#", "%23")
            .replace("<", "%3C")
            .replace(">", "%3E")
            .replace("\"", "%22")
            .replace("'", "%27")
            .replace(" ", "%20")
    }

    /**
     * Generates SVG stroke border with caching
     *
     * @param stroke Stroke color (CSS color value)
     * @param strokeWidth Stroke width in pixels
     * @param opacity Stroke opacity (0.0 - 1.0)
     * @param width Total SVG width in pixels
     * @param height Total SVG height in pixels
     * @param radiusPx Border radius in pixels
     * @return Data URI string ready for use in CSS
     */
    fun svgStroke(
        stroke: String,
        strokeWidth: Double,
        opacity: Double,
        width: Int,
        height: Int,
        radiusPx: Int
    ): String {
        val key = "$stroke|$strokeWidth|$opacity|$width|$height|$radiusPx"
        return cache.getOrPut(key) {
            val inset = 0.75
            val rectW = width - (inset * 2)
            val rectH = height - (inset * 2)

            val svg = """<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 $width $height' preserveAspectRatio='none'><rect x='$inset' y='$inset' width='$rectW' height='$rectH' rx='$radiusPx' ry='$radiusPx' fill='none' stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/></svg>""".replace("\n", "")

            "data:image/svg+xml;utf8,${encodeSvg(svg)}"
        }
    }

    /**
     * Clears the SVG cache (useful for testing or memory management)
     */
    fun clearCache() {
        cache.clear()
    }

    /**
     * Gets current cache size
     */
    fun getCacheSize(): Int = cache.size
}
