package llinsoft.site.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

/**
 * Gallery Rail - Horizontal scrollable thumbnail gallery
 *
 * Displays all thumbnails in a single scrollable row. A native scrollbar
 * appears automatically when thumbnails exceed the container width.
 *
 * @param images List of image URLs to display
 * @param projectTitle Project title for alt text
 * @param onThumbnailClick Callback when a thumbnail is clicked
 */
@Composable
fun GalleryRail(
    images: List<String>,
    projectTitle: String,
    onThumbnailClick: (Int) -> Unit
) {
    if (images.isEmpty()) {
        SpanText("No gallery images available.")
        return
    }

    Div(
        attrs = Modifier
            .fillMaxWidth()
            .padding(top = 0.75.cssRem, bottom = 1.cssRem, leftRight = 0.6.cssRem)
            .toAttrs {
                style {
                    property("overflow-x", "auto")
                    property("-webkit-overflow-scrolling", "touch")
                }
            }
    ) {
        Row(Modifier.gap(0.62.cssRem).padding(leftRight = 0.2.cssRem)) {
            images.forEachIndexed { index, imageUrl ->
                GalleryThumbnail(
                    imageUrl = imageUrl,
                    alt = "$projectTitle gallery preview ${index + 1}",
                    ariaLabel = "Open screenshot ${index + 1}",
                    width = "7.3rem",
                    height = "12.8rem",
                    borderRadius = "0.62rem",
                    borderRadiusPx = 10,
                    estimatedWidthPx = 117,
                    estimatedHeightPx = 205,
                    isSelected = false,
                    onClick = { onThumbnailClick(index) }
                )
            }
        }
    }
}
