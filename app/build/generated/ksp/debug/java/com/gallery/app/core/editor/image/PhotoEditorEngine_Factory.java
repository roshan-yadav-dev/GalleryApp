package com.gallery.app.core.editor.image;

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
public final class PhotoEditorEngine_Factory implements Factory<PhotoEditorEngine> {
  private final Provider<Context> contextProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public PhotoEditorEngine_Factory(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.contextProvider = contextProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public PhotoEditorEngine get() {
    return newInstance(contextProvider.get(), dispatchersProvider.get());
  }

  public static PhotoEditorEngine_Factory create(Provider<Context> contextProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new PhotoEditorEngine_Factory(contextProvider, dispatchersProvider);
  }

  public static PhotoEditorEngine newInstance(Context context, DispatcherProvider dispatchers) {
    return new PhotoEditorEngine(context, dispatchers);
  }
}
