package com.gallery.app.core.storage;

import android.content.Context;
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
public final class MediaStoreObserver_Factory implements Factory<MediaStoreObserver> {
  private final Provider<Context> contextProvider;

  public MediaStoreObserver_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MediaStoreObserver get() {
    return newInstance(contextProvider.get());
  }

  public static MediaStoreObserver_Factory create(Provider<Context> contextProvider) {
    return new MediaStoreObserver_Factory(contextProvider);
  }

  public static MediaStoreObserver newInstance(Context context) {
    return new MediaStoreObserver(context);
  }
}
