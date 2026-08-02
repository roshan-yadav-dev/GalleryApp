package com.gallery.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gallery.app.core.database.dao.AlbumDao
import com.gallery.app.core.database.dao.FavoriteDao
import com.gallery.app.core.database.dao.TrashDao
import com.gallery.app.core.database.entity.CustomAlbumEntity
import com.gallery.app.core.database.entity.FavoriteEntity
import com.gallery.app.core.database.entity.TrashEntity

@Database(
    entities = [
        FavoriteEntity::class,
        TrashEntity::class,
        CustomAlbumEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun trashDao(): TrashDao
    abstract fun albumDao(): AlbumDao
}
