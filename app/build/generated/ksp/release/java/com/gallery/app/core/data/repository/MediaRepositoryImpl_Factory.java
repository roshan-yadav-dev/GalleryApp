package com.gallery.app.core.data.repository;

import android.content.Context;
import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.data.source.local.MediaStoreDataSource;
import com.gallery.app.core.database.dao.FavoriteDao;
import com.gallery.app.core.database.dao.TrashDao;
import com.gallery.app.core.storage.MediaStoreObserver;
import com.gallery.app.core.storage.StorageAccessFrameworkHelper;
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
public final class MediaRepositoryImpl_Factory implements Factory<MediaRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<MediaStoreDataSource> mediaStoreDataSourceProvider;

  private final Provider<MediaStoreObserver> mediaStoreObserverProvider;

  private final Provider<FavoriteDao> favoriteDaoProvider;

  private final Provider<TrashDao> trashDaoProvider;

  private final Provider<StorageAccessFrameworkHelper> safHelperProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public MediaRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider,
      Provider<FavoriteDao> favoriteDaoProvider, Provider<TrashDao> trashDaoProvider,
      Provider<StorageAccessFrameworkHelper> safHelperProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.contextProvider = contextProvider;
    this.mediaStoreDataSourceProvider = mediaStoreDataSourceProvider;
    this.mediaStoreObserverProvider = mediaStoreObserverProvider;
    this.favoriteDaoProvider = favoriteDaoProvider;
    this.trashDaoProvider = trashDaoProvider;
    this.safHelperProvider = safHelperProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public MediaRepositoryImpl get() {
    return newInstance(contextProvider.get(), mediaStoreDataSourceProvider.get(), mediaStoreObserverProvider.get(), favoriteDaoProvider.get(), trashDaoProvider.get(), safHelperProvider.get(), dispatchersProvider.get());
  }

  public static MediaRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<MediaStoreDataSource> mediaStoreDataSourceProvider,
      Provider<MediaStoreObserver> mediaStoreObserverProvider,
      Provider<FavoriteDao> favoriteDaoProvider, Provider<TrashDao> trashDaoProvider,
      Provider<StorageAccessFrameworkHelper> safHelperProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new MediaRepositoryImpl_Factory(contextProvider, mediaStoreDataSourceProvider, mediaStoreObserverProvider, favoriteDaoProvider, trashDaoProvider, safHelperProvider, dispatchersProvider);
  }

  public static MediaRepositoryImpl newInstance(Context context,
      MediaStoreDataSource mediaStoreDataSource, MediaStoreObserver mediaStoreObserver,
      FavoriteDao favoriteDao, TrashDao trashDao, StorageAccessFrameworkHelper safHelper,
      DispatcherProvider dispatchers) {
    return new MediaRepositoryImpl(context, mediaStoreDataSource, mediaStoreObserver, favoriteDao, trashDao, safHelper, dispatchers);
  }
}
