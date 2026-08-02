package com.gallery.app.feature.editor

import com.gallery.app.core.domain.model.editor.ImageFilterType
import com.gallery.app.core.domain.model.editor.ImageEditorState
import com.gallery.app.core.editor.image.PhotoEditorEngine
import com.gallery.app.feature.editor.image.ImageEditorViewModel
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ImageEditorViewModelTest {

    private lateinit var viewModel: ImageEditorViewModel
    private val mockEngine: PhotoEditorEngine = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ImageEditorViewModel(mockEngine)
    }

    @Test
    fun `test rotateRight updates state and enables undo`() {
        assertFalse(viewModel.canUndo())
        assertEquals(0f, viewModel.uiState.value.rotationDegrees, 0.01f)

        viewModel.rotateRight()
        assertEquals(90f, viewModel.uiState.value.rotationDegrees, 0.01f)
        assertTrue(viewModel.canUndo())

        viewModel.undo()
        assertEquals(0f, viewModel.uiState.value.rotationDegrees, 0.01f)
        assertTrue(viewModel.canRedo())

        viewModel.redo()
        assertEquals(90f, viewModel.uiState.value.rotationDegrees, 0.01f)
    }

    @Test
    fun `test filter and adjustment state updates`() {
        viewModel.setFilter(ImageFilterType.SEPIA)
        assertEquals(ImageFilterType.SEPIA, viewModel.uiState.value.activeFilter)

        viewModel.updateBrightness(0.2f)
        assertEquals(0.2f, viewModel.uiState.value.brightness, 0.01f)

        viewModel.updateContrast(1.5f)
        assertEquals(1.5f, viewModel.uiState.value.contrast, 0.01f)
    }
}
