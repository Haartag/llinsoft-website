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
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.breakpoint.displayUntil
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
val HeroNameStyle = CssStyle {
    base {
        Modifier
            .fontSize(2.cssRem)
            .fontWeight(700)
            .lineHeight(1.1)
            .margin(bottom = 0.3.cssRem)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(2.2.cssRem)
            .margin(bottom = 0.35.cssRem)
    }
}

/**
 * Style for hero role label (metadata)
 */
val HeroRoleStyle = CssStyle {
    base {
        Modifier
            .fontSize(0.75.cssRem)
            .fontWeight(500)
            .color(colorMode.toSitePalette().brand.cyan)
            .opacity(0.8)
            .margin(bottom = 1.6.cssRem)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(0.8.cssRem)
            .margin(bottom = 3.24.cssRem)
    }
}

/**
 * Style for hero headline (main message)
 */
val HeroHeadlineStyle = CssStyle {
    base {
        Modifier
            .fontSize(1.5.cssRem)
            .fontWeight(700)
            .lineHeight(1.15)
            .margin(bottom = 1.4.cssRem)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(1.83.cssRem)
            .lineHeight(1.1)
            .margin(bottom = 1.98.cssRem)
    }
}

/**
 * Style for hero support line (subtitle)
 */
val HeroSupportLineStyle = CssStyle {
    base {
        Modifier
            .fontSize(0.98.cssRem)
            .lineHeight(1.6)
            .opacity(0.7)
            .maxWidth(32.cssRem)
            .margin(bottom = 1.6.cssRem)
    }
    Breakpoint.MD {
        Modifier
            .fontSize(1.02.cssRem)
            .maxWidth(42.cssRem)
            .margin(bottom = 2.34.cssRem)
    }
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

val HeroAvatarShellStyle = CssStyle.base {
    Modifier
        .padding(2.px)
        .backgroundColor(colorMode.toSitePalette().brand.cyan)
        .borderRadius(50.percent)
        .display(DisplayStyle.InlineBlock)
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

    val avatarContent: @Composable (String) -> Unit = { sizeCss ->
        Div(
            attrs = HeroAvatarShellStyle.toModifier().toAttrs {
                style {
                    property("width", "calc($sizeCss + 4px)")
                    property("height", "calc($sizeCss + 4px)")
                    property("display", "inline-flex")
                    property("align-items", "center")
                    property("justify-content", "center")
                    property("flex-shrink", "0")
                }
            }
        ) {
            Image(
                bio.primaryPhotoUrl,
                "${bio.name} profile",
                Modifier
                    .fillMaxSize()
                    .borderRadius(50.percent)
                    .objectFit(ObjectFit.Cover)
                    .display(DisplayStyle.Block)
            )
        }
    }

    val textContent: @Composable () -> Unit = {
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

    Column(Modifier.fillMaxWidth().gap(0.px)) {
        Div(
            Modifier
                .fillMaxWidth()
                .padding(topBottom = 3.6.cssRem, leftRight = 1.cssRem)
                .maxWidth(72.cssRem)
                .toAttrs {
                    style {
                        property("margin", "0 auto")
                    }
                }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .gap(1.35.cssRem)
                    .displayUntil(Breakpoint.MD)
            ) {
                avatarContent("clamp(9rem, 26vw, 10.5rem)")
                textContent()
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .gap(1.8.cssRem)
                    .displayIfAtLeast(Breakpoint.MD),
                verticalAlignment = Alignment.Top
            ) {
                avatarContent("13.2rem")
                textContent()
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
