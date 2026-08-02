package com.gallery.app.core.common

import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

object FileUtils {

    fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        val formattedSize = DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble()))
        return "$formattedSize ${units[digitGroups]}"
    }

    fun getFileExtension(filename: String): String {
        return filename.substringAfterLast('.', "").uppercase(Locale.getDefault())
    }

    fun isVideoMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("video/", ignoreCase = true)
    }

    fun isImageMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("image/", ignoreCase = true)
    }

    fun isGifMimeType(mimeType: String): Boolean {
        return mimeType.equals(Constants.MIME_TYPE_GIF, ignoreCase = true)
    }

    fun isRawMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("image/x-", ignoreCase = true) ||
                mimeType.equals("image/dng", ignoreCase = true) ||
                mimeType.equals("image/raw", ignoreCase = true)
    }
}
