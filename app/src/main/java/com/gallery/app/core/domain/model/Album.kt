package com.gallery.app.core.domain.model

import android.net.Uri

enum class AlbumType {
    CAMERA,
    SCREENSHOTS,
    DOWNLOADS,
    WHATSAPP,
    TELEGRAM,
    INSTAGRAM,
    FACEBOOK,
    FAVORITES,
    VIDEOS,
    GIFS,
    HIDDEN,
    TRASH,
    CUSTOM
}

data class Album(
    val id: Long,
    val name: String,
    val coverUri: Uri?,
    val itemCount: Int,
    val albumType: AlbumType,
    val relativePath: String = "",
    val lastModified: Long = 0L
)
