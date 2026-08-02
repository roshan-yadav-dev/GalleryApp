package com.gallery.app.core.data.repository

import com.gallery.app.core.datastore.AppTheme
import com.gallery.app.core.datastore.DataStoreManager
import com.gallery.app.core.datastore.UserPreferences
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import com.gallery.app.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override val userPreferences: Flow<UserPreferences>
        get() = dataStoreManager.userPreferences

    override suspend fun setTheme(theme: AppTheme) {
        dataStoreManager.setTheme(theme)
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        dataStoreManager.setDynamicColor(enabled)
    }

    override suspend fun setGridSize(gridSize: GridSize) {
        dataStoreManager.setGridSize(gridSize)
    }

    override suspend fun setSortOrder(sortBy: SortBy, direction: SortDirection) {
        dataStoreManager.setSortOrder(sortBy, direction)
    }

    override suspend fun setShowHiddenAlbums(show: Boolean) {
        dataStoreManager.setShowHiddenAlbums(show)
    }
}
