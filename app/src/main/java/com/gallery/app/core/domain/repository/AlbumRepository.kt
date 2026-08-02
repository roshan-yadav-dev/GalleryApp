package com.gallery.app.core.domain.repository

import com.gallery.app.core.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<List<Album>>
    suspend fun createCustomAlbum(name: String, coverUri: String?): Long
    suspend fun deleteCustomAlbum(id: Long)
}
