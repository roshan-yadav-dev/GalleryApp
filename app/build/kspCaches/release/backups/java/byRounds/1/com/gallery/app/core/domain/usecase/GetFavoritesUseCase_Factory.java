package com.gallery.app.core.domain.usecase;

import com.gallery.app.core.domain.repository.FavoritesRepository;
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
public final class GetFavoritesUseCase_Factory implements Factory<GetFavoritesUseCase> {
  private final Provider<FavoritesRepository> favoritesRepositoryProvider;

  public GetFavoritesUseCase_Factory(Provider<FavoritesRepository> favoritesRepositoryProvider) {
    this.favoritesRepositoryProvider = favoritesRepositoryProvider;
  }

  @Override
  public GetFavoritesUseCase get() {
    return newInstance(favoritesRepositoryProvider.get());
  }

  public static GetFavoritesUseCase_Factory create(
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    return new GetFavoritesUseCase_Factory(favoritesRepositoryProvider);
  }

  public static GetFavoritesUseCase newInstance(FavoritesRepository favoritesRepository) {
    return new GetFavoritesUseCase(favoritesRepository);
  }
}
