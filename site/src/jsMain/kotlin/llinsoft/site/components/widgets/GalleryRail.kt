package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

val GalleryRailViewportStyle = CssStyle.base {
    Modifier
        .fillMaxWidth()
        .padding(
            top = 0.75.cssRem,
            bottom = 1.cssRem,
            leftRight = 0.6.cssRem
        )
}

/**
 * Gallery Rail - Horizontal scrollable thumbnail gallery
 *
 * Displays a rail of thumbnail images with left/right scroll controls
 * when there are more images than can fit in the viewport.
 *
 * @param images List of image URLs to display
 * @param projectTitle Project title for alt text
 * @param galleryState Current gallery state
 * @param onGalleryStateChange Callback when gallery state changes
 * @param onThumbnailClick Callback when a thumbnail is clicked
 * @param visibleCount Number of thumbnails visible at once (default: 4)
 */
@Composable
fun GalleryRail(
    images: List<String>,
    projectTitle: String,
    galleryState: GalleryState,
    onGalleryStateChange: (GalleryState) -> Unit,
    onThumbnailClick: (Int) -> Unit,
    visibleCount: Int = 4
) {
    if (images.isEmpty()) {
        SpanText("No gallery images available.")
        return
    }

    val imageCount = images.size
    val maxRailStart = (imageCount - visibleCount).coerceAtLeast(0)
    val safeRailStart = galleryState.railStart.coerceIn(0, maxRailStart)

    Box(GalleryRailViewportStyle.toModifier()) {
        Row(
            Modifier
                .fillMaxWidth()
                .gap(0.62.cssRem)
                .padding(leftRight = if (maxRailStart > 0) 2.9.cssRem else 0.cssRem)
        ) {
            images
                .drop(safeRailStart)
                .take(visibleCount)
                .forEachIndexed { offset, imageUrl ->
                    val imageIndex = safeRailStart + offset
                    GalleryThumbnail(
                        imageUrl = imageUrl,
                        alt = "$projectTitle gallery preview ${imageIndex + 1}",
                        ariaLabel = "Open screenshot ${imageIndex + 1}",
                        width = "7.3rem",
                        height = "12.8rem",
                        borderRadius = "0.62rem",
                        borderRadiusPx = 10,
                        estimatedWidthPx = 117,
                        estimatedHeightPx = 205,
                        isSelected = false,
                        onClick = { onThumbnailClick(imageIndex) }
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
                    onClick = {
                        onGalleryStateChange(
                            galleryState.withRailStart((safeRailStart - 1).coerceAtLeast(0))
                        )
                    }
                ) {
                    SpanText("←")
                }
                IconButton(
                    ariaLabel = "Scroll gallery right",
                    onClick = {
                        onGalleryStateChange(
                            galleryState.withRailStart((safeRailStart + 1).coerceAtMost(maxRailStart))
                        )
                    }
                ) {
                    SpanText("→")
                }
            }
        }
    }

    Div(Modifier.margin(top = 0.62.cssRem).opacity(0.8).toAttrs()) {
        SpanText("${galleryState.selectedIndex + 1} / $imageCount")
    }
}
