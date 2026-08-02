package com.gallery.app.core.storage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.gallery.app.core.domain.model.MediaItem
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageAccessFrameworkHelper @Inject constructor(
    private val context: Context
) {
    fun deleteMediaItem(item: MediaItem): Boolean {
        return try {
            val rows = context.contentResolver.delete(item.uri, null, null)
            rows > 0
        } catch (e: SecurityException) {
            Timber.e(e, "SecurityException deleting media item: ${item.uri}")
            false
        } catch (e: Exception) {
            Timber.e(e, "Error deleting media item: ${item.uri}")
            false
        }
    }

    fun renameMediaItem(item: MediaItem, newName: String): Boolean {
        return try {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, newName)
            }
            val updated = context.contentResolver.update(item.uri, values, null, null)
            updated > 0
        } catch (e: Exception) {
            Timber.e(e, "Error renaming media item: ${item.uri}")
            false
        }
    }

    fun copyMediaItem(item: MediaItem, destinationPath: String): Boolean {
        return try {
            val file = File(item.path)
            if (!file.exists()) return false
            val destFile = File(destinationPath, item.displayName)
            file.copyTo(destFile, overwrite = true)
            true
        } catch (e: Exception) {
            Timber.e(e, "Error copying media item: ${item.uri}")
            false
        }
    }
}
