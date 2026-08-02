package com.gallery.app;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GalleryApplication_MembersInjector implements MembersInjector<GalleryApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public GalleryApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<GalleryApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new GalleryApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(GalleryApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.gallery.app.GalleryApplication.workerFactory")
  public static void injectWorkerFactory(GalleryApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
