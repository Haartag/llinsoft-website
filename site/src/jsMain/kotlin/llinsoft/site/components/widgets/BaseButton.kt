package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import llinsoft.site.components.utils.SvgGenerator
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens interface for button styling
 */
internal interface ButtonTokens {
    val radiusPx: Int
    val defaultWidth: Int
    val defaultHeight: Int
    val paddingLeftRight: CSSNumericValue<CSSUnit.rem>
    val paddingTopBottom: CSSNumericValue<CSSUnit.rem>
    val fontSize: CSSNumericValue<CSSUnit.rem>
    val gap: CSSNumericValue<CSSUnit.rem>?

    // Colors
    val idleStroke: String
    val hoverInner: String
    val hoverOuter: String
    val activeInner: String
    val activeOuter: String

    // Stroke widths
    val idleStrokeWidth: Double
    val hoverInnerStrokeWidth: Double
    val hoverOuterStrokeWidth: Double
    val activeInnerStrokeWidth: Double
    val activeOuterStrokeWidth: Double

    // Effects
    val transitionMs: Int
    val hoverGlow: String
    val activeGlow: String
    val hoverTranslateY: String
}

/**
 * Generates button overlay based on state and tokens
 */
private fun buttonOverlay(
    state: String,
    width: Int,
    height: Int,
    tokens: ButtonTokens
): String {
    return when (state) {
        "idle" -> {
            val idle = SvgGenerator.svgStroke(
                tokens.idleStroke,
                tokens.idleStrokeWidth,
                1.0,
                width,
                height,
                tokens.radiusPx
            )
            "url(\"$idle\")"
        }
        "hover" -> {
            val inner = SvgGenerator.svgStroke(
                tokens.hoverInner,
                tokens.hoverInnerStrokeWidth,
                1.0,
                width,
                height,
                tokens.radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                tokens.hoverOuter,
                tokens.hoverOuterStrokeWidth,
                1.0,
                width,
                height,
                tokens.radiusPx
            )
            "url(\"$inner\"), url(\"$outer\")"
        }
        "active" -> {
            val inner = SvgGenerator.svgStroke(
                tokens.activeInner,
                tokens.activeInnerStrokeWidth,
                1.0,
                width,
                height,
                tokens.radiusPx
            )
            val outer = SvgGenerator.svgStroke(
                tokens.activeOuter,
                tokens.activeOuterStrokeWidth,
                1.0,
                width,
                height,
                tokens.radiusPx
            )
            "url(\"$inner\"), url(\"$outer\")"
        }
        else -> "none"
    }
}

/**
 * Base Button - Shared implementation for all button variants
 *
 * Features:
 * - SVG-based dynamic borders with hover/active states
 * - Drop-shadow glow effect (500ms transition for Safari compatibility)
 * - TranslateY animation on hover
 * - Optional icon support (left or right position)
 *
 * @param text Button text
 * @param onClick Click handler
 * @param tokens Design tokens for styling
 * @param width Button width in pixels (default: from tokens)
 * @param height Button height in pixels (default: from tokens)
 * @param iconSrc Optional icon image path
 * @param iconFirst If true, icon appears before text; if false, after text
 */
@Composable
internal fun BaseButton(
    text: String,
    onClick: () -> Unit,
    tokens: ButtonTokens,
    width: Int = tokens.defaultWidth,
    height: Int = tokens.defaultHeight,
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
        pressed -> tokens.activeGlow
        hovered -> tokens.hoverGlow
        else -> "none"
    }

    val overlayBg = buttonOverlay(state, width, height, tokens)

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
                    property("border-radius", "${tokens.radiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(${tokens.hoverTranslateY})" else "translateY(0px)")
                    property("transition", "filter 500ms ease, transform ${tokens.transitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .width(width.px)
                .height(height.px)
                .padding(leftRight = tokens.paddingLeftRight, topBottom = tokens.paddingTopBottom)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(tokens.radiusPx.px)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                        if (tokens.gap != null) {
                            property("gap", tokens.gap.toString())
                        }
                        property("font-size", tokens.fontSize.toString())
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
                    property("border-radius", "${tokens.radiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}
