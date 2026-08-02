package com.gallery.app.core.data.repository;

import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.data.source.local.MediaStoreDataSource;
import com.gallery.app.core.database.dao.FavoriteDao;
import com.gallery.app.core.storage.MediaStoreObserver;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class FavoritesRepositoryImpl_Factory implements Factory<FavoritesRepositoryImpl> {
  private final Provider<MediaStoreDataSource> mediaStoreDataSourceProvider;

  private final Provider<MediaStoreObserver> mediaStoreObserverProvider;

  private final Provider<FavoriteDao> favoriteDaoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public FavoritesRepositoryImpl_Factory(
      Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider,
      Provider<FavoriteDao> favoriteDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    this.mediaStoreDataSourceProvider = mediaStoreDataSourceProvider;
    this.mediaStoreObserverProvider = mediaStoreObserverProvider;
    this.favoriteDaoProvider = favoriteDaoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public FavoritesRepositoryImpl get() {
    return newInstance(mediaStoreDataSourceProvider.get(), mediaStoreObserverProvider.get(), favoriteDaoProvider.get(), dispatchersProvider.get());
  }

  public static FavoritesRepositoryImpl_Factory create(
      Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider,
      Provider<FavoriteDao> favoriteDaoProvider, Provider<DispatcherProvider> dispatchersProvider) {
    return new FavoritesRepositoryImpl_Factory(mediaStoreDataSourceProvider, mediaStoreObserverProvider, favoriteDaoProvider, dispatchersProvider);
  }

  public static FavoritesRepositoryImpl newInstance(MediaStoreDataSource mediaStoreDataSource,
      MediaStoreObserver mediaStoreObserver, FavoriteDao favoriteDao,
      DispatcherProvider dispatchers) {
    return new FavoritesRepositoryImpl(mediaStoreDataSource, mediaStoreObserver, favoriteDao, dispatchers);
  }
}
