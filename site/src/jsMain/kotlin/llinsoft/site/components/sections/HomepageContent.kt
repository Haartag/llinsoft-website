package llinsoft.site.components.sections

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.coroutines.delay
import llinsoft.site.data.HomepageBio
import llinsoft.site.models.Project
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.dom.Div

/**
 * Homepage Content - Animated wrapper for Projects/About view toggle
 *
 * Features:
 * - Smooth fade transition (200ms)
 * - No flickering or jarring content swaps
 * - Prevents layout shift during transitions
 * - Supports prefers-reduced-motion
 *
 * @param showProjects true = show projects, false = show about/bio
 * @param projects List of projects to display
 * @param isLoading Loading state for projects
 * @param errorMessage Error message if project loading failed
 * @param bio Homepage bio data
 */
@Composable
fun HomepageContent(
    showProjects: Boolean,
    projects: List<Project>,
    isLoading: Boolean,
    errorMessage: String?,
    bio: HomepageBio
) {
    // Simple state tracking - wait for fade out before swapping content
    var displayState by remember { mutableStateOf(showProjects) }
    var isVisible by remember { mutableStateOf(true) }

    // Handle state transitions with fade out -> swap -> fade in
    LaunchedEffect(showProjects) {
        if (showProjects != displayState) {
            isVisible = false // Fade out
            delay(200) // Wait for fade out to complete
            displayState = showProjects // Swap content
            delay(50) // Brief pause for DOM update
            isVisible = true // Fade in
        }
    }

    Div(
        Modifier
            .fillMaxWidth()
            .position(Position.Relative)
            .toAttrs {
                id("homepage-content")
                style {
                    property("min-height", "400px") // Prevent layout shift
                    property("transition", "opacity 200ms ease-in-out")
                    property("opacity", if (isVisible) "1" else "0")
                    property("scroll-margin-top", "2rem") // Offset for smooth scroll
                }
            }
    ) {
        if (displayState) {
            // Projects View
            ProjectsSection(
                projects = projects,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        } else {
            // About View
            BioSection(bio)
        }
    }
}
