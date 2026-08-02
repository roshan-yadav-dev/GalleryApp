package com.gallery.app.feature.editor.image

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.domain.model.editor.ImageFilterType
import com.gallery.app.core.domain.model.editor.ImageEditorState
import com.gallery.app.core.editor.image.PhotoEditorEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import javax.inject.Inject

@HiltViewModel
class ImageEditorViewModel @Inject constructor(
    private val editorEngine: PhotoEditorEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageEditorState())
    val uiState: StateFlow<ImageEditorState> = _uiState.asStateFlow()

    private val _previewBitmap = MutableStateFlow<Bitmap?>(null)
    val previewBitmap: StateFlow<Bitmap?> = _previewBitmap.asStateFlow()

    private var rawSourceBitmap: Bitmap? = null
    private val undoStack = ArrayDeque<ImageEditorState>()
    private val redoStack = ArrayDeque<ImageEditorState>()

    fun loadMedia(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(sourceUri = uri) }
            val loaded = editorEngine.loadBitmap(uri)
            rawSourceBitmap = loaded
            renderPreview()
        }
    }

    private fun pushState(newState: ImageEditorState) {
        undoStack.push(_uiState.value)
        redoStack.clear()
        _uiState.value = newState
        renderPreview()
    }

    fun rotateRight() {
        val currentRot = _uiState.value.rotationDegrees
        pushState(_uiState.value.copy(rotationDegrees = (currentRot + 90f) % 360f))
    }

    fun flipHorizontal() {
        pushState(_uiState.value.copy(flipHorizontal = !_uiState.value.flipHorizontal))
    }

    fun flipVertical() {
        pushState(_uiState.value.copy(flipVertical = !_uiState.value.flipVertical))
    }

    fun updateBrightness(value: Float) {
        pushState(_uiState.value.copy(brightness = value))
    }

    fun updateContrast(value: Float) {
        pushState(_uiState.value.copy(contrast = value))
    }

    fun updateSaturation(value: Float) {
        pushState(_uiState.value.copy(saturation = value))
    }

    fun setFilter(filter: ImageFilterType) {
        pushState(_uiState.value.copy(activeFilter = filter))
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_uiState.value)
            _uiState.value = undoStack.pop()
            renderPreview()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_uiState.value)
            _uiState.value = redoStack.pop()
            renderPreview()
        }
    }

    private fun renderPreview() {
        val raw = rawSourceBitmap ?: return
        viewModelScope.launch {
            val edited = editorEngine.applyEdits(raw, _uiState.value)
            _previewBitmap.value = edited
        }
    }

    fun saveImage() {
        val bitmapToSave = _previewBitmap.value ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val savedUri = editorEngine.saveEditedImage(bitmapToSave)
            if (savedUri != null) {
                _uiState.update { it.copy(isSaving = false, savedUri = savedUri) }
            } else {
                _uiState.update { it.copy(isSaving = false, errorMessage = "Failed to save edited photo") }
            }
        }
    }
}
