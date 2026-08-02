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
public final class SearchMediaUseCase_Factory implements Factory<SearchMediaUseCase> {
  private final Provider<MediaRepository> mediaRepositoryProvider;

  public SearchMediaUseCase_Factory(Provider<MediaRepository> mediaRepositoryProvider) {
    this.mediaRepositoryProvider = mediaRepositoryProvider;
  }

  @Override
  public SearchMediaUseCase get() {
    return newInstance(mediaRepositoryProvider.get());
  }

  public static SearchMediaUseCase_Factory create(
      Provider<MediaRepository> mediaRepositoryProvider) {
    return new SearchMediaUseCase_Factory(mediaRepositoryProvider);
  }

  public static SearchMediaUseCase newInstance(MediaRepository mediaRepository) {
    return new SearchMediaUseCase(mediaRepository);
  }
}
