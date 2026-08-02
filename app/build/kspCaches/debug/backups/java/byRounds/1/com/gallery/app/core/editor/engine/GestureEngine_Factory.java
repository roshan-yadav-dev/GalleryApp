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
public final class GestureEngine_Factory implements Factory<GestureEngine> {
  @Override
  public GestureEngine get() {
    return newInstance();
  }

  public static GestureEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GestureEngine newInstance() {
    return new GestureEngine();
  }

  private static final class InstanceHolder {
    private static final GestureEngine_Factory INSTANCE = new GestureEngine_Factory();
  }
}
