package com.gallery.app.feature.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.usecase.GetMediaDetailsUseCase
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase
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
class MediaViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMediaItemsUseCase: GetMediaItemsUseCase,
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val moveToTrashUseCase: MoveToTrashUseCase
) : ViewModel() {

    private val initialMediaId: Long = savedStateHandle.get<Long>("mediaId") ?: -1L

    private val _uiState = MutableStateFlow(MediaViewerUiState())
    val uiState: StateFlow<MediaViewerUiState> = _uiState.asStateFlow()

    init {
        loadMediaItems()
    }

    private fun loadMediaItems() {
        getMediaItemsUseCase()
            .onEach { list ->
                val startIndex = list.indexOfFirst { it.id == initialMediaId }.coerceAtLeast(0)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        mediaItems = list,
                        currentIndex = startIndex
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onPageChanged(index: Int) {
        _uiState.update { it.copy(currentIndex = index) }
    }

    fun toggleSystemBars() {
        _uiState.update { it.copy(showSystemBars = !it.showSystemBars) }
    }

    fun toggleFavorite() {
        val currentItem = uiState.value.currentMediaItem ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(currentItem)
        }
    }

    fun deleteCurrentMedia() {
        val currentItem = uiState.value.currentMediaItem ?: return
        viewModelScope.launch {
            moveToTrashUseCase(currentItem)
        }
    }

    fun showDetails() {
        val currentItem = uiState.value.currentMediaItem ?: return
        viewModelScope.launch {
            val metadata = getMediaDetailsUseCase(currentItem)
            _uiState.update { it.copy(currentMetadata = metadata, showDetailsSheet = true) }
        }
    }

    fun hideDetails() {
        _uiState.update { it.copy(showDetailsSheet = false) }
    }
}
