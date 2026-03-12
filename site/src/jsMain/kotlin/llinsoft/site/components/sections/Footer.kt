package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span

val FooterStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderTop(width = 1.px, color = colorMode.toSitePalette().border)
        .padding(topBottom = 1.5.cssRem, leftRight = 1.cssRem)
}

@Composable
fun Footer(modifier: Modifier = Modifier) {
    val palette = ColorMode.current.toSitePalette()

    Box(FooterStyle.toModifier().then(modifier), contentAlignment = Alignment.Center) {
        Span(
            Modifier
                .textAlign(TextAlign.Center)
                .color(palette.textMuted)
                .toAttrs()
        ) {
            SpanText("Portfolio website")
        }
    }
}
