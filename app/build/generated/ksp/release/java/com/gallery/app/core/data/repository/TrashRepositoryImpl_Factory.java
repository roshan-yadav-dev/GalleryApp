package com.gallery.app.core.data.repository;

import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.database.dao.TrashDao;
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
public final class TrashRepositoryImpl_Factory implements Factory<TrashRepositoryImpl> {
  private final Provider<TrashDao> trashDaoProvider;

  private final Provider<StorageAccessFrameworkHelper> safHelperProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public TrashRepositoryImpl_Factory(Provider<TrashDao> trashDaoProvider,
      Provider<StorageAccessFrameworkHelper> safHelperProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.trashDaoProvider = trashDaoProvider;
    this.safHelperProvider = safHelperProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public TrashRepositoryImpl get() {
    return newInstance(trashDaoProvider.get(), safHelperProvider.get(), dispatchersProvider.get());
  }

  public static TrashRepositoryImpl_Factory create(Provider<TrashDao> trashDaoProvider,
      Provider<StorageAccessFrameworkHelper> safHelperProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new TrashRepositoryImpl_Factory(trashDaoProvider, safHelperProvider, dispatchersProvider);
  }

  public static TrashRepositoryImpl newInstance(TrashDao trashDao,
      StorageAccessFrameworkHelper safHelper, DispatcherProvider dispatchers) {
    return new TrashRepositoryImpl(trashDao, safHelper, dispatchers);
  }
}
