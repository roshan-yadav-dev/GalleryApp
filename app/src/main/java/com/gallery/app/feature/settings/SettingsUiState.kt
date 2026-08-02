package com.gallery.app.feature.settings

import com.gallery.app.core.datastore.AppTheme
import com.gallery.app.core.datastore.UserPreferences

data class SettingsUiState(
    val isLoading: Boolean = true,
    val userPreferences: UserPreferences = UserPreferences()
)
