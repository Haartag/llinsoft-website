package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.components.widgets.HomepageSegmentedControl
import llinsoft.site.components.widgets.TechTag
import llinsoft.site.data.HomepageBioData
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Style for hero name
 */
val HeroNameStyle = CssStyle.base {
    Modifier
        .fontSize(2.2.cssRem)
        .fontWeight(700)
        .lineHeight(1.1)
        .margin(bottom = 0.35.cssRem)
}

/**
 * Style for hero role label (metadata)
 */
val HeroRoleStyle = CssStyle.base {
    Modifier
        .fontSize(0.8.cssRem)
        .fontWeight(500)
        .color(colorMode.toSitePalette().brand.cyan)
        .opacity(0.8)
        .margin(bottom = 3.24.cssRem)
}

/**
 * Style for hero headline (main message)
 */
val HeroHeadlineStyle = CssStyle.base {
    Modifier
        .fontSize(1.83.cssRem)
        .fontWeight(700)
        .lineHeight(1.1)
        .margin(bottom = 1.98.cssRem)
}

/**
 * Style for hero support line (subtitle)
 */
val HeroSupportLineStyle = CssStyle.base {
    Modifier
        .fontSize(1.02.cssRem)
        .lineHeight(1.6)
        .opacity(0.7)
        .maxWidth(42.cssRem)
        .margin(bottom = 2.34.cssRem)
}

/**
 * Style for segmented control wrapper
 */
val ControlWrapperStyle = CssStyle.base {
    Modifier
        .margin(bottom = 1.5.cssRem)
}

/**
 * Style for individual proof chip
 */
val ProofChipStyle = CssStyle.base {
    Modifier
        .padding(leftRight = 0.75.cssRem, topBottom = 0.4.cssRem)
        .borderRadius(0.5.cssRem)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.875.cssRem)
        .fontWeight(500)
        .color(colorMode.toSitePalette().brand.lime)
}

/**
 * Hero Section - Modern hero with photo, headline, tech tags, and view control
 *
 * @param showProjects Current view state (true = Projects, false = About)
 * @param onToggle Callback when view is toggled
 */
@Composable
fun HeroSection(
    showProjects: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val palette = ColorMode.current.toSitePalette()
    val bio = HomepageBioData.content

    Column(Modifier.fillMaxWidth().gap(0.px)) {
        Div(
            Modifier
                .fillMaxWidth()
                .padding(topBottom = 3.6.cssRem, leftRight = 1.cssRem)
                .toAttrs {
                    style {
                        property("display", "grid")
                        property("grid-template-columns", "auto 1fr")
                        property("gap", "1.8rem")
                        property("max-width", "72rem")
                        property("margin", "0 auto")
                        property("align-items", "start")
                    }
                }
        ) {
            // Left Column: Avatar with cyan border ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(2.px) // Border thickness
                    .backgroundColor(palette.brand.cyan) // Border color
                    .borderRadius(50.percent)
            ) {
                Image(
                    bio.primaryPhotoUrl,
                    "${bio.name} profile",
                    Modifier
                        .size(13.2.cssRem)
                        .borderRadius(50.percent)
                        .objectFit(ObjectFit.Cover)
                        .display(DisplayStyle.Block)
                )
            }

            // Right Column: All text content with shared left edge
            Column(
                Modifier
                    .fillMaxWidth()
                    .gap(0.px)
            ) {
                // Name (one line)
                Div(HeroNameStyle.toAttrs()) {
                    SpanText("Valeriy ")
                    Div(
                        Modifier
                            .color(palette.brand.cyan)
                            .display(DisplayStyle.Inline)
                            .toAttrs()
                    ) {
                        SpanText("Timofeev")
                    }
                }

                // Role label (metadata style)
                Div(HeroRoleStyle.toAttrs()) {
                    SpanText(bio.role.uppercase())
                }

                // Headline
                Div(HeroHeadlineStyle.toAttrs()) {
                    SpanText("I build cross-platform mobile products with Kotlin Multiplatform.")
                }

                // Support Line
                Div(HeroSupportLineStyle.toAttrs()) {
                    SpanText("With clean architecture, reliable API and backend integration, and careful attention to user experience.")
                }

                // Tech Tags - Supporting detail
                Row(
                    Modifier
                        .gap(0.75.cssRem)
                        .flexWrap(FlexWrap.Wrap)
                        .margin(bottom = 1.56.cssRem)
                ) {
                    TechTag("Kotlin Multiplatform")
                    TechTag("Android")
                    TechTag("APIs")
                    TechTag("Backend Integration")
                    TechTag("Clean Architecture")
                }
            }
        }

        // Segmented Control (View Toggle) - Full width, centered like project cards
        Div(
            Modifier.fillMaxWidth().toAttrs {
                style {
                    property("display", "flex")
                    property("justify-content", "center")
                    property("padding", "0 1rem")
                    property("margin-bottom", "1.35rem")
                }
            }
        ) {
            HomepageSegmentedControl(
                showProjects = showProjects,
                onToggle = onToggle
            )
        }
    }
}
