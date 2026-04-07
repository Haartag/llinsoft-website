package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
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
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
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
    Modifier
        .fillMaxWidth()
        .margin(top = 2.52.cssRem)
}

val BioTitleStyle = CssStyle.base {
    Modifier
        .fontSize(2.1.cssRem)
        .color(colorMode.toSitePalette().brand.cyan)
}

val BioCardStyle = CssStyle.base {
    Modifier
        .padding(40.px)
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(1.cssRem)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
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
        .fontSize(16.px)
        .lineHeight(1.6)
        .color(org.jetbrains.compose.web.css.Color("#E5E7EB"))
        .opacity(0.95)
        .letterSpacing(0.01.cssRem)
        .textAlign(TextAlign.Start)
}

@Composable
fun BioSection(bio: HomepageBio) {
    Div(BioContainerStyle.toAttrs()) {
        Div(BioTitleStyle.toAttrs()) {
            SpanText("About Me")
        }

        Div(
            attrs = Modifier
                .fillMaxWidth()
                .margin(top = 1.22.cssRem)
                .toAttrs {
                    style {
                        property("max-width", "1150px")
                    }
                }
        ) {
            Div(BioCardStyle.toAttrs()) {
                Div(
                    attrs = Modifier
                        .toAttrs {
                            style {
                                property("display", "flex")
                                property("flex-direction", "column")
                            }
                        }
                ) {
                    bio.paragraphs.forEachIndexed { index, paragraph ->
                        val isLast = index == bio.paragraphs.size - 1

                        Div(
                            attrs = BioParagraphStyle.toModifier().toAttrs {
                                style {
                                    property("font-weight", "300")
                                    // Line height is 1.6, so margin-bottom = 1.6 * 1.75 = 2.8em
                                    if (!isLast) {
                                        property("margin-bottom", "2.8em")
                                    }
                                    if (isLast) {
                                        property("font-style", "italic")
                                        property("margin-top", "1.5rem")
                                    }
                                }
                            }
                        ) {
                            SpanText(paragraph)
                        }
                    }
                }
            }
        }
    }
}
