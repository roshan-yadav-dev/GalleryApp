package com.gallery.app.core.editor.engine;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class OverlayEngine_Factory implements Factory<OverlayEngine> {
  @Override
  public OverlayEngine get() {
    return newInstance();
  }

  public static OverlayEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OverlayEngine newInstance() {
    return new OverlayEngine();
  }

  private static final class InstanceHolder {
    private static final OverlayEngine_Factory INSTANCE = new OverlayEngine_Factory();
  }
}
