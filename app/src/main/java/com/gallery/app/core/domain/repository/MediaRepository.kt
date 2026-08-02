package com.gallery.app.core.domain.repository

import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaMetadata
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaItems(filterOptions: FilterOptions): Flow<List<MediaItem>>
    suspend fun getMediaItemById(id: Long): MediaItem?
    suspend fun getMediaMetadata(item: MediaItem): MediaMetadata
    suspend fun renameMedia(item: MediaItem, newName: String): Boolean
    suspend fun deleteMedia(item: MediaItem): Boolean
    suspend fun copyMedia(item: MediaItem, destinationPath: String): Boolean
}
