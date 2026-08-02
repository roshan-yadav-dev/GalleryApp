package com.gallery.app.core.data.repository;

import com.gallery.app.core.datastore.DataStoreManager;
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
public final class SettingsRepositoryImpl_Factory implements Factory<SettingsRepositoryImpl> {
  private final Provider<DataStoreManager> dataStoreManagerProvider;

  public SettingsRepositoryImpl_Factory(Provider<DataStoreManager> dataStoreManagerProvider) {
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public SettingsRepositoryImpl get() {
    return newInstance(dataStoreManagerProvider.get());
  }

  public static SettingsRepositoryImpl_Factory create(
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new SettingsRepositoryImpl_Factory(dataStoreManagerProvider);
  }

  public static SettingsRepositoryImpl newInstance(DataStoreManager dataStoreManager) {
    return new SettingsRepositoryImpl(dataStoreManager);
  }
}
