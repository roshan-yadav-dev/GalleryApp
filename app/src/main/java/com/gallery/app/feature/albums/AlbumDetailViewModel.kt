package com.gallery.app.feature.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.common.DateFormatter
import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.usecase.GetAlbumsUseCase
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase
import com.gallery.app.feature.gallery.GalleryGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumDetailUiState(
    val isLoading: Boolean = true,
    val albumId: Long = 0L,
    val albumName: String = "",
    val allItems: List<MediaItem> = emptyList(),
    val groups: List<GalleryGroup> = emptyList(),
    val selectedItemIds: Set<Long> = emptySet(),
    val isSelectionMode: Boolean = false
)

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val moveToTrashUseCase: MoveToTrashUseCase
) : ViewModel() {

    val albumId: Long = savedStateHandle.get<Long>("albumId") ?: 0L

    private val _uiState = MutableStateFlow(AlbumDetailUiState(albumId = albumId))
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    init {
        loadAlbumContent()
    }

    private fun loadAlbumContent() {
        combine(
            getAlbumsUseCase(),
            getMediaItemsUseCase(FilterOptions(albumId = albumId))
        ) { albums, items ->
            val album = albums.find { it.id == albumId }
            val name = album?.name ?: items.firstOrNull()?.bucketName ?: "Album"
            val groups = items.groupBy { DateFormatter.formatDateHeader(it.dateAdded * 1000) }
                .map { (header, groupItems) -> GalleryGroup(header, groupItems) }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    albumName = name,
                    allItems = items,
                    groups = groups
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
}
