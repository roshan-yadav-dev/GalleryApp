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
public final class SelectionEngine_Factory implements Factory<SelectionEngine> {
  @Override
  public SelectionEngine get() {
    return newInstance();
  }

  public static SelectionEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SelectionEngine newInstance() {
    return new SelectionEngine();
  }

  private static final class InstanceHolder {
    private static final SelectionEngine_Factory INSTANCE = new SelectionEngine_Factory();
  }
}
