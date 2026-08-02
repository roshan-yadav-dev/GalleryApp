package com.gallery.app.core.storage

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.gallery.app.core.common.DateFormatter
import com.gallery.app.core.common.FileUtils
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaMetadata
import timber.log.Timber
import java.io.InputStream

object ExifHelper {

    fun getMediaMetadata(context: Context, item: MediaItem): MediaMetadata {
        return if (item.isVideo) {
            getVideoMetadata(context, item)
        } else {
            getImageMetadata(context, item)
        }
    }

    private fun getImageMetadata(context: Context, item: MediaItem): MediaMetadata {
        var cameraMake: String? = null
        var cameraModel: String? = null
        var aperture: String? = null
        var iso: String? = null
        var shutterSpeed: String? = null
        var focalLength: String? = null
        var lat: Double? = null
        var lon: Double? = null

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(item.uri)
            inputStream?.use { stream ->
                val exif = ExifInterface(stream)
                cameraMake = exif.getAttribute(ExifInterface.TAG_MAKE)
                cameraModel = exif.getAttribute(ExifInterface.TAG_MODEL)
                aperture = exif.getAttribute(ExifInterface.TAG_F_NUMBER)?.let { "f/$it" }
                iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
                shutterSpeed = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)?.let { "${it}s" }
                focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)?.let { "${it}mm" }

                val coords = exif.latLong
                if (coords != null && coords.size >= 2) {
                    lat = coords[0].toDouble()
                    lon = coords[1].toDouble()
                }

            }
        } catch (e: Exception) {
            Timber.e(e, "Error reading EXIF data for ${item.uri}")
        }

        return MediaMetadata(
            filename = item.displayName,
            filePath = item.path,
            fileSizeFormatted = FileUtils.formatFileSize(item.size),
            mimeType = item.mimeType,
            width = item.width,
            height = item.height,
            resolution = "${item.width} × ${item.height}",
            dateCreated = DateFormatter.formatDateTime(item.dateAdded * 1000),
            dateModified = DateFormatter.formatDateTime(item.dateModified * 1000),
            cameraMake = cameraMake,
            cameraModel = cameraModel,
            aperture = aperture,
            iso = iso,
            shutterSpeed = shutterSpeed,
            focalLength = focalLength,
            latitude = lat,
            longitude = lon
        )
    }

    private fun getVideoMetadata(context: Context, item: MediaItem): MediaMetadata {
        var durationFormatted: String? = null
        var videoCodec: String? = null
        var videoBitrate: String? = null
        var videoFrameRate: String? = null

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, item.uri)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            durationFormatted = DateFormatter.formatDuration(duration)
            videoCodec = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
            val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull()
            if (bitrate != null) {
                videoBitrate = "${bitrate / 1000} kbps"
            }
            videoFrameRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)
        } catch (e: Exception) {
            Timber.e(e, "Error extracting video metadata for ${item.uri}")
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {}
        }

        return MediaMetadata(
            filename = item.displayName,
            filePath = item.path,
            fileSizeFormatted = FileUtils.formatFileSize(item.size),
            mimeType = item.mimeType,
            width = item.width,
            height = item.height,
            resolution = "${item.width} × ${item.height}",
            dateCreated = DateFormatter.formatDateTime(item.dateAdded * 1000),
            dateModified = DateFormatter.formatDateTime(item.dateModified * 1000),
            videoDuration = durationFormatted ?: DateFormatter.formatDuration(item.duration),
            videoCodec = videoCodec,
            videoBitrate = videoBitrate,
            videoFrameRate = videoFrameRate
        )
    }
}
