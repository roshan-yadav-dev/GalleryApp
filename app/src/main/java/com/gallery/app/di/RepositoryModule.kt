package com.gallery.app.di

import com.gallery.app.core.data.repository.AlbumRepositoryImpl
import com.gallery.app.core.data.repository.FavoritesRepositoryImpl
import com.gallery.app.core.data.repository.MediaRepositoryImpl
import com.gallery.app.core.data.repository.SettingsRepositoryImpl
import com.gallery.app.core.data.repository.TrashRepositoryImpl
import com.gallery.app.core.domain.repository.AlbumRepository
import com.gallery.app.core.domain.repository.FavoritesRepository
import com.gallery.app.core.domain.repository.MediaRepository
import com.gallery.app.core.domain.repository.SettingsRepository
import com.gallery.app.core.domain.repository.TrashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(impl: AlbumRepositoryImpl): AlbumRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindTrashRepository(impl: TrashRepositoryImpl): TrashRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
