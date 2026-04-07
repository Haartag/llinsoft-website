# Bug Fix: Private CssStyles Causing Recomposition Crash

## Problem Description

When navigating to project detail pages (`/projects/{slug}`), the application would freeze with a `GlobalSnapshotManager$globalWriteObserver` error in the browser console. The URL would change correctly, but the page would become completely unresponsive with infinite recomposition errors.

**Symptoms:**
- Navigation URL changes successfully
- Page freezes immediately on load
- Console shows: `Uncaught Object { stack: "", ... } Recomposer.kt:781` and `GlobalSnapshotManager$globalWriteObserver`
- Build warnings: `w: [ksp] Not registering css style 'val ProjectSectionContainerStyle', as it is not public`

## Root Cause

**Private CssStyles that access `colorMode` were not being registered by Kobweb's KSP (Kotlin Symbol Processing) annotation processor.**

When Compose tried to render components using these unregistered styles, they attempted to read `colorMode` state during the composition phase. This triggered state access violations, causing the `GlobalSnapshotManager` to detect writes during composition and throw errors, leading to infinite recomposition loops.

The specific problematic styles were:
- `ProjectSectionContainerStyle` in ProjectSection.kt
- `SectionHeadingStyle` in ProjectSection.kt
- `ProjectHeroTitleStyle` in ProjectHero.kt

All three styles used `colorMode.toSitePalette()` to access theme colors but were marked `private`, preventing Kobweb from properly registering them in the theme system.

## Solution

**Change all CssStyle declarations from `private` to `public` (or internal).**

```kotlin
// BEFORE (causes crash)
private val ProjectSectionContainerStyle = CssStyle.base {
    Modifier.backgroundColor(colorMode.toSitePalette().surface)
}

// AFTER (works correctly)
val ProjectSectionContainerStyle = CssStyle.base {
    Modifier.backgroundColor(colorMode.toSitePalette().surface)
}
```

Making the styles public allows Kobweb's KSP processor to find and register them during build time, ensuring proper state management during composition.

## Prevention

1. **Always make CssStyles public** unless you have a specific reason and manually register them in an `@InitSilk` block
2. **Monitor build warnings** - KSP warnings about unregistered styles indicate potential runtime issues
3. **If you must use private styles**, manually register them:
   ```kotlin
   @InitSilk
   fun initStyles(ctx: InitSilkContext) {
       ctx.theme.registerStyle(MyPrivateStyle)
   }
   ```
4. **Test navigation immediately** after extracting components to catch state-related issues early

## Additional Notes

This bug was particularly difficult to diagnose because:
- Navigation routing worked correctly (URL changed)
- The error message was generic and didn't point to CSS styles
- The freeze happened during initial composition, not user interaction
- Similar patterns (ProjectCard, GalleryThumbnail) worked fine because they didn't use private CssStyles accessing colorMode

Binary search debugging (progressively adding half the features at a time) successfully isolated the problematic component (ProjectSection) in under 5 test iterations.
