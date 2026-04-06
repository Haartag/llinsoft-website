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
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import llinsoft.site.components.utils.SvgGenerator
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
 * Generates overlay background based on hover state
 */
private fun overlayBackground(hovered: Boolean, width: Int, height: Int): String {
    if (!hovered) {
        val idle = SvgGenerator.svgStroke(
            ProjectCardTokens.outlineIdle,
            ProjectCardTokens.idleStrokeWidth,
            1.0,
            width,
            height,
            ProjectCardTokens.borderRadiusPx
        )
        return "url(\"$idle\")"
    }

    val inner = SvgGenerator.svgStroke(
        ProjectCardTokens.outlineActiveInner,
        ProjectCardTokens.activeInnerStrokeWidth,
        1.0,
        width,
        height,
        ProjectCardTokens.borderRadiusPx
    )
    val outer = SvgGenerator.svgStroke(
        ProjectCardTokens.outlineActiveOuter,
        ProjectCardTokens.activeOuterStrokeWidth,
        ProjectCardTokens.activeOuterOpacity,
        width,
        height,
        ProjectCardTokens.borderRadiusPx
    )

    return "url(\"$inner\"), url(\"$outer\")"
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
    Modifier
        .fontSize(0.95.cssRem)
        .opacity(0.85)
        .lineHeight(1.5)
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
 * @param estimatedWidth Estimated card width in pixels for SVG optimization (default: 550)
 * @param estimatedHeight Estimated card height in pixels for SVG optimization (default: 400)
 */
@Composable
fun ProjectCard(
    project: Project,
    estimatedWidth: Int = 550,
    estimatedHeight: Int = 400
) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember(project.slug) { mutableStateOf(false) }

    val glowFilter = if (hovered) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, 0.5))"
    } else {
        "none"
    }

    val overlayBackground = remember(hovered, estimatedWidth, estimatedHeight) {
        overlayBackground(hovered, estimatedWidth, estimatedHeight)
    }

    Column(
        Modifier
            .width(550.px)
            .height(400.px)
    ) {
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${ProjectCardTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter 500ms ease-out, transform ${ProjectCardTokens.transitionMs}ms ease")
                        property("max-width", "100%")
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
                        Div(
                            ProjectDescStyle.toModifier().toAttrs {
                                style {
                                    property("display", "-webkit-box")
                                    property("-webkit-line-clamp", "2")
                                    property("-webkit-box-orient", "vertical")
                                    property("overflow", "hidden")
                                    property("word-break", "break-word")
                                    property("overflow-wrap", "break-word")
                                    property("white-space", "normal")
                                    property("min-width", "0")
                                    property("height", "2.85rem")
                                }
                            }
                        ) { SpanText(project.shortDescription) }

                        Row(Modifier.fillMaxWidth().gap(0.45.cssRem).margin(top = 0.2.cssRem, bottom = 1.cssRem)) {
                            project.featuredTech.forEach { tech ->
                                TechTag(tech)
                            }
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
    var hovered by remember(project.slug, index) { mutableStateOf(false) }

    val glowFilter = if (hovered) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, 0.5))"
    } else {
        "none"
    }

    val overlayBackground = remember(hovered, estimatedWidth, estimatedHeight) {
        overlayBackground(hovered, estimatedWidth, estimatedHeight)
    }

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
