package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
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
import llinsoft.site.MonoTagTextStyle
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

val ProjectCardShellStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(0.32.cssRem)
            .margin(top = 0.px, bottom = 0.px)
    }

    cssRule(":hover") {
        Modifier.margin(top = (-4).px, bottom = 4.px)
    }
}

val ProjectCardStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .backgroundColor(colorMode.toSitePalette().surface)
            .borderRadius(1.cssRem)
            .overflow(com.varabyte.kobweb.compose.css.Overflow.Hidden)
            .border(width = 1.px, color = colorMode.toSitePalette().border)
    }

    cssRule(":hover") {
        Modifier
            .backgroundColor(colorMode.toSitePalette().elevatedSurface)
            .border(width = 1.px, color = colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.45f))
    }
}

val ProjectCardGlowFrameStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(1.05.cssRem)
            .padding(1.px)
            .backgroundColor(colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.16f))
    }

    cssRule(":hover") {
        Modifier
            .backgroundColor(colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.42f))
    }
}

val ProjectTitleStyle = CssStyle.base {
    Modifier.fontSize(1.28.cssRem)
}

val ProjectDescriptionStyle = CssStyle.base {
    Modifier
        .fontSize(0.95.cssRem)
        .opacity(0.85)
}

val ProjectTagStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.24.cssRem, leftRight = 0.52.cssRem)
        .borderRadius(999.px)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .fontSize(0.74.cssRem)
}

@Composable
fun ProjectCard(project: Project) {
    val palette = ColorMode.current.toSitePalette()

    Link(
        "/projects/${project.slug}",
        variant = UndecoratedLinkVariant.then(UncoloredLinkVariant)
    ) {
        Column(ProjectCardShellStyle.toModifier()) {
            Column(ProjectCardGlowFrameStyle.toModifier()) {
                Column(ProjectCardStyle.toModifier()) {
                    Image(
                        src = project.thumbnailUrl,
                        description = "${project.title} thumbnail",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.cssRem)
                            .objectFit(ObjectFit.Cover)
                            .display(DisplayStyle.Block)
                    )

                    Column(
                        Modifier
                            .padding(1.cssRem)
                            .gap(0.7.cssRem)
                    ) {
                        Div(ProjectTitleStyle.toAttrs()) { SpanText(project.title) }
                        Div(ProjectDescriptionStyle.toAttrs()) { SpanText(project.shortDescription) }

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
                }
            }

        }
    }
}
