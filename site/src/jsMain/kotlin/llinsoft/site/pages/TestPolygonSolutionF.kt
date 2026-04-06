package llinsoft.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import llinsoft.site.components.widgets.*
import llinsoft.site.models.Project
import llinsoft.site.models.ProjectLinks
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/**
 * Test page for all reusable UI components
 *
 * This page demonstrates all extracted components from the widgets package:
 * - IconButton (round icon buttons)
 * - PrimaryButton (large primary actions)
 * - SecondaryButton (medium secondary actions)
 * - TechTag (technology stack pills)
 * - ProjectHero (hero section with image, title, description)
 * - ProjectSection (reusable section containers)
 * - GalleryThumbnail (interactive gallery thumbnails)
 * - ProjectCard (large landscape cards)
 * - SmallProjectCard (small vertical cards)
 */
@Page("test/polygon-solution-f")
@Composable
fun PolygonSolutionFTestPage() {
    val testProject1 = Project(
        id = "test-1",
        slug = "test-project-1",
        title = "Large Card Test",
        shortDescription = "Testing reusable components with large landscape cards.",
        fullDescription = "",
        techStack = listOf("Kotlin", "Compose", "Ktor"),
        featuredTech = listOf("Kotlin", "Compose", "Ktor"),
        features = emptyList(),
        thumbnailUrl = "/images/projects/project-1-thumb.svg",
        heroImageUrl = "",
        galleryImages = emptyList(),
        links = ProjectLinks(),
        date = "2026-01",
        order = 1
    )

    val testProject2 = Project(
        id = "test-2",
        slug = "test-project-2",
        title = "Vertical Card",
        shortDescription = "Testing small vertical cards.",
        fullDescription = "",
        techStack = listOf("React", "TypeScript", "Node"),
        featuredTech = listOf("React", "TypeScript", "Node"),
        features = emptyList(),
        thumbnailUrl = "/images/projects/project-2-thumb.svg",
        heroImageUrl = "",
        galleryImages = emptyList(),
        links = ProjectLinks(),
        date = "2026-02",
        order = 2
    )

    Box(
        modifier = Modifier
            .minHeight(100.vh)
            .padding(32.px)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .maxWidth(1400.px)
                .toAttrs {
                    style {
                        property("background", "linear-gradient(180deg, #0b1020 0%, #11182b 100%)")
                        property("color", "#eaf1ff")
                        property("font-family", "Inter, system-ui, sans-serif")
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(32.px)
            ) {
                // Header
                Column(Modifier.gap(12.px)) {
                    H1 { Text("UI Components Test Page") }
                    P(attrs = {
                        style {
                            fontSize(16.px)
                            property("color", "rgba(234, 241, 255, 0.82)")
                        }
                    }) {
                        Text("All components extracted to components/widgets/ - Production ready and reusable!")
                    }
                }

                // Button Showcase Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("Button Components")
                        }
                    }

                    // IconButton examples
                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("IconButton (50x50)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            IconButton(
                                ariaLabel = "Close",
                                onClick = { console.log("Close clicked") }
                            ) {
                                SpanText("✕")
                            }
                            IconButton(
                                ariaLabel = "Left",
                                onClick = { console.log("Left clicked") }
                            ) {
                                SpanText("←")
                            }
                            IconButton(
                                ariaLabel = "Right",
                                onClick = { console.log("Right clicked") }
                            ) {
                                SpanText("→")
                            }
                            IconButton(
                                ariaLabel = "Check",
                                onClick = { console.log("Check clicked") }
                            ) {
                                SpanText("✓")
                            }
                        }
                    }

                    // PrimaryButton examples
                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("PrimaryButton (220x54)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            PrimaryButton(
                                text = "Primary Action",
                                onClick = { console.log("Primary clicked") }
                            )
                            PrimaryButton(
                                text = "With Icon Left",
                                onClick = { console.log("Icon left clicked") },
                                iconSrc = "/icons/chevron-left.svg",
                                iconFirst = true
                            )
                            PrimaryButton(
                                text = "With Icon Right",
                                onClick = { console.log("Icon right clicked") },
                                iconSrc = "/icons/chevron-right.svg",
                                iconFirst = false
                            )
                        }
                    }

                    // SecondaryButton examples
                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("SecondaryButton (160x42)")
                        }
                        Row(Modifier.gap(16.px).fillMaxWidth()) {
                            SecondaryButton(
                                text = "Secondary",
                                onClick = { console.log("Secondary clicked") }
                            )
                            SecondaryButton(
                                text = "Cancel",
                                onClick = { console.log("Cancel clicked") }
                            )
                            SecondaryButton(
                                text = "Learn More",
                                onClick = { console.log("Learn More clicked") }
                            )
                        }
                    }
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // TechTag Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("TechTag Component")
                        }
                    }

                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Auto-sizing Tech Tags")
                        }
                        P(attrs = {
                            style {
                                fontSize(14.px)
                                property("color", "rgba(234, 241, 255, 0.7)")
                                property("margin-bottom", "16px")
                            }
                        }) {
                            Text("Hover to see bold border effect. Green theme, monospace font, no glow.")
                        }
                        Row(Modifier.gap(12.px).fillMaxWidth().flexWrap(FlexWrap.Wrap)) {
                            TechTag("Kotlin")
                            TechTag("Compose Web")
                            TechTag("Ktor")
                            TechTag("Material Design")
                            TechTag("SQLDelight")
                            TechTag("PostgreSQL")
                            TechTag("Docker")
                            TechTag("Kubernetes")
                        }
                    }
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // ProjectHero Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("ProjectHero Component")
                        }
                    }

                    ProjectHero(
                        title = "Sample Project Title",
                        description = "This is a sample hero section with a large title and description.",
                        heroImageUrl = "/images/projects/project-1-thumb.svg"
                    )
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // ProjectSection Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("ProjectSection Component")
                        }
                    }

                    ProjectSection(heading = "Section with Heading") {
                        P { Text("This is a reusable section container with consistent padding, background, and border radius.") }
                    }

                    ProjectSection {
                        P { Text("This is a section without a heading - just content.") }
                    }
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // GalleryThumbnail Section
                Column(Modifier.gap(24.px)) {
                    Div(Modifier.toAttrs()) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(24.px)
                                    property("color", "rgba(150, 210, 255, 0.95)")
                                    property("margin-bottom", "16px")
                                }
                            }
                        ) {
                            Text("GalleryThumbnail Component")
                        }
                    }

                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Gallery Thumbnails (7.3rem x 12.8rem)")
                        }
                        P(attrs = {
                            style {
                                fontSize(14.px)
                                property("color", "rgba(234, 241, 255, 0.7)")
                                property("margin-bottom", "16px")
                            }
                        }) {
                            Text("Interactive thumbnails with SVG borders, glow effects, and selected state.")
                        }
                        Row(Modifier.gap(12.px).fillMaxWidth()) {
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-1-thumb.svg",
                                alt = "Gallery thumbnail 1",
                                ariaLabel = "Open screenshot 1",
                                width = "7.3rem",
                                height = "12.8rem",
                                borderRadius = "0.62rem",
                                borderRadiusPx = 10,
                                estimatedWidthPx = 117,
                                estimatedHeightPx = 205,
                                isSelected = false,
                                onClick = { console.log("Thumbnail 1 clicked") }
                            )
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-2-thumb.svg",
                                alt = "Gallery thumbnail 2",
                                ariaLabel = "Open screenshot 2",
                                width = "7.3rem",
                                height = "12.8rem",
                                borderRadius = "0.62rem",
                                borderRadiusPx = 10,
                                estimatedWidthPx = 117,
                                estimatedHeightPx = 205,
                                isSelected = true,
                                onClick = { console.log("Thumbnail 2 clicked") }
                            )
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-1-thumb.svg",
                                alt = "Gallery thumbnail 3",
                                ariaLabel = "Open screenshot 3",
                                width = "7.3rem",
                                height = "12.8rem",
                                borderRadius = "0.62rem",
                                borderRadiusPx = 10,
                                estimatedWidthPx = 117,
                                estimatedHeightPx = 205,
                                isSelected = false,
                                onClick = { console.log("Thumbnail 3 clicked") }
                            )
                        }
                    }

                    Div(Modifier.toAttrs()) {
                        H3(
                            attrs = {
                                style {
                                    fontSize(18.px)
                                    property("color", "rgba(234, 241, 255, 0.9)")
                                    property("margin-bottom", "12px")
                                }
                            }
                        ) {
                            Text("Small Thumbnails (3.2rem x 5.7rem)")
                        }
                        Row(Modifier.gap(8.px).fillMaxWidth()) {
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-1-thumb.svg",
                                alt = "Small thumbnail 1",
                                ariaLabel = "Select screenshot 1",
                                width = "3.2rem",
                                height = "5.7rem",
                                borderRadius = "0.5rem",
                                borderRadiusPx = 8,
                                estimatedWidthPx = 51,
                                estimatedHeightPx = 91,
                                isSelected = false,
                                onClick = { console.log("Small thumbnail 1 clicked") }
                            )
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-2-thumb.svg",
                                alt = "Small thumbnail 2",
                                ariaLabel = "Select screenshot 2",
                                width = "3.2rem",
                                height = "5.7rem",
                                borderRadius = "0.5rem",
                                borderRadiusPx = 8,
                                estimatedWidthPx = 51,
                                estimatedHeightPx = 91,
                                isSelected = true,
                                onClick = { console.log("Small thumbnail 2 clicked") }
                            )
                            GalleryThumbnail(
                                imageUrl = "/images/projects/project-1-thumb.svg",
                                alt = "Small thumbnail 3",
                                ariaLabel = "Select screenshot 3",
                                width = "3.2rem",
                                height = "5.7rem",
                                borderRadius = "0.5rem",
                                borderRadiusPx = 8,
                                estimatedWidthPx = 51,
                                estimatedHeightPx = 91,
                                isSelected = false,
                                onClick = { console.log("Small thumbnail 3 clicked") }
                            )
                        }
                    }
                }

                // Divider
                Div(attrs = {
                    style {
                        property("height", "1px")
                        property("background", "rgba(120, 180, 245, 0.2)")
                        property("margin", "32px 0")
                    }
                }) {}

                // ProjectCard Section
                Div(Modifier.toAttrs()) {
                    H2(
                        attrs = {
                            style {
                                fontSize(24.px)
                                property("color", "rgba(150, 210, 255, 0.95)")
                                property("margin-bottom", "16px")
                            }
                        }
                    ) {
                        Text("ProjectCard Components")
                    }
                }

                // Row 1: 2 large landscape cards
                Div(
                    attrs = Modifier.fillMaxWidth().toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(2, 1fr)")
                            property("gap", "24px")
                        }
                    }
                ) {
                    ProjectCard(testProject1, 650, 400)
                    ProjectCard(testProject1, 650, 400)
                }

                // Row 2: 5 small vertical cards
                Div(
                    attrs = Modifier.fillMaxWidth().toAttrs {
                        style {
                            property("display", "grid")
                            property("grid-template-columns", "repeat(5, 1fr)")
                            property("gap", "16px")
                        }
                    }
                ) {
                    SmallProjectCard(testProject2, 1, 267, 420)
                    SmallProjectCard(testProject2, 2, 267, 420)
                    SmallProjectCard(testProject2, 3, 267, 420)
                    SmallProjectCard(testProject2, 4, 267, 420)
                    SmallProjectCard(testProject2, 5, 267, 420)
                }
            }
        }
    }
}
