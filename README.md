# llinsoft-website

Personal portfolio website built with Kobweb, Kotlin, and Compose Web.

## Highlights
- Responsive homepage with hero, bio, and featured projects
- Dynamic project pages via `/projects/{slug}` routes
- Reusable UI components
- JSON-backed project repository (no backend required)

## Tech Stack
- Kobweb 0.23.3
- Kotlin 2.2.20
- Compose Web 1.8.0

## Development
- Build: `./website/gradlew -p website :site:compileKotlinJs`
- Run: `./website/gradlew -p website kobwebStart -t`
