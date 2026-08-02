package com.gallery.app.core.editor.engine

import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.TimelineState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectionEngine @Inject constructor() {

    fun selectClip(state: TimelineState, clipId: String?): TimelineState {
        val selectedClip = state.tracks.flatMap { it.clips }.find { it.id == clipId }
        return state.copy(
            selectedClipId = clipId,
            selectedTrackId = selectedClip?.trackId
        )
    }

    fun clearSelection(state: TimelineState): TimelineState {
        return state.copy(selectedClipId = null, selectedTrackId = null)
    }
}
