package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px

val NavHeaderStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .height(3.cssRem)
            .padding(leftRight = 1.cssRem)
            .backgroundColor(colorMode.toSitePalette().surface)
            .borderBottom(width = 1.px, color = colorMode.toSitePalette().border)
    }
    Breakpoint.MD {
        Modifier
            .height(3.cssRem)
            .padding(leftRight = 2.cssRem)
    }
}

@Composable
fun NavHeader() {
    Row(
        NavHeaderStyle.toModifier(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Link("/") {
            Image(
                src = "/logo.jpg",
                description = "Site logo",
                modifier = Modifier
                    .height(3.cssRem)
                    .objectFit(ObjectFit.Contain)
                    .display(DisplayStyle.Block)
            )
        }
    }
}
