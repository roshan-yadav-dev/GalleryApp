package com.gallery.app.feature.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.domain.model.TrashItem
import com.gallery.app.core.domain.usecase.DeletePermanentlyUseCase
import com.gallery.app.core.domain.usecase.GetTrashItemsUseCase
import com.gallery.app.core.domain.usecase.RestoreFromTrashUseCase
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
class TrashViewModel @Inject constructor(
    private val getTrashItemsUseCase: GetTrashItemsUseCase,
    private val restoreFromTrashUseCase: RestoreFromTrashUseCase,
    private val deletePermanentlyUseCase: DeletePermanentlyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrashUiState())
    val uiState: StateFlow<TrashUiState> = _uiState.asStateFlow()

    init {
        loadTrash()
    }

    private fun loadTrash() {
        getTrashItemsUseCase()
            .onEach { list ->
                val totalSize = list.sumOf { it.sizeBytes }
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        trashItems = list,
                        totalSizeBytes = totalSize
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun restoreItem(trashItem: TrashItem) {
        viewModelScope.launch {
            restoreFromTrashUseCase(trashItem)
        }
    }

    fun deleteItemPermanently(trashItem: TrashItem) {
        viewModelScope.launch {
            deletePermanentlyUseCase(trashItem)
        }
    }
}
