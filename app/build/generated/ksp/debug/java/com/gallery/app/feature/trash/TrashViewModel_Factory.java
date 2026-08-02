package com.gallery.app.feature.trash;

import com.gallery.app.core.domain.usecase.DeletePermanentlyUseCase;
import com.gallery.app.core.domain.usecase.GetTrashItemsUseCase;
import com.gallery.app.core.domain.usecase.RestoreFromTrashUseCase;
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
public final class TrashViewModel_Factory implements Factory<TrashViewModel> {
  private final Provider<GetTrashItemsUseCase> getTrashItemsUseCaseProvider;

  private final Provider<RestoreFromTrashUseCase> restoreFromTrashUseCaseProvider;

  private final Provider<DeletePermanentlyUseCase> deletePermanentlyUseCaseProvider;

  public TrashViewModel_Factory(Provider<GetTrashItemsUseCase> getTrashItemsUseCaseProvider,
      Provider<RestoreFromTrashUseCase> restoreFromTrashUseCaseProvider,
      Provider<DeletePermanentlyUseCase> deletePermanentlyUseCaseProvider) {
    this.getTrashItemsUseCaseProvider = getTrashItemsUseCaseProvider;
    this.restoreFromTrashUseCaseProvider = restoreFromTrashUseCaseProvider;
    this.deletePermanentlyUseCaseProvider = deletePermanentlyUseCaseProvider;
  }

  @Override
  public TrashViewModel get() {
    return newInstance(getTrashItemsUseCaseProvider.get(), restoreFromTrashUseCaseProvider.get(), deletePermanentlyUseCaseProvider.get());
  }

  public static TrashViewModel_Factory create(
      Provider<GetTrashItemsUseCase> getTrashItemsUseCaseProvider,
      Provider<RestoreFromTrashUseCase> restoreFromTrashUseCaseProvider,
      Provider<DeletePermanentlyUseCase> deletePermanentlyUseCaseProvider) {
    return new TrashViewModel_Factory(getTrashItemsUseCaseProvider, restoreFromTrashUseCaseProvider, deletePermanentlyUseCaseProvider);
  }

  public static TrashViewModel newInstance(GetTrashItemsUseCase getTrashItemsUseCase,
      RestoreFromTrashUseCase restoreFromTrashUseCase,
      DeletePermanentlyUseCase deletePermanentlyUseCase) {
    return new TrashViewModel(getTrashItemsUseCase, restoreFromTrashUseCase, deletePermanentlyUseCase);
  }
}
