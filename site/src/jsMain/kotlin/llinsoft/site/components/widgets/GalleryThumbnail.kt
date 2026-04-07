package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import llinsoft.site.components.utils.SvgGenerator
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Button as HtmlButton

/**
 * Generates overlay background CSS property for gallery thumbnails
 */
private fun overlayBackground(hovered: Boolean, selected: Boolean, radiusPx: Int, widthPx: Int, heightPx: Int): String {
    val idleStroke = SvgGenerator.svgStroke("rgba(120, 180, 245, 0.28)", 0.8, 1.0, widthPx, heightPx, radiusPx)
    val activeInner = SvgGenerator.svgStroke(
        if (selected) "rgba(160, 220, 255, 1)" else "rgba(150, 210, 255, 0.95)",
        1.2,
        1.0,
        widthPx,
        heightPx,
        radiusPx
    )
    val activeOuter = SvgGenerator.svgStroke("rgba(120, 190, 255, 0.55)", 2.0, 0.4, widthPx, heightPx, radiusPx)

    return if (hovered || selected) {
        "background-image: url(\"$activeInner\"), url(\"$activeOuter\")"
    } else {
        "background-image: url(\"$idleStroke\")"
    }
}

/**
 * Gallery Thumbnail - Interactive thumbnail button with SVG borders and glow
 *
 * Features:
 * - SVG-based dynamic borders (idle/hover/selected states)
 * - Drop-shadow glow effect (500ms transition for Safari compatibility)
 * - Hover lift animation
 * - Customizable size and border radius
 *
 * @param imageUrl URL to thumbnail image
 * @param alt Alt text for accessibility
 * @param ariaLabel ARIA label for button
 * @param width Thumbnail width (CSS string, e.g., "7.3rem")
 * @param height Thumbnail height (CSS string, e.g., "12.8rem")
 * @param borderRadius Border radius (CSS string, e.g., "0.62rem")
 * @param borderRadiusPx Border radius in pixels for SVG (default: 10)
 * @param estimatedWidthPx Estimated width in pixels for SVG viewBox (default: 117 for 7.3rem)
 * @param estimatedHeightPx Estimated height in pixels for SVG viewBox (default: 205 for 12.8rem)
 * @param isSelected Whether this thumbnail is currently selected
 * @param onClick Click handler
 */
@Composable
fun GalleryThumbnail(
    imageUrl: String,
    alt: String,
    ariaLabel: String,
    width: String = "7.3rem",
    height: String = "12.8rem",
    borderRadius: String = "0.62rem",
    borderRadiusPx: Int = 10,
    estimatedWidthPx: Int = 117,
    estimatedHeightPx: Int = 205,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    var hovered by remember(imageUrl) { mutableStateOf(false) }

    val glowFilter = when {
        hovered -> "drop-shadow(0 0 12px rgba(90, 170, 255, 0.38))"
        isSelected -> "drop-shadow(0 0 10px rgba(90, 170, 255, 0.28))"
        else -> "none"
    }

    val overlayBg = remember(hovered, isSelected, borderRadiusPx, estimatedWidthPx, estimatedHeightPx) {
        overlayBackground(hovered, isSelected, borderRadiusPx, estimatedWidthPx, estimatedHeightPx)
    }

    Div(
        attrs = Modifier.toAttrs {
            style {
                property("display", "inline-block")
                property("border-radius", borderRadius)
                property("filter", glowFilter)
                property("transition", "filter 500ms ease, transform 180ms ease")
                property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
            }
            onMouseEnter { hovered = true }
            onMouseLeave { hovered = false }
        }
    ) {
        HtmlButton(
            attrs = {
                attr("type", "button")
                attr("aria-label", ariaLabel)
                style {
                    property("width", width)
                    property("min-width", width)
                    property("height", height)
                    property("border-radius", borderRadius)
                    property("overflow", "hidden")
                    property("background", "#121b2f")
                    property("box-sizing", "border-box")
                    property("flex", "0 0 auto")
                    property("cursor", "pointer")
                    property("transition", "all 180ms ease")
                    property("position", "relative")
                    property("border", "0")
                    property("padding", "0")
                    property("display", "inline-block")
                }
                onClick { onClick() }
            }
        ) {
            Img(
                src = imageUrl,
                alt = alt,
                attrs = {
                    style {
                        property("width", "100%")
                        property("height", "100%")
                        property("display", "block")
                        property("object-fit", "cover")
                    }
                }
            )

            // SVG border overlay
            Div(
                attrs = {
                    attr(
                        "style",
                        "position: absolute; inset: 0; width: 100%; height: 100%; pointer-events: none; background-size: 100% 100%; background-repeat: no-repeat; $overlayBg"
                    )
                }
            ) {}
        }
    }
}
