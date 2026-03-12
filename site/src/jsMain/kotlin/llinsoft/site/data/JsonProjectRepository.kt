package llinsoft.site.data

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import llinsoft.site.models.Project

class JsonProjectRepository(
    private val path: String = "/projects.json",
    private val json: Json = Json { ignoreUnknownKeys = true },
) : ProjectRepository {
    private var cachedProjects: List<Project>? = null

    override suspend fun getAllProjects(): List<Project> {
        cachedProjects?.let { return it }

        val response = window.fetch(path).await()
        if (!response.ok) {
            error("Failed to load project data from $path (status ${response.status.toInt()})")
        }

        val payload = response.text().await()
        return json.decodeFromString<List<Project>>(payload)
            .sortedBy(Project::order)
            .also { cachedProjects = it }
    }

    override suspend fun getProjectBySlug(slug: String): Project? {
        return getAllProjects().firstOrNull { it.slug == slug }
    }
}
