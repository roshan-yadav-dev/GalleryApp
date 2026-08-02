package com.gallery.app.feature.search

import com.gallery.app.core.domain.model.MediaItem

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<MediaItem> = emptyList(),
    val errorMessage: String? = null
)
