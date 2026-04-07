package llinsoft.site.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val slug: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val techStack: List<String>,
    val featuredTech: List<String>,
    val features: List<String>,
    val thumbnailUrl: String,
    val heroImageUrl: String,
    val galleryImages: List<String>,
    val links: ProjectLinks,
    val date: String,
    val order: Int,
)

@Serializable
data class ProjectLinks(
    val github: String? = null,
    val liveDemo: String? = null,
    val playStore: String? = null,
    val appStore: String? = null,
)
