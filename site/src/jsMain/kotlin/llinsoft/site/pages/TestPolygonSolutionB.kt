package llinsoft.site.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
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
import llinsoft.site.models.ProjectLinks
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Design tokens for polygon test B
 */
private object PolygonTokensB {
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
 * Dynamic SVG generator for polygon test B
 */
private object PolygonSvgGeneratorB {
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
            val idle = svgStroke(PolygonTokensB.outlineIdle, PolygonTokensB.idleStrokeWidth, 1.0, width, height)
            return "url(\"$idle\")"
        }

        val inner = svgStroke(PolygonTokensB.outlineActiveInner, PolygonTokensB.activeInnerStrokeWidth, 1.0, width, height)
        val outer = svgStroke(PolygonTokensB.outlineActiveOuter, PolygonTokensB.activeOuterStrokeWidth, PolygonTokensB.activeOuterOpacity, width, height)

        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * Styles for polygon test cards B
 */
val PolygonCardInnerStyleB = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(PolygonTokensB.borderRadiusPx.px)
        .overflow(Overflow.Hidden)
        .position(Position.Relative)
}

val PolygonTitleStyleB = CssStyle.base {
    Modifier.fontSize(1.28.cssRem)
}

val PolygonDescStyleB = CssStyle.base {
    Modifier.fontSize(0.95.cssRem).opacity(0.85)
}

val PolygonTagStyleB = CssStyle.base {
    Modifier
        .padding(topBottom = 0.24.cssRem, leftRight = 0.52.cssRem)
        .borderRadius(999.px)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.74.cssRem)
}

/**
 * Solution B Card - Hybrid approach: SVG borders + box-shadow glow
 * - Borders: SVG overlay (proven solution from gallery investigation)
 * - Glow: 3-layer box-shadow with dynamic interpolation (bright 1.0 → settled 0.6)
 * - Timing: 32ms bright flash → 700ms smooth fade to settled
 */
@Composable
private fun SolutionBPolygonCard(project: Project, estimatedWidth: Int, estimatedHeight: Int) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var glowBrightness by remember { mutableStateOf(0f) }

    LaunchedEffect(hovered) {
        if (hovered) {
            glowBrightness = 1.0f  // Bright state - gets rendered
            delay(32)              // Wait 32ms (~2 frames) to show bright flash
            glowBrightness = 0.6f  // Settled state (CSS smoothly transitions)
        } else {
            glowBrightness = 0f
        }
    }

    // Smooth interpolation: bright (1.0) → settled (0.6) → idle (0.0)
    val glowAlphaTight = glowBrightness * 0.5f      // 0.5 at bright, 0.3 at settled
    val glowAlphaMedium = glowBrightness * 0.35f    // 0.35 at bright, 0.21 at settled
    val glowAlphaWide = glowBrightness * 0.25f      // 0.25 at bright, 0.15 at settled

    // Box-shadow for glow only (no inset borders - using SVG for that)
    val glowBoxShadow = if (glowAlphaTight > 0) {
        // 3-layer glow
        "0 0 12px rgba(90, 170, 255, $glowAlphaTight), " +
        "0 0 24px rgba(90, 170, 255, $glowAlphaMedium), " +
        "0 0 40px rgba(90, 170, 255, $glowAlphaWide)"
    } else {
        "none"
    }

    val overlayBackground = PolygonSvgGeneratorB.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${PolygonTokensB.borderRadiusPx}px")
                        property("box-shadow", glowBoxShadow)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        // Smooth continuous fade: 700ms for bright→settled transition
                        property("transition", "box-shadow 700ms ease-out, transform ${PolygonTokensB.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(PolygonCardInnerStyleB.toModifier()) {
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
                        Div(PolygonTitleStyleB.toAttrs()) { SpanText(project.title) }
                        Div(PolygonDescStyleB.toAttrs()) { SpanText(project.shortDescription) }

                        Row(Modifier.fillMaxWidth().gap(0.45.cssRem).margin(top = 0.2.cssRem)) {
                            project.techStack.take(3).forEach { tech ->
                                Div(PolygonTagStyleB.toModifier().then(MonoTagTextStyle.toModifier()).toAttrs()) {
                                    SpanText(tech)
                                }
                            }
                        }

                        Div(Modifier.margin(top = 0.25.cssRem).fontSize(0.79.cssRem).color(palette.textMuted).toAttrs()) {
                            SpanText(project.date)
                        }
                    }

                    // SVG overlay for borders (proven solution)
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${PolygonTokensB.borderRadiusPx}px")
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
 * Small vertical card for row 2 - Solution B (hybrid SVG + box-shadow)
 */
@Composable
private fun SmallVerticalCardB(project: Project, index: Int, estimatedWidth: Int, estimatedHeight: Int) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var glowBrightness by remember { mutableStateOf(0f) }

    LaunchedEffect(hovered) {
        if (hovered) {
            glowBrightness = 1.0f  // Bright state - gets rendered
            delay(32)              // Wait 32ms (~2 frames) to show bright flash
            glowBrightness = 0.6f  // Settled state (CSS smoothly transitions)
        } else {
            glowBrightness = 0f
        }
    }

    val glowAlphaTight = glowBrightness * 0.5f
    val glowAlphaMedium = glowBrightness * 0.35f
    val glowAlphaWide = glowBrightness * 0.25f

    val glowBoxShadow = if (glowAlphaTight > 0) {
        "0 0 12px rgba(90, 170, 255, $glowAlphaTight), " +
        "0 0 24px rgba(90, 170, 255, $glowAlphaMedium), " +
        "0 0 40px rgba(90, 170, 255, $glowAlphaWide)"
    } else {
        "none"
    }

    val overlayBackground = PolygonSvgGeneratorB.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Div(
        attrs = Modifier
            .display(DisplayStyle.Block)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .toAttrs {
                style {
                    property("border-radius", "${PolygonTokensB.borderRadiusPx}px")
                    property("box-shadow", glowBoxShadow)
                    property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    property("transition", "box-shadow 700ms ease-out, transform ${PolygonTokensB.transitionMs}ms ease")
                }
            }
    ) {
        Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
            Column(
                PolygonCardInnerStyleB.toModifier().then(
                    Modifier.fillMaxWidth().height(100.percent)
                )
            ) {
                // Tall image for vertical portrait cards
                Image(
                    src = project.thumbnailUrl,
                    description = "${project.title} #$index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.cssRem)
                        .objectFit(ObjectFit.Cover)
                        .display(DisplayStyle.Block)
                )

                Column(Modifier.padding(0.8.cssRem).gap(0.5.cssRem)) {
                    Div(Modifier.fontSize(1.cssRem).toAttrs()) {
                        SpanText("${project.title} #$index")
                    }
                    Div(Modifier.fontSize(0.75.cssRem).opacity(0.7).toAttrs()) {
                        SpanText(project.techStack.take(2).joinToString(", "))
                    }
                }

                // SVG overlay for borders (proven solution)
                Div(attrs = {
                    style {
                        position(Position.Absolute)
                        property("inset", "0")
                        width(100.percent)
                        height(100.percent)
                        property("border-radius", "${PolygonTokensB.borderRadiusPx}px")
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

/**
 * Test polygon page - Solution B: Hybrid SVG borders + box-shadow glow
 *
 * Improvements over Solution A:
 * 1. Quick bright flash: 32ms bright state → 700ms smooth fade to settled (no ugly pause)
 * 2. Stronger glow: 3-layer box-shadow (12px/24px/40px) with dynamic alpha interpolation
 * 3. Visible borders: SVG overlay (proven solution - works reliably on rounded corners)
 * 4. Safari-safe: Box-shadow glow avoids filter: drop-shadow rendering bugs
 */
@Page("test/polygon-solution-b")
@Composable
fun PolygonSolutionBTestPage() {
    val testProject1 = Project(
        id = "test-1",
        slug = "test-project-1",
        title = "Large Card Test B",
        shortDescription = "32ms bright flash → 700ms smooth fade. SVG borders + 3-layer box-shadow glow.",
        fullDescription = "",
        techStack = listOf("Kotlin", "Compose", "Ktor"),
        featuredTech = listOf("Kotlin", "Compose", "Ktor"),
        features = emptyList(),
        thumbnailUrl = "/images/projects/project-1-thumb.svg",
        heroImageUrl = "",
        galleryImages = emptyList(),
        links = ProjectLinks(),
        date = "2026-01",
        order = 1
    )

    val testProject2 = Project(
        id = "test-2",
        slug = "test-project-2",
        title = "Vertical Card B",
        shortDescription = "Quick bright flash, smooth fade. Visible SVG borders on all states.",
        fullDescription = "",
        techStack = listOf("React", "TypeScript", "Node"),
        featuredTech = listOf("React", "TypeScript", "Node"),
        features = emptyList(),
        thumbnailUrl = "/images/projects/project-2-thumb.svg",
        heroImageUrl = "",
        galleryImages = emptyList(),
        links = ProjectLinks(),
        date = "2026-02",
        order = 2
    )

    Box(
        modifier = Modifier
            .minHeight(100.vh)
            .padding(32.px)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .maxWidth(1400.px)
                .toAttrs {
                    style {
                        property("background", "linear-gradient(180deg, #0b1020 0%, #11182b 100%)")
                        property("color", "#eaf1ff")
                        property("font-family", "Inter, system-ui, sans-serif")
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(32.px)
            ) {
                // Header
                Column(Modifier.gap(12.px)) {
                    H1 { Text("Polygon Test - Solution B") }
                    P(attrs = {
                        style {
                            fontSize(16.px)
                            property("color", "rgba(234, 241, 255, 0.82)")
                        }
                    }) {
                        Text("Testing Solution B: Hybrid SVG borders + box-shadow glow (32ms flash → 700ms fade)")
                    }
                    P(attrs = {
                        style {
                            fontSize(14.px)
                            property("color", "rgba(234, 241, 255, 0.65)")
                            property("font-style", "italic")
                        }
                    }) {
                        Text("SVG borders + 3-layer box-shadow. Quick bright flash then smooth continuous fade to settled.")
                    }
                }

                // Row 1: 2 big landscape tiles
                Div(
                    attrs = Modifier.fillMaxWidth().toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(2, 1fr)")
                            property("gap", "24px")
                        }
                    }
                ) {
                    SolutionBPolygonCard(testProject1, 650, 400)
                    SolutionBPolygonCard(testProject1, 650, 400)
                }

                // Row 2: 5 small vertical tiles
                Div(
                    attrs = Modifier.fillMaxWidth().toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(5, 1fr)")
                            property("gap", "16px")
                        }
                    }
                ) {
                    SmallVerticalCardB(testProject2, 1, 267, 420)
                    SmallVerticalCardB(testProject2, 2, 267, 420)
                    SmallVerticalCardB(testProject2, 3, 267, 420)
                    SmallVerticalCardB(testProject2, 4, 267, 420)
                    SmallVerticalCardB(testProject2, 5, 267, 420)
                }
            }
        }
    }
}
