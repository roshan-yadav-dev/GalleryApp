package com.gallery.app.feature.editor

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.domain.model.editor.ClipType
import com.gallery.app.core.domain.model.editor.ColorAdjustmentConfig
import com.gallery.app.core.domain.model.editor.CropConfig
import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.EditorTrack
import com.gallery.app.core.domain.model.editor.ExportProgress
import com.gallery.app.core.domain.model.editor.OverlayType
import com.gallery.app.core.domain.model.editor.TextStyleConfig
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import com.gallery.app.core.domain.model.editor.TransformConfig
import com.gallery.app.core.domain.model.editor.VideoFilterType
import com.gallery.app.core.editor.engine.GestureEngine
import com.gallery.app.core.editor.engine.RenderingEngine
import com.gallery.app.core.editor.engine.SelectionEngine
import com.gallery.app.core.editor.engine.TimelineEngine
import com.gallery.app.core.editor.export.FFmpegExportEngine
import com.gallery.app.core.editor.history.TimelineHistoryManager
import com.gallery.app.core.editor.player.VideoEditorPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

data class VideoEditorUiState(
    val isLoading: Boolean = true,
    val timelineState: TimelineState = TimelineState(),
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val exportProgress: ExportProgress = ExportProgress(),
    val errorMessage: String? = null
)

@HiltViewModel
class VideoEditorViewModel @Inject constructor(
    val playerManager: VideoEditorPlayerManager,
    private val timelineEngine: TimelineEngine,
    private val selectionEngine: SelectionEngine,
    private val gestureEngine: GestureEngine,
    private val renderingEngine: RenderingEngine,
    private val historyManager: TimelineHistoryManager,
    private val exportEngine: FFmpegExportEngine,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoEditorUiState())
    val uiState: StateFlow<VideoEditorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            playerManager.playheadPositionMs.collect { pos ->
                _uiState.update { state ->
                    state.copy(
                        timelineState = state.timelineState.copy(playheadMs = pos)
                    )
                }
            }
        }

        viewModelScope.launch {
            exportEngine.exportProgress.collect { progress ->
                _uiState.update { state ->
                    state.copy(exportProgress = progress)
                }
            }
        }
    }

    fun loadVideo(context: Context, sourceUri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val durationMs = withContext(dispatchers.io) {
                try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(context, sourceUri)
                    val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    retriever.release()
                    durStr?.toLongOrNull() ?: 5000L
                } catch (e: Exception) {
                    Timber.e(e, "Failed to extract metadata for source video")
                    5000L
                }
            }

            val videoTrackId = UUID.randomUUID().toString()
            val initialVideoClip = EditorClip(
                id = UUID.randomUUID().toString(),
                trackId = videoTrackId,
                type = ClipType.VIDEO,
                sourceUri = sourceUri,
                sourceDurationMs = durationMs,
                timelineStartMs = 0L,
                timelineEndMs = durationMs,
                trimStartMs = 0L,
                trimEndMs = durationMs
            )

            val tracks = listOf(
                EditorTrack(
                    id = videoTrackId,
                    type = TrackType.VIDEO,
                    title = "Video Track",
                    clips = listOf(initialVideoClip)
                ),
                EditorTrack(
                    id = UUID.randomUUID().toString(),
                    type = TrackType.AUDIO,
                    title = "Audio Track",
                    clips = emptyList()
                ),
                EditorTrack(
                    id = UUID.randomUUID().toString(),
                    type = TrackType.TEXT,
                    title = "Text Overlay",
                    clips = emptyList()
                ),
                EditorTrack(
                    id = UUID.randomUUID().toString(),
                    type = TrackType.OVERLAY,
                    title = "Stickers & Overlays",
                    clips = emptyList()
                )
            )

            val initialState = TimelineState(
                tracks = tracks,
                selectedClipId = initialVideoClip.id,
                zoomPxPerSec = 60f
            )

            historyManager.pushState(initialState)
            playerManager.prepareSource(sourceUri)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    timelineState = initialState,
                    canUndo = historyManager.canUndo(),
                    canRedo = historyManager.canRedo()
                )
            }
        }
    }

    private fun pushNewState(newState: TimelineState) {
        historyManager.pushState(newState)
        _uiState.update {
            it.copy(
                timelineState = newState,
                canUndo = historyManager.canUndo(),
                canRedo = historyManager.canRedo()
            )
        }
    }

    fun seekTo(positionMs: Long) {
        val clampedPos = positionMs.coerceIn(0L, uiState.value.timelineState.durationMs)
        playerManager.seekTo(clampedPos)
        _uiState.update { state ->
            state.copy(timelineState = state.timelineState.copy(playheadMs = clampedPos))
        }
    }

    fun stepFrame(forward: Boolean) {
        playerManager.stepFrame(forward)
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
        _uiState.update { state ->
            state.copy(timelineState = state.timelineState.copy(isPlaying = !state.timelineState.isPlaying))
        }
    }

    fun selectClip(clipId: String?) {
        val newState = selectionEngine.selectClip(uiState.value.timelineState, clipId)
        _uiState.update { it.copy(timelineState = newState) }
    }

    fun setZoom(zoomPxPerSec: Float) {
        val clampedZoom = gestureEngine.calculatePinchZoom(uiState.value.timelineState.zoomPxPerSec, zoomPxPerSec / uiState.value.timelineState.zoomPxPerSec)
        _uiState.update { state ->
            state.copy(timelineState = state.timelineState.copy(zoomPxPerSec = clampedZoom))
        }
    }

    fun splitSelectedClip() {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val playhead = uiState.value.timelineState.playheadMs
        val newState = timelineEngine.splitClip(uiState.value.timelineState, selectedClipId, playhead)
        pushNewState(newState)
    }

    fun trimSelectedClip(newTrimStartMs: Long, newTrimEndMs: Long) {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.trimClip(uiState.value.timelineState, selectedClipId, newTrimStartMs, newTrimEndMs)
        pushNewState(newState)
    }

    fun deleteSelectedClip() {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.deleteClip(uiState.value.timelineState, selectedClipId)
        pushNewState(newState)
    }

    fun duplicateSelectedClip() {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.duplicateClip(uiState.value.timelineState, selectedClipId)
        pushNewState(newState)
    }

    fun setClipVolume(volume: Float) {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.setClipVolume(uiState.value.timelineState, selectedClipId, volume)
        playerManager.setVolume(volume)
        pushNewState(newState)
    }

    fun setClipSpeed(speed: Float) {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.setClipSpeed(uiState.value.timelineState, selectedClipId, speed)
        playerManager.setSpeed(speed)
        pushNewState(newState)
    }

    fun setClipFilter(filter: VideoFilterType) {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.setClipFilter(uiState.value.timelineState, selectedClipId, filter)
        pushNewState(newState)
    }

    fun setClipColorAdjustments(adjustments: ColorAdjustmentConfig) {
        val selectedClipId = uiState.value.timelineState.selectedClipId ?: return
        val newState = timelineEngine.setClipColorAdjustments(uiState.value.timelineState, selectedClipId, adjustments)
        pushNewState(newState)
    }

    fun rotateSelectedClip() {
        val selectedClip = uiState.value.timelineState.selectedClip ?: return
        val currentTransform = selectedClip.transformConfig ?: TransformConfig()
        val newDeg = (currentTransform.rotationDeg + 90f) % 360f
        val newTransform = currentTransform.copy(rotationDeg = newDeg)
        val newState = timelineEngine.setClipTransform(uiState.value.timelineState, selectedClip.id, newTransform)
        pushNewState(newState)
    }

    fun flipSelectedClip(horizontal: Boolean) {
        val selectedClip = uiState.value.timelineState.selectedClip ?: return
        val currentTransform = selectedClip.transformConfig ?: TransformConfig()
        val newTransform = if (horizontal) {
            currentTransform.copy(flipHorizontal = !currentTransform.flipHorizontal)
        } else {
            currentTransform.copy(flipVertical = !currentTransform.flipVertical)
        }
        val newState = timelineEngine.setClipTransform(uiState.value.timelineState, selectedClip.id, newTransform)
        pushNewState(newState)
    }

    fun addOverlay(type: OverlayType, textConfig: TextStyleConfig? = null) {
        val newState = timelineEngine.addOverlayClip(uiState.value.timelineState, type, textConfig)
        pushNewState(newState)
    }

    fun toggleSafeGuides() {
        _uiState.update { state ->
            state.copy(timelineState = state.timelineState.copy(showSafeGuides = !state.timelineState.showSafeGuides))
        }
    }

    fun toggleLooping() {
        val newLoop = !uiState.value.timelineState.isLooping
        playerManager.setLooping(newLoop)
        _uiState.update { state ->
            state.copy(timelineState = state.timelineState.copy(isLooping = newLoop))
        }
    }

    fun undo() {
        val previousState = historyManager.undo(uiState.value.timelineState) ?: return
        _uiState.update {
            it.copy(
                timelineState = previousState,
                canUndo = historyManager.canUndo(),
                canRedo = historyManager.canRedo()
            )
        }
        playerManager.seekTo(previousState.playheadMs)
    }

    fun redo() {
        val nextState = historyManager.redo() ?: return
        _uiState.update {
            it.copy(
                timelineState = nextState,
                canUndo = historyManager.canUndo(),
                canRedo = historyManager.canRedo()
            )
        }
        playerManager.seekTo(nextState.playheadMs)
    }

    fun exportVideo() {
        viewModelScope.launch {
            playerManager.pause()
            exportEngine.exportTimeline(uiState.value.timelineState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
