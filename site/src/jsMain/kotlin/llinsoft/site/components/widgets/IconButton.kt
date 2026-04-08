package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import llinsoft.site.components.utils.SvgGenerator
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for icon buttons
 */
private object IconButtonTokens {
    const val radiusPx = 50 // Full circle

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
 * Generates button overlay based on state
 */
private fun buttonOverlay(state: String, width: Int, height: Int, radiusPx: Int): String {
    return when (state) {
        "idle" -> {
            val idle = SvgGenerator.svgStroke(
                IconButtonTokens.idleStroke,
                IconButtonTokens.idleStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            "url(\"$idle\")"
        }
        "hover" -> {
            val inner = SvgGenerator.svgStroke(
                IconButtonTokens.hoverInner,
                IconButtonTokens.hoverInnerStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                IconButtonTokens.hoverOuter,
                IconButtonTokens.hoverOuterStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            "url(\"$inner\"), url(\"$outer\")"
        }
        "active" -> {
            val inner = SvgGenerator.svgStroke(
                IconButtonTokens.activeInner,
                IconButtonTokens.activeInnerStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                IconButtonTokens.activeOuter,
                IconButtonTokens.activeOuterStrokeWidth,
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

/**
 * Icon Button - Small circular button for icons (close, arrows, navigation controls)
 *
 * Features:
 * - SVG-based dynamic borders with hover/active states
 * - Drop-shadow glow effect (500ms transition for Safari compatibility)
 * - Default size: 50x50px, customizable
 *
 * @param ariaLabel Accessibility label for the button
 * @param onClick Click handler
 * @param width Button width in pixels (default: 50)
 * @param height Button height in pixels (default: 50)
 * @param content Button content (typically an icon or single character)
 */
@Composable
fun IconButton(
    ariaLabel: String,
    onClick: () -> Unit,
    width: Int = 50,
    height: Int = 50,
    content: @Composable () -> Unit
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
        pressed -> "drop-shadow(0 0 18px rgba(90, 170, 255, 0.7))"
        hovered -> "drop-shadow(0 0 14px rgba(90, 170, 255, 0.5))"
        else -> "none"
    }

    val overlayBg = buttonOverlay(state, width, height, IconButtonTokens.radiusPx)

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
                    property("border-radius", "${IconButtonTokens.radiusPx}%")
                    property("filter", glowFilter)
                    property("transition", "filter 500ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .size(width.px, height.px)
                .padding(0.px)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(IconButtonTokens.radiusPx.percent)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    attr("aria-label", ariaLabel)
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                        property("-webkit-appearance", "none")
                        property("font-size", "1.1rem")
                        property("line-height", "1")
                        property("font-family", "Inter, Roboto, Segoe UI, Helvetica Neue, Arial, sans-serif")
                    }
                    onClick { onClick() }
                    onMouseDown { pressed = true }
                    onMouseUp { pressed = false }
                }
        ) {
            content()

            // SVG overlay
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    width(100.percent)
                    height(100.percent)
                    property("border-radius", "${IconButtonTokens.radiusPx}%")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}
