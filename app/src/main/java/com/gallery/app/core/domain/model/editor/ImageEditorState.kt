package com.gallery.app.core.domain.model.editor

import android.net.Uri

enum class ImageFilterType(val displayName: String) {
    NONE("Original"),
    GRAYSCALE("Mono"),
    SEPIA("Sepia"),
    VINTAGE("Vintage"),
    WARM("Warm"),
    COOL("Cool"),
    INVERT("Invert")
}

data class ImageEditorState(
    val sourceUri: Uri? = null,
    val rotationDegrees: Float = 0f,
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val brightness: Float = 0f, // -1f to 1f
    val contrast: Float = 1f, // 0.5f to 2f
    val saturation: Float = 1f, // 0f to 2f
    val activeFilter: ImageFilterType = ImageFilterType.NONE,
    val isSaving: Boolean = false,
    val savedUri: Uri? = null,
    val errorMessage: String? = null
)
