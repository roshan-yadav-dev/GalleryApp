package com.gallery.app.core.data.repository

import android.content.Context
import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.data.source.local.MediaStoreDataSource
import com.gallery.app.core.database.dao.FavoriteDao
import com.gallery.app.core.database.dao.TrashDao
import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaMetadata
import com.gallery.app.core.domain.repository.MediaRepository
import com.gallery.app.core.storage.ExifHelper
import com.gallery.app.core.storage.MediaStoreObserver
import com.gallery.app.core.storage.StorageAccessFrameworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val context: Context,
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val mediaStoreObserver: MediaStoreObserver,
    private val favoriteDao: FavoriteDao,
    private val trashDao: TrashDao,
    private val safHelper: StorageAccessFrameworkHelper,
    private val dispatchers: DispatcherProvider
) : MediaRepository {

    override fun getMediaItems(filterOptions: FilterOptions): Flow<List<MediaItem>> {
        return combine(
            mediaStoreObserver.observeMediaChanges(),
            favoriteDao.getAllFavoriteUris(),
            trashDao.getAllTrashItems()
        ) { _, favUris, trashEntities ->
            val trashUris = trashEntities.map { it.mediaUri }.toSet()
            val favSet = favUris.toSet()

            val rawItems = mediaStoreDataSource.fetchMediaItems(filterOptions)
            rawItems
                .filter { it.uri.toString() !in trashUris }
                .map { item ->
                    item.copy(isFavorite = item.uri.toString() in favSet)
                }
        }.flowOn(dispatchers.io)
    }

    override suspend fun getMediaItemById(id: Long): MediaItem? = withContext(dispatchers.io) {
        val items = mediaStoreDataSource.fetchMediaItems()
        items.find { it.id == id }
    }

    override suspend fun getMediaMetadata(item: MediaItem): MediaMetadata = withContext(dispatchers.io) {
        ExifHelper.getMediaMetadata(context, item)
    }

    override suspend fun renameMedia(item: MediaItem, newName: String): Boolean = withContext(dispatchers.io) {
        safHelper.renameMediaItem(item, newName)
    }

    override suspend fun deleteMedia(item: MediaItem): Boolean = withContext(dispatchers.io) {
        safHelper.deleteMediaItem(item)
    }

    override suspend fun copyMedia(item: MediaItem, destinationPath: String): Boolean = withContext(dispatchers.io) {
        safHelper.copyMediaItem(item, destinationPath)
    }
}
