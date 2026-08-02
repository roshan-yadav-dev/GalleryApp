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
public final class TimelineEngine_Factory implements Factory<TimelineEngine> {
  @Override
  public TimelineEngine get() {
    return newInstance();
  }

  public static TimelineEngine_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimelineEngine newInstance() {
    return new TimelineEngine();
  }

  private static final class InstanceHolder {
    private static final TimelineEngine_Factory INSTANCE = new TimelineEngine_Factory();
  }
}
