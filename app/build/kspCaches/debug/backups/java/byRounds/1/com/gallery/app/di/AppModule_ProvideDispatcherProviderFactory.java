package com.gallery.app.di;

import com.gallery.app.core.common.DispatcherProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideDispatcherProviderFactory implements Factory<DispatcherProvider> {
  @Override
  public DispatcherProvider get() {
    return provideDispatcherProvider();
  }

  public static AppModule_ProvideDispatcherProviderFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DispatcherProvider provideDispatcherProvider() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDispatcherProvider());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideDispatcherProviderFactory INSTANCE = new AppModule_ProvideDispatcherProviderFactory();
  }
}
