package com.gallery.app.feature.favorites;

import com.gallery.app.core.domain.usecase.GetFavoritesUseCase;
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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<GetFavoritesUseCase> getFavoritesUseCaseProvider;

  public FavoritesViewModel_Factory(Provider<GetFavoritesUseCase> getFavoritesUseCaseProvider) {
    this.getFavoritesUseCaseProvider = getFavoritesUseCaseProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(getFavoritesUseCaseProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<GetFavoritesUseCase> getFavoritesUseCaseProvider) {
    return new FavoritesViewModel_Factory(getFavoritesUseCaseProvider);
  }

  public static FavoritesViewModel newInstance(GetFavoritesUseCase getFavoritesUseCase) {
    return new FavoritesViewModel(getFavoritesUseCase);
  }
}
