package com.gallery.app.core.domain.repository

import com.gallery.app.core.datastore.AppTheme
import com.gallery.app.core.datastore.UserPreferences
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setTheme(theme: AppTheme)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setGridSize(gridSize: GridSize)
    suspend fun setSortOrder(sortBy: SortBy, direction: SortDirection)
    suspend fun setShowHiddenAlbums(show: Boolean)
}
