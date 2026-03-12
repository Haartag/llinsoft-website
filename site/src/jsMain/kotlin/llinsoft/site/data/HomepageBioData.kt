package llinsoft.site.data

data class HomepageBio(
    val name: String,
    val role: String,
    val paragraphs: List<String>,
    val primaryPhotoUrl: String,
    val secondaryPhotoUrl: String,
)

object HomepageBioData {
    val content = HomepageBio(
        name = "Llin Soft",
        role = "Kotlin Multiplatform Engineer",
        paragraphs = listOf(
            "I build practical software products with Kotlin-first architecture, clean abstractions, and maintainable codebases.",
            "My recent work focuses on mobile and web experiences where product clarity, performance, and reliability are equally important.",
            "This portfolio highlights selected projects and the technical decisions behind them."
        ),
        primaryPhotoUrl = "/images/profile/profile-primary.svg",
        secondaryPhotoUrl = "/images/profile/profile-secondary.svg",
    )
}
