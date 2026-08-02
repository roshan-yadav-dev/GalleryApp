package com.gallery.app.core.domain.model

data class FilterOptions(
    val mediaType: MediaType = MediaType.ALL,
    val albumId: Long? = null,
    val albumType: AlbumType? = null,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder()
)
