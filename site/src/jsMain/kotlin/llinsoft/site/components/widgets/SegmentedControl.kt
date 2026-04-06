package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button as HtmlButton
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for segmented control
 */
private object SegmentedControlTokens {
    const val radiusPx = 10
    const val segmentWidth = 150
    const val segmentHeight = 44
    const val transitionMs = 160

    // Container outline colors
    const val containerIdleStroke = "rgba(120, 180, 245, 0.35)"
    const val containerHoverInner = "rgba(150, 210, 255, 1.0)"
    const val containerHoverOuter = "rgba(120, 190, 255, 0.65)"

    // Selected tab outline colors
    const val selectedInner = "rgba(150, 210, 255, 1.0)"
    const val selectedOuter = "rgba(120, 190, 255, 0.65)"

    // Stroke widths
    const val containerIdleStrokeWidth = 1.0
    const val containerHoverInnerWidth = 1.4
    const val containerHoverOuterWidth = 2.2
    const val selectedInnerWidth = 1.4
    const val selectedOuterWidth = 2.2
}

/**
 * SVG generator for segmented control borders
 */
private object SegmentedControlSvgGenerator {
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

    fun containerOutline(hovered: Boolean, width: Int, height: Int): String {
        if (!hovered) {
            val idle = svgStroke(
                SegmentedControlTokens.containerIdleStroke,
                SegmentedControlTokens.containerIdleStrokeWidth,
                1.0,
                width,
                height,
                SegmentedControlTokens.radiusPx
            )
            return "url(\"$idle\")"
        }

        val inner = svgStroke(
            SegmentedControlTokens.containerHoverInner,
            SegmentedControlTokens.containerHoverInnerWidth,
            1.0,
            width,
            height,
            SegmentedControlTokens.radiusPx
        )
        val outer = svgStroke(
            SegmentedControlTokens.containerHoverOuter,
            SegmentedControlTokens.containerHoverOuterWidth,
            1.0,
            width,
            height,
            SegmentedControlTokens.radiusPx
        )
        return "url(\"$inner\"), url(\"$outer\")"
    }

    fun selectedTabOutline(width: Int, height: Int): String {
        val inner = svgStroke(
            SegmentedControlTokens.selectedInner,
            SegmentedControlTokens.selectedInnerWidth,
            1.0,
            width,
            height,
            SegmentedControlTokens.radiusPx - 2
        )
        val outer = svgStroke(
            SegmentedControlTokens.selectedOuter,
            SegmentedControlTokens.selectedOuterWidth,
            1.0,
            width,
            height,
            SegmentedControlTokens.radiusPx - 2
        )
        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * Segmented Control - Two-state toggle control for view switching
 *
 * Features:
 * - Clean, modern design matching project tiles
 * - Cyan SVG outline on container
 * - Selected tab has cyan border + transparent cyan fill
 * - Smooth state transitions
 * - Full keyboard navigation support
 * - WCAG 2.2 compliant (44px+ touch targets, ARIA attributes)
 *
 * @param showProjects true = Projects view, false = About view
 * @param onToggle Callback when state changes
 */
@Composable
fun HomepageSegmentedControl(
    showProjects: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val palette = ColorMode.current.toSitePalette()
    var containerHovered by remember { mutableStateOf(false) }

    val containerWidth = (SegmentedControlTokens.segmentWidth * 2) + 12 // 2 segments + gap + padding
    val containerHeight = SegmentedControlTokens.segmentHeight + 8 // segment height + padding

    val containerOutline = remember(containerHovered, containerWidth, containerHeight) {
        SegmentedControlSvgGenerator.containerOutline(containerHovered, containerWidth, containerHeight)
    }

    val glowFilter = if (containerHovered) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, 0.5))"
    } else {
        "none"
    }

    org.jetbrains.compose.web.dom.Div(
        attrs = {
            style {
                property("filter", glowFilter)
                property("transition", "filter 500ms ease")
            }
        }
    ) {
        Div(
            Modifier
                .display(DisplayStyle.Flex)
                .gap(0.25.cssRem)
                .padding(0.25.cssRem)
                .backgroundColor(palette.surface) // Same as project tiles
                .borderRadius(SegmentedControlTokens.radiusPx.px)
                .border(width = 0.px)
                .position(Position.Relative)
                .onMouseEnter { containerHovered = true }
                .onMouseLeave { containerHovered = false }
                .toAttrs {
                    attr("role", "group")
                    attr("aria-label", "Homepage view selector")
                    style {
                        property("width", "fit-content")
                    }
                }
        ) {
            SegmentButton(
                text = "Projects",
                isSelected = showProjects,
                onClick = { onToggle(true) },
                palette = palette
            )
            SegmentButton(
                text = "About Me",
                isSelected = !showProjects,
                onClick = { onToggle(false) },
                palette = palette
            )

            // SVG overlay for container outline
            org.jetbrains.compose.web.dom.Div(attrs = {
                style {
                    property("position", "absolute")
                    property("inset", "0")
                    property("width", "100%")
                    property("height", "100%")
                    property("border-radius", "${SegmentedControlTokens.radiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", containerOutline)
                }
            }) {}
        }
    }
}

/**
 * Individual segment button within the segmented control
 */
@Composable
private fun SegmentButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    palette: llinsoft.site.SitePalette
) {
    val selectedTabOutline = remember(isSelected) {
        if (isSelected) {
            SegmentedControlSvgGenerator.selectedTabOutline(
                SegmentedControlTokens.segmentWidth,
                SegmentedControlTokens.segmentHeight
            )
        } else {
            "none"
        }
    }

    org.jetbrains.compose.web.dom.Div(
        attrs = {
            style {
                property("position", "relative")
                property("display", "inline-block")
            }
        }
    ) {
        HtmlButton(
            attrs = Modifier
                .width(SegmentedControlTokens.segmentWidth.px)
                .height(SegmentedControlTokens.segmentHeight.px)
                .borderRadius((SegmentedControlTokens.radiusPx - 2).px)
                .backgroundColor(palette.surface)
                .color(palette.textPrimary)
                .border(width = 0.px)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    attr("role", "tab")
                    attr("aria-selected", isSelected.toString())
                    attr("aria-label", "$text view")
                    style {
                        property("cursor", "pointer")
                        property("font-size", "0.9rem")
                        property("font-weight", if (isSelected) "600" else "500")
                        property("transition", "all ${SegmentedControlTokens.transitionMs}ms ease")
                        // Cyan tinted background for selected state
                        if (isSelected) {
                            property("background", "linear-gradient(180deg, rgba(120, 190, 255, 0.18) 0%, rgba(120, 190, 255, 0.12) 100%)")
                        }
                    }
                    onClick { onClick() }
                    onKeyDown { event ->
                        when (event.key) {
                            "ArrowLeft", "ArrowRight" -> {
                                onClick()
                                event.preventDefault()
                            }
                        }
                    }
                }
        ) {
            SpanText(text)

            // SVG overlay for selected tab
            if (isSelected) {
                org.jetbrains.compose.web.dom.Div(attrs = {
                    style {
                        property("position", "absolute")
                        property("inset", "0")
                        property("width", "100%")
                        property("height", "100%")
                        property("border-radius", "${SegmentedControlTokens.radiusPx - 2}px")
                        property("pointer-events", "none")
                        property("background-size", "100% 100%")
                        property("background-repeat", "no-repeat")
                        property("background-image", selectedTabOutline)
                    }
                }) {}
            }
        }
    }
}
