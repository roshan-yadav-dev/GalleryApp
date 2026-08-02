package com.gallery.app.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.common.DateFormatter
import com.gallery.app.core.datastore.UserPreferences
import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase
import com.gallery.app.core.domain.usecase.GetSettingsUseCase
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase
import com.gallery.app.core.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val moveToTrashUseCase: MoveToTrashUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        combine(
            getSettingsUseCase(),
            getMediaItemsUseCase()
        ) { settings: UserPreferences, items: List<MediaItem> ->
            val groups = items.groupBy { DateFormatter.formatDateHeader(it.dateAdded * 1000) }
                .map { (header, groupItems) -> GalleryGroup(header, groupItems) }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    allItems = items,
                    groups = groups,
                    gridSize = settings.gridSize,
                    sortOrder = state.sortOrder.copy(
                        sortBy = settings.sortBy,
                        direction = settings.sortDirection
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun toggleItemSelection(id: Long) {
        _uiState.update { state ->
            val currentSelected = state.selectedItemIds.toMutableSet()
            if (currentSelected.contains(id)) {
                currentSelected.remove(id)
            } else {
                currentSelected.add(id)
            }
            state.copy(
                selectedItemIds = currentSelected,
                isSelectionMode = currentSelected.isNotEmpty()
            )
        }
    }

    fun selectAll() {
        _uiState.update { state ->
            val allIds = state.allItems.map { it.id }.toSet()
            state.copy(
                selectedItemIds = allIds,
                isSelectionMode = allIds.isNotEmpty()
            )
        }
    }

    fun clearSelection() {
        _uiState.update { state ->
            state.copy(
                selectedItemIds = emptySet(),
                isSelectionMode = false
            )
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            val selected = _uiState.value.selectedItemIds
            val itemsToDelete = _uiState.value.allItems.filter { it.id in selected }
            itemsToDelete.forEach { item ->
                moveToTrashUseCase(item)
            }
            clearSelection()
        }
    }

    fun toggleFavoriteSelectedItems() {
        viewModelScope.launch {
            val selected = _uiState.value.selectedItemIds
            val itemsToFavorite = _uiState.value.allItems.filter { it.id in selected }
            itemsToFavorite.forEach { item ->
                toggleFavoriteUseCase(item)
            }
            clearSelection()
        }
    }

    fun setGridSize(gridSize: GridSize) {
        viewModelScope.launch {
            updateSettingsUseCase.setGridSize(gridSize)
        }
    }

    fun setSortOrder(sortBy: SortBy, direction: SortDirection) {
        viewModelScope.launch {
            updateSettingsUseCase.setSortOrder(sortBy, direction)
        }
    }
}
