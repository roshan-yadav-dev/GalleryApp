package com.gallery.app.core.domain.usecase

import com.gallery.app.core.datastore.AppTheme
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import com.gallery.app.core.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend fun setTheme(theme: AppTheme) {
        settingsRepository.setTheme(theme)
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        settingsRepository.setDynamicColor(enabled)
    }

    suspend fun setGridSize(gridSize: GridSize) {
        settingsRepository.setGridSize(gridSize)
    }

    suspend fun setSortOrder(sortBy: SortBy, direction: SortDirection) {
        settingsRepository.setSortOrder(sortBy, direction)
    }

    suspend fun setShowHiddenAlbums(show: Boolean) {
        settingsRepository.setShowHiddenAlbums(show)
    }
}
