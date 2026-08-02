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
public final class MoveToTrashUseCase_Factory implements Factory<MoveToTrashUseCase> {
  private final Provider<TrashRepository> trashRepositoryProvider;

  public MoveToTrashUseCase_Factory(Provider<TrashRepository> trashRepositoryProvider) {
    this.trashRepositoryProvider = trashRepositoryProvider;
  }

  @Override
  public MoveToTrashUseCase get() {
    return newInstance(trashRepositoryProvider.get());
  }

  public static MoveToTrashUseCase_Factory create(
      Provider<TrashRepository> trashRepositoryProvider) {
    return new MoveToTrashUseCase_Factory(trashRepositoryProvider);
  }

  public static MoveToTrashUseCase newInstance(TrashRepository trashRepository) {
    return new MoveToTrashUseCase(trashRepository);
  }
}
