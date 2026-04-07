package llinsoft.site.pages.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.window
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.components.layouts.PageLayoutData
import llinsoft.site.components.widgets.*
import llinsoft.site.data.ProjectDataSource
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul

val ProjectPageStyle = CssStyle.base {
    Modifier.fillMaxWidth().gap(1.25.cssRem)
}

val ProjectTopNavStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .padding(topBottom = 0.6.cssRem, leftRight = 0.9.cssRem)
        .backgroundColor(colorMode.toSitePalette().surface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .borderRadius(0.75.cssRem)
}

val ProjectSectionStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .padding(1.25.cssRem)
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(1.cssRem)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
}

val ProjectDetailTitleStyle = CssStyle.base {
    Modifier.fontSize(2.2.cssRem)
}

val ProjectSectionHeadingStyle = CssStyle.base {
    Modifier
        .fontSize(1.26.cssRem)
        .margin(bottom = 0.65.cssRem)
        .color(colorMode.toSitePalette().brand.cyan)
}

@InitRoute
fun initProjectSlugPage(ctx: InitRouteContext) {
    ctx.data.add(PageLayoutData("Project"))
}

/**
 * Simplified state holder for project detail page
 */
private class ProjectDetailState {
    var projects by mutableStateOf<List<Project>>(emptyList())
    var project by mutableStateOf<Project?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)
    var galleryState by mutableStateOf(GalleryState())

    fun resetGalleryState() {
        galleryState = GalleryState()
    }
}

@Page("/projects/{slug}")
@Layout(".components.layouts.PageLayout")
@Composable
fun ProjectSlugPage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params["slug"]

    val state = remember { ProjectDetailState() }

    LaunchedEffect(slug) {
        if (slug == null) {
            state.project = null
            state.projects = emptyList()
            state.errorMessage = "Invalid project URL."
            state.isLoading = false
            state.resetGalleryState()
            return@LaunchedEffect
        }

        state.isLoading = true
        runCatching {
            val allProjects = ProjectDataSource.repository.getAllProjects()
            Pair(allProjects, allProjects.firstOrNull { it.slug == slug })
        }.onSuccess { (allProjects, selected) ->
            state.projects = allProjects
            state.project = selected
            state.errorMessage = null
        }.onFailure {
            state.projects = emptyList()
            state.project = null
            state.errorMessage = "Failed to load project details. Please refresh and try again."
        }
        state.isLoading = false
        state.resetGalleryState()
    }

    val palette = ColorMode.current.toSitePalette()

    Column(ProjectPageStyle.toModifier()) {
        Row(ProjectTopNavStyle.toModifier()) {
            PrimaryButton(
                text = "Back to homepage",
                onClick = { pageContext.router.navigateTo("/") },
                iconSrc = "/icons/chevron-left.svg",
                iconFirst = true
            )
        }

        when {
            state.isLoading -> {
                ProjectSection {
                    SpanText("Loading project details...")
                }
            }
            state.errorMessage != null -> {
                ProjectSection {
                    Div(Modifier.color(palette.brand.lime).toAttrs()) {
                        SpanText(state.errorMessage ?: "Failed to load project details.")
                    }
                }
            }
            state.project == null -> {
                ProjectSection(heading = "Project not found") {
                    SpanText("The requested project slug does not exist.")
                }
            }
            else -> {
                val current = state.project!!
                val currentIndex = state.projects.indexOfFirst { it.slug == current.slug }
                val previous = if (currentIndex > 0) state.projects[currentIndex - 1] else null
                val next = if (currentIndex >= 0 && currentIndex < state.projects.lastIndex) state.projects[currentIndex + 1] else null

                ProjectHero(
                    title = current.title,
                    description = current.shortDescription,
                    heroImageUrl = current.heroImageUrl
                )

                ProjectSection(heading = "Overview") {
                    Div(Modifier.opacity(0.95).toAttrs()) { SpanText(current.fullDescription) }
                }

                ProjectSection(heading = "Technology stack") {
                    Row(Modifier.fillMaxWidth().flexWrap(FlexWrap.Wrap).gap(0.5.cssRem)) {
                        current.techStack.forEach { tech ->
                            TechTag(tech)
                        }
                    }
                }

                ProjectSection(heading = "Key features") {
                    Ul {
                        current.features.forEach { feature ->
                            Li { SpanText(feature) }
                        }
                    }
                }

                ProjectSection(heading = "Gallery") {
                    GalleryRail(
                        images = current.galleryImages,
                        projectTitle = current.title,
                        galleryState = state.galleryState,
                        onGalleryStateChange = { newState ->
                            state.galleryState = newState
                        },
                        onThumbnailClick = { index ->
                            state.galleryState = state.galleryState
                                .withSelectedIndex(index)
                                .withLightboxOpen(true)
                        }
                    )

                    if (state.galleryState.isLightboxOpen) {
                        GalleryLightbox(
                            images = current.galleryImages,
                            projectTitle = current.title,
                            galleryState = state.galleryState,
                            onGalleryStateChange = { newState ->
                                state.galleryState = newState
                            },
                            onClose = {
                                state.galleryState = state.galleryState.withLightboxOpen(false)
                            }
                        )
                    }
                }

                val links = listOfNotNull(
                    current.links.github?.let { "GitHub" to it },
                    current.links.liveDemo?.let { "Live Demo" to it },
                    current.links.playStore?.let { "Play Store" to it },
                    current.links.appStore?.let { "App Store" to it },
                )

                if (links.isNotEmpty()) {
                    ProjectSection(heading = "Project links") {
                        Row(Modifier.fillMaxWidth().flexWrap(FlexWrap.Wrap).gap(0.6.cssRem)) {
                            links.forEach { (label, url) ->
                                SecondaryButton(
                                    text = label,
                                    onClick = { window.open(url, "_blank") }
                                )
                            }
                        }
                    }
                }

                ProjectSection(heading = "More projects") {
                    Row(Modifier.fillMaxWidth().gap(1.cssRem)) {
                        if (previous != null) {
                            PrimaryButton(
                                text = previous.title,
                                onClick = { pageContext.router.navigateTo("/projects/${previous.slug}") },
                                iconSrc = "/icons/chevron-left.svg",
                                iconFirst = true
                            )
                        }
                        Spacer()
                        if (next != null) {
                            PrimaryButton(
                                text = next.title,
                                onClick = { pageContext.router.navigateTo("/projects/${next.slug}") },
                                iconSrc = "/icons/chevron-right.svg",
                                iconFirst = false
                            )
                        }
                    }
                }
            }
        }
    }
}
