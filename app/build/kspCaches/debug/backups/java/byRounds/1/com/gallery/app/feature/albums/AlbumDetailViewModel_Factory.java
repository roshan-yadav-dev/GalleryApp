package com.gallery.app.feature.albums;

import androidx.lifecycle.SavedStateHandle;
import com.gallery.app.core.domain.usecase.GetAlbumsUseCase;
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase;
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase;
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase;
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
public final class AlbumDetailViewModel_Factory implements Factory<AlbumDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider;

  private final Provider<GetAlbumsUseCase> getAlbumsUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider;

  public AlbumDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetAlbumsUseCase> getAlbumsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getMediaItemsUseCaseProvider = getMediaItemsUseCaseProvider;
    this.getAlbumsUseCaseProvider = getAlbumsUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.moveToTrashUseCaseProvider = moveToTrashUseCaseProvider;
  }

  @Override
  public AlbumDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getMediaItemsUseCaseProvider.get(), getAlbumsUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), moveToTrashUseCaseProvider.get());
  }

  public static AlbumDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetAlbumsUseCase> getAlbumsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    return new AlbumDetailViewModel_Factory(savedStateHandleProvider, getMediaItemsUseCaseProvider, getAlbumsUseCaseProvider, toggleFavoriteUseCaseProvider, moveToTrashUseCaseProvider);
  }

  public static AlbumDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetMediaItemsUseCase getMediaItemsUseCase, GetAlbumsUseCase getAlbumsUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase, MoveToTrashUseCase moveToTrashUseCase) {
    return new AlbumDetailViewModel(savedStateHandle, getMediaItemsUseCase, getAlbumsUseCase, toggleFavoriteUseCase, moveToTrashUseCase);
  }
}
