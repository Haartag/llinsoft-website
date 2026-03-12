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
        .padding(leftRight = 0.2.cssRem, topBottom = 0.2.cssRem)
}

val GalleryRailFrameStyle = CssStyle.base {
    Modifier
        .width(7.3.cssRem)
        .height(12.8.cssRem)
        .padding(0.px)
        .borderRadius(0.62.cssRem)
        .overflow(com.varabyte.kobweb.compose.css.Overflow.Hidden)
}

val GalleryCardOutlineFrameStyle = CssStyle {
    base {
        Modifier
            .borderRadius(0.7.cssRem)
            .border(width = 1.px, color = colorMode.toSitePalette().brand.cyan.toRgb().copyf(alpha = 0.56f))
    }
    cssRule(":hover") {
        Modifier
            .border(width = 2.px, color = colorMode.toSitePalette().brand.cyan)
    }
}

val GalleryCardOutlineFrameSelectedStyle = CssStyle.base {
    Modifier.border(width = 2.px, color = colorMode.toSitePalette().brand.cyan)
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

val GalleryLightboxThumbnailButtonStyle = CssStyle.base {
    Modifier
        .width(3.2.cssRem)
        .height(5.7.cssRem)
        .padding(0.px)
        .borderRadius(0.5.cssRem)
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

@Page("/projects/{slug}")
@Layout(".components.layouts.PageLayout")
@Composable
fun ProjectSlugPage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params["slug"]

    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var project by remember { mutableStateOf<Project?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var galleryIndex by remember { mutableStateOf(0) }
    var galleryRailStart by remember { mutableStateOf(0) }
    var lightboxRailStart by remember { mutableStateOf(0) }
    var isGalleryLightboxOpen by remember { mutableStateOf(false) }

    LaunchedEffect(slug) {
        if (slug == null) {
            project = null
            projects = emptyList()
            errorMessage = "Invalid project URL."
            isLoading = false
            galleryIndex = 0
            galleryRailStart = 0
            lightboxRailStart = 0
            isGalleryLightboxOpen = false
            return@LaunchedEffect
        }

        isLoading = true
        runCatching {
            val allProjects = ProjectDataSource.repository.getAllProjects()
            Pair(allProjects, allProjects.firstOrNull { it.slug == slug })
        }.onSuccess { (allProjects, selected) ->
            projects = allProjects
            project = selected
            errorMessage = null
        }.onFailure {
            projects = emptyList()
            project = null
            errorMessage = "Failed to load project details. Please refresh and try again."
        }
        isLoading = false
        galleryIndex = 0
        galleryRailStart = 0
        lightboxRailStart = 0
        isGalleryLightboxOpen = false
    }

    val palette = ColorMode.current.toSitePalette()

    Column(ProjectPageStyle.toModifier()) {
        Row(ProjectTopNavStyle.toModifier()) {
            ProjectNavLinkButton(
                href = "/",
                text = "Back to homepage",
                iconSrc = "/icons/chevron-left.svg",
                iconFirst = true
            )
        }

        when {
            isLoading -> {
                Div(ProjectSectionStyle.toAttrs()) { SpanText("Loading project details...") }
            }
            errorMessage != null -> {
                Div(ProjectSectionStyle.toAttrs()) {
                    Div(Modifier.color(palette.brand.lime).toAttrs()) {
                        SpanText(errorMessage ?: "Failed to load project details.")
                    }
                }
            }
            project == null -> {
                Div(ProjectSectionStyle.toAttrs()) {
                    Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Project not found") }
                    SpanText("The requested project slug does not exist.")
                }
            }
            else -> {
                val current = project!!
                val currentIndex = projects.indexOfFirst { it.slug == current.slug }
                val previous = if (currentIndex > 0) projects[currentIndex - 1] else null
                val next = if (currentIndex >= 0 && currentIndex < projects.lastIndex) projects[currentIndex + 1] else null

                Div(ProjectSectionStyle.toAttrs()) {
                    Image(
                        src = current.heroImageUrl,
                        description = "${current.title} hero image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.cssRem)
                            .objectFit(ObjectFit.Cover)
                            .borderRadius(1.cssRem)
                            .display(DisplayStyle.Block)
                    )

                    Div(Modifier.margin(top = 1.cssRem).toAttrs()) {
                        Div(ProjectDetailTitleStyle.toAttrs()) { SpanText(current.title) }
                        Div(Modifier.margin(top = 0.45.cssRem).opacity(0.85).toAttrs()) {
                            SpanText(current.shortDescription)
                        }
                    }
                }

                Div(ProjectSectionStyle.toAttrs()) {
                    Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Overview") }
                    Div(Modifier.opacity(0.95).toAttrs()) { SpanText(current.fullDescription) }
                }

                Div(ProjectSectionStyle.toAttrs()) {
                    Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Technology stack") }
                    Row(Modifier.fillMaxWidth().flexWrap(FlexWrap.Wrap).gap(0.5.cssRem)) {
                        current.techStack.forEach { tech ->
                            Div(TechChipGlowFrameStyle.toAttrs()) {
                                Div(TechPillStyle.toModifier().then(MonoTagTextStyle.toModifier()).toAttrs()) {
                                    SpanText(tech)
                                }
                            }
                        }
                    }
                }

                Div(ProjectSectionStyle.toAttrs()) {
                    Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Key features") }
                    Ul {
                        current.features.forEach { feature ->
                            Li { SpanText(feature) }
                        }
                    }
                }

                Div(ProjectSectionStyle.toAttrs()) {
                    Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Gallery") }
                    if (current.galleryImages.isEmpty()) {
                        SpanText("No gallery images available.")
                    } else {
                        val imageCount = current.galleryImages.size
                        val railVisibleCount = 4
                        val safeGalleryIndex = galleryIndex.coerceIn(0, imageCount - 1)
                        val maxRailStart = (imageCount - railVisibleCount).coerceAtLeast(0)
                        val safeRailStart = galleryRailStart.coerceIn(0, maxRailStart)
                        if (safeGalleryIndex != galleryIndex) galleryIndex = safeGalleryIndex
                        if (safeRailStart != galleryRailStart) galleryRailStart = safeRailStart

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
                                        Div(
                                            GalleryCardOutlineFrameStyle.toAttrs()
                                        ) {
                                            GalleryButton(
                                                ariaLabel = "Open screenshot ${imageIndex + 1}",
                                                onClick = {
                                                    galleryIndex = imageIndex
                                                    isGalleryLightboxOpen = true
                                                },
                                                modifier = GalleryControlButtonStyle.toModifier()
                                                    .then(GalleryRailFrameStyle.toModifier()),
                                            ) {
                                                Image(
                                                    src = imageUrl,
                                                    description = "${current.title} gallery preview ${imageIndex + 1}",
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .objectFit(ObjectFit.Cover)
                                                        .display(DisplayStyle.Block)
                                                )
                                            }
                                        }
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
                                    Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                        NavIconButton(
                                            ariaLabel = "Scroll gallery left",
                                            iconSrc = "/icons/chevron-left.svg",
                                            onClick = { galleryRailStart = (safeRailStart - 1).coerceAtLeast(0) },
                                            modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                        )
                                    }
                                    Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                        NavIconButton(
                                            ariaLabel = "Scroll gallery right",
                                            iconSrc = "/icons/chevron-right.svg",
                                            onClick = { galleryRailStart = (safeRailStart + 1).coerceAtMost(maxRailStart) },
                                            modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                        )
                                    }
                                }
                            }
                        }

                        Div(Modifier.margin(top = 0.62.cssRem).opacity(0.8).toAttrs()) {
                            SpanText("${safeGalleryIndex + 1} / $imageCount")
                        }

                        if (isGalleryLightboxOpen) {
                            val lightboxRailVisibleCount = 8
                            val maxLightboxRailStart = (imageCount - lightboxRailVisibleCount).coerceAtLeast(0)
                            val safeLightboxRailStart = lightboxRailStart.coerceIn(0, maxLightboxRailStart)
                            if (safeLightboxRailStart != lightboxRailStart) lightboxRailStart = safeLightboxRailStart

                            Box(GalleryLightboxOverlayStyle.toModifier(), contentAlignment = Alignment.Center) {
                                Column(GalleryLightboxPanelStyle.toModifier().gap(0.85.cssRem)) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                            NavIconButton(
                                                ariaLabel = "Close screenshot viewer",
                                                iconSrc = "/icons/close.svg",
                                                onClick = { isGalleryLightboxOpen = false },
                                                modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                            )
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
                                                    Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                                        NavIconButton(
                                                            ariaLabel = "Previous screenshot",
                                                            iconSrc = "/icons/chevron-left.svg",
                                                            onClick = {
                                                                galleryIndex = if (safeGalleryIndex > 0) safeGalleryIndex - 1 else imageCount - 1
                                                            },
                                                            modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                                        )
                                                    }
                                                    Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                                        NavIconButton(
                                                            ariaLabel = "Next screenshot",
                                                            iconSrc = "/icons/chevron-right.svg",
                                                            onClick = {
                                                                galleryIndex = if (safeGalleryIndex < imageCount - 1) safeGalleryIndex + 1 else 0
                                                            },
                                                            modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                                        )
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
                                                .padding(leftRight = if (maxLightboxRailStart > 0) 2.9.cssRem else 0.cssRem)
                                        ) {
                                            current.galleryImages
                                                .drop(safeLightboxRailStart)
                                                .take(lightboxRailVisibleCount)
                                                .forEachIndexed { offset, imageUrl ->
                                                    val imageIndex = safeLightboxRailStart + offset
                                                    val isSelected = imageIndex == safeGalleryIndex
                                                    Div(
                                                        GalleryCardOutlineFrameStyle.toModifier()
                                                            .then(if (isSelected) GalleryCardOutlineFrameSelectedStyle.toModifier() else Modifier)
                                                            .toAttrs()
                                                    ) {
                                                        GalleryButton(
                                                            ariaLabel = "Select screenshot ${imageIndex + 1}",
                                                            onClick = { galleryIndex = imageIndex },
                                                            modifier = GalleryControlButtonStyle.toModifier()
                                                                .then(GalleryLightboxThumbnailButtonStyle.toModifier())
                                                        ) {
                                                            Image(
                                                                src = imageUrl,
                                                                description = "${current.title} screenshot thumbnail ${imageIndex + 1}",
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .objectFit(ObjectFit.Cover)
                                                                    .display(DisplayStyle.Block)
                                                            )
                                                        }
                                                    }
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
                                                Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                                    NavIconButton(
                                                        ariaLabel = "Scroll thumbnails left",
                                                        iconSrc = "/icons/chevron-left.svg",
                                                        onClick = {
                                                            lightboxRailStart =
                                                                (safeLightboxRailStart - 1).coerceAtLeast(0)
                                                        },
                                                        modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                                    )
                                                }
                                                Div(GalleryArrowOutlineFrameStyle.toAttrs()) {
                                                    NavIconButton(
                                                        ariaLabel = "Scroll thumbnails right",
                                                        iconSrc = "/icons/chevron-right.svg",
                                                        onClick = {
                                                            lightboxRailStart =
                                                                (safeLightboxRailStart + 1).coerceAtMost(maxLightboxRailStart)
                                                        },
                                                        modifier = GalleryControlButtonStyle.toModifier().then(GalleryArrowButtonStyle.toModifier())
                                                    )
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
                    Div(ProjectSectionStyle.toAttrs()) {
                        Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("Project links") }
                        Row(Modifier.fillMaxWidth().flexWrap(FlexWrap.Wrap).gap(0.6.cssRem)) {
                            links.forEach { (label, url) ->
                                Link(
                                    url,
                                    label,
                                    ProjectActionLinkStyle.toModifier(),
                                    variant = UndecoratedLinkVariant.then(UncoloredLinkVariant),
                                )
                            }
                        }
                    }
                }

                    Div(ProjectSectionStyle.toAttrs()) {
                        Div(ProjectSectionHeadingStyle.toAttrs()) { SpanText("More projects") }
                        Row(Modifier.fillMaxWidth().gap(1.cssRem)) {
                            if (previous != null) {
                                ProjectNavLinkButton(
                                    href = "/projects/${previous.slug}",
                                    text = previous.title,
                                    iconSrc = "/icons/chevron-left.svg",
                                    iconFirst = true
                                )
                            }
                            Spacer()
                            if (next != null) {
                                ProjectNavLinkButton(
                                    href = "/projects/${next.slug}",
                                    text = next.title,
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
