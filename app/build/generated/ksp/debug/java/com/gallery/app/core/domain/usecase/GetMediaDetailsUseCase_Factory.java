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
public final class GetMediaDetailsUseCase_Factory implements Factory<GetMediaDetailsUseCase> {
  private final Provider<MediaRepository> mediaRepositoryProvider;

  public GetMediaDetailsUseCase_Factory(Provider<MediaRepository> mediaRepositoryProvider) {
    this.mediaRepositoryProvider = mediaRepositoryProvider;
  }

  @Override
  public GetMediaDetailsUseCase get() {
    return newInstance(mediaRepositoryProvider.get());
  }

  public static GetMediaDetailsUseCase_Factory create(
      Provider<MediaRepository> mediaRepositoryProvider) {
    return new GetMediaDetailsUseCase_Factory(mediaRepositoryProvider);
  }

  public static GetMediaDetailsUseCase newInstance(MediaRepository mediaRepository) {
    return new GetMediaDetailsUseCase(mediaRepository);
  }
}
