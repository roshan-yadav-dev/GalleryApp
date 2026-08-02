package com.gallery.app.core.domain.model

import android.net.Uri

data class TrashItem(
    val id: Long,
    val mediaUri: Uri,
    val originalPath: String,
    val displayName: String,
    val sizeBytes: Long,
    val mimeType: String,
    val trashedTimestamp: Long,
    val expiryTimestamp: Long
) {
    val isExpired: Boolean get() = System.currentTimeMillis() >= expiryTimestamp
}
