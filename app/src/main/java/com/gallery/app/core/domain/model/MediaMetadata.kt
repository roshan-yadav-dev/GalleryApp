package com.gallery.app.core.domain.model

data class MediaMetadata(
    val filename: String,
    val filePath: String,
    val fileSizeFormatted: String,
    val mimeType: String,
    val width: Int,
    val height: Int,
    val resolution: String,
    val dateCreated: String,
    val dateModified: String,
    val cameraMake: String? = null,
    val cameraModel: String? = null,
    val aperture: String? = null,
    val iso: String? = null,
    val shutterSpeed: String? = null,
    val focalLength: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null,
    val videoDuration: String? = null,
    val videoCodec: String? = null,
    val videoBitrate: String? = null,
    val videoFrameRate: String? = null
)
