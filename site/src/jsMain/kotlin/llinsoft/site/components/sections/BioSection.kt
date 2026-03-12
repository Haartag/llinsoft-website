package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
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
import llinsoft.site.data.HomepageBio
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

val BioContainerStyle = CssStyle.base {
    Modifier.fillMaxWidth().gap(1.4.cssRem)
}

val BioNameStyle = CssStyle {
    base {
        Modifier
            .fontSize(2.4.cssRem)
            .lineHeight(1.08)
    }
    Breakpoint.MD {
        Modifier.fontSize(3.6.cssRem)
    }
}

val BioRoleStyle = CssStyle.base {
    Modifier
        .fontSize(1.08.cssRem)
        .color(colorMode.toSitePalette().brand.cyan)
}

val BioParagraphStyle = CssStyle.base {
    Modifier
        .fontSize(1.02.cssRem)
        .lineHeight(1.7)
        .color(colorMode.toSitePalette().textMuted)
}

@Composable
fun BioSection(bio: HomepageBio) {
    val palette = ColorMode.current.toSitePalette()
    val words = bio.name.split(" ").filter { it.isNotBlank() }
    val firstLine = words.take(1).joinToString(" ")
    val secondLine = words.drop(1).joinToString(" ").ifBlank { bio.name }

    Column(BioContainerStyle.toModifier()) {
        Column(
            Modifier
                .fillMaxWidth()
                .gap(1.cssRem)
                .displayUntil(Breakpoint.MD),
        ) {
            Row(Modifier.fillMaxWidth().gap(1.cssRem)) {
                Image(
                    bio.primaryPhotoUrl,
                    "${bio.name} profile",
                    Modifier
                        .size(7.2.cssRem)
                        .borderRadius(50.percent)
                        .border(width = 3.px, color = palette.brand.cyan.toRgb().copyf(alpha = 0.52f))
                        .objectFit(ObjectFit.Cover)
                        .display(DisplayStyle.Block)
                )
                Column(Modifier.gap(0.5.cssRem)) {
                    Div(BioNameStyle.toAttrs()) {
                        SpanText(firstLine)
                        Div(Modifier.color(palette.brand.cyan).toAttrs()) { SpanText(secondLine) }
                    }
                    Div(BioRoleStyle.toAttrs()) { SpanText(bio.role) }
                }
            }

            Column(Modifier.fillMaxWidth().gap(0.7.cssRem)) {
                bio.paragraphs.forEach { paragraph ->
                    Div(BioParagraphStyle.toAttrs()) { SpanText(paragraph) }
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .gap(2.2.cssRem)
                .displayIfAtLeast(Breakpoint.MD),
        ) {
            Image(
                bio.primaryPhotoUrl,
                "${bio.name} profile",
                Modifier
                    .size(14.cssRem)
                    .borderRadius(50.percent)
                    .border(width = 4.px, color = palette.brand.cyan.toRgb().copyf(alpha = 0.54f))
                    .objectFit(ObjectFit.Cover)
                    .display(DisplayStyle.Block)
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .maxWidth(46.cssRem)
                    .gap(0.9.cssRem)
                    .textAlign(TextAlign.Start)
            ) {
                Div(BioNameStyle.toAttrs()) {
                    SpanText(firstLine)
                    Div(Modifier.color(palette.brand.cyan).toAttrs()) { SpanText(secondLine) }
                }
                Div(BioRoleStyle.toAttrs()) { SpanText(bio.role) }

                Column(Modifier.fillMaxWidth().gap(0.72.cssRem)) {
                    bio.paragraphs.forEach { paragraph ->
                        Div(BioParagraphStyle.toAttrs()) { SpanText(paragraph) }
                    }
                }
            }
        }
    }
}
