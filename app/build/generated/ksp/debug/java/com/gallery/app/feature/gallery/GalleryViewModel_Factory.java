package com.gallery.app.feature.gallery;

import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase;
import com.gallery.app.core.domain.usecase.GetSettingsUseCase;
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase;
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase;
import com.gallery.app.core.domain.usecase.UpdateSettingsUseCase;
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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider;

  private final Provider<GetSettingsUseCase> getSettingsUseCaseProvider;

  private final Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider;

  private final Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider;

  public GalleryViewModel_Factory(Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetSettingsUseCase> getSettingsUseCaseProvider,
      Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    this.getMediaItemsUseCaseProvider = getMediaItemsUseCaseProvider;
    this.getSettingsUseCaseProvider = getSettingsUseCaseProvider;
    this.updateSettingsUseCaseProvider = updateSettingsUseCaseProvider;
    this.toggleFavoriteUseCaseProvider = toggleFavoriteUseCaseProvider;
    this.moveToTrashUseCaseProvider = moveToTrashUseCaseProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(getMediaItemsUseCaseProvider.get(), getSettingsUseCaseProvider.get(), updateSettingsUseCaseProvider.get(), toggleFavoriteUseCaseProvider.get(), moveToTrashUseCaseProvider.get());
  }

  public static GalleryViewModel_Factory create(
      Provider<GetMediaItemsUseCase> getMediaItemsUseCaseProvider,
      Provider<GetSettingsUseCase> getSettingsUseCaseProvider,
      Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteUseCaseProvider,
      Provider<MoveToTrashUseCase> moveToTrashUseCaseProvider) {
    return new GalleryViewModel_Factory(getMediaItemsUseCaseProvider, getSettingsUseCaseProvider, updateSettingsUseCaseProvider, toggleFavoriteUseCaseProvider, moveToTrashUseCaseProvider);
  }

  public static GalleryViewModel newInstance(GetMediaItemsUseCase getMediaItemsUseCase,
      GetSettingsUseCase getSettingsUseCase, UpdateSettingsUseCase updateSettingsUseCase,
      ToggleFavoriteUseCase toggleFavoriteUseCase, MoveToTrashUseCase moveToTrashUseCase) {
    return new GalleryViewModel(getMediaItemsUseCase, getSettingsUseCase, updateSettingsUseCase, toggleFavoriteUseCase, moveToTrashUseCase);
  }
}
