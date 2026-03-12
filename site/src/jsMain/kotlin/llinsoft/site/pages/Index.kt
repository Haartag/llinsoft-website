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
import org.jetbrains.compose.web.css.cssRem
import llinsoft.site.components.layouts.PageLayoutData
import llinsoft.site.components.sections.BioSection
import llinsoft.site.components.sections.ProjectsSection
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
        BioSection(HomepageBioData.content)
        ProjectsSection(
            projects = projects,
            isLoading = isLoading,
            errorMessage = errorMessage,
        )
    }
}
