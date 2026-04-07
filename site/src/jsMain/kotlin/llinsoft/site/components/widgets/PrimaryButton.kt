package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*

/**
 * Design tokens for primary buttons
 */
private object PrimaryButtonTokens : ButtonTokens {
    override val radiusPx = 10
    override val defaultWidth = 220
    override val defaultHeight = 54
    override val paddingLeftRight = 1.2.cssRem
    override val paddingTopBottom = 0.8.cssRem
    override val fontSize = 1.cssRem
    override val gap: CSSNumericValue<CSSUnit.rem> = 0.7.cssRem

    // Colors
    override val idleStroke = "rgba(120, 180, 245, 0.35)"
    override val hoverInner = "rgba(150, 210, 255, 1.0)"
    override val hoverOuter = "rgba(120, 190, 255, 0.65)"
    override val activeInner = "rgba(90, 170, 255, 1.0)"
    override val activeOuter = "rgba(60, 140, 235, 0.75)"

    // Stroke widths
    override val idleStrokeWidth = 1.0
    override val hoverInnerStrokeWidth = 1.4
    override val hoverOuterStrokeWidth = 2.2
    override val activeInnerStrokeWidth = 1.6
    override val activeOuterStrokeWidth = 2.8

    // Effects
    override val transitionMs = 160
    override val hoverGlow = "drop-shadow(0 0 16px rgba(90, 170, 255, 0.55))"
    override val activeGlow = "drop-shadow(0 0 20px rgba(90, 170, 255, 0.75))"
    override val hoverTranslateY = "-2px"
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
    BaseButton(
        text = text,
        onClick = onClick,
        tokens = PrimaryButtonTokens,
        width = width,
        height = height,
        iconSrc = iconSrc,
        iconFirst = iconFirst
    )
}
