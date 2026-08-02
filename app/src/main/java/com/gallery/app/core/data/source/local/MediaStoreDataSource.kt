package com.gallery.app.core.data.source.local

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.gallery.app.core.common.Constants
import com.gallery.app.core.domain.model.Album
import com.gallery.app.core.domain.model.AlbumType
import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaType
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreDataSource @Inject constructor(
    private val context: Context
) {
    fun fetchMediaItems(filterOptions: FilterOptions = FilterOptions()): List<MediaItem> {
        val items = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.WIDTH,
            MediaStore.Files.FileColumns.HEIGHT,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME
        )

        // Build selection clause
        val selectionClauses = mutableListOf<String>()

        when (filterOptions.mediaType) {
            MediaType.IMAGE -> {
                selectionClauses.add("${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}")
            }
            MediaType.VIDEO -> {
                selectionClauses.add("${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}")
            }
            MediaType.GIF -> {
                selectionClauses.add("${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}")
                selectionClauses.add("${MediaStore.Files.FileColumns.MIME_TYPE} = 'image/gif'")
            }
            MediaType.RAW -> {
                selectionClauses.add("${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}")
                selectionClauses.add("(${MediaStore.Files.FileColumns.MIME_TYPE} LIKE '%raw%' OR ${MediaStore.Files.FileColumns.MIME_TYPE} LIKE '%dng%')")
            }
            MediaType.ALL -> {
                selectionClauses.add("(${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE} OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO})")
            }
        }

        if (filterOptions.albumId != null) {
            selectionClauses.add("${MediaStore.Files.FileColumns.BUCKET_ID} = ${filterOptions.albumId}")
        }

        val selection = if (selectionClauses.isNotEmpty()) {
            selectionClauses.joinToString(" AND ")
        } else null

        // Build sort order
        val sortColumn = when (filterOptions.sortOrder.sortBy) {
            SortBy.DATE -> MediaStore.Files.FileColumns.DATE_ADDED
            SortBy.NAME -> MediaStore.Files.FileColumns.DISPLAY_NAME
            SortBy.SIZE -> MediaStore.Files.FileColumns.SIZE
        }

        val sortDirectionStr = if (filterOptions.sortOrder.direction == SortDirection.DESCENDING) "DESC" else "ASC"
        val sortOrder = "$sortColumn $sortDirectionStr"

        val queryUri = MediaStore.Files.getContentUri("external")

        try {
            context.contentResolver.query(
                queryUri,
                projection,
                selection,
                null,
                sortOrder
            )?.use { c ->
                val idCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val dataCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val nameCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val mimeCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val dateAddedCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val dateModCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val widthCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH)
                val heightCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT)
                val mediaTypeCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val durationCol = c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val orientationCol = c.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)
                val bucketIdCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
                val bucketNameCol = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)

                while (c.moveToNext()) {
                    val id = c.getLong(idCol)
                    val mediaTypeInt = c.getInt(mediaTypeCol)
                    val isVideo = mediaTypeInt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

                    val contentUri: Uri = if (isVideo) {
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    } else {
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    }

                    val path = c.getString(dataCol) ?: ""
                    val displayName = c.getString(nameCol) ?: (if (isVideo) "Video_$id" else "Image_$id")
                    val mimeType = c.getString(mimeCol) ?: (if (isVideo) "video/mp4" else "image/jpeg")

                    val item = MediaItem(
                        id = id,
                        uri = contentUri,
                        path = path,
                        displayName = displayName,
                        size = c.getLong(sizeCol),
                        mimeType = mimeType,
                        dateAdded = c.getLong(dateAddedCol),
                        dateModified = c.getLong(dateModCol),
                        width = c.getInt(widthCol),
                        height = c.getInt(heightCol),
                        duration = if (isVideo) c.getLong(durationCol) else 0L,
                        bucketId = c.getLong(bucketIdCol),
                        bucketName = c.getString(bucketNameCol) ?: "",
                        orientation = if (!isVideo) c.getInt(orientationCol) else 0
                    )
                    items.add(item)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching media items from MediaStore")
        }

        var sequence = items.asSequence()

        // Apply album type filter if needed
        if (filterOptions.albumType != null) {
            sequence = applyAlbumTypeFilter(sequence, filterOptions.albumType)
        }

        // Apply search query filter if needed
        if (filterOptions.searchQuery.isNotBlank()) {
            val query = filterOptions.searchQuery.trim().lowercase()
            sequence = sequence.filter {
                it.displayName.lowercase().contains(query) ||
                        it.path.lowercase().contains(query) ||
                        it.bucketName.lowercase().contains(query) ||
                        it.mimeType.lowercase().contains(query)
            }
        }

        return sequence.toList()
    }

    private fun applyAlbumTypeFilter(sequence: Sequence<MediaItem>, albumType: AlbumType): Sequence<MediaItem> {
        return when (albumType) {
            AlbumType.CAMERA -> sequence.filter { it.bucketName.equals(Constants.FOLDER_CAMERA, ignoreCase = true) || it.path.contains("Camera", ignoreCase = true) }
            AlbumType.SCREENSHOTS -> sequence.filter { it.bucketName.contains("Screenshot", ignoreCase = true) || it.path.contains("Screenshot", ignoreCase = true) }
            AlbumType.DOWNLOADS -> sequence.filter { it.bucketName.equals(Constants.FOLDER_DOWNLOADS, ignoreCase = true) || it.path.contains("Download", ignoreCase = true) }
            AlbumType.WHATSAPP -> sequence.filter { it.path.contains(Constants.FOLDER_WHATSAPP, ignoreCase = true) }
            AlbumType.TELEGRAM -> sequence.filter { it.path.contains(Constants.FOLDER_TELEGRAM, ignoreCase = true) }
            AlbumType.INSTAGRAM -> sequence.filter { it.path.contains(Constants.FOLDER_INSTAGRAM, ignoreCase = true) }
            AlbumType.FACEBOOK -> sequence.filter { it.path.contains(Constants.FOLDER_FACEBOOK, ignoreCase = true) }
            AlbumType.VIDEOS -> sequence.filter { it.isVideo }
            AlbumType.GIFS -> sequence.filter { it.isGif }
            else -> sequence
        }
    }

    fun fetchAlbums(): List<Album> {
        val allMedia = fetchMediaItems()
        val albumMap = LinkedHashMap<Long, MutableList<MediaItem>>()

        allMedia.forEach { item ->
            val list = albumMap.getOrPut(item.bucketId) { mutableListOf() }
            list.add(item)
        }

        val albums = ArrayList<Album>(albumMap.size)

        albumMap.forEach { (bucketId, items) ->
            if (items.isNotEmpty()) {
                val first = items.first()
                val albumName = first.bucketName.ifBlank { "Internal Storage" }
                val albumType = determineAlbumType(albumName, first.path)

                albums.add(
                    Album(
                        id = bucketId,
                        name = albumName,
                        coverUri = first.uri,
                        itemCount = items.size,
                        albumType = albumType,
                        relativePath = first.path.substringBeforeLast('/', ""),
                        lastModified = items.maxOf { it.dateModified }
                    )
                )
            }
        }

        return albums.sortedByDescending { it.lastModified }
    }

    private fun determineAlbumType(bucketName: String, path: String): AlbumType {
        val lowerName = bucketName.lowercase()
        val lowerPath = path.lowercase()

        return when {
            lowerName == "camera" || lowerPath.contains("/camera") -> AlbumType.CAMERA
            lowerName.contains("screenshot") || lowerPath.contains("screenshot") -> AlbumType.SCREENSHOTS
            lowerName == "download" || lowerPath.contains("/download") -> AlbumType.DOWNLOADS
            lowerPath.contains("whatsapp") -> AlbumType.WHATSAPP
            lowerPath.contains("telegram") -> AlbumType.TELEGRAM
            lowerPath.contains("instagram") -> AlbumType.INSTAGRAM
            lowerPath.contains("facebook") -> AlbumType.FACEBOOK
            else -> AlbumType.CUSTOM
        }
    }
}
