package com.gallery.app.feature.trash

import com.gallery.app.core.domain.model.TrashItem

data class TrashUiState(
    val isLoading: Boolean = true,
    val trashItems: List<TrashItem> = emptyList(),
    val totalSizeBytes: Long = 0L,
    val errorMessage: String? = null
)
