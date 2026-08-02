package com.gallery.app.core.editor.engine

import android.net.Uri
import com.gallery.app.core.domain.model.editor.ClipType
import com.gallery.app.core.domain.model.editor.ColorAdjustmentConfig
import com.gallery.app.core.domain.model.editor.CropConfig
import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.EditorTrack
import com.gallery.app.core.domain.model.editor.OverlayType
import com.gallery.app.core.domain.model.editor.TextStyleConfig
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import com.gallery.app.core.domain.model.editor.TransformConfig
import com.gallery.app.core.domain.model.editor.VideoFilterType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimelineEngine @Inject constructor() {

    fun splitClip(state: TimelineState, clipId: String, playheadMs: Long): TimelineState {
        val clip = state.tracks.flatMap { it.clips }.find { it.id == clipId } ?: return state
        if (playheadMs <= clip.timelineStartMs || playheadMs >= clip.timelineEndMs) return state

        val splitOffset = playheadMs - clip.timelineStartMs
        val clip1TrimEnd = clip.trimStartMs + splitOffset

        val clip1 = clip.copy(
            id = UUID.randomUUID().toString(),
            timelineEndMs = playheadMs,
            trimEndMs = clip1TrimEnd
        )

        val clip2 = clip.copy(
            id = UUID.randomUUID().toString(),
            timelineStartMs = playheadMs,
            trimStartMs = clip1TrimEnd
        )

        val newTracks = state.tracks.map { track ->
            if (track.id == clip.trackId) {
                val newClips = track.clips.flatMap { c ->
                    if (c.id == clip.id) listOf(clip1, clip2) else listOf(c)
                }
                track.copy(clips = newClips)
            } else track
        }

        return state.copy(tracks = newTracks, selectedClipId = clip1.id)
    }

    fun trimClip(state: TimelineState, clipId: String, newTrimStartMs: Long, newTrimEndMs: Long): TimelineState {
        val clip = state.tracks.flatMap { it.clips }.find { it.id == clipId } ?: return state
        val validStart = newTrimStartMs.coerceIn(0L, newTrimEndMs - 200L)
        val validEnd = newTrimEndMs.coerceIn(validStart + 200L, clip.sourceDurationMs)
        val newDuration = validEnd - validStart

        val updatedClip = clip.copy(
            trimStartMs = validStart,
            trimEndMs = validEnd,
            timelineEndMs = clip.timelineStartMs + newDuration
        )

        val newTracks = state.tracks.map { track ->
            if (track.id == clip.trackId) {
                track.copy(clips = track.clips.map { if (it.id == clip.id) updatedClip else it })
            } else track
        }

        return state.copy(tracks = newTracks)
    }

    fun deleteClip(state: TimelineState, clipId: String): TimelineState {
        val clip = state.tracks.flatMap { it.clips }.find { it.id == clipId } ?: return state
        val newTracks = state.tracks.map { track ->
            if (track.id == clip.trackId) {
                track.copy(clips = track.clips.filterNot { it.id == clip.id })
            } else track
        }
        return state.copy(tracks = newTracks, selectedClipId = null)
    }

    fun duplicateClip(state: TimelineState, clipId: String): TimelineState {
        val clip = state.tracks.flatMap { it.clips }.find { it.id == clipId } ?: return state
        val newClip = clip.copy(
            id = UUID.randomUUID().toString(),
            timelineStartMs = clip.timelineEndMs,
            timelineEndMs = clip.timelineEndMs + clip.durationMs
        )

        val newTracks = state.tracks.map { track ->
            if (track.id == clip.trackId) {
                track.copy(clips = track.clips + newClip)
            } else track
        }
        return state.copy(tracks = newTracks, selectedClipId = newClip.id)
    }

    fun setClipVolume(state: TimelineState, clipId: String, volume: Float): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(volume = volume.coerceIn(0f, 1f)) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun setClipSpeed(state: TimelineState, clipId: String, speed: Float): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(speed = speed.coerceIn(0.25f, 4.0f)) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun setClipFilter(state: TimelineState, clipId: String, filter: VideoFilterType): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(filterType = filter) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun setClipColorAdjustments(state: TimelineState, clipId: String, adjustments: ColorAdjustmentConfig): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(colorAdjustments = adjustments) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun setClipTransform(state: TimelineState, clipId: String, transform: TransformConfig): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(transformConfig = transform) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun setClipCrop(state: TimelineState, clipId: String, crop: CropConfig): TimelineState {
        val newTracks = state.tracks.map { track ->
            track.copy(clips = track.clips.map { clip ->
                if (clip.id == clipId) clip.copy(cropConfig = crop) else clip
            })
        }
        return state.copy(tracks = newTracks)
    }

    fun addOverlayClip(
        state: TimelineState,
        overlayType: OverlayType,
        textConfig: TextStyleConfig? = null
    ): TimelineState {
        val trackType = if (overlayType == OverlayType.TEXT) TrackType.TEXT else TrackType.OVERLAY
        val targetTrack = state.tracks.find { it.type == trackType } ?: return state
        val playhead = state.playheadMs

        val newClip = EditorClip(
            id = UUID.randomUUID().toString(),
            trackId = targetTrack.id,
            type = if (overlayType == OverlayType.TEXT) ClipType.TEXT else ClipType.OVERLAY,
            sourceUri = Uri.EMPTY,
            sourceDurationMs = 3000L,
            timelineStartMs = playhead,
            timelineEndMs = playhead + 3000L,
            textConfig = textConfig,
            overlayType = overlayType,
            transformConfig = TransformConfig()
        )

        val newTracks = state.tracks.map { track ->
            if (track.id == targetTrack.id) {
                track.copy(clips = track.clips + newClip)
            } else track
        }
        return state.copy(tracks = newTracks, selectedClipId = newClip.id)
    }
}
