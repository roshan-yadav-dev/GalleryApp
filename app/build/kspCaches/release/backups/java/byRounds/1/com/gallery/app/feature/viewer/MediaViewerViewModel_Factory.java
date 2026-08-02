package com.gallery.app.feature.viewer;

import androidx.lifecycle.SavedStateHandle;
import com.gallery.app.core.domain.usecase.GetMediaDetailsUseCase;
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
public final class MediaViewerViewModel_Factory implements Factory<MediaViewerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider;

  private final Provider<GetMediaDetailsUseCase> getMediaDetailsUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider;

  public MediaViewerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetMediaDetailsUseCase> getMediaDetailsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getMediaItemsUseCaseProvider = getMediaItemsUseCaseProvider;
    this.getMediaDetailsUseCaseProvider = getMediaDetailsUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.moveToTrashUseCaseProvider = moveToTrashUseCaseProvider;
  }

  @Override
  public MediaViewerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getMediaItemsUseCaseProvider.get(), getMediaDetailsUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), moveToTrashUseCaseProvider.get());
  }

  public static MediaViewerViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetMediaDetailsUseCase> getMediaDetailsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    return new MediaViewerViewModel_Factory(savedStateHandleProvider, getMediaItemsUseCaseProvider, getMediaDetailsUseCaseProvider, toggleFavoriteUseCaseProvider, moveToTrashUseCaseProvider);
  }

  public static MediaViewerViewModel newInstance(SavedStateHandle savedStateHandle,
      GetMediaItemsUseCase getMediaItemsUseCase, GetMediaDetailsUseCase getMediaDetailsUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase, MoveToTrashUseCase moveToTrashUseCase) {
    return new MediaViewerViewModel(savedStateHandle, getMediaItemsUseCase, getMediaDetailsUseCase, toggleFavoriteUseCase, moveToTrashUseCase);
  }
}
