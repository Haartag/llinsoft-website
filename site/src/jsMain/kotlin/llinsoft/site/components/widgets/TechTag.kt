package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for tech tags
 */
private object TechTagTokens {
    const val radiusPx = 7
    const val transitionMs = 180
}

/**
 * Tech Tag - Small pill-shaped tag for displaying technology stack items
 *
 * Features:
 * - Auto-sizes to fit text content
 * - Green/lime color theme
 * - Monospace font (JetBrains Mono, Fira Code)
 * - Bold border on hover (NO glow effect)
 * - Uses pure CSS (no SVG) for auto-sizing compatibility
 *
 * Design:
 * - Idle: 1px solid border with 52% opacity
 * - Hover: 1.5px solid border with 95% opacity + subtle outer box-shadow ring
 *
 * @param text Tech stack name (e.g., "Kotlin", "React", "PostgreSQL")
 */
@Composable
fun TechTag(text: String) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    // CSS-based approach: use border + box-shadow for double-stroke effect
    val borderWidth = if (hovered) "1.5px" else "1px"

    val borderColor = if (hovered) {
        "rgba(132, 204, 22, 0.95)" // bright lime green
    } else {
        "rgba(132, 204, 22, 0.52)" // dim lime green
    }

    val boxShadow = if (hovered) {
        // Single outer ring using box-shadow (CSS-Tricks recommended pattern)
        "0 0 0 1px rgba(132, 204, 22, 0.35)"
    } else {
        "none"
    }

    Div(
        attrs = Modifier
            .display(DisplayStyle.InlineBlock)
            .padding(leftRight = 0.64.cssRem, topBottom = 0.26.cssRem)
            .backgroundColor(palette.surface)
            .borderRadius(TechTagTokens.radiusPx.px)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .toAttrs {
                style {
                    property("display", "inline-flex")
                    property("align-items", "center")
                    property("justify-content", "center")
                    property("white-space", "nowrap")
                    property("font-size", "0.82rem")
                    property("font-weight", "400")
                    property("color", "${palette.brand.lime}") // Green text
                    property("font-family", "JetBrains Mono, Fira Code, monospace")
                    property("border", "$borderWidth solid $borderColor")
                    property("box-shadow", boxShadow)
                    property("transition", "border ${TechTagTokens.transitionMs}ms ease, box-shadow ${TechTagTokens.transitionMs}ms ease")
                }
            }
    ) {
        SpanText(text)
    }
}
