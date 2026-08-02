package com.gallery.app.core.data.repository;

import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.data.source.local.MediaStoreDataSource;
import com.gallery.app.core.database.dao.AlbumDao;
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
public final class AlbumRepositoryImpl_Factory implements Factory<AlbumRepositoryImpl> {
  private final Provider<MediaStoreDataSource> mediaStoreDataSourceProvider;

  private final Provider<MediaStoreObserver> mediaStoreObserverProvider;

  private final Provider<AlbumDao> albumDaoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public AlbumRepositoryImpl_Factory(Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider, Provider<AlbumDao> albumDaoProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.mediaStoreDataSourceProvider = mediaStoreDataSourceProvider;
    this.mediaStoreObserverProvider = mediaStoreObserverProvider;
    this.albumDaoProvider = albumDaoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public AlbumRepositoryImpl get() {
    return newInstance(mediaStoreDataSourceProvider.get(), mediaStoreObserverProvider.get(), albumDaoProvider.get(), dispatchersProvider.get());
  }

  public static AlbumRepositoryImpl_Factory create(
      Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider, Provider<AlbumDao> albumDaoProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new AlbumRepositoryImpl_Factory(mediaStoreDataSourceProvider, mediaStoreObserverProvider, albumDaoProvider, dispatchersProvider);
  }

  public static AlbumRepositoryImpl newInstance(MediaStoreDataSource mediaStoreDataSource,
      MediaStoreObserver mediaStoreObserver, AlbumDao albumDao, DispatcherProvider dispatchers) {
    return new AlbumRepositoryImpl(mediaStoreDataSource, mediaStoreObserver, albumDao, dispatchers);
  }
}
