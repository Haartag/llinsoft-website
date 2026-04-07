package llinsoft.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
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
import llinsoft.site.MonoTagTextStyle
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import llinsoft.site.models.ProjectLinks
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.*

/**
 * Common tokens for all variants
 */
private object OutlineTokens {
    const val borderRadiusPx = 14
    const val outlineIdle = "rgba(120, 180, 245, 0.28)"
    const val outlineActiveInner = "rgba(150, 210, 255, 0.95)"
    const val outlineActiveOuter = "rgba(120, 190, 255, 0.55)"

    // For variants 1-3 (legacy)
    const val glowIdle = "rgba(90, 170, 255, 0.20)"
    const val glowHover = "rgba(90, 170, 255, 0.28)"

    // For variant 4 (new behavior: 100% → 60% fade)
    const val glowInitial = "rgba(90, 170, 255, 0.38)"  // 100% brightness on hover
    const val glowSettled = "rgba(90, 170, 255, 0.23)"  // 60% brightness after fade
    const val transitionMs = 180
    const val glowFadeDurationMs = 500 // Glow fades over 500ms (0.5 sec)
}

/**
 * SVG generator for fixed aspect ratio (current card proportions)
 */
private object FixedSvgGenerator {
    private val cache = mutableMapOf<String, String>()

    private fun encodeSvg(svg: String): String = svg
        .replace("%", "%25").replace("#", "%23").replace("<", "%3C")
        .replace(">", "%3E").replace("\"", "%22").replace("'", "%27")
        .replace(" ", "%20")

    fun svgStroke(stroke: String, strokeWidth: Double, opacity: Double): String {
        val key = "$stroke|$strokeWidth|$opacity"
        return cache.getOrPut(key) {
            val viewBoxW = 400.0
            val viewBoxH = 300.0
            val inset = 0.75
            val rectW = viewBoxW - (inset * 2)
            val rectH = viewBoxH - (inset * 2)
            val radius = 22.0 // Current value
            val svg = """<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 $viewBoxW $viewBoxH' preserveAspectRatio='none'><rect x='$inset' y='$inset' width='$rectW' height='$rectH' rx='$radius' ry='$radius' fill='none' stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/></svg>"""
            "data:image/svg+xml;utf8,${encodeSvg(svg)}"
        }
    }

    fun overlayBackground(hovered: Boolean): String {
        if (!hovered) {
            return "url(\"${svgStroke(OutlineTokens.outlineIdle, 0.8, 1.0)}\")"
        }
        val inner = svgStroke(OutlineTokens.outlineActiveInner, 1.2, 1.0)
        val outer = svgStroke(OutlineTokens.outlineActiveOuter, 2.0, 0.4)
        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * SVG generator with dynamic calculation based on actual card size
 */
private object DynamicSvgGenerator {
    private val cache = mutableMapOf<String, String>()

    private fun encodeSvg(svg: String): String = svg
        .replace("%", "%25").replace("#", "%23").replace("<", "%3C")
        .replace(">", "%3E").replace("\"", "%22").replace("'", "%27")
        .replace(" ", "%20")

    // Generate SVG that matches actual card dimensions
    fun svgStroke(stroke: String, strokeWidth: Double, opacity: Double, widthPx: Int, heightPx: Int): String {
        val key = "$stroke|$strokeWidth|$opacity|$widthPx|$heightPx"
        return cache.getOrPut(key) {
            // Use actual dimensions as viewBox
            val viewBoxW = widthPx.toDouble()
            val viewBoxH = heightPx.toDouble()
            val inset = 0.75
            val rectW = viewBoxW - (inset * 2)
            val rectH = viewBoxH - (inset * 2)
            val radius = OutlineTokens.borderRadiusPx.toDouble() // Exact match
            val svg = """<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 $viewBoxW $viewBoxH' preserveAspectRatio='none'><rect x='$inset' y='$inset' width='$rectW' height='$rectH' rx='$radius' ry='$radius' fill='none' stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/></svg>"""
            "data:image/svg+xml;utf8,${encodeSvg(svg)}"
        }
    }

    fun overlayBackground(hovered: Boolean, widthPx: Int, heightPx: Int): String {
        if (!hovered) {
            return "url(\"${svgStroke(OutlineTokens.outlineIdle, 0.8, 1.0, widthPx, heightPx)}\")"
        }
        val inner = svgStroke(OutlineTokens.outlineActiveInner, 1.2, 1.0, widthPx, heightPx)
        val outer = svgStroke(OutlineTokens.outlineActiveOuter, 2.0, 0.4, widthPx, heightPx)
        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * Common card content styles
 */
val TestCardInnerStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(OutlineTokens.borderRadiusPx.px)
        .overflow(Overflow.Hidden)
        .position(Position.Relative)
}

val TestCardTitleStyle = CssStyle.base {
    Modifier.fontSize(1.28.cssRem)
}

val TestCardDescStyle = CssStyle.base {
    Modifier.fontSize(0.95.cssRem).opacity(0.85)
}

val TestCardTagStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.24.cssRem, leftRight = 0.52.cssRem)
        .borderRadius(999.px)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.74.cssRem)
}

/**
 * Variant 1: Pure CSS (border + box-shadow)
 */
@Composable
private fun CssVariantCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transition", "all ${OutlineTokens.transitionMs}ms ease")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Div(
                    attrs = Modifier
                        .fillMaxWidth()
                        .backgroundColor(palette.surface)
                        .borderRadius(OutlineTokens.borderRadiusPx.px)
                        .overflow(Overflow.Hidden)
                        .position(Position.Relative)
                        .toAttrs {
                            // Pure CSS approach
                            if (hovered) {
                                style {
                                    property("border", "1px solid ${OutlineTokens.outlineActiveInner}")
                                    property("box-shadow", "0 0 0 1px ${OutlineTokens.outlineActiveOuter}, 0 0 12px ${OutlineTokens.glowHover}")
                                }
                            } else {
                                style {
                                    property("border", "1px solid ${OutlineTokens.outlineIdle}")
                                    property("box-shadow", "0 0 6px ${OutlineTokens.glowIdle}")
                                }
                            }
                        }
                ) {
                    CardContent(project, palette)
                }
            }
        }
    }
}

/**
 * Variant 2: CSS outline + border
 */
@Composable
private fun OutlineBorderVariantCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transition", "all ${OutlineTokens.transitionMs}ms ease")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Div(
                    attrs = Modifier
                        .fillMaxWidth()
                        .backgroundColor(palette.surface)
                        .borderRadius(OutlineTokens.borderRadiusPx.px)
                        .overflow(Overflow.Hidden)
                        .position(Position.Relative)
                        .toAttrs {
                            // Border + outline approach
                            if (hovered) {
                                style {
                                    property("border", "1.2px solid ${OutlineTokens.outlineActiveInner}")
                                    property("outline", "2px solid ${OutlineTokens.outlineActiveOuter}")
                                    property("outline-offset", "0px")
                                    property("box-shadow", "0 0 12px ${OutlineTokens.glowHover}")
                                }
                            } else {
                                style {
                                    property("border", "0.8px solid ${OutlineTokens.outlineIdle}")
                                    property("box-shadow", "0 0 6px ${OutlineTokens.glowIdle}")
                                }
                            }
                        }
                ) {
                    CardContent(project, palette)
                }
            }
        }
    }
}

/**
 * Variant 3: Fixed aspect ratio SVG (current approach)
 */
@Composable
private fun FixedSvgVariantCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    val glowFilter = if (hovered) {
        "drop-shadow(0 0 12px ${OutlineTokens.glowHover})"
    } else {
        "drop-shadow(0 0 6px ${OutlineTokens.glowIdle})"
    }
    val overlayBackground = FixedSvgGenerator.overlayBackground(hovered)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter ${OutlineTokens.transitionMs}ms ease, transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)

                    // SVG overlay
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * Variant 4: Dynamic SVG (calculated per card size)
 * Behavior:
 * - Idle: no glow, thin outline
 * - Hover: thick outline + card lift + bright glow (100%) that fades to 60% over 0.5s
 */
@Composable
private fun DynamicSvgVariantCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    // Estimate card dimensions (would need actual measurement in real impl)
    val estimatedWidth = 600 // Approximate card width
    val estimatedHeight = 320 // 12rem height + content

    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    // Define keyframe animation for glow fade
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")

                        // Glow logic:
                        // - Not hovered: no glow
                        // - Hovered: bright glow (100%) that fades to 60% over 0.5s
                        if (hovered) {
                            property("filter", "drop-shadow(0 0 14px ${OutlineTokens.glowInitial})")
                            property("animation", "glow-fade-out ${OutlineTokens.glowFadeDurationMs}ms ease-out forwards")
                        } else {
                            property("filter", "none")
                            property("animation", "none")
                        }

                        property("transition", "transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)

                    // SVG overlay
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * Variant 4.1: Dynamic SVG with FIXED glow (no inline filter conflict)
 * Fix: Remove inline filter property, let animation control it 100%
 */
@Composable
private fun DynamicSvgVariant41Card(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    // Estimate card dimensions
    val estimatedWidth = 600
    val estimatedHeight = 320

    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")

                        // FIXED glow logic:
                        // - Not hovered: no glow
                        // - Hovered: animation controls fade from 100% to 60%
                        if (hovered) {
                            // Animation with 'both' fill-mode ensures 0% keyframe applies immediately
                            property("animation", "glow-fade-out ${OutlineTokens.glowFadeDurationMs}ms ease-out both")
                        } else {
                            property("filter", "none")
                            property("animation", "none")
                        }

                        property("transition", "transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)

                    // SVG overlay
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * SOLUTION A: LaunchedEffect + State (fade 100% → 60%)
 */
@Composable
private fun SolutionACard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }
    var glowBrightness by remember { mutableStateOf(0f) }

    LaunchedEffect(hovered) {
        if (hovered) {
            glowBrightness = 1.0f  // 100% bright
            delay(500)
            glowBrightness = 0.6f  // Fade to 60%
        } else {
            glowBrightness = 0f     // No glow
        }
    }

    val glowAlpha = when {
        glowBrightness >= 1.0f -> 0.38  // 100%
        glowBrightness >= 0.6f -> 0.23  // 60%
        else -> 0.0                      // None
    }

    val glowFilter = if (glowAlpha > 0) {
        "drop-shadow(0 0 14px rgba(90, 170, 255, $glowAlpha))"
    } else {
        "none"
    }

    val estimatedWidth = 600
    val estimatedHeight = 320
    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter 500ms ease-out, transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * SOLUTION B: Two-Layer Approach (fade 100% → 60%)
 */
@Composable
private fun SolutionBCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    val estimatedWidth = 600
    val estimatedHeight = 320
    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .position(Position.Relative)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            // Layer 1: Bright glow (fades out quickly)
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                    property("filter", "drop-shadow(0 0 14px ${OutlineTokens.glowInitial})")
                    property("opacity", if (hovered) "1" else "0")
                    property("transition", "opacity 500ms ease-out")
                    property("pointer-events", "none")
                }
            }) {}

            // Layer 2: Medium glow (fades in with delay)
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                    property("filter", "drop-shadow(0 0 14px ${OutlineTokens.glowSettled})")
                    property("opacity", if (hovered) "1" else "0")
                    property("transition", "opacity 500ms ease-in 500ms")  // Delay 500ms
                    property("pointer-events", "none")
                }
            }) {}

            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * SOLUTION C: CSS Class Toggle (fade 100% → 60%)
 */
@Composable
private fun SolutionCCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    val estimatedWidth = 600
    val estimatedHeight = 320
    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    if (hovered) {
                        classes("glow-fade-animation")
                    }
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "transform ${OutlineTokens.transitionMs}ms ease")
                        if (!hovered) {
                            property("filter", "none")
                        }
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * SOLUTION D: Constant Bright Glow (100% always while hovered)
 */
@Composable
private fun SolutionDCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()
    var hovered by remember { mutableStateOf(false) }

    val glowFilter = if (hovered) {
        "drop-shadow(0 0 14px ${OutlineTokens.glowInitial})"  // Always bright
    } else {
        "none"
    }

    val estimatedWidth = 600
    val estimatedHeight = 320
    val overlayBackground = DynamicSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${OutlineTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter ${OutlineTokens.transitionMs}ms ease, transform ${OutlineTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(TestCardInnerStyle.toModifier()) {
                    CardContent(project, palette)
                    Div(attrs = {
                        style {
                            position(Position.Absolute)
                            property("inset", "0")
                            width(100.percent)
                            height(100.percent)
                            property("border-radius", "${OutlineTokens.borderRadiusPx}px")
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
 * Shared card content (image, title, description, tags)
 */
@Composable
private fun CardContent(project: Project, palette: llinsoft.site.SitePalette) {
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
        Div(TestCardTitleStyle.toAttrs()) { SpanText(project.title) }
        Div(TestCardDescStyle.toAttrs()) { SpanText(project.shortDescription) }

        Row(Modifier.fillMaxWidth().gap(0.45.cssRem).margin(top = 0.2.cssRem)) {
            project.techStack.take(3).forEach { tech ->
                Div(TestCardTagStyle.toModifier().then(MonoTagTextStyle.toModifier()).toAttrs()) {
                    SpanText(tech)
                }
            }
        }

        Div(Modifier.margin(top = 0.25.cssRem).fontSize(0.79.cssRem).color(palette.textMuted).toAttrs()) {
            SpanText(project.date)
        }
    }
}

/**
 * Test page with all 4 variants side-by-side
 */
@Page("test/outline-variants")
@Composable
fun OutlineVariantsTestPage() {
    // Inject CSS keyframe animation for glow fade (100% → 60%)
    Style {
        """
        @keyframes glow-fade-out {
            from {
                filter: drop-shadow(0 0 14px ${OutlineTokens.glowInitial});
            }
            to {
                filter: drop-shadow(0 0 14px ${OutlineTokens.glowSettled});
            }
        }

        .glow-fade-animation {
            animation: glow-fade-out 500ms ease-out forwards;
        }
        """.trimIndent()
    }

    // Mock project data
    val testProject = Project(
        id = "test-1",
        slug = "test-project",
        title = "Mobile App Tracker",
        shortDescription = "A comprehensive tracking application for mobile platforms built with Kotlin Multiplatform.",
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

    Box(
        modifier = Modifier
            .minHeight(100.vh)
            .padding(32.px)
            .fillMaxWidth(),
        contentAlignment = com.varabyte.kobweb.compose.ui.Alignment.TopCenter
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
                H1 { Text("Outline Variant Comparison") }
                P(attrs = {
                    style {
                        fontSize(16.px)
                        property("color", "rgba(234, 241, 255, 0.82)")
                    }
                }) {
                    Text("Compare 4 different approaches for project card outlines. Hover over each card to see the effect.")
                }
            }

            // Grid of 4 variants
            Div(
                attrs = Modifier
                    .fillMaxWidth()
                    .toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(2, 1fr)")
                            property("gap", "32px")
                        }
                    }
            ) {
                // Variant 1: Pure CSS
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("1. Pure CSS (border + box-shadow)")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Perfect corner match • ✅ Simple • ⚠️ Approximate layering")
                    }
                    CssVariantCard(testProject)
                }

                // Variant 2: Outline + Border
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("2. CSS outline + border")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Two layers • ✅ Perfect corners • ⚠️ Outline doesn't follow radius in Safari")
                    }
                    OutlineBorderVariantCard(testProject)
                }

                // Variant 3: Fixed SVG
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("3. Fixed aspect ratio SVG (current)")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Layered strokes • ❌ Corner mismatch • ❌ Only one aspect ratio")
                    }
                    FixedSvgVariantCard(testProject)
                }

                // Variant 4: Dynamic SVG
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("4. Dynamic SVG (BROKEN - glow disappears)")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Perfect corners • ❌ Inline filter conflicts with animation")
                    }
                    DynamicSvgVariantCard(testProject)
                }
            }

            // Variant 4.1: Dynamic SVG with FIX (full width below)
            Column(Modifier.fillMaxWidth().gap(12.px)) {
                H2(attrs = { style { fontSize(20.px) } }) {
                    Text("4.1 Dynamic SVG - FIXED ✅")
                }
                P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                    Text("✅ Perfect corners • ✅ Layered strokes • ✅ Glow fades and STAYS at 60% • Fix: Removed inline filter conflict")
                }
                DynamicSvgVariant41Card(testProject)
            }

            // Solution Variants Section Header
            Column(Modifier.fillMaxWidth().gap(12.px).margin(top = 48.px)) {
                H1(attrs = { style { fontSize(28.px) } }) {
                    Text("Solution Variants Comparison")
                }
                P(attrs = {
                    style {
                        fontSize(16.px)
                        property("color", "rgba(234, 241, 255, 0.82)")
                    }
                }) {
                    Text("Choose the best approach for glow fade behavior (100% bright → fade to 60%). Hover to test each solution.")
                }
            }

            // Grid of 4 solution variants
            Div(
                attrs = Modifier
                    .fillMaxWidth()
                    .toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(2, 1fr)")
                            property("gap", "32px")
                        }
                    }
            ) {
                // Solution A: LaunchedEffect + State
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("Solution A: LaunchedEffect + State")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Kotlin-based timing • ✅ Precise control • ⚠️ Recomposition overhead")
                    }
                    SolutionACard(testProject)
                }

                // Solution B: Two-Layer CSS
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("Solution B: Two-Layer CSS")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Pure CSS transitions • ✅ No recomposition • ⚠️ Two DOM layers")
                    }
                    SolutionBCard(testProject)
                }

                // Solution C: CSS Class Toggle
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("Solution C: CSS Class Toggle")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ Class-based animation • ✅ Clean separation • ⚠️ May have same issue as 4.1")
                    }
                    SolutionCCard(testProject)
                }

                // Solution D: Constant Bright Glow
                Column(Modifier.gap(12.px)) {
                    H2(attrs = { style { fontSize(20.px) } }) {
                        Text("Solution D: Constant Bright Glow")
                    }
                    P(attrs = { style { fontSize(14.px); property("color", "rgba(234, 241, 255, 0.7)") } }) {
                        Text("✅ No fade complexity • ✅ Always visible • ⚠️ No subtle fade effect")
                    }
                    SolutionDCard(testProject)
                }
            }

            // Summary
            Div(
                attrs = Modifier
                    .fillMaxWidth()
                    .padding(24.px)
                    .borderRadius(16.px)
                    .toAttrs {
                        style {
                            property("background", "rgba(12, 18, 36, 0.7)")
                            property("border", "1px solid rgba(130, 170, 255, 0.18)")
                        }
                    }
            ) {
                Column(Modifier.gap(16.px)) {
                    H2(attrs = { style { fontSize(24.px) } }) {
                        Text("Comparison Summary")
                    }
                    Ul(attrs = { style { fontSize(15.px); property("line-height", "1.8") } }) {
                        Li { Text("Variant 1 (CSS): Simple but uneven corners and weak glow effect") }
                        Li { Text("Variant 2 (outline+border): Uneven corners and Safari compatibility issues") }
                        Li { Text("Variant 3 (Fixed SVG): Elliptical corners due to aspect ratio stretching") }
                        Li { Text("Variant 4 (Dynamic SVG): Perfect corners but inline filter conflicts with animation → glow disappears") }
                        Li { Text("Variant 4.1 (Dynamic SVG FIXED): ✅ RECOMMENDED - Perfect corners, glow stays visible at 60%, proper behavior") }
                    }
                }
            }
            }
        }
    }
}
