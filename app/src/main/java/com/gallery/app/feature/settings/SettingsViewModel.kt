package com.gallery.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.datastore.AppTheme
import com.gallery.app.core.domain.usecase.GetSettingsUseCase
import com.gallery.app.core.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        getSettingsUseCase()
            .onEach { prefs ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        userPreferences = prefs
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            updateSettingsUseCase.setTheme(theme)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setDynamicColor(enabled)
        }
    }

    fun setShowHiddenAlbums(show: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setShowHiddenAlbums(show)
        }
    }
}
