package com.gallery.app.feature.gallery

import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.SortOrder

data class GalleryGroup(
    val dateHeader: String,
    val items: List<MediaItem>
)

data class GalleryUiState(
    val isLoading: Boolean = true,
    val groups: List<GalleryGroup> = emptyList(),
    val allItems: List<MediaItem> = emptyList(),
    val selectedItemIds: Set<Long> = emptySet(),
    val isSelectionMode: Boolean = false,
    val gridSize: GridSize = GridSize.MEDIUM,
    val sortOrder: SortOrder = SortOrder(),
    val errorMessage: String? = null
)
