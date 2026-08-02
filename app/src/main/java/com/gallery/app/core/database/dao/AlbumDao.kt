package com.gallery.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gallery.app.core.database.entity.CustomAlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM custom_albums ORDER BY name ASC")
    fun getAllCustomAlbums(): Flow<List<CustomAlbumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomAlbum(album: CustomAlbumEntity): Long

    @Query("DELETE FROM custom_albums WHERE id = :id")
    suspend fun deleteCustomAlbum(id: Long)
}
