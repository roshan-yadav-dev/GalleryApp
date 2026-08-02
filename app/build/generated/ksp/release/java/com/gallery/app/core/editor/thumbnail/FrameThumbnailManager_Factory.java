package com.gallery.app.core.editor.thumbnail;

import android.content.Context;
import com.gallery.app.core.common.DispatcherProvider;
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
public final class FrameThumbnailManager_Factory implements Factory<FrameThumbnailManager> {
  private final Provider<Context> contextProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public FrameThumbnailManager_Factory(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.contextProvider = contextProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public FrameThumbnailManager get() {
    return newInstance(contextProvider.get(), dispatchersProvider.get());
  }

  public static FrameThumbnailManager_Factory create(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new FrameThumbnailManager_Factory(contextProvider, dispatchersProvider);
  }

  public static FrameThumbnailManager newInstance(Context context, DispatcherProvider dispatchers) {
    return new FrameThumbnailManager(context, dispatchers);
  }
}
