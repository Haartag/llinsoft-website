package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import llinsoft.site.components.utils.SvgGenerator
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for secondary buttons
 */
private object SecondaryButtonTokens {
    const val radiusPx = 8
    const val defaultWidth = 160
    const val defaultHeight = 42

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
                SecondaryButtonTokens.idleStroke,
                SecondaryButtonTokens.idleStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            "url(\"$idle\")"
        }
        "hover" -> {
            val inner = SvgGenerator.svgStroke(
                SecondaryButtonTokens.hoverInner,
                SecondaryButtonTokens.hoverInnerStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                SecondaryButtonTokens.hoverOuter,
                SecondaryButtonTokens.hoverOuterStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            "url(\"$inner\"), url(\"$outer\")"
        }
        "active" -> {
            val inner = SvgGenerator.svgStroke(
                SecondaryButtonTokens.activeInner,
                SecondaryButtonTokens.activeInnerStrokeWidth,
                1.0,
                width,
                height,
                radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                SecondaryButtonTokens.activeOuter,
                SecondaryButtonTokens.activeOuterStrokeWidth,
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
 * Secondary Button - Medium secondary action button
 *
 * Features:
 * - SVG-based dynamic borders with hover/active states
 * - Drop-shadow glow effect (500ms transition for Safari compatibility)
 * - TranslateY animation on hover
 * - Default size: 160x42px, customizable
 *
 * @param text Button text
 * @param onClick Click handler
 * @param width Button width in pixels (default: 160)
 * @param height Button height in pixels (default: 42)
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    width: Int = SecondaryButtonTokens.defaultWidth,
    height: Int = SecondaryButtonTokens.defaultHeight
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

    val overlayBg = buttonOverlay(state, width, height, SecondaryButtonTokens.radiusPx)

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
                    property("border-radius", "${SecondaryButtonTokens.radiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-1px)" else "translateY(0px)")
                    property("transition", "filter 500ms ease, transform ${SecondaryButtonTokens.transitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .width(width.px)
                .height(height.px)
                .padding(leftRight = 0.9.cssRem, topBottom = 0.6.cssRem)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(SecondaryButtonTokens.radiusPx.px)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                        property("font-size", "0.9rem")
                        property("font-weight", "500")
                        property("color", "${palette.textPrimary}")
                    }
                    onClick { onClick() }
                    onMouseDown { pressed = true }
                    onMouseUp { pressed = false }
                }
        ) {
            SpanText(text)

            // SVG overlay
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    width(100.percent)
                    height(100.percent)
                    property("border-radius", "${SecondaryButtonTokens.radiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}
