package llinsoft.site.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.delay
import llinsoft.site.MonoTagTextStyle
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Design tokens for project cards
 */
private object ProjectCardTokens {
    const val borderRadiusPx = 14
    const val outlineIdle = "rgba(120, 180, 245, 0.28)"
    const val outlineActiveInner = "rgba(150, 210, 255, 0.95)"
    const val outlineActiveOuter = "rgba(120, 190, 255, 0.55)"
    const val glowInitial = "rgba(90, 170, 255, 0.38)"
    const val glowSettled = "rgba(90, 170, 255, 0.23)"
    const val idleStrokeWidth = 0.8
    const val activeInnerStrokeWidth = 1.2
    const val activeOuterStrokeWidth = 2.0
    const val activeOuterOpacity = 0.4
    const val transitionMs = 180
}

/**
 * SVG generator for project cards
 */
private object ProjectCardSvgGenerator {
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

    fun svgStroke(stroke: String, strokeWidth: Double, opacity: Double, width: Int, height: Int): String {
        val key = "$stroke|$strokeWidth|$opacity|$width|$height"
        return cache.getOrPut(key) {
            val inset = 0.75
            val rectW = width - (inset * 2)
            val rectH = height - (inset * 2)
            val radius = 14.0

            val svg = """<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 $width $height' preserveAspectRatio='none'><rect x='$inset' y='$inset' width='$rectW' height='$rectH' rx='$radius' ry='$radius' fill='none' stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/></svg>""".replace("\n", "")

            "data:image/svg+xml;utf8,${encodeSvg(svg)}"
        }
    }

    fun overlayBackground(hovered: Boolean, width: Int, height: Int): String {
        if (!hovered) {
            val idle = svgStroke(ProjectCardTokens.outlineIdle, ProjectCardTokens.idleStrokeWidth, 1.0, width, height)
            return "url(\"$idle\")"
        }

        val inner = svgStroke(ProjectCardTokens.outlineActiveInner, ProjectCardTokens.activeInnerStrokeWidth, 1.0, width, height)
        val outer = svgStroke(ProjectCardTokens.outlineActiveOuter, ProjectCardTokens.activeOuterStrokeWidth, ProjectCardTokens.activeOuterOpacity, width, height)

        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * Styles for project cards
 */
val ProjectCardInnerStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(ProjectCardTokens.borderRadiusPx.px)
        .overflow(Overflow.Hidden)
        .position(Position.Relative)
}

val ProjectTitleStyle = CssStyle.base {
    Modifier.fontSize(1.28.cssRem)
}

val ProjectDescStyle = CssStyle.base {
    Modifier.fontSize(0.95.cssRem).opacity(0.85)
}

val ProjectTagStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.24.cssRem, leftRight = 0.52.cssRem)
        .borderRadius(999.px)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.74.cssRem)
}

/**
 * Project Card - Large landscape card for project showcases
 *
 * Features:
 * - SVG-based dynamic borders with hover states
 * - Glow fade sequence: bright → dim (500ms transition for Safari compatibility)
 * - Hover lift animation
 * - Includes thumbnail, title, description, tech tags, date
 *
 * @param project Project data model
 * @param estimatedWidth Estimated card width in pixels for SVG optimization (default: 650)
 * @param estimatedHeight Estimated card height in pixels for SVG optimization (default: 400)
 */
@Composable
fun ProjectCard(
    project: Project,
    estimatedWidth: Int = 650,
    estimatedHeight: Int = 400
) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var glowBrightness by remember { mutableStateOf(0f) }

    LaunchedEffect(hovered) {
        if (hovered) {
            glowBrightness = 1.0f  // 100% bright
            delay(500)
            glowBrightness = 0.6f  // Fade to 60%
        } else {
            glowBrightness = 0f
        }
    }

    val glowAlpha = when {
        glowBrightness >= 1.0f -> 0.38
        glowBrightness >= 0.6f -> 0.23
        else -> 0.0
    }

    val glowFilter = if (glowAlpha > 0) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, $glowAlpha))"
    } else {
        "none"
    }

    val overlayBackground = ProjectCardSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${ProjectCardTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter 500ms ease-out, transform ${ProjectCardTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(ProjectCardInnerStyle.toModifier()) {
                    Image(
                        src = project.thumbnailUrl,
                        description = "${project.title} thumbnail",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.cssRem)
                            .objectFit(ObjectFit.Cover)
                            .display(DisplayStyle.Block)
                    )

                    Column(Modifier.padding(1.cssRem).gap(0.7.cssRem)) {
                        Div(ProjectTitleStyle.toAttrs()) { SpanText(project.title) }
                        Div(ProjectDescStyle.toAttrs()) { SpanText(project.shortDescription) }

                        Row(Modifier.fillMaxWidth().gap(0.45.cssRem).margin(top = 0.2.cssRem)) {
                            project.techStack.take(3).forEach { tech ->
                                Div(ProjectTagStyle.toModifier().then(MonoTagTextStyle.toModifier()).toAttrs()) {
                                    SpanText(tech)
                                }
                            }
                        }

                        Div(Modifier.margin(top = 0.25.cssRem).fontSize(0.79.cssRem).color(palette.textMuted).toAttrs()) {
                            SpanText(project.date)
                        }
                    }

                    // SVG overlay
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${ProjectCardTokens.borderRadiusPx}px")
                            property("pointer-events", "none")
                            property("background-size", "100% 100%")
                            property("background-repeat", "no-repeat")
                            property("background-image", overlayBackground)
                        }
                    }) {}
                }
            }
        }
    }
}

/**
 * Small Vertical Project Card - Portrait card for gallery layouts
 *
 * Features:
 * - Same as ProjectCard but with taller image proportion
 * - Optimized for grid layouts with multiple small cards
 *
 * @param project Project data model
 * @param index Card index for display purposes
 * @param estimatedWidth Estimated card width in pixels (default: 267)
 * @param estimatedHeight Estimated card height in pixels (default: 420)
 */
@Composable
fun SmallProjectCard(
    project: Project,
    index: Int = 0,
    estimatedWidth: Int = 267,
    estimatedHeight: Int = 420
) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var glowBrightness by remember { mutableStateOf(0f) }

    LaunchedEffect(hovered) {
        if (hovered) {
            glowBrightness = 1.0f
            delay(500)
            glowBrightness = 0.6f
        } else {
            glowBrightness = 0f
        }
    }

    val glowAlpha = when {
        glowBrightness >= 1.0f -> 0.38
        glowBrightness >= 0.6f -> 0.23
        else -> 0.0
    }

    val glowFilter = if (glowAlpha > 0) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, $glowAlpha))"
    } else {
        "none"
    }

    val overlayBackground = ProjectCardSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Div(
        attrs = Modifier
            .display(DisplayStyle.Block)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .toAttrs {
                style {
                    property("border-radius", "${ProjectCardTokens.borderRadiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    property("transition", "filter 500ms ease-out, transform ${ProjectCardTokens.transitionMs}ms ease")
                }
            }
    ) {
        Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
            Column(
                ProjectCardInnerStyle.toModifier().then(
                    Modifier.fillMaxWidth().height(100.percent)
                )
            ) {
                // Tall image for vertical portrait cards
                Image(
                    src = project.thumbnailUrl,
                    description = "${project.title}${if (index > 0) " #$index" else ""}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.cssRem)
                        .objectFit(ObjectFit.Cover)
                        .display(DisplayStyle.Block)
                )

                Column(Modifier.padding(0.8.cssRem).gap(0.5.cssRem)) {
                    Div(Modifier.fontSize(1.cssRem).toAttrs()) {
                        SpanText("${project.title}${if (index > 0) " #$index" else ""}")
                    }
                    Div(Modifier.fontSize(0.75.cssRem).opacity(0.7).toAttrs()) {
                        SpanText(project.techStack.take(2).joinToString(", "))
                    }
                }

                // SVG overlay
                Div(attrs = {
                    style {
                        position(Position.Absolute)
                        property("inset", "0")
                        width(100.percent)
                        height(100.percent)
                        property("border-radius", "${ProjectCardTokens.borderRadiusPx}px")
                        property("pointer-events", "none")
                        property("background-size", "100% 100%")
                        property("background-repeat", "no-repeat")
                        property("background-image", overlayBackground)
                    }
                }) {}
            }
        }
    }
}
