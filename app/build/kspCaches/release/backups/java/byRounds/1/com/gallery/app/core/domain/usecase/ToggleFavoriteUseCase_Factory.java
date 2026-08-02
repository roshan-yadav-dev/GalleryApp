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
public final class ToggleFavoriteUseCase_Factory implements Factory<ToggleFavoriteUseCase> {
  private final Provider<FavoritesRepository> favoritesRepositoryProvider;

  public ToggleFavoriteUseCase_Factory(Provider<FavoritesRepository> favoritesRepositoryProvider) {
    this.favoritesRepositoryProvider = favoritesRepositoryProvider;
  }

  @Override
  public ToggleFavoriteUseCase get() {
    return newInstance(favoritesRepositoryProvider.get());
  }

  public static ToggleFavoriteUseCase_Factory create(
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    return new ToggleFavoriteUseCase_Factory(favoritesRepositoryProvider);
  }

  public static ToggleFavoriteUseCase newInstance(FavoritesRepository favoritesRepository) {
    return new ToggleFavoriteUseCase(favoritesRepository);
  }
}
