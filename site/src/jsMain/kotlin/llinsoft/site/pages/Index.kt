package llinsoft.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.cssRem
import llinsoft.site.components.layouts.PageLayoutData
import llinsoft.site.components.sections.HeroSection
import llinsoft.site.components.sections.HomepageContent
import llinsoft.site.components.widgets.HomepageSegmentedControl
import llinsoft.site.data.HomepageBioData
import llinsoft.site.data.ProjectDataSource
import llinsoft.site.models.Project

@InitRoute
fun initHomePage(ctx: InitRouteContext) {
    ctx.data.add(PageLayoutData("Home"))
}

@Page
@Layout(".components.layouts.PageLayout")
@Composable
fun HomePage() {
    // State: true = Projects view, false = About view
    var showProjects by remember { mutableStateOf(true) }

    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        runCatching { ProjectDataSource.repository.getAllProjects() }
            .onSuccess {
                projects = it
                errorMessage = null
            }
            .onFailure {
                projects = emptyList()
                errorMessage = "Failed to load projects. Please refresh and try again."
            }
        isLoading = false
    }

    Column(
        Modifier
            .fillMaxWidth()
            .gap(1.cssRem)
    ) {
        // Hero section with integrated view toggle
        HeroSection(
            showProjects = showProjects,
            onToggle = { newValue ->
                // Smart scroll: only if content is not sufficiently visible
                val contentElement = document.getElementById("homepage-content")
                if (contentElement != null) {
                    val rect = contentElement.getBoundingClientRect()
                    val viewportHeight = window.innerHeight.toDouble()

                    // Check if at least 200px of content is visible
                    // If less than 200px showing, user can't see enough to understand the toggle
                    val visibleContentHeight = viewportHeight - rect.top
                    val needsScroll = visibleContentHeight < 200

                    if (needsScroll) {
                        // Content is not visible - need to scroll
                        if (newValue) {
                            // Switching TO Projects: immediate full scroll to show cards
                            contentElement.scrollIntoView(
                                js("{ behavior: 'smooth', block: 'start' }")
                            )
                        } else {
                            // Switching TO About Me: gentle scroll after content swaps (delayed)
                            // Delay prevents jarring jump from page height change
                            window.setTimeout({
                                contentElement.scrollIntoView(
                                    js("{ behavior: 'smooth', block: 'nearest' }")
                                )
                            }, 250)
                        }
                    }
                    // If content already visible: no scroll needed
                }

                showProjects = newValue
            }
        )

        // Animated content wrapper
        HomepageContent(
            showProjects = showProjects,
            projects = projects,
            isLoading = isLoading,
            errorMessage = errorMessage,
            bio = HomepageBioData.content
        )
    }
}
