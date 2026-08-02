package com.gallery.app.core.domain.usecase;

import com.gallery.app.core.domain.repository.AlbumRepository;
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
public final class GetAlbumsUseCase_Factory implements Factory<GetAlbumsUseCase> {
  private final Provider<AlbumRepository> albumRepositoryProvider;

  public GetAlbumsUseCase_Factory(Provider<AlbumRepository> albumRepositoryProvider) {
    this.albumRepositoryProvider = albumRepositoryProvider;
  }

  @Override
  public GetAlbumsUseCase get() {
    return newInstance(albumRepositoryProvider.get());
  }

  public static GetAlbumsUseCase_Factory create(Provider<AlbumRepository> albumRepositoryProvider) {
    return new GetAlbumsUseCase_Factory(albumRepositoryProvider);
  }

  public static GetAlbumsUseCase newInstance(AlbumRepository albumRepository) {
    return new GetAlbumsUseCase(albumRepository);
  }
}
