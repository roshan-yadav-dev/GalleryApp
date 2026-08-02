package com.gallery.app.core.domain.usecase;

import com.gallery.app.core.domain.repository.MediaRepository;
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
public final class GetMediaItemsUseCase_Factory implements Factory<GetMediaItemsUseCase> {
  private final Provider<MediaRepository> mediaRepositoryProvider;

  public GetMediaItemsUseCase_Factory(Provider<MediaRepository> mediaRepositoryProvider) {
    this.mediaRepositoryProvider = mediaRepositoryProvider;
  }

  @Override
  public GetMediaItemsUseCase get() {
    return newInstance(mediaRepositoryProvider.get());
  }

  public static GetMediaItemsUseCase_Factory create(
      Provider<MediaRepository> mediaRepositoryProvider) {
    return new GetMediaItemsUseCase_Factory(mediaRepositoryProvider);
  }

  public static GetMediaItemsUseCase newInstance(MediaRepository mediaRepository) {
    return new GetMediaItemsUseCase(mediaRepository);
  }
}
