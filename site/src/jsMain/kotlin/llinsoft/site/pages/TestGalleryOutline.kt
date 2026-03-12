package llinsoft.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

private data class DemoShot(
    val title: String,
    val url: String,
)

private enum class Variant {
    BORDER_GLOW,
    INSET_GLOW,
    FRAME_WRAP,
    BORDER_GLOW_A1,
    BORDER_GLOW_A2,
    BORDER_GLOW_A3,
    BORDER_GLOW_A1B,
    BORDER_GLOW_A3B,
    BORDER_GLOW_A3C,
    BORDER_GLOW_A1D,
    BORDER_GLOW_A1E,
}

private val demoShots = listOf(
    DemoShot("Home", "/images/projects/project-1-thumb.svg"),
    DemoShot("Details", "/images/projects/project-2-thumb.svg"),
    DemoShot("Stats", "/images/projects/project-3-thumb.svg"),
    DemoShot("Settings", "/images/projects/project-1-thumb.svg"),
    DemoShot("Onboarding", "/images/projects/project-2-thumb.svg"),
)

private fun css(vararg rules: String): String {
    return rules.joinToString("; ")
}

private fun encodeSvg(svg: String): String {
    return svg
        .replace("%", "%25")
        .replace("#", "%23")
        .replace("<", "%3C")
        .replace(">", "%3E")
        .replace("\"", "%22")
        .replace(" ", "%20")
}

private fun svgStrokeData(stroke: String, strokeWidth: Double, opacity: Double): String {
    val svg = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 160 90' preserveAspectRatio='none'>" +
        "<rect x='0.75' y='0.75' width='158.5' height='88.5' rx='14' ry='14' fill='none' " +
        "stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/>" +
        "</svg>"
    return "data:image/svg+xml;utf8,${encodeSvg(svg)}"
}

private fun overlayBackgroundCss(variant: Variant, hovered: Boolean, selected: Boolean): String {
    val baseStroke = if (selected) "rgba(140, 210, 255, 0.95)" else "rgba(130, 190, 255, 0.75)"
    val hoverStroke = if (selected) "rgba(160, 220, 255, 1)" else "rgba(150, 210, 255, 0.95)"
    val strokeColor = if (hovered) hoverStroke else baseStroke
    return when (variant) {
        Variant.BORDER_GLOW_A1 -> {
            val inner = svgStrokeData(strokeColor, 1.2, 1.0)
            val outer = svgStrokeData("rgba(120, 190, 255, 0.55)", 2.0, 0.4)
            "background-image: url(\"$inner\"), url(\"$outer\")"
        }
        Variant.BORDER_GLOW_A1D -> {
            val idleStroke = svgStrokeData("rgba(120, 180, 245, 0.28)", 0.8, 1.0)
            val activeInner = svgStrokeData(strokeColor, 1.2, 1.0)
            val activeOuter = svgStrokeData("rgba(120, 190, 255, 0.55)", 2.0, 0.4)
            if (hovered || selected) {
                "background-image: url(\"$activeInner\"), url(\"$activeOuter\")"
            } else {
                "background-image: url(\"$idleStroke\")"
            }
        }
        Variant.BORDER_GLOW_A1E -> {
            val idleStroke = svgStrokeData("rgba(120, 180, 245, 0.28)", 0.8, 1.0)
            val activeInner = svgStrokeData(strokeColor, 1.2, 1.0)
            val activeOuter = svgStrokeData("rgba(120, 190, 255, 0.55)", 2.0, 0.4)
            if (hovered || selected) {
                "background-image: url(\"$activeInner\"), url(\"$activeOuter\")"
            } else {
                "background-image: url(\"$idleStroke\")"
            }
        }
        Variant.BORDER_GLOW_A2 -> {
            val inner = svgStrokeData(strokeColor, 1.0, 1.0)
            val outer = svgStrokeData("rgba(120, 190, 255, 0.45)", 2.5, 0.32)
            "background-image: url(\"$inner\"), url(\"$outer\")"
        }
        Variant.BORDER_GLOW_A3 -> {
            val inner = svgStrokeData(strokeColor, 1.4, 1.0)
            val outer = svgStrokeData("rgba(120, 190, 255, 0.45)", 3.0, 0.28)
            "background-image: url(\"$inner\"), url(\"$outer\")"
        }
        Variant.BORDER_GLOW_A3C -> {
            val inner = svgStrokeData(
                if (hovered) "rgba(120, 190, 255, 0.75)" else "rgba(110, 170, 240, 0.6)",
                1.8,
                1.0
            )
            val outer = svgStrokeData("rgba(120, 190, 255, 0.4)", 2.6, 0.18)
            "background-image: url(\"$inner\"), url(\"$outer\")"
        }
        else -> ""
    }
}

private fun baseThumbCss(): String {
    return css(
        "width: 160px",
        "min-width: 160px",
        "aspect-ratio: 16 / 9",
        "border-radius: 14px",
        "overflow: hidden",
        "background: #121b2f",
        "box-sizing: border-box",
        "flex: 0 0 auto",
        "cursor: pointer",
        "transition: all 180ms ease",
        "position: relative",
    )
}

private fun variantCss(variant: Variant, hovered: Boolean, selected: Boolean): String {
    return when (variant) {
        Variant.BORDER_GLOW -> {
            val base = listOf(
                "border: 1px solid rgba(130, 170, 255, 0.38)",
                "box-shadow: 0 6px 18px rgba(0, 0, 0, 0.24)",
            )
            val hover = if (hovered) listOf(
                "border-color: rgba(150, 197, 255, 0.95)",
                "box-shadow: 0 0 0 1px rgba(150, 197, 255, 0.85), 0 0 22px 4px rgba(88, 152, 255, 0.28), 0 8px 24px rgba(0, 0, 0, 0.34)",
                "transform: translateY(-2px)",
            ) else emptyList()
            val selectedCss = if (selected) listOf(
                "border-color: rgba(116, 203, 255, 1)",
                "box-shadow: 0 0 0 1px rgba(116, 203, 255, 1), 0 0 26px 6px rgba(79, 165, 255, 0.34), 0 10px 26px rgba(0, 0, 0, 0.36)",
            ) else emptyList()
            css(*(base + hover + selectedCss).toTypedArray())
        }
        Variant.INSET_GLOW -> {
            val base = listOf("box-shadow: inset 0 0 0 1px rgba(120, 190, 255, 0.55)")
            val hover = if (hovered) listOf(
                "box-shadow: inset 0 0 0 1px rgba(150, 210, 255, 1), 0 0 18px 3px rgba(88, 152, 255, 0.32)",
                "transform: translateY(-2px)",
            ) else emptyList()
            val selectedCss = if (selected) listOf(
                "box-shadow: inset 0 0 0 2px rgba(130, 210, 255, 1), 0 0 22px 6px rgba(88, 152, 255, 0.36)",
            ) else emptyList()
            css(*(base + hover + selectedCss).toTypedArray())
        }
        Variant.FRAME_WRAP -> ""
        Variant.BORDER_GLOW_A1 -> {
            val base = listOf(
                "border: 0",
                "box-shadow: 0 6px 18px rgba(0, 0, 0, 0.22)",
            )
            val hover = if (hovered) listOf(
                "box-shadow: 0 0 14px 3px rgba(88, 152, 255, 0.2), 0 8px 22px rgba(0, 0, 0, 0.32)",
                "transform: translateY(-2px)",
            ) else emptyList()
            val selectedCss = if (selected) listOf(
                "box-shadow: 0 0 18px 4px rgba(79, 165, 255, 0.24), 0 10px 24px rgba(0, 0, 0, 0.36)",
            ) else emptyList()
            css(*(base + hover + selectedCss).toTypedArray())
        }
        Variant.BORDER_GLOW_A2 -> {
            val base = listOf(
                "border: 0",
                "box-shadow: 0 6px 16px rgba(0, 0, 0, 0.22)",
            )
            val hover = if (hovered) listOf(
                "box-shadow: 0 0 12px 3px rgba(88, 152, 255, 0.18), 0 8px 20px rgba(0, 0, 0, 0.3)",
                "transform: translateY(-2px)",
            ) else emptyList()
            val selectedCss = if (selected) listOf(
                "box-shadow: 0 0 16px 4px rgba(79, 165, 255, 0.22), 0 10px 22px rgba(0, 0, 0, 0.34)",
            ) else emptyList()
            css(*(base + hover + selectedCss).toTypedArray())
        }
        Variant.BORDER_GLOW_A3 -> {
            val base = listOf(
                "border: 0",
                "box-shadow: 0 7px 20px rgba(0, 0, 0, 0.24)",
            )
            val hover = if (hovered) listOf(
                "box-shadow: 0 0 16px 4px rgba(88, 152, 255, 0.22), 0 10px 24px rgba(0, 0, 0, 0.32)",
                "transform: translateY(-2px)",
            ) else emptyList()
            val selectedCss = if (selected) listOf(
                "box-shadow: 0 0 18px 5px rgba(79, 165, 255, 0.22), 0 10px 26px rgba(0, 0, 0, 0.36)",
            ) else emptyList()
            css(*(base + hover + selectedCss).toTypedArray())
        }
        Variant.BORDER_GLOW_A1B, Variant.BORDER_GLOW_A3B, Variant.BORDER_GLOW_A3C, Variant.BORDER_GLOW_A1D, Variant.BORDER_GLOW_A1E -> {
            // Glow handled on wrapper to avoid clipping; inner keeps only subtle depth.
            val base = listOf(
                "border: 0",
                "box-shadow: 0 4px 14px rgba(0, 0, 0, 0.2)",
            )
            css(*base.toTypedArray())
        }
    }
}

@Composable
private fun Thumbnail(
    variant: Variant,
    shot: DemoShot,
    selected: Boolean,
    hovered: Boolean,
    onHover: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    if (variant == Variant.FRAME_WRAP) {
        Div(
            attrs = {
                attr(
                    "style",
                    css(
                        "padding: 1px",
                        "border-radius: 16px",
                        "background: ${if (hovered) "rgba(150, 210, 255, 0.95)" else "rgba(120, 190, 255, 0.55)"}",
                        "transition: background 180ms ease",
                    )
                )
                onMouseEnter { onHover(true) }
                onMouseLeave { onHover(false) }
                onClick { onClick() }
            }
        ) {
            Div(
                attrs = {
                    attr("style", baseThumbCss())
                }
            ) {
                Img(
                    src = shot.url,
                    alt = shot.title,
                    attrs = {
                        attr(
                            "style",
                            css(
                                "width: 100%",
                                "height: 100%",
                                "display: block",
                                "object-fit: cover",
                            )
                        )
                    }
                )
            }
        }
        return
    }

    if (variant == Variant.BORDER_GLOW_A1B || variant == Variant.BORDER_GLOW_A3B || variant == Variant.BORDER_GLOW_A3C || variant == Variant.BORDER_GLOW_A1D || variant == Variant.BORDER_GLOW_A1E) {
        val baseGlow = when (variant) {
            Variant.BORDER_GLOW_A1B -> if (selected)
                "drop-shadow(0 0 10px rgba(90, 170, 255, 0.22))"
            else
                "drop-shadow(0 0 6px rgba(90, 170, 255, 0.12))"
            Variant.BORDER_GLOW_A3B -> if (selected)
                "drop-shadow(0 0 12px rgba(90, 170, 255, 0.24))"
            else
                "drop-shadow(0 0 7px rgba(90, 170, 255, 0.14))"
            Variant.BORDER_GLOW_A3C -> if (selected)
                "drop-shadow(0 0 12px rgba(90, 170, 255, 0.24))"
            else
                "drop-shadow(0 0 7px rgba(90, 170, 255, 0.14))"
            Variant.BORDER_GLOW_A1D -> if (selected)
                "drop-shadow(0 0 10px rgba(90, 170, 255, 0.22))"
            else
                "none"
            Variant.BORDER_GLOW_A1E -> if (selected)
                "drop-shadow(0 0 10px rgba(90, 170, 255, 0.22))"
            else
                "none"
            else -> "none"
        }
        val hoverGlow = if (hovered)
            "drop-shadow(0 0 12px rgba(90, 170, 255, 0.24))"
        else
            baseGlow
        Div(
            attrs = {
                attr(
                    "style",
                    css(
                        "display: inline-block",
                        "border-radius: 14px",
                        "filter: $hoverGlow",
                        "transition: filter 180ms ease, transform 180ms ease",
                        if (variant == Variant.BORDER_GLOW_A1E && hovered) "transform: translateY(-2px)" else "transform: translateY(0px)",
                    )
                )
                onMouseEnter { onHover(true) }
                onMouseLeave { onHover(false) }
                onClick { onClick() }
            }
        ) {
            Div(
                attrs = {
                    attr("style", css(baseThumbCss(), variantCss(variant, hovered, selected)))
                }
            ) {
                Img(
                    src = shot.url,
                    alt = shot.title,
                    attrs = {
                        attr(
                            "style",
                            css(
                                "width: 100%",
                                "height: 100%",
                                "display: block",
                                "object-fit: cover",
                            )
                        )
                    }
                )
                if (variant == Variant.BORDER_GLOW_A1B || variant == Variant.BORDER_GLOW_A3B || variant == Variant.BORDER_GLOW_A3C || variant == Variant.BORDER_GLOW_A1D || variant == Variant.BORDER_GLOW_A1E) {
                    val bg = when (variant) {
                        Variant.BORDER_GLOW_A1B -> overlayBackgroundCss(Variant.BORDER_GLOW_A1, hovered, selected)
                        Variant.BORDER_GLOW_A3B -> overlayBackgroundCss(Variant.BORDER_GLOW_A3, hovered, selected)
                        Variant.BORDER_GLOW_A3C -> overlayBackgroundCss(Variant.BORDER_GLOW_A3C, hovered, selected)
                        Variant.BORDER_GLOW_A1D -> overlayBackgroundCss(Variant.BORDER_GLOW_A1D, hovered, selected)
                        Variant.BORDER_GLOW_A1E -> overlayBackgroundCss(Variant.BORDER_GLOW_A1E, hovered, selected)
                        else -> ""
                    }
                    Div(
                        attrs = {
                            attr(
                                "style",
                                css(
                                    "position: absolute",
                                    "inset: 0",
                                    "width: 100%",
                                    "height: 100%",
                                    "pointer-events: none",
                                    "background-size: 100% 100%",
                                    "background-repeat: no-repeat",
                                    bg,
                                )
                            )
                        }
                    ) {}
                }
            }
        }
        return
    }

    Div(
        attrs = {
            attr("style", css(baseThumbCss(), variantCss(variant, hovered, selected)))
            onMouseEnter { onHover(true) }
            onMouseLeave { onHover(false) }
            onClick { onClick() }
        }
    ) {
        Img(
            src = shot.url,
            alt = shot.title,
            attrs = {
                attr(
                    "style",
                    css(
                        "width: 100%",
                        "height: 100%",
                        "display: block",
                        "object-fit: cover",
                    )
                )
            }
        )
        if (variant == Variant.BORDER_GLOW_A1 || variant == Variant.BORDER_GLOW_A2 || variant == Variant.BORDER_GLOW_A3) {
            Div(
                attrs = {
                    val bg = overlayBackgroundCss(variant, hovered, selected)
                    attr(
                        "style",
                        css(
                            "position: absolute",
                            "inset: 0",
                            "width: 100%",
                            "height: 100%",
                            "pointer-events: none",
                            "background-size: 100% 100%",
                            "background-repeat: no-repeat",
                            bg,
                        )
                    )
                }
            ) {}
        }
    }
}

@Page("test/gallery-outline")
@Composable
fun GalleryOutlineTestPage() {
    var selectedA by remember { mutableStateOf(0) }
    var selectedB by remember { mutableStateOf(1) }
    var selectedC by remember { mutableStateOf(2) }
    var selectedA1 by remember { mutableStateOf(0) }
    var selectedA2 by remember { mutableStateOf(1) }
    var selectedA3 by remember { mutableStateOf(2) }
    var hoveredA by remember { mutableStateOf<Int?>(null) }
    var hoveredB by remember { mutableStateOf<Int?>(null) }
    var hoveredC by remember { mutableStateOf<Int?>(null) }
    var hoveredA1 by remember { mutableStateOf<Int?>(null) }
    var hoveredA2 by remember { mutableStateOf<Int?>(null) }
    var hoveredA3 by remember { mutableStateOf<Int?>(null) }

    val selectedPreview = demoShots[selectedA.coerceIn(0, demoShots.lastIndex)]

    Div(
        attrs = {
            attr(
                "style",
                css(
                    "min-height: 100vh",
                    "padding: 32px",
                    "box-sizing: border-box",
                    "background: linear-gradient(180deg, #0b1020 0%, #11182b 100%)",
                    "color: #eaf1ff",
                    "font-family: Inter, system-ui, sans-serif",
                )
            )
        }
    ) {
        Div(
            attrs = {
                attr(
                    "style",
                    css(
                        "max-width: 1120px",
                        "margin: 0 auto",
                        "display: grid",
                        "gap: 24px",
                    )
                )
            }
        ) {
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 8px",
                        )
                    )
                }
            ) {
                H1 { Text("Screenshot Outline / Hover / Glow Lab") }
                P { Text("Compare three outline treatments side-by-side and pick the best fit for the main site.") }
            }

            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "width: 100%",
                            "aspect-ratio: 16 / 9",
                            "border-radius: 18px",
                            "overflow: hidden",
                            "border: 1px solid rgba(130, 170, 255, 0.30)",
                            "background: #0f1728",
                            "box-shadow: 0 16px 40px rgba(0, 0, 0, 0.35)",
                        )
                    )
                }
            ) {
                Img(
                    src = selectedPreview.url,
                    alt = selectedPreview.title,
                    attrs = {
                        attr(
                            "style",
                            css(
                                "width: 100%",
                                "height: 100%",
                                "display: block",
                                "object-fit: cover",
                            )
                        )
                    }
                )
            }

            // Option A
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A: Real Border + Glow") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA].title}") }
                    Span { Text("Border stays crisp; glow adds depth") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW,
                            shot = shot,
                            selected = index == selectedA,
                            hovered = hoveredA == index,
                            onHover = { hovered -> hoveredA = if (hovered) index else null },
                            onClick = { selectedA = index }
                        )
                    }
                }
            }

            // Option A1
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A1: SVG Stroke Overlay (Even Corners)") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA1].title}") }
                    Span { Text("SVG stroke for uniform thickness around curves") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A1,
                            shot = shot,
                            selected = index == selectedA1,
                            hovered = hoveredA1 == index,
                            onHover = { hovered -> hoveredA1 = if (hovered) index else null },
                            onClick = { selectedA1 = index }
                        )
                    }
                }
            }

            // Option A2
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A2: SVG Stroke + Soft Outer Halo") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA2].title}") }
                    Span { Text("Uniform stroke plus a faint outer halo") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A2,
                            shot = shot,
                            selected = index == selectedA2,
                            hovered = hoveredA2 == index,
                            onHover = { hovered -> hoveredA2 = if (hovered) index else null },
                            onClick = { selectedA2 = index }
                        )
                    }
                }
            }

            // Option A3
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A3: SVG Stroke + Fuller Glow") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA3].title}") }
                    Span { Text("Uniform stroke with a stronger outer line") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A3,
                            shot = shot,
                            selected = index == selectedA3,
                            hovered = hoveredA3 == index,
                            onHover = { hovered -> hoveredA3 = if (hovered) index else null },
                            onClick = { selectedA3 = index }
                        )
                    }
                }
            }

            // Option A1B
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A1B: A1 + Unclipped Glow") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA1].title}") }
                    Span { Text("Glow rendered on outer wrapper") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 12px 10px 16px 10px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A1B,
                            shot = shot,
                            selected = index == selectedA1,
                            hovered = hoveredA1 == index,
                            onHover = { hovered -> hoveredA1 = if (hovered) index else null },
                            onClick = { selectedA1 = index }
                        )
                    }
                }
            }

            // Option A3B
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A3B: A3 + Unclipped Glow") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA3].title}") }
                    Span { Text("Same stroke, glow rendered outside clip") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 12px 10px 16px 10px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A3B,
                            shot = shot,
                            selected = index == selectedA3,
                            hovered = hoveredA3 == index,
                            onHover = { hovered -> hoveredA3 = if (hovered) index else null },
                            onClick = { selectedA3 = index }
                        )
                    }
                }
            }

            // Option A3C
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A3C: A3B + Thicker Softer Outline") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA3].title}") }
                    Span { Text("Thicker line, lower brightness") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 12px 10px 16px 10px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A3C,
                            shot = shot,
                            selected = index == selectedA3,
                            hovered = hoveredA3 == index,
                            onHover = { hovered -> hoveredA3 = if (hovered) index else null },
                            onClick = { selectedA3 = index }
                        )
                    }
                }
            }

            // Option A1D
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A1D: A1B Idle-Subtle, Active-Bright") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA1].title}") }
                    Span { Text("Idle = faint outline; hover/selected = A1B glow") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 12px 10px 16px 10px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A1D,
                            shot = shot,
                            selected = index == selectedA1,
                            hovered = hoveredA1 == index,
                            onHover = { hovered -> hoveredA1 = if (hovered) index else null },
                            onClick = { selectedA1 = index }
                        )
                    }
                }
            }

            // Option A1E
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option A1E: A1D + Hover Lift") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedA1].title}") }
                    Span { Text("Idle faint; hover/selected bright + lift") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 12px 10px 16px 10px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.BORDER_GLOW_A1E,
                            shot = shot,
                            selected = index == selectedA1,
                            hovered = hoveredA1 == index,
                            onHover = { hovered -> hoveredA1 = if (hovered) index else null },
                            onClick = { selectedA1 = index }
                        )
                    }
                }
            }

            // Option B
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option B: Inset Stroke + Glow") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedB].title}") }
                    Span { Text("No real border, just inset stroke") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.INSET_GLOW,
                            shot = shot,
                            selected = index == selectedB,
                            hovered = hoveredB == index,
                            onHover = { hovered -> hoveredB = if (hovered) index else null },
                            onClick = { selectedB = index }
                        )
                    }
                }
            }

            // Option C
            Div(
                attrs = {
                    attr(
                        "style",
                        css(
                            "display: grid",
                            "gap: 14px",
                            "padding: 18px",
                            "border-radius: 16px",
                            "background: rgba(12, 18, 36, 0.7)",
                            "border: 1px solid rgba(130, 170, 255, 0.18)",
                        )
                    )
                }
            ) {
                H2 { Text("Option C: Outer Frame Wrapper") }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "align-items: center",
                                "justify-content: space-between",
                                "gap: 12px",
                                "font-size: 14px",
                                "color: rgba(234, 241, 255, 0.82)",
                            )
                        )
                    }
                ) {
                    Span { Text("Selected: ${demoShots[selectedC].title}") }
                    Span { Text("Closest to current structure") }
                }
                Div(
                    attrs = {
                        attr(
                            "style",
                            css(
                                "display: flex",
                                "gap: 14px",
                                "overflow-x: auto",
                                "padding: 4px 2px 10px 2px",
                                "scrollbar-width: thin",
                            )
                        )
                    }
                ) {
                    demoShots.forEachIndexed { index, shot ->
                        Thumbnail(
                            variant = Variant.FRAME_WRAP,
                            shot = shot,
                            selected = index == selectedC,
                            hovered = hoveredC == index,
                            onHover = { hovered -> hoveredC = if (hovered) index else null },
                            onClick = { selectedC = index }
                        )
                    }
                }
            }
        }
    }
}
