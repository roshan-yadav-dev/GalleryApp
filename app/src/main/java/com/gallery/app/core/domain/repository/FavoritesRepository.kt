package com.gallery.app.core.domain.repository

import com.gallery.app.core.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteMediaItems(): Flow<List<MediaItem>>
    fun getFavoriteUris(): Flow<List<String>>
    suspend fun isFavorite(uri: String): Boolean
    suspend fun toggleFavorite(mediaItem: MediaItem): Boolean
}
