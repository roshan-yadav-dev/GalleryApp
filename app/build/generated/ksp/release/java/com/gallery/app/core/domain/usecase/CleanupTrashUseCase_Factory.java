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
public final class CleanupTrashUseCase_Factory implements Factory<CleanupTrashUseCase> {
  private final Provider<TrashRepository> trashRepositoryProvider;

  public CleanupTrashUseCase_Factory(Provider<TrashRepository> trashRepositoryProvider) {
    this.trashRepositoryProvider = trashRepositoryProvider;
  }

  @Override
  public CleanupTrashUseCase get() {
    return newInstance(trashRepositoryProvider.get());
  }

  public static CleanupTrashUseCase_Factory create(
      Provider<TrashRepository> trashRepositoryProvider) {
    return new CleanupTrashUseCase_Factory(trashRepositoryProvider);
  }

  public static CleanupTrashUseCase newInstance(TrashRepository trashRepository) {
    return new CleanupTrashUseCase(trashRepository);
  }
}
