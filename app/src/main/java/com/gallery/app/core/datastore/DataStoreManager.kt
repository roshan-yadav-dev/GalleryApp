package com.gallery.app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gallery.app.core.common.Constants
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

@Singleton
class DataStoreManager @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val GRID_SIZE = stringPreferencesKey("grid_size")
        val SORT_BY = stringPreferencesKey("sort_by")
        val SORT_DIRECTION = stringPreferencesKey("sort_direction")
        val SHOW_HIDDEN = booleanPreferencesKey("show_hidden")
        val ENABLE_TRASH = booleanPreferencesKey("enable_trash")
        val ANIMATIONS = booleanPreferencesKey("animations_enabled")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            theme = AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.SYSTEM.name),
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true,
            gridSize = GridSize.valueOf(prefs[Keys.GRID_SIZE] ?: GridSize.MEDIUM.name),
            sortBy = SortBy.valueOf(prefs[Keys.SORT_BY] ?: SortBy.DATE.name),
            sortDirection = SortDirection.valueOf(prefs[Keys.SORT_DIRECTION] ?: SortDirection.DESCENDING.name),
            showHiddenAlbums = prefs[Keys.SHOW_HIDDEN] ?: false,
            enableTrash = prefs[Keys.ENABLE_TRASH] ?: true,
            animationsEnabled = prefs[Keys.ANIMATIONS] ?: true
        )
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { prefs -> prefs[Keys.THEME] = theme.name }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun setGridSize(gridSize: GridSize) {
        context.dataStore.edit { prefs -> prefs[Keys.GRID_SIZE] = gridSize.name }
    }

    suspend fun setSortOrder(sortBy: SortBy, direction: SortDirection) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SORT_BY] = sortBy.name
            prefs[Keys.SORT_DIRECTION] = direction.name
        }
    }

    suspend fun setShowHiddenAlbums(show: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SHOW_HIDDEN] = show }
    }
}
