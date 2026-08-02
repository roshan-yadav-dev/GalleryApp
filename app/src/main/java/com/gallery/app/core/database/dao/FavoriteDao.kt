package com.gallery.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gallery.app.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedDate DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT mediaUri FROM favorites")
    fun getAllFavoriteUris(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mediaUri = :uri)")
    suspend fun isFavorite(uri: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE mediaUri = :uri")
    suspend fun deleteFavoriteByUri(uri: String)
}
