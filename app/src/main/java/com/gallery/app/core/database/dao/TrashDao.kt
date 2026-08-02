package com.gallery.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gallery.app.core.database.entity.TrashEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {
    @Query("SELECT * FROM trash ORDER BY trashedTimestamp DESC")
    fun getAllTrashItems(): Flow<List<TrashEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrash(item: TrashEntity)

    @Query("DELETE FROM trash WHERE id = :id")
    suspend fun deleteTrashById(id: Long)

    @Query("DELETE FROM trash WHERE mediaUri = :uri")
    suspend fun deleteTrashByUri(uri: String)

    @Query("SELECT * FROM trash WHERE expiryTimestamp <= :currentTime")
    suspend fun getExpiredTrash(currentTime: Long): List<TrashEntity>

    @Query("DELETE FROM trash WHERE expiryTimestamp <= :currentTime")
    suspend fun deleteExpiredTrash(currentTime: Long): Int

    @Query("DELETE FROM trash")
    suspend fun clearTrash()
}
