package llinsoft.site.data

import llinsoft.site.models.Project

interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectBySlug(slug: String): Project?
}
