package com.gallery.app.di

import android.content.Context
import androidx.room.Room
import com.gallery.app.core.common.Constants
import com.gallery.app.core.database.GalleryDatabase
import com.gallery.app.core.database.dao.AlbumDao
import com.gallery.app.core.database.dao.FavoriteDao
import com.gallery.app.core.database.dao.TrashDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GalleryDatabase {
        return Room.databaseBuilder(
            context,
            GalleryDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFavoriteDao(database: GalleryDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideTrashDao(database: GalleryDatabase): TrashDao = database.trashDao()

    @Provides
    fun provideAlbumDao(database: GalleryDatabase): AlbumDao = database.albumDao()
}
