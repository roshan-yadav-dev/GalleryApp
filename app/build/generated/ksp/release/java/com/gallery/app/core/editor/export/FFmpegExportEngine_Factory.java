package com.gallery.app.core.editor.export;

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
public final class FFmpegExportEngine_Factory implements Factory<FFmpegExportEngine> {
  private final Provider<Context> contextProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public FFmpegExportEngine_Factory(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.contextProvider = contextProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public FFmpegExportEngine get() {
    return newInstance(contextProvider.get(), dispatchersProvider.get());
  }

  public static FFmpegExportEngine_Factory create(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new FFmpegExportEngine_Factory(contextProvider, dispatchersProvider);
  }

  public static FFmpegExportEngine newInstance(Context context, DispatcherProvider dispatchers) {
    return new FFmpegExportEngine(context, dispatchers);
  }
}
