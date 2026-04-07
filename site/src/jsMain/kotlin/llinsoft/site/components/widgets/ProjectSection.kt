package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

/**
 * Style for project section containers
 */
val ProjectSectionContainerStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .padding(1.25.cssRem)
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(1.cssRem)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
}

/**
 * Style for section headings
 */
val SectionHeadingStyle = CssStyle.base {
    Modifier
        .fontSize(1.26.cssRem)
        .margin(bottom = 0.65.cssRem)
        .color(colorMode.toSitePalette().brand.cyan)
}

/**
 * Project Section - Reusable container for project page sections
 *
 * Features:
 * - Consistent padding, background, border radius
 * - Optional heading with brand color
 * - Flexible content slot
 *
 * @param heading Optional section heading text
 * @param content Section content
 */
@Composable
fun ProjectSection(
    heading: String? = null,
    content: @Composable () -> Unit
) {
    Div(ProjectSectionContainerStyle.toAttrs()) {
        Column(Modifier.fillMaxWidth().gap(0.cssRem)) {
            if (heading != null) {
                Div(SectionHeadingStyle.toAttrs()) {
                    SpanText(heading)
                }
            }
            content()
        }
    }
}
