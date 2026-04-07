package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*

/**
 * Design tokens for secondary buttons
 */
private object SecondaryButtonTokens : ButtonTokens {
    override val radiusPx = 8
    override val defaultWidth = 160
    override val defaultHeight = 42
    override val paddingLeftRight = 0.9.cssRem
    override val paddingTopBottom = 0.6.cssRem
    override val fontSize = 0.9.cssRem
    override val gap: CSSNumericValue<CSSUnit.rem>? = null

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
    override val hoverGlow = "drop-shadow(0 0 14px rgba(90, 170, 255, 0.5))"
    override val activeGlow = "drop-shadow(0 0 18px rgba(90, 170, 255, 0.7))"
    override val hoverTranslateY = "-1px"
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
    BaseButton(
        text = text,
        onClick = onClick,
        tokens = SecondaryButtonTokens,
        width = width,
        height = height,
        iconSrc = null,
        iconFirst = true
    )
}
