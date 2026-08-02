package com.gallery.app.core.domain.model

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val path: String,
    val displayName: String,
    val size: Long,
    val mimeType: String,
    val dateAdded: Long,
    val dateModified: Long,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Long = 0L,
    val bucketId: Long = 0L,
    val bucketName: String = "",
    val isFavorite: Boolean = false,
    val isTrash: Boolean = false,
    val orientation: Int = 0
) {
    val isVideo: Boolean get() = mimeType.startsWith("video/", ignoreCase = true)
    val isGif: Boolean get() = mimeType.equals("image/gif", ignoreCase = true)
}
