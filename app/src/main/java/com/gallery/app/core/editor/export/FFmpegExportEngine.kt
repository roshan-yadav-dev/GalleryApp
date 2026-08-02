package com.gallery.app.core.editor.export

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.domain.model.editor.ExportProgress
import com.gallery.app.core.domain.model.editor.ExportStatus
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(UnstableApi::class)
class FFmpegExportEngine @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider
) {
    private val _exportProgress = MutableStateFlow(ExportProgress())
    val exportProgress: StateFlow<ExportProgress> = _exportProgress.asStateFlow()

    suspend fun exportTimeline(
        timelineState: TimelineState,
        outputFileName: String = "Edited_${System.currentTimeMillis()}.mp4"
    ): Uri? = withContext(dispatchers.main) {
        _exportProgress.value = ExportProgress(status = ExportStatus.EXPORTING, percentage = 0.0f)

        val totalDurationMs = timelineState.durationMs
        if (totalDurationMs <= 0) {
            _exportProgress.value = ExportProgress(
                status = ExportStatus.ERROR,
                errorMessage = "Invalid timeline duration"
            )
            return@withContext null
        }

        val outputDir = File(context.cacheDir, "exports").apply { mkdirs() }
        val outputFile = File(outputDir, outputFileName)
        if (outputFile.exists()) outputFile.delete()

        val videoTrack = timelineState.tracks.find { it.type == TrackType.VIDEO }
        val videoClips = videoTrack?.clips ?: emptyList()

        if (videoClips.isEmpty()) {
            _exportProgress.value = ExportProgress(
                status = ExportStatus.ERROR,
                errorMessage = "No video clips found on timeline"
            )
            return@withContext null
        }

        val editedItems = mutableListOf<EditedMediaItem>()
        videoClips.forEach { clip ->
            val clippingConfiguration = MediaItem.ClippingConfiguration.Builder()
                .setStartPositionMs(clip.trimStartMs)
                .setEndPositionMs(clip.trimEndMs)
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(clip.sourceUri)
                .setClippingConfiguration(clippingConfiguration)
                .build()

            val editedItem = EditedMediaItem.Builder(mediaItem)
                .setRemoveAudio(clip.volume == 0f)
                .build()

            editedItems.add(editedItem)
        }

        val sequence = EditedMediaItemSequence(editedItems)
        val composition = Composition.Builder(listOf(sequence)).build()

        var exportSuccess = false
        var exportError: Exception? = null

        val listener = object : Transformer.Listener {
            override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                exportSuccess = true
            }

            override fun onError(
                composition: Composition,
                exportResult: ExportResult,
                exportException: ExportException
            ) {
                exportError = exportException
            }
        }

        val transformer = Transformer.Builder(context)
            .setVideoMimeType(MimeTypes.VIDEO_H264)
            .setAudioMimeType(MimeTypes.AUDIO_AAC)
            .addListener(listener)
            .build()

        try {
            transformer.start(composition, outputFile.absolutePath)
            val progressHolder = ProgressHolder()

            while (!exportSuccess && exportError == null) {
                val progressState = transformer.getProgress(progressHolder)
                if (progressState == Transformer.PROGRESS_STATE_AVAILABLE) {
                    val percent = progressHolder.progress.toFloat().coerceIn(0f, 99f)
                    _exportProgress.value = ExportProgress(
                        status = ExportStatus.EXPORTING,
                        percentage = percent
                    )
                }
                delay(100L)
            }

            if (exportSuccess) {
                val savedUri = saveToMediaStore(outputFile, outputFileName)
                _exportProgress.value = ExportProgress(
                    status = ExportStatus.SUCCESS,
                    percentage = 100.0f,
                    outputUri = savedUri
                )
                return@withContext savedUri
            } else {
                val errMsg = exportError?.message ?: "Export failed"
                Timber.e(exportError, "Media3 Transformer Export Error")
                _exportProgress.value = ExportProgress(
                    status = ExportStatus.ERROR,
                    errorMessage = errMsg
                )
                return@withContext null
            }
        } catch (e: Exception) {
            Timber.e(e, "Exception during media export")
            _exportProgress.value = ExportProgress(
                status = ExportStatus.ERROR,
                errorMessage = e.message ?: "Export exception"
            )
            return@withContext null
        }
    }

    private fun saveToMediaStore(file: File, fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/GalleryAPP_Edited")
        }

        val uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let { destUri ->
            context.contentResolver.openOutputStream(destUri)?.use { out ->
                file.inputStream().use { input ->
                    input.copyTo(out)
                }
            }
        }
        return uri
    }
}
