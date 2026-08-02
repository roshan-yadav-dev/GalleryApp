package com.gallery.app.core.domain.repository

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.TrashItem
import kotlinx.coroutines.flow.Flow

interface TrashRepository {
    fun getTrashItems(): Flow<List<TrashItem>>
    suspend fun moveToTrash(mediaItem: MediaItem): Boolean
    suspend fun restoreFromTrash(trashItem: TrashItem): Boolean
    suspend fun deletePermanently(trashItem: TrashItem): Boolean
    suspend fun cleanupExpiredTrash(): Int
    suspend fun clearTrash()
}
