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
public final class GetTrashItemsUseCase_Factory implements Factory<GetTrashItemsUseCase> {
  private final Provider<TrashRepository> trashRepositoryProvider;

  public GetTrashItemsUseCase_Factory(Provider<TrashRepository> trashRepositoryProvider) {
    this.trashRepositoryProvider = trashRepositoryProvider;
  }

  @Override
  public GetTrashItemsUseCase get() {
    return newInstance(trashRepositoryProvider.get());
  }

  public static GetTrashItemsUseCase_Factory create(
      Provider<TrashRepository> trashRepositoryProvider) {
    return new GetTrashItemsUseCase_Factory(trashRepositoryProvider);
  }

  public static GetTrashItemsUseCase newInstance(TrashRepository trashRepository) {
    return new GetTrashItemsUseCase(trashRepository);
  }
}
