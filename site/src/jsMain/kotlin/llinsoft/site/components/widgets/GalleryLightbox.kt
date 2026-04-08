package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import llinsoft.site.toSitePalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

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

/**
 * Gallery Lightbox - Full-screen modal image viewer
 *
 * Displays selected image in a modal with navigation controls and thumbnail rail.
 *
 * @param images List of image URLs to display
 * @param projectTitle Project title for alt text
 * @param galleryState Current gallery state
 * @param onGalleryStateChange Callback when gallery state changes
 * @param onClose Callback when lightbox is closed
 * @param lightboxRailVisibleCount Number of thumbnails visible in lightbox rail (default: 8)
 */
@Composable
fun GalleryLightbox(
    images: List<String>,
    projectTitle: String,
    galleryState: GalleryState,
    onGalleryStateChange: (GalleryState) -> Unit,
    onClose: () -> Unit,
    lightboxRailVisibleCount: Int = 8
) {
    val imageCount = images.size
    val safeGalleryIndex = galleryState.selectedIndex.coerceIn(0, imageCount - 1)
    val maxLightboxRailStart = (imageCount - lightboxRailVisibleCount).coerceAtLeast(0)
    val safeLightboxRailStart = galleryState.lightboxRailStart.coerceIn(0, maxLightboxRailStart)

    Box(
        GalleryLightboxOverlayStyle.toModifier().onClick { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Div(
            attrs = {
                onClick { it.stopPropagation() }
                attr("style", "display: inline-block")
            }
        ) {
            Column(GalleryLightboxPanelStyle.toModifier().gap(0.85.cssRem)) {
                // Close button
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        ariaLabel = "Close screenshot viewer",
                        onClick = onClose
                    ) {
                        SpanText("✕")
                    }
                }

                // Main image stage
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
                                src = images[safeGalleryIndex],
                                description = "$projectTitle screenshot ${safeGalleryIndex + 1}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .objectFit(ObjectFit.Contain)
                                    .display(DisplayStyle.Block)
                            )
                        }

                        // Previous/Next navigation (if more than 1 image)
                        if (imageCount > 1) {
                            Row(
                                Modifier.fillMaxWidth().padding(leftRight = 0.4.cssRem),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    ariaLabel = "Previous screenshot",
                                    onClick = {
                                        val newIndex = if (safeGalleryIndex > 0)
                                            safeGalleryIndex - 1
                                        else
                                            imageCount - 1
                                        onGalleryStateChange(galleryState.withSelectedIndex(newIndex))
                                    }
                                ) {
                                    Span(attrs = { style { property("transform", "translateY(-2px)"); property("display", "inline-block") } }) { Text("←") }
                                }
                                IconButton(
                                    ariaLabel = "Next screenshot",
                                    onClick = {
                                        val newIndex = if (safeGalleryIndex < imageCount - 1)
                                            safeGalleryIndex + 1
                                        else
                                            0
                                        onGalleryStateChange(galleryState.withSelectedIndex(newIndex))
                                    }
                                ) {
                                    Span(attrs = { style { property("transform", "translateY(-2px)"); property("display", "inline-block") } }) { Text("→") }
                                }
                            }
                        }
                    }
                }

                // Thumbnail rail
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
                        images
                            .drop(safeLightboxRailStart)
                            .take(lightboxRailVisibleCount)
                            .forEachIndexed { offset, imageUrl ->
                                val imageIndex = safeLightboxRailStart + offset
                                GalleryThumbnail(
                                    imageUrl = imageUrl,
                                    alt = "$projectTitle screenshot thumbnail ${imageIndex + 1}",
                                    ariaLabel = "Select screenshot ${imageIndex + 1}",
                                    width = "3.2rem",
                                    height = "5.7rem",
                                    borderRadius = "0.5rem",
                                    borderRadiusPx = 8,
                                    estimatedWidthPx = 51,
                                    estimatedHeightPx = 91,
                                    isSelected = imageIndex == safeGalleryIndex,
                                    onClick = {
                                        onGalleryStateChange(galleryState.withSelectedIndex(imageIndex))
                                    }
                                )
                            }
                    }

                    // Thumbnail rail scroll buttons
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
                                    onGalleryStateChange(
                                        galleryState.withLightboxRailStart(
                                            (safeLightboxRailStart - 1).coerceAtLeast(0)
                                        )
                                    )
                                }
                            ) {
                                Span(attrs = { style { property("transform", "translateY(-2px)"); property("display", "inline-block") } }) { Text("←") }
                            }
                            IconButton(
                                ariaLabel = "Scroll thumbnails right",
                                onClick = {
                                    onGalleryStateChange(
                                        galleryState.withLightboxRailStart(
                                            (safeLightboxRailStart + 1).coerceAtMost(maxLightboxRailStart)
                                        )
                                    )
                                }
                            ) {
                                Span(attrs = { style { property("transform", "translateY(-2px)"); property("display", "inline-block") } }) { Text("→") }
                            }
                        }
                    }
                }
            }
        }
    }
}
