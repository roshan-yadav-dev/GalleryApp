package com.gallery.app.feature.search;

import com.gallery.app.core.domain.usecase.SearchMediaUseCase;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<SearchMediaUseCase> searchMediaUseCaseProvider;

  public SearchViewModel_Factory(Provider<SearchMediaUseCase> searchMediaUseCaseProvider) {
    this.searchMediaUseCaseProvider = searchMediaUseCaseProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(searchMediaUseCaseProvider.get());
  }

  public static SearchViewModel_Factory create(
      Provider<SearchMediaUseCase> searchMediaUseCaseProvider) {
    return new SearchViewModel_Factory(searchMediaUseCaseProvider);
  }

  public static SearchViewModel newInstance(SearchMediaUseCase searchMediaUseCase) {
    return new SearchViewModel(searchMediaUseCase);
  }
}
