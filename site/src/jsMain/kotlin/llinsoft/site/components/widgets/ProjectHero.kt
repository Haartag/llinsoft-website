package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toAttrs
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img

/**
 * Style for project hero title
 */
val ProjectHeroTitleStyle = CssStyle.base {
    Modifier.fontSize(2.2.cssRem)
}

/**
 * Project Hero - Hero section with large image, title, and description
 *
 * Features:
 * - Full-width hero image with cover fit
 * - Large title text
 * - Subtitle/short description
 *
 * @param title Project title
 * @param description Short description or tagline
 * @param heroImageUrl URL to hero image
 */
@Composable
fun ProjectHero(
    title: String,
    description: String,
    heroImageUrl: String
) {
    ProjectSection {
        Img(
            src = heroImageUrl,
            alt = "$title hero image",
            attrs = {
                attr("fetchpriority", "high")
                style {
                    property("width", "100%")
                    property("height", "20rem")
                    property("object-fit", "cover")
                    property("border-radius", "1rem")
                    property("display", "block")
                }
            }
        )

        Div(Modifier.margin(top = 1.cssRem).toAttrs()) {
            Div(ProjectHeroTitleStyle.toAttrs()) { SpanText(title) }
            Div(Modifier.margin(top = 0.45.cssRem).opacity(0.85).toAttrs()) {
                SpanText(description)
            }
        }
    }
}
