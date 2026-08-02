package com.gallery.app.core.domain.model.editor

import android.net.Uri

enum class TrackType {
    VIDEO,
    AUDIO,
    TEXT,
    OVERLAY
}

enum class ClipType {
    VIDEO,
    AUDIO,
    TEXT,
    OVERLAY
}

enum class OverlayType {
    NONE,
    TEXT,
    STICKER,
    SHAPE,
    WATERMARK,
    EMOJI,
    BLUR,
    DRAWING
}

enum class VideoFilterType {
    NONE,
    GRAYSCALE,
    SEPIA,
    VINTAGE,
    WARM,
    COOL,
    INVERT,
    BLACK_WHITE,
    VIBRANT
}

data class ColorAdjustmentConfig(
    val brightness: Float = 0.0f,   // -1.0f to 1.0f
    val contrast: Float = 1.0f,     // 0.0f to 2.0f
    val saturation: Float = 1.0f,   // 0.0f to 2.0f
    val temperature: Float = 0.0f,  // -1.0f to 1.0f
    val vignette: Float = 0.0f      // 0.0f to 1.0f
)

data class CropConfig(
    val leftRatio: Float = 0.0f,
    val topRatio: Float = 0.0f,
    val rightRatio: Float = 1.0f,
    val bottomRatio: Float = 1.0f
)

data class TextStyleConfig(
    val text: String = "Sample Text",
    val textColor: Long = 0xFFFFFFFF, // Color ARGB
    val fontSizeSp: Int = 24,
    val positionXRatio: Float = 0.5f, // 0.0 to 1.0 relative screen width
    val positionYRatio: Float = 0.5f  // 0.0 to 1.0 relative screen height
)

data class TransformConfig(
    val xOffsetRatio: Float = 0.5f,
    val yOffsetRatio: Float = 0.5f,
    val scale: Float = 1.0f,
    val rotationDeg: Float = 0.0f,
    val opacity: Float = 1.0f,
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val layerIndex: Int = 0
)

data class EditorClip(
    val id: String,
    val trackId: String,
    val type: ClipType,
    val sourceUri: Uri,
    val sourceDurationMs: Long,
    val timelineStartMs: Long,
    val timelineEndMs: Long,
    val trimStartMs: Long = 0L,
    val trimEndMs: Long = sourceDurationMs,
    val volume: Float = 1.0f,
    val speed: Float = 1.0f,
    val isMuted: Boolean = false,
    val isReversed: Boolean = false,
    val isFrozen: Boolean = false,
    val filterType: VideoFilterType = VideoFilterType.NONE,
    val colorAdjustments: ColorAdjustmentConfig = ColorAdjustmentConfig(),
    val cropConfig: CropConfig = CropConfig(),
    val textConfig: TextStyleConfig? = null,
    val transformConfig: TransformConfig? = null,
    val overlayType: OverlayType = OverlayType.NONE
) {
    val durationMs: Long
        get() = (timelineEndMs - timelineStartMs).coerceAtLeast(1L)

    val trimDurationMs: Long
        get() = (trimEndMs - trimStartMs).coerceAtLeast(1L)
}

data class EditorTrack(
    val id: String,
    val type: TrackType,
    val title: String,
    val clips: List<EditorClip> = emptyList(),
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
    val volume: Float = 1.0f
)

data class TimelineState(
    val tracks: List<EditorTrack> = emptyList(),
    val playheadMs: Long = 0L,
    val selectedClipId: String? = null,
    val selectedTrackId: String? = null,
    val zoomPxPerSec: Float = 60.0f, // Pixels per second in timeline
    val isPlaying: Boolean = false,
    val showSafeGuides: Boolean = false,
    val isLooping: Boolean = true
) {
    val durationMs: Long
        get() {
            var maxEnd = 0L
            tracks.forEach { track ->
                track.clips.forEach { clip ->
                    if (clip.timelineEndMs > maxEnd) {
                        maxEnd = clip.timelineEndMs
                    }
                }
            }
            return maxEnd.coerceAtLeast(1000L) // Default minimum 1s
        }

    val selectedClip: EditorClip?
        get() {
            val id = selectedClipId ?: return null
            tracks.forEach { track ->
                track.clips.find { it.id == id }?.let { return it }
            }
            return null
        }
}

enum class ExportStatus {
    IDLE,
    EXPORTING,
    SUCCESS,
    ERROR
}

data class ExportProgress(
    val status: ExportStatus = ExportStatus.IDLE,
    val percentage: Float = 0.0f,
    val outputUri: Uri? = null,
    val errorMessage: String? = null
)
