package com.gallery.app.core.data.repository

import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.data.source.local.MediaStoreDataSource
import com.gallery.app.core.database.dao.AlbumDao
import com.gallery.app.core.database.entity.CustomAlbumEntity
import com.gallery.app.core.domain.model.Album
import com.gallery.app.core.domain.repository.AlbumRepository
import com.gallery.app.core.storage.MediaStoreObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val mediaStoreObserver: MediaStoreObserver,
    private val albumDao: AlbumDao,
    private val dispatchers: DispatcherProvider
) : AlbumRepository {

    override fun getAlbums(): Flow<List<Album>> {
        return combine(
            mediaStoreObserver.observeMediaChanges(),
            albumDao.getAllCustomAlbums()
        ) { _, customAlbums ->
            val systemAlbums = mediaStoreDataSource.fetchAlbums()
            systemAlbums
        }.flowOn(dispatchers.io)
    }

    override suspend fun createCustomAlbum(name: String, coverUri: String?): Long = withContext(dispatchers.io) {
        albumDao.insertCustomAlbum(
            CustomAlbumEntity(
                name = name,
                coverUri = coverUri
            )
        )
    }

    override suspend fun deleteCustomAlbum(id: Long) = withContext(dispatchers.io) {
        albumDao.deleteCustomAlbum(id)
    }
}
