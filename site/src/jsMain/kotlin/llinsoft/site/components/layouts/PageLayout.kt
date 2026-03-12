package llinsoft.site.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateRows
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.data.getValue
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import kotlinx.browser.document
import llinsoft.site.components.sections.Footer
import llinsoft.site.components.sections.NavHeader
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div

val PageContentStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .maxWidth(78.cssRem)
            .padding(leftRight = 1.cssRem)
            .margin(top = 4.8.cssRem, bottom = 3.cssRem)
    }
    Breakpoint.SM {
        Modifier.padding(leftRight = 1.5.cssRem)
    }
    Breakpoint.MD {
        Modifier.padding(leftRight = 2.cssRem)
    }
}

class PageLayoutData(val title: String)

@Composable
@Layout
fun PageLayout(ctx: PageContext, content: @Composable ColumnScope.() -> Unit) {
    val data = ctx.data.getValue<PageLayoutData>()
    LaunchedEffect(data.title) {
        document.title = "Portfolio - ${data.title}"
    }

    Box(
        Modifier
            .fillMaxWidth()
            .minHeight(100.vh)
            .gridTemplateRows { size(1.fr); size(minContent) },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier.fillMaxSize().gridRow(1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavHeader()
            Div(PageContentStyle.toAttrs()) {
                content()
            }
        }

        Footer(Modifier.fillMaxWidth().gridRow(2))
    }
}
