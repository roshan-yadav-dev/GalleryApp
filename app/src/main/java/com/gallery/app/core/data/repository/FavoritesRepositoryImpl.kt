package com.gallery.app.core.data.repository

import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.data.source.local.MediaStoreDataSource
import com.gallery.app.core.database.dao.FavoriteDao
import com.gallery.app.core.database.entity.FavoriteEntity
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.FavoritesRepository
import com.gallery.app.core.storage.MediaStoreObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val mediaStoreObserver: MediaStoreObserver,
    private val favoriteDao: FavoriteDao,
    private val dispatchers: DispatcherProvider
) : FavoritesRepository {

    override fun getFavoriteMediaItems(): Flow<List<MediaItem>> {
        return combine(
            mediaStoreObserver.observeMediaChanges(),
            favoriteDao.getAllFavoriteUris()
        ) { _, favUris ->
            val favSet = favUris.toSet()
            val allItems = mediaStoreDataSource.fetchMediaItems()
            allItems.filter { it.uri.toString() in favSet }.map { it.copy(isFavorite = true) }
        }.flowOn(dispatchers.io)
    }

    override fun getFavoriteUris(): Flow<List<String>> {
        return favoriteDao.getAllFavoriteUris()
    }

    override suspend fun isFavorite(uri: String): Boolean = withContext(dispatchers.io) {
        favoriteDao.isFavorite(uri)
    }

    override suspend fun toggleFavorite(mediaItem: MediaItem): Boolean = withContext(dispatchers.io) {
        val uriStr = mediaItem.uri.toString()
        val currentlyFav = favoriteDao.isFavorite(uriStr)
        if (currentlyFav) {
            favoriteDao.deleteFavoriteByUri(uriStr)
            false
        } else {
            favoriteDao.insertFavorite(
                FavoriteEntity(
                    mediaUri = uriStr,
                    mediaId = mediaItem.id
                )
            )
            true
        }
    }
}
