package com.gallery.app.core.editor.player;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class VideoEditorPlayerManager_Factory implements Factory<VideoEditorPlayerManager> {
  private final Provider<Context> contextProvider;

  public VideoEditorPlayerManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VideoEditorPlayerManager get() {
    return newInstance(contextProvider.get());
  }

  public static VideoEditorPlayerManager_Factory create(Provider<Context> contextProvider) {
    return new VideoEditorPlayerManager_Factory(contextProvider);
  }

  public static VideoEditorPlayerManager newInstance(Context context) {
    return new VideoEditorPlayerManager(context);
  }
}
