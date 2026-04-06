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
 * Design tokens for polygon test
 */
private object PolygonTokens {
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
 * Design tokens for polygon buttons
 */
private object PolygonButtonTokens {
    // Button border radius
    const val roundButtonRadiusPx = 50 // Full circle
    const val largeButtonRadiusPx = 10
    const val mediumButtonRadiusPx = 8

    // Button colors
    const val buttonIdleStroke = "rgba(120, 180, 245, 0.35)"
    const val buttonHoverInner = "rgba(150, 210, 255, 1.0)"
    const val buttonHoverOuter = "rgba(120, 190, 255, 0.65)"
    const val buttonActiveInner = "rgba(90, 170, 255, 1.0)"
    const val buttonActiveOuter = "rgba(60, 140, 235, 0.75)"

    // Button glow
    const val buttonGlowHover = "rgba(90, 170, 255, 0.45)"
    const val buttonGlowActive = "rgba(90, 170, 255, 0.6)"

    // Stroke widths
    const val buttonIdleStrokeWidth = 1.0
    const val buttonHoverInnerStrokeWidth = 1.4
    const val buttonHoverOuterStrokeWidth = 2.2
    const val buttonActiveInnerStrokeWidth = 1.6
    const val buttonActiveOuterStrokeWidth = 2.8

    // Transition
    const val buttonTransitionMs = 160
}

/**
 * Dynamic SVG generator for polygon test
 */
private object PolygonSvgGenerator {
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
            val idle = svgStroke(PolygonTokens.outlineIdle, PolygonTokens.idleStrokeWidth, 1.0, width, height)
            return "url(\"$idle\")"
        }

        val inner = svgStroke(PolygonTokens.outlineActiveInner, PolygonTokens.activeInnerStrokeWidth, 1.0, width, height)
        val outer = svgStroke(PolygonTokens.outlineActiveOuter, PolygonTokens.activeOuterStrokeWidth, PolygonTokens.activeOuterOpacity, width, height)

        return "url(\"$inner\"), url(\"$outer\")"
    }
}

/**
 * SVG generator for polygon buttons
 */
private object PolygonButtonSvgGenerator {
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

    fun buttonOverlay(state: String, width: Int, height: Int, radiusPx: Int): String {
        return when (state) {
            "idle" -> {
                val idle = svgStroke(
                    PolygonButtonTokens.buttonIdleStroke,
                    PolygonButtonTokens.buttonIdleStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                "url(\"$idle\")"
            }
            "hover" -> {
                val inner = svgStroke(
                    PolygonButtonTokens.buttonHoverInner,
                    PolygonButtonTokens.buttonHoverInnerStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                val outer = svgStroke(
                    PolygonButtonTokens.buttonHoverOuter,
                    PolygonButtonTokens.buttonHoverOuterStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                "url(\"$inner\"), url(\"$outer\")"
            }
            "active" -> {
                val inner = svgStroke(
                    PolygonButtonTokens.buttonActiveInner,
                    PolygonButtonTokens.buttonActiveInnerStrokeWidth,
                    1.0,
                    width,
                    height,
                    radiusPx
                )
                val outer = svgStroke(
                    PolygonButtonTokens.buttonActiveOuter,
                    PolygonButtonTokens.buttonActiveOuterStrokeWidth,
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
}

/**
 * Styles for polygon test cards
 */
val PolygonCardInnerStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(PolygonTokens.borderRadiusPx.px)
        .overflow(Overflow.Hidden)
        .position(Position.Relative)
}

val PolygonTitleStyle = CssStyle.base {
    Modifier.fontSize(1.28.cssRem)
}

val PolygonDescStyle = CssStyle.base {
    Modifier.fontSize(0.95.cssRem).opacity(0.85)
}

val PolygonTagStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.24.cssRem, leftRight = 0.52.cssRem)
        .borderRadius(999.px)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.74.cssRem)
}

/**
 * Polygon Round Button - Small circular button (e.g., close, arrows)
 */
@Composable
private fun PolygonRoundButton(
    ariaLabel: String,
    onClick: () -> Unit,
    width: Int = 50,
    height: Int = 50,
    content: @Composable () -> Unit
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

    val overlayBg = PolygonButtonSvgGenerator.buttonOverlay(state, width, height, PolygonButtonTokens.roundButtonRadiusPx)

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
                    property("border-radius", "${PolygonButtonTokens.roundButtonRadiusPx}%")
                    property("filter", glowFilter)
                    property("transition", "filter ${PolygonButtonTokens.buttonTransitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .size(width.px, height.px)
                .padding(0.px)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(PolygonButtonTokens.roundButtonRadiusPx.percent)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    attr("aria-label", ariaLabel)
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                    }
                    onClick { onClick() }
                    onMouseDown { pressed = true }
                    onMouseUp { pressed = false }
                }
        ) {
            content()

            // SVG overlay
            Div(attrs = {
                style {
                    position(Position.Absolute)
                    property("inset", "0")
                    width(100.percent)
                    height(100.percent)
                    property("border-radius", "${PolygonButtonTokens.roundButtonRadiusPx}%")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}

/**
 * Polygon Large Button - Primary action button
 */
@Composable
private fun PolygonLargeButton(
    text: String,
    onClick: () -> Unit,
    width: Int = 220,
    height: Int = 54,
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
        pressed -> "drop-shadow(0 0 20px rgba(90, 170, 255, 0.75))"
        hovered -> "drop-shadow(0 0 16px rgba(90, 170, 255, 0.55))"
        else -> "none"
    }

    val overlayBg = PolygonButtonSvgGenerator.buttonOverlay(state, width, height, PolygonButtonTokens.largeButtonRadiusPx)

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
                    property("border-radius", "${PolygonButtonTokens.largeButtonRadiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    property("transition", "filter ${PolygonButtonTokens.buttonTransitionMs}ms ease, transform ${PolygonButtonTokens.buttonTransitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .width(width.px)
                .height(height.px)
                .padding(leftRight = 1.2.cssRem, topBottom = 0.8.cssRem)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(PolygonButtonTokens.largeButtonRadiusPx.px)
                .border(width = 0.px, color = palette.surface)
                .position(Position.Relative)
                .toAttrs {
                    attr("type", "button")
                    style {
                        property("cursor", "pointer")
                        property("display", "flex")
                        property("align-items", "center")
                        property("justify-content", "center")
                        property("gap", "0.7rem")
                        property("font-size", "1rem")
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
                    property("border-radius", "${PolygonButtonTokens.largeButtonRadiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}

/**
 * Polygon Medium Button - Secondary action button
 */
@Composable
private fun PolygonMediumButton(
    text: String,
    onClick: () -> Unit,
    width: Int = 160,
    height: Int = 42
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

    val overlayBg = PolygonButtonSvgGenerator.buttonOverlay(state, width, height, PolygonButtonTokens.mediumButtonRadiusPx)

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
                    property("border-radius", "${PolygonButtonTokens.mediumButtonRadiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-1px)" else "translateY(0px)")
                    property("transition", "filter ${PolygonButtonTokens.buttonTransitionMs}ms ease, transform ${PolygonButtonTokens.buttonTransitionMs}ms ease")
                }
            }
    ) {
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .width(width.px)
                .height(height.px)
                .padding(leftRight = 0.9.cssRem, topBottom = 0.6.cssRem)
                .backgroundColor(palette.elevatedSurface)
                .borderRadius(PolygonButtonTokens.mediumButtonRadiusPx.px)
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
                    property("border-radius", "${PolygonButtonTokens.mediumButtonRadiusPx}px")
                    property("pointer-events", "none")
                    property("background-size", "100% 100%")
                    property("background-repeat", "no-repeat")
                    property("background-image", overlayBg)
                }
            }) {}
        }
    }
}

/**
 * Solution A Card - with custom dimensions
 */
@Composable
private fun SolutionAPolygonCard(project: Project, estimatedWidth: Int, estimatedHeight: Int) {
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

    val overlayBackground = PolygonSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Column(Modifier.fillMaxWidth()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.Block)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .toAttrs {
                    style {
                        property("border-radius", "${PolygonTokens.borderRadiusPx}px")
                        property("filter", glowFilter)
                        property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                        property("transition", "filter 500ms ease-out, transform ${PolygonTokens.transitionMs}ms ease")
                    }
                }
        ) {
            Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
                Column(PolygonCardInnerStyle.toModifier()) {
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
                        Div(PolygonTitleStyle.toAttrs()) { SpanText(project.title) }
                        Div(PolygonDescStyle.toAttrs()) { SpanText(project.shortDescription) }

                        Row(Modifier.fillMaxWidth().gap(0.45.cssRem).margin(top = 0.2.cssRem)) {
                            project.techStack.take(3).forEach { tech ->
                                Div(PolygonTagStyle.toModifier().then(MonoTagTextStyle.toModifier()).toAttrs()) {
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
                            property("border-radius", "${PolygonTokens.borderRadiusPx}px")
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
 * Small vertical card for row 2
 */
@Composable
private fun SmallVerticalCard(project: Project, index: Int, estimatedWidth: Int, estimatedHeight: Int) {
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

    val overlayBackground = PolygonSvgGenerator.overlayBackground(hovered, estimatedWidth, estimatedHeight)

    Div(
        attrs = Modifier
            .display(DisplayStyle.Block)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .toAttrs {
                style {
                    property("border-radius", "${PolygonTokens.borderRadiusPx}px")
                    property("filter", glowFilter)
                    property("transform", if (hovered) "translateY(-2px)" else "translateY(0px)")
                    property("transition", "filter 500ms ease-out, transform ${PolygonTokens.transitionMs}ms ease")
                }
            }
    ) {
        Link("/projects/${project.slug}", Modifier.fillMaxWidth(), UndecoratedLinkVariant.then(UncoloredLinkVariant)) {
            Column(
                PolygonCardInnerStyle.toModifier().then(
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

                // SVG overlay
                Div(attrs = {
                    style {
                        position(Position.Absolute)
                        property("inset", "0")
                        width(100.percent)
                        height(100.percent)
                        property("border-radius", "${PolygonTokens.borderRadiusPx}px")
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
 * Test polygon page with different card sizes
 */
@Page("test/polygon-solution-a")
@Composable
fun PolygonSolutionATestPage() {
    val testProject1 = Project(
        id = "test-1",
        slug = "test-project-1",
        title = "Large Card Test",
        shortDescription = "Testing Solution A with large landscape cards to verify glow fade behavior.",
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
        title = "Vertical Card",
        shortDescription = "Testing small vertical cards.",
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
                    H1 { Text("Polygon Test - Solution A") }
                    P(attrs = {
                        style {
                            fontSize(16.px)
                            property("color", "rgba(234, 241, 255, 0.82)")
                        }
                    }) {
                        Text("Testing Solution A (LaunchedEffect + State) with cards and buttons featuring SVG strokes and glow effects.")
                    }
                }

                // Button Showcase Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        org.jetbrains.compose.web.dom.H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("Button Components")
                        }
                    }

                    // Round Buttons Row
                    Div(Modifier.toAttrs()) {
                        org.jetbrains.compose.web.dom.H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Small Round Buttons (50x50)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            PolygonRoundButton(
                                ariaLabel = "Close",
                                onClick = { console.log("Close clicked") }
                            ) {
                                SpanText("✕")
                            }
                            PolygonRoundButton(
                                ariaLabel = "Left",
                                onClick = { console.log("Left clicked") }
                            ) {
                                SpanText("←")
                            }
                            PolygonRoundButton(
                                ariaLabel = "Right",
                                onClick = { console.log("Right clicked") }
                            ) {
                                SpanText("→")
                            }
                            PolygonRoundButton(
                                ariaLabel = "Check",
                                onClick = { console.log("Check clicked") }
                            ) {
                                SpanText("✓")
                            }
                        }
                    }

                    // Large Buttons Row
                    Div(Modifier.toAttrs()) {
                        org.jetbrains.compose.web.dom.H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Large Buttons (220x54)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            PolygonLargeButton(
                                text = "Primary Action",
                                onClick = { console.log("Primary clicked") }
                            )
                            PolygonLargeButton(
                                text = "With Icon Left",
                                onClick = { console.log("Icon left clicked") },
                                iconSrc = "/icons/chevron-left.svg",
                                iconFirst = true
                            )
                            PolygonLargeButton(
                                text = "With Icon Right",
                                onClick = { console.log("Icon right clicked") },
                                iconSrc = "/icons/chevron-right.svg",
                                iconFirst = false
                            )
                        }
                    }

                    // Medium Buttons Row
                    Div(Modifier.toAttrs()) {
                        org.jetbrains.compose.web.dom.H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Medium Buttons (160x42)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            PolygonMediumButton(
                                text = "Secondary",
                                onClick = { console.log("Secondary clicked") }
                            )
                            PolygonMediumButton(
                                text = "Cancel",
                                onClick = { console.log("Cancel clicked") }
                            )
                            PolygonMediumButton(
                                text = "Learn More",
                                onClick = { console.log("Learn More clicked") }
                            )
                        }
                    }

                    // Interactive Demo
                    Div(Modifier.toAttrs()) {
                        org.jetbrains.compose.web.dom.H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Interactive Demo")
                        }
                        P(attrs = {
                            style {
                                fontSize(14.px)
                                property("color", "rgba(234, 241, 255, 0.7)")
                                property("margin-bottom", "16px")
                            }
                        }) {
                            Text("Hover over buttons to see glow effects. Click to see active state.")
                        }
                        Row(Modifier.gap(24.px).fillMaxWidth()) {
                            Column(Modifier.gap(12.px)) {
                                PolygonLargeButton(
                                    text = "Get Started",
                                    onClick = { console.log("Get Started clicked") }
                                )
                                PolygonMediumButton(
                                    text = "View Demo",
                                    onClick = { console.log("View Demo clicked") }
                                )
                            }
                            Column(Modifier.gap(12.px)) {
                                Row(Modifier.gap(8.px)) {
                                    PolygonRoundButton(
                                        ariaLabel = "Previous",
                                        onClick = { console.log("Previous") }
                                    ) {
                                        SpanText("◀")
                                    }
                                    PolygonRoundButton(
                                        ariaLabel = "Play",
                                        onClick = { console.log("Play") }
                                    ) {
                                        SpanText("▶")
                                    }
                                    PolygonRoundButton(
                                        ariaLabel = "Next",
                                        onClick = { console.log("Next") }
                                    ) {
                                        SpanText("▶▶")
                                    }
                                }
                            }
                        }
                    }
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // Cards Section Header
                Div(Modifier.toAttrs()) {
                    org.jetbrains.compose.web.dom.H2(
                        attrs = {
                            style {
                                fontSize(24.px)
                                property("color", "rgba(150, 210, 255, 0.95)")
                                property("margin-bottom", "16px")
                            }
                        }
                    ) {
                        Text("Card Components")
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
                    SolutionAPolygonCard(testProject1, 650, 400)
                    SolutionAPolygonCard(testProject1, 650, 400)
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
                    SmallVerticalCard(testProject2, 1, 267, 420)
                    SmallVerticalCard(testProject2, 2, 267, 420)
                    SmallVerticalCard(testProject2, 3, 267, 420)
                    SmallVerticalCard(testProject2, 4, 267, 420)
                    SmallVerticalCard(testProject2, 5, 267, 420)
                }
            }
        }
    }
}
