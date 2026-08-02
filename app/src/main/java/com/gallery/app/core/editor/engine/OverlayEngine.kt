package com.gallery.app.core.editor.engine

import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.OverlayType
import com.gallery.app.core.domain.model.editor.TimelineState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayEngine @Inject constructor() {

    fun getActiveOverlaysAtTime(state: TimelineState, playheadMs: Long): List<EditorClip> {
        val overlayClips = mutableListOf<EditorClip>()
        state.tracks.forEach { track ->
            track.clips.forEach { clip ->
                if (clip.overlayType != OverlayType.NONE || clip.textConfig != null) {
                    if (playheadMs in clip.timelineStartMs..clip.timelineEndMs) {
                        overlayClips.add(clip)
                    }
                }
            }
        }
        return overlayClips.sortedBy { it.transformConfig?.layerIndex ?: 0 }
    }
}
