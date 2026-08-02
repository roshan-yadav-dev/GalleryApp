package com.gallery.app.core.datastore

import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK
}

data class UserPreferences(
    val theme: AppTheme = AppTheme.SYSTEM,
    val dynamicColor: Boolean = true,
    val gridSize: GridSize = GridSize.MEDIUM,
    val sortBy: SortBy = SortBy.DATE,
    val sortDirection: SortDirection = SortDirection.DESCENDING,
    val showHiddenAlbums: Boolean = false,
    val enableTrash: Boolean = true,
    val animationsEnabled: Boolean = true
)
