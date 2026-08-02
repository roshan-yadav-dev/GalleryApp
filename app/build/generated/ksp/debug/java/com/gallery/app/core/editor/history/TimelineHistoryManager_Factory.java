package com.gallery.app.core.editor.history;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class TimelineHistoryManager_Factory implements Factory<TimelineHistoryManager> {
  @Override
  public TimelineHistoryManager get() {
    return newInstance();
  }

  public static TimelineHistoryManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimelineHistoryManager newInstance() {
    return new TimelineHistoryManager();
  }

  private static final class InstanceHolder {
    private static final TimelineHistoryManager_Factory INSTANCE = new TimelineHistoryManager_Factory();
  }
}
