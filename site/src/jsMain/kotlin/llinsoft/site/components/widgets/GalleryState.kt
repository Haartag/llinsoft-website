package llinsoft.site.components.widgets

/**
 * Gallery state management for image galleries with lightbox
 *
 * Encapsulates all gallery-related state in an immutable data structure
 * to simplify state management and make state transitions clear.
 *
 * @param selectedIndex Currently selected image index
 * @param railStart Starting index for the gallery rail viewport
 * @param lightboxRailStart Starting index for the lightbox thumbnail rail
 * @param isLightboxOpen Whether the lightbox modal is currently open
 */
data class GalleryState(
    val selectedIndex: Int = 0,
    val railStart: Int = 0,
    val lightboxRailStart: Int = 0,
    val isLightboxOpen: Boolean = false
) {
    /**
     * Update lightbox open state
     */
    fun withLightboxOpen(open: Boolean) = copy(isLightboxOpen = open)

    /**
     * Update selected image index
     */
    fun withSelectedIndex(index: Int) = copy(selectedIndex = index)

    /**
     * Update rail starting position
     */
    fun withRailStart(start: Int) = copy(railStart = start)

    /**
     * Update lightbox rail starting position
     */
    fun withLightboxRailStart(start: Int) = copy(lightboxRailStart = start)

    /**
     * Reset to initial state
     */
    fun reset() = GalleryState()
}
