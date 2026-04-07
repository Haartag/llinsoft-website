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
        name = "Valeriy Timofeev",
        role = "Mobile Developer",
        paragraphs = listOf(
            "I am a mobile developer based in Novi Sad, specializing in cross-platform products powered by Kotlin Multiplatform. Building on a strong foundation in Android, I focus on creating applications that share robust core logic across platforms without sacrificing a native, carefully refined user experience.",
            "My approach to engineering is heavily product-oriented. I believe that clean architecture and maintainable code are most valuable when they directly improve the user experience. Whether I am writing shared logic or connecting backend systems, my goal is always to bridge the gap between solid infrastructure and smooth interaction.",
            "I take ownership of the entire development lifecycle for the consumer apps I build. By handling everything from backend architecture and authentication to the final UI and monetization logic, I deliver polished, production-ready applications.",
            "Seeing a project evolve from bare infrastructure into an app that feels completely natural to use is why I enjoy this process. For me, the most rewarding part of engineering is shipping something that simply works exactly as it should.",
            "(And when I'm not building apps, I'm usually coding my own indie projects, riding my bike, or planning my next backpacking trip.)"
        ),
        primaryPhotoUrl = "/images/profile/photo.png",
        secondaryPhotoUrl = "/images/profile/profile-secondary.svg",
    )
}
