package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for primary buttons
 */
private object PrimaryButtonTokens {
    const val radiusPx = 10
    const val defaultWidth = 220
    const val defaultHeight = 54

    // Button colors
    const val idleStroke = "rgba(120, 180, 245, 0.35)"
    const val hoverInner = "rgba(150, 210, 255, 1.0)"
    const val hoverOuter = "rgba(120, 190, 255, 0.65)"
    const val activeInner = "rgba(90, 170, 255, 1.0)"
    const val activeOuter = "rgba(60, 140, 235, 0.75)"

    // Stroke widths
    const val idleStrokeWidth = 1.0
    const val hoverInnerStrokeWidth = 1.4
    const val hoverOuterStrokeWidth = 2.2
    const val activeInnerStrokeWidth = 1.6
    const val activeOuterStrokeWidth = 2.8

    // Transition
    const val transitionMs = 160
}

/**
 * SVG generator for primary button borders
 */
private object PrimaryButtonSvgGenerator {
    private val cache = mutableMapOf<String, String>()

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

    fun svgStroke(stroke: String, strokeWidth: Double, opacity: Double, width: Int, height: Int, radiusPx: Int): String {
        val key = "$stroke|$strokeWidth|$opacity|$width|$height|$radiusPx"
        return cache.getOrPut(key) {
            val inset = 0.75
            val rectW = width - (inset * 2)
            val rectH = height - (inset * 2)

            val svg = """<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 $width $height' preserveAspectRatio='none'><rect x='$inset' y='$inset' width='$rectW' height='$rectH' rx='$radiusPx' ry='$radiusPx' fill='none' stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/></svg>""".replace("\n", "")

            "data:image/svg+xml;utf8,${encodeSvg(svg)}"
        }
    }

    fun buttonOverlay(state: String, width: Int, height: Int, radiusPx: Int): String {
        return when (state) {
            "idle" -> {
                val idle = svgStroke(
                    PrimaryButtonTokens.idleStroke,
                    PrimaryButtonTokens.idleStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                "url(\"$idle\")"
            }
            "hover" -> {
                val inner = svgStroke(
                    PrimaryButtonTokens.hoverInner,
                    PrimaryButtonTokens.hoverInnerStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                val outer = svgStroke(
                    PrimaryButtonTokens.hoverOuter,
                    PrimaryButtonTokens.hoverOuterStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                "url(\"$inner\"), url(\"$outer\")"
            }
            "active" -> {
                val inner = svgStroke(
                    PrimaryButtonTokens.activeInner,
                    PrimaryButtonTokens.activeInnerStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                val outer = svgStroke(
                    PrimaryButtonTokens.activeOuter,
                    PrimaryButtonTokens.activeOuterStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                "url(\"$inner\"), url(\"$outer\")"
            }
            else -> "none"
        }
    }
}

/**
 * Primary Button - Large primary action button with optional icon
 *
 * Features:
 * - SVG-based dynamic borders with hover/active states
 * - Drop-shadow glow effect (500ms transition for Safari compatibility)
 * - TranslateY animation on hover
 * - Optional icon support (left or right position)
 * - Default size: 220x54px, customizable
 *
 * @param text Button text
 * @param onClick Click handler
 * @param width Button width in pixels (default: 220)
 * @param height Button height in pixels (default: 54)
 * @param iconSrc Optional icon image path
 * @param iconFirst If true, icon appears before text; if false, after text
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    width: Int = PrimaryButtonTokens.defaultWidth,
    height: Int = PrimaryButtonTokens.defaultHeight,
    iconSrc: String? = null,
    iconFirst: Boolean = true
) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val state = when {
        pressed -> "active"
        hovered -> "hover"
        else -> "idle"
    }

    val glowFilter = when {
        pressed -> "drop-shadow(0 0 20px rgba(90, 170, 255, 0.75))"
        hovered -> "drop-shadow(0 0 16px rgba(90, 170, 255, 0.55))"
        else -> "none"
    }

    val overlayBg = PrimaryButtonSvgGenerator.buttonOverlay(state, width, height, PrimaryButtonTokens.radiusPx)

    Div(
        attrs = Modifier
            .display(DisplayStyle.InlineBlock)
            .onMouseEnter { hovered = true }
            .onMouseLeave {
                hovered = false
                pressed = false
            }
            .toAttrs {
                style {
                    property("border-radius", "${PrimaryButtonTokens.radiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    property("transition", "filter 500ms ease, transform ${PrimaryButtonTokens.transitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .width(width.px)
                .height(height.px)
                .padding(leftRight = 1.2.cssRem, topBottom = 0.8.cssRem)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(PrimaryButtonTokens.radiusPx.px)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                        property("gap", "0.7rem")
                        property("font-size", "1rem")
                        property("font-weight", "500")
                        property("color", "${palette.textPrimary}")
                    }
                    onClick { onClick() }
                    onMouseDown { pressed = true }
                    onMouseUp { pressed = false }
                }
        ) {
            if (iconSrc != null && iconFirst) {
                Image(iconSrc, "", Modifier.size(1.1.cssRem).display(DisplayStyle.Block))
            }
            SpanText(text)
            if (iconSrc != null && !iconFirst) {
                Image(iconSrc, "", Modifier.size(1.1.cssRem).display(DisplayStyle.Block))
            }

            // SVG overlay
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    width(100.percent)
                    height(100.percent)
                    property("border-radius", "${PrimaryButtonTokens.radiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}
