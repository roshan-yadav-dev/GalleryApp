package com.gallery.app.core.data.repository

import android.net.Uri
import com.gallery.app.core.common.Constants
import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.database.dao.TrashDao
import com.gallery.app.core.database.entity.TrashEntity
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.TrashItem
import com.gallery.app.core.domain.repository.TrashRepository
import com.gallery.app.core.storage.StorageAccessFrameworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrashRepositoryImpl @Inject constructor(
    private val trashDao: TrashDao,
    private val safHelper: StorageAccessFrameworkHelper,
    private val dispatchers: DispatcherProvider
) : TrashRepository {

    override fun getTrashItems(): Flow<List<TrashItem>> {
        return trashDao.getAllTrashItems().map { list ->
            list.map { entity ->
                TrashItem(
                    id = entity.id,
                    mediaUri = Uri.parse(entity.mediaUri),
                    originalPath = entity.originalPath,
                    displayName = entity.displayName,
                    sizeBytes = entity.sizeBytes,
                    mimeType = entity.mimeType,
                    trashedTimestamp = entity.trashedTimestamp,
                    expiryTimestamp = entity.expiryTimestamp
                )
            }
        }.flowOn(dispatchers.io)
    }

    override suspend fun moveToTrash(mediaItem: MediaItem): Boolean = withContext(dispatchers.io) {
        val now = System.currentTimeMillis()
        val expiry = now + TimeUnit.DAYS.toMillis(Constants.TRASH_RETENTION_DAYS)

        val trashEntity = TrashEntity(
            mediaUri = mediaItem.uri.toString(),
            originalPath = mediaItem.path,
            displayName = mediaItem.displayName,
            sizeBytes = mediaItem.size,
            mimeType = mediaItem.mimeType,
            trashedTimestamp = now,
            expiryTimestamp = expiry
        )

        trashDao.insertTrash(trashEntity)
        true
    }

    override suspend fun restoreFromTrash(trashItem: TrashItem): Boolean = withContext(dispatchers.io) {
        trashDao.deleteTrashById(trashItem.id)
        true
    }

    override suspend fun deletePermanently(trashItem: TrashItem): Boolean = withContext(dispatchers.io) {
        trashDao.deleteTrashById(trashItem.id)
        true
    }

    override suspend fun cleanupExpiredTrash(): Int = withContext(dispatchers.io) {
        val now = System.currentTimeMillis()
        trashDao.deleteExpiredTrash(now)
    }

    override suspend fun clearTrash() = withContext(dispatchers.io) {
        trashDao.clearTrash()
    }
}
