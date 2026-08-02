package com.gallery.app.core.editor.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.LruCache
import com.gallery.app.core.common.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FrameThumbnailManager @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider
) {
    // 50 MB memory cache
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = (maxMemory / 8).coerceAtLeast(1024 * 10) // 10MB to 50MB

    private val bitmapCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    suspend fun getThumbnail(
        uri: Uri,
        timeMs: Long,
        widthPx: Int = 120,
        heightPx: Int = 90
    ): Bitmap? = withContext(dispatchers.io) {
        val cacheKey = "${uri}_${timeMs}_${widthPx}x${heightPx}"
        bitmapCache.get(cacheKey)?.let { return@withContext it }

        var retriever: MediaMetadataRetriever? = null
        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)

            val timeUs = timeMs * 1000L
            val frame = if (android.os.Build.VERSION.SDK_INT >= 27) {
                retriever.getScaledFrameAtTime(
                    timeUs,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
                    widthPx,
                    heightPx
                ) ?: retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } else {
                retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            }

            frame?.let { bmp ->
                val scaled = if (bmp.width != widthPx || bmp.height != heightPx) {
                    Bitmap.createScaledBitmap(bmp, widthPx, heightPx, true)
                } else bmp
                bitmapCache.put(cacheKey, scaled)
                return@withContext scaled
            }
        } catch (e: Exception) {
            Timber.e(e, "Error retrieving thumbnail frame at %d ms for %s", timeMs, uri)
        } finally {
            try {
                retriever?.release()
            } catch (e: Exception) {
                // Ignore release errors
            }
        }
        return@withContext null
    }

    fun clearCache() {
        bitmapCache.evictAll()
    }
}
