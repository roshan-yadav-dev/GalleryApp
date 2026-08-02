package com.gallery.app.feature.albums

import com.gallery.app.core.domain.model.Album

data class AlbumsUiState(
    val isLoading: Boolean = true,
    val albums: List<Album> = emptyList(),
    val errorMessage: String? = null
)
