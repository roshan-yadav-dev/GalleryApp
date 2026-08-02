package com.gallery.app.feature.favorites

import com.gallery.app.core.domain.model.MediaItem

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<MediaItem> = emptyList(),
    val errorMessage: String? = null
)
