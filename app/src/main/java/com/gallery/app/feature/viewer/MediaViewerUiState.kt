package com.gallery.app.feature.viewer

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaMetadata

data class MediaViewerUiState(
    val isLoading: Boolean = true,
    val mediaItems: List<MediaItem> = emptyList(),
    val currentIndex: Int = 0,
    val currentMetadata: MediaMetadata? = null,
    val showSystemBars: Boolean = true,
    val showDetailsSheet: Boolean = false,
    val isSlideshowActive: Boolean = false,
    val errorMessage: String? = null
) {
    val currentMediaItem: MediaItem? get() = mediaItems.getOrNull(currentIndex)
}
