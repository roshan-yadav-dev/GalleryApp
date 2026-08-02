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
public final class RenderingEngine_Factory implements Factory<RenderingEngine> {
  @Override
  public RenderingEngine get() {
    return newInstance();
  }

  public static RenderingEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RenderingEngine newInstance() {
    return new RenderingEngine();
  }

  private static final class InstanceHolder {
    private static final RenderingEngine_Factory INSTANCE = new RenderingEngine_Factory();
  }
}
