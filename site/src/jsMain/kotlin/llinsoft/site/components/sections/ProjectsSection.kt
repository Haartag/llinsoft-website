package llinsoft.site.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.grid
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.components.widgets.ProjectCard
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div

val ProjectsTitleStyle = CssStyle.base {
    Modifier
        .fontSize(2.1.cssRem)
        .color(colorMode.toSitePalette().brand.cyan)
}

val ProjectsSubtitleStyle = CssStyle.base {
    Modifier
        .opacity(0.8)
        .margin(top = 0.35.cssRem)
}

val ProjectsGridStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(0.9.cssRem)
            .grid {
                columns { repeat(1) { size(1.fr) } }
            }
    }
    Breakpoint.MD {
        Modifier.grid {
            columns { repeat(2) { size(1.fr) } }
        }
    }
}

@Composable
fun ProjectsSection(
    projects: List<Project>,
    isLoading: Boolean,
    errorMessage: String?,
) {
    val palette = ColorMode.current.toSitePalette()

    Div(Modifier.fillMaxWidth().margin(top = 2.52.cssRem).toAttrs()) {
        Div(ProjectsTitleStyle.toAttrs()) { SpanText("Selected Projects") }

        when {
            isLoading -> Div(Modifier.margin(top = 0.9.cssRem).opacity(0.8).toAttrs()) { SpanText("Loading projects...") }
            errorMessage != null -> Div(Modifier.margin(top = 0.9.cssRem).opacity(0.8).color(palette.brand.lime).toAttrs()) { SpanText(errorMessage) }
            projects.isEmpty() -> Div(Modifier.margin(top = 0.9.cssRem).opacity(0.8).toAttrs()) { SpanText("No projects to display yet.") }
            else -> {
                Div(
                    ProjectsGridStyle.toModifier()
                        .margin(top = 1.22.cssRem)
                        .toAttrs {
                            style {
                                property("max-width", "1350px")
                            }
                        }
                ) {
                    projects.forEach { project ->
                        ProjectCard(project)
                    }
                }
            }
        }
    }
}
