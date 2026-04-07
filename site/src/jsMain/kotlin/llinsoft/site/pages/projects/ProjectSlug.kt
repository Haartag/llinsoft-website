package llinsoft.site.pages.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.grid
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.window
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.MonoTagTextStyle
import llinsoft.site.components.layouts.PageLayoutData
import llinsoft.site.components.widgets.TechTag
import llinsoft.site.components.widgets.IconButton
import llinsoft.site.components.widgets.PrimaryButton
import llinsoft.site.components.widgets.SecondaryButton
import llinsoft.site.components.widgets.ProjectSection
import llinsoft.site.components.widgets.ProjectHero
import llinsoft.site.components.widgets.GalleryThumbnail
import llinsoft.site.data.ProjectDataSource
import llinsoft.site.models.Project
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Button as HtmlButton
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
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

val TechPillStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.26.cssRem, leftRight = 0.64.cssRem)
        .borderRadius(0.45.cssRem)
        .backgroundColor(colorMode.toSitePalette().surface)
        .border(width = 1.px, color = colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.52f))
        .fontSize(0.82.cssRem)
        .color(colorMode.toSitePalette().brand.lime)
}

val TechChipGlowFrameStyle = CssStyle.base {
    Modifier
        .padding(1.px)
        .borderRadius(0.5.cssRem)
        .backgroundColor(colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.2f))
}

val GalleryRailViewportStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .padding(
            top = 0.75.cssRem,
            bottom = 1.cssRem,
            leftRight = 0.6.cssRem
        )
}

val GalleryLightboxOverlayStyle = CssStyle.base {
    Modifier
        .position(Position.Fixed)
        .top(0.px)
        .left(0.px)
        .right(0.px)
        .bottom(0.px)
        .backgroundColor(colorMode.toSitePalette().background.toRgb().copyf(alpha = 0.9f))
        .padding(leftRight = 1.cssRem, topBottom = 1.2.cssRem)
        .zIndex(120)
}

val GalleryLightboxPanelStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .maxWidth(55.cssRem)
            .padding(0.95.cssRem)
            .borderRadius(1.cssRem)
            .backgroundColor(colorMode.toSitePalette().elevatedSurface)
            .border(width = 1.px, color = colorMode.toSitePalette().border)
            .overflow(com.varabyte.kobweb.compose.css.Overflow.Auto)
    }
    Breakpoint.MD {
        Modifier.maxWidth(64.cssRem)
    }
}

val GalleryLightboxStageShellStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .height(66.vh)
            .maxWidth(45.cssRem)
            .padding(leftRight = 0.2.cssRem)
    }
    Breakpoint.MD {
        Modifier.height(72.vh)
    }
}

val GalleryLightboxImageStageStyle = CssStyle.base {
    Modifier
        .fillMaxSize()
        .backgroundColor(colorMode.toSitePalette().surface)
        .borderRadius(0.7.cssRem)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
        .overflow(com.varabyte.kobweb.compose.css.Overflow.Hidden)
}

val GalleryControlButtonStyle = CssStyle.base {
    Modifier
        .padding(0.px)
        .backgroundColor(colorMode.toSitePalette().surface)
        .border(width = 0.px, color = colorMode.toSitePalette().surface)
}

val GalleryArrowOutlineFrameStyle = CssStyle {
    base {
        Modifier
            .padding(1.px)
            .borderRadius(50.percent)
            .backgroundColor(colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.62f))
    }
    cssRule(":hover") {
        Modifier.backgroundColor(colorMode.toSitePalette().brand.cyan)
    }
}

val GalleryArrowButtonStyle = CssStyle {
    base {
        Modifier
            .size(2.45.cssRem)
            .padding(0.px)
            .borderRadius(50.percent)
            .backgroundColor(colorMode.toSitePalette().surface.toRgb().copyf(alpha = 0.92f))
            .border(width = 0.px, color = colorMode.toSitePalette().surface)
            .opacity(0.84f)
    }
    cssRule(":hover") {
        Modifier
            .backgroundColor(colorMode.toSitePalette().elevatedSurface)
            .opacity(1f)
    }
}

@Composable
private fun GalleryButton(
    text: String? = null,
    ariaLabel: String,
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null,
) {
    HtmlButton(
        attrs = modifier.toAttrs {
            attr("type", "button")
            attr("aria-label", ariaLabel)
            style {
                property("display", "flex")
                property("align-items", "center")
                property("justify-content", "center")
                property("margin", "0")
            }
            onClick { onClick() }
        }
    ) {
        if (content != null) {
            content()
        } else if (text != null) {
            SpanText(text)
        }
    }
}

private fun css(vararg rules: String): String {
    return rules.joinToString("; ")
}

private fun encodeSvg(svg: String): String {
    return svg
        .replace("%", "%25")
        .replace("#", "%23")
        .replace("<", "%3C")
        .replace(">", "%3E")
        .replace("\"", "%22")
        .replace(" ", "%20")
}

private fun svgStrokeData(stroke: String, strokeWidth: Double, opacity: Double, radiusPx: Int): String {
    val svg = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 160 90' preserveAspectRatio='none'>" +
        "<rect x='0.75' y='0.75' width='158.5' height='88.5' rx='$radiusPx' ry='$radiusPx' fill='none' " +
        "stroke='$stroke' stroke-width='$strokeWidth' stroke-opacity='$opacity' shape-rendering='geometricPrecision'/>" +
        "</svg>"
    return "data:image/svg+xml;utf8,${encodeSvg(svg)}"
}

private fun overlayBackgroundA1E(hovered: Boolean, selected: Boolean, radiusPx: Int): String {
    val idleStroke = svgStrokeData("rgba(120, 180, 245, 0.28)", 0.8, 1.0, radiusPx)
    val activeInner = svgStrokeData(
        if (selected) "rgba(160, 220, 255, 1)" else "rgba(150, 210, 255, 0.95)",
        1.2,
        1.0,
        radiusPx
    )
    val activeOuter = svgStrokeData("rgba(120, 190, 255, 0.55)", 2.0, 0.4, radiusPx)
    return if (hovered || selected) {
        "background-image: url(\"$activeInner\"), url(\"$activeOuter\")"
    } else {
        "background-image: url(\"$idleStroke\")"
    }
}

private fun a1eWrapperGlow(hovered: Boolean, selected: Boolean): String {
    return when {
        hovered -> "drop-shadow(0 0 12px rgba(90, 170, 255, 0.38))"
        selected -> "drop-shadow(0 0 10px rgba(90, 170, 255, 0.28))"
        else -> "none"
    }
}

private fun baseThumbCss(width: String, height: String, radius: String): String {
    return css(
        "width: $width",
        "min-width: $width",
        "height: $height",
        "border-radius: $radius",
        "overflow: hidden",
        "background: #121b2f",
        "box-sizing: border-box",
        "flex: 0 0 auto",
        "cursor: pointer",
        "transition: all 180ms ease",
        "position: relative",
        "border: 0",
        "padding: 0",
        "display: inline-block",
    )
}

@Composable
private fun NavIconButton(
    ariaLabel: String,
    iconSrc: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    GalleryButton(
        ariaLabel = ariaLabel,
        onClick = onClick,
        modifier = modifier,
        content = {
            Image(
                src = iconSrc,
                description = "",
                modifier = Modifier
                    .size(1.12.cssRem)
                    .display(DisplayStyle.Block)
            )
        }
    )
}

val ProjectActionLinkStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.38.cssRem, leftRight = 0.72.cssRem)
        .borderRadius(0.5.cssRem)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .border(width = 1.px, color = colorMode.toSitePalette().border)
}

val ProjectNavLinkOutlineFrameStyle = CssStyle {
    base {
        Modifier
            .padding(1.px)
            .borderRadius(0.62.cssRem)
            .backgroundColor(colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.58f))
    }
    cssRule(":hover") {
        Modifier.backgroundColor(colorMode.toSitePalette().brand.cyan)
    }
}

val ProjectNavLinkButtonStyle = CssStyle.base {
    Modifier
        .padding(topBottom = 0.52.cssRem, leftRight = 0.74.cssRem)
        .borderRadius(0.56.cssRem)
        .backgroundColor(colorMode.toSitePalette().elevatedSurface)
        .display(DisplayStyle.Flex)
        .gap(0.7.cssRem)
}

val ProjectNavLinkIconStyle = CssStyle.base {
    Modifier
        .size(0.92.cssRem)
        .display(DisplayStyle.Block)
}

@Composable
private fun ProjectNavLinkButton(
    href: String,
    text: String,
    iconSrc: String,
    iconFirst: Boolean,
    modifier: Modifier = Modifier,
) {
    Div(ProjectNavLinkOutlineFrameStyle.toModifier().then(modifier).toAttrs()) {
        Link(
            path = href,
            modifier = ProjectNavLinkButtonStyle.toModifier(),
            variant = UndecoratedLinkVariant.then(UncoloredLinkVariant)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (iconFirst) {
                    Image(iconSrc, "", ProjectNavLinkIconStyle.toModifier())
                }
                SpanText(text)
                if (!iconFirst) {
                    Image(iconSrc, "", ProjectNavLinkIconStyle.toModifier())
                }
            }
        }
    }
}

@InitRoute
fun initProjectSlugPage(ctx: InitRouteContext) {
    ctx.data.add(PageLayoutData("Project"))
}

/**
 * State holder for project detail page
 */
private class ProjectDetailState {
    var projects by mutableStateOf<List<Project>>(emptyList())
    var project by mutableStateOf<Project?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)
    var galleryIndex by mutableStateOf(0)
    var galleryRailStart by mutableStateOf(0)
    var lightboxRailStart by mutableStateOf(0)
    var isGalleryLightboxOpen by mutableStateOf(false)
    var galleryHoveredIndex by mutableStateOf<Int?>(null)
    var lightboxHoveredIndex by mutableStateOf<Int?>(null)

    fun resetGalleryState() {
        galleryIndex = 0
        galleryRailStart = 0
        lightboxRailStart = 0
        isGalleryLightboxOpen = false
        galleryHoveredIndex = null
        lightboxHoveredIndex = null
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
                    if (current.galleryImages.isEmpty()) {
                        SpanText("No gallery images available.")
                    } else {
                        val imageCount = current.galleryImages.size
                        val railVisibleCount = 4
                        val safeGalleryIndex = state.galleryIndex.coerceIn(0, imageCount - 1)
                        val maxRailStart = (imageCount - railVisibleCount).coerceAtLeast(0)
                        val safeRailStart = state.galleryRailStart.coerceIn(0, maxRailStart)

                        Box(GalleryRailViewportStyle.toModifier()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .gap(0.62.cssRem)
                                    .padding(leftRight = if (maxRailStart > 0) 2.9.cssRem else 0.cssRem)
                            ) {
                                current.galleryImages
                                    .drop(safeRailStart)
                                    .take(railVisibleCount)
                                    .forEachIndexed { offset, imageUrl ->
                                        val imageIndex = safeRailStart + offset
                                        GalleryThumbnail(
                                            imageUrl = imageUrl,
                                            alt = "${current.title} gallery preview ${imageIndex + 1}",
                                            ariaLabel = "Open screenshot ${imageIndex + 1}",
                                            width = "7.3rem",
                                            height = "12.8rem",
                                            borderRadius = "0.62rem",
                                            borderRadiusPx = 10,
                                            estimatedWidthPx = 117,
                                            estimatedHeightPx = 205,
                                            isSelected = false,
                                            onClick = {
                                                state.galleryIndex = imageIndex
                                                state.isGalleryLightboxOpen = true
                                            }
                                        )
                                    }
                            }

                            if (maxRailStart > 0) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .position(Position.Absolute)
                                        .top(0.px)
                                        .bottom(0.px),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        ariaLabel = "Scroll gallery left",
                                        onClick = { state.galleryRailStart = (safeRailStart - 1).coerceAtLeast(0) }
                                    ) {
                                        SpanText("←")
                                    }
                                    IconButton(
                                        ariaLabel = "Scroll gallery right",
                                        onClick = { state.galleryRailStart = (safeRailStart + 1).coerceAtMost(maxRailStart) }
                                    ) {
                                        SpanText("→")
                                    }
                                }
                            }
                        }

                        Div(Modifier.margin(top = 0.62.cssRem).opacity(0.8).toAttrs()) {
                            SpanText("${safeGalleryIndex + 1} / $imageCount")
                        }

                        if (state.isGalleryLightboxOpen) {
                            val lightboxRailVisibleCount = 8
                            val maxLightboxRailStart = (imageCount - lightboxRailVisibleCount).coerceAtLeast(0)
                            val safeLightboxRailStart = state.lightboxRailStart.coerceIn(0, maxLightboxRailStart)

                            Box(
                                GalleryLightboxOverlayStyle.toModifier().onClick { state.isGalleryLightboxOpen = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Div(
                                    attrs = {
                                        onClick { it.stopPropagation() }
                                        attr("style", "display: inline-block")
                                    }
                                ) {
                                    Column(GalleryLightboxPanelStyle.toModifier().gap(0.85.cssRem)) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            ariaLabel = "Close screenshot viewer",
                                            onClick = { state.isGalleryLightboxOpen = false }
                                        ) {
                                            SpanText("✕")
                                        }
                                    }

                                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Box(
                                            GalleryLightboxStageShellStyle.toModifier(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                GalleryLightboxImageStageStyle.toModifier(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    src = current.galleryImages[safeGalleryIndex],
                                                    description = "${current.title} screenshot ${safeGalleryIndex + 1}",
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .objectFit(ObjectFit.Contain)
                                                        .display(DisplayStyle.Block)
                                                )
                                            }

                                            if (imageCount > 1) {
                                                Row(
                                                    Modifier.fillMaxWidth().padding(leftRight = 0.4.cssRem),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    IconButton(
                                                        ariaLabel = "Previous screenshot",
                                                        onClick = {
                                                            state.galleryIndex = if (safeGalleryIndex > 0) safeGalleryIndex - 1 else imageCount - 1
                                                        }
                                                    ) {
                                                        SpanText("←")
                                                    }
                                                    IconButton(
                                                        ariaLabel = "Next screenshot",
                                                        onClick = {
                                                            state.galleryIndex = if (safeGalleryIndex < imageCount - 1) safeGalleryIndex + 1 else 0
                                                        }
                                                    ) {
                                                        SpanText("→")
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Box(Modifier.fillMaxWidth()) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .gap(0.45.cssRem)
                                                .padding(
                                                    leftRight = if (maxLightboxRailStart > 0) 2.9.cssRem else 0.cssRem,
                                                    topBottom = 0.6.cssRem
                                                )
                                        ) {
                                            current.galleryImages
                                                .drop(safeLightboxRailStart)
                                                .take(lightboxRailVisibleCount)
                                                .forEachIndexed { offset, imageUrl ->
                                                    val imageIndex = safeLightboxRailStart + offset
                                                    GalleryThumbnail(
                                                        imageUrl = imageUrl,
                                                        alt = "${current.title} screenshot thumbnail ${imageIndex + 1}",
                                                        ariaLabel = "Select screenshot ${imageIndex + 1}",
                                                        width = "3.2rem",
                                                        height = "5.7rem",
                                                        borderRadius = "0.5rem",
                                                        borderRadiusPx = 8,
                                                        estimatedWidthPx = 51,
                                                        estimatedHeightPx = 91,
                                                        isSelected = imageIndex == safeGalleryIndex,
                                                        onClick = { state.galleryIndex = imageIndex }
                                                    )
                                                }
                                        }

                                            if (maxLightboxRailStart > 0) {
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .position(Position.Absolute)
                                                        .top(0.px)
                                                    .bottom(0.px),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconButton(
                                                    ariaLabel = "Scroll thumbnails left",
                                                    onClick = {
                                                        state.lightboxRailStart = (safeLightboxRailStart - 1).coerceAtLeast(0)
                                                    }
                                                ) {
                                                    SpanText("←")
                                                }
                                                IconButton(
                                                    ariaLabel = "Scroll thumbnails right",
                                                    onClick = {
                                                        state.lightboxRailStart = (safeLightboxRailStart + 1).coerceAtMost(maxLightboxRailStart)
                                                    }
                                                ) {
                                                    SpanText("→")
                                                }
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        }
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
