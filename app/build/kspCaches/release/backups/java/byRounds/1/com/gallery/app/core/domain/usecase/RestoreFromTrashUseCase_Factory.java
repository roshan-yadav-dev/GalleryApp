package com.gallery.app.core.domain.usecase;

import com.gallery.app.core.domain.repository.TrashRepository;
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
public final class RestoreFromTrashUseCase_Factory implements Factory<RestoreFromTrashUseCase> {
  private final Provider<TrashRepository> trashRepositoryProvider;

  public RestoreFromTrashUseCase_Factory(Provider<TrashRepository> trashRepositoryProvider) {
    this.trashRepositoryProvider = trashRepositoryProvider;
  }

  @Override
  public RestoreFromTrashUseCase get() {
    return newInstance(trashRepositoryProvider.get());
  }

  public static RestoreFromTrashUseCase_Factory create(
      Provider<TrashRepository> trashRepositoryProvider) {
    return new RestoreFromTrashUseCase_Factory(trashRepositoryProvider);
  }

  public static RestoreFromTrashUseCase newInstance(TrashRepository trashRepository) {
    return new RestoreFromTrashUseCase(trashRepository);
  }
}
