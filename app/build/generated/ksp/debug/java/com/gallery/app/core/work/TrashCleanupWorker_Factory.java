package com.gallery.app.core.work;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.gallery.app.core.domain.usecase.CleanupTrashUseCase;
import dagger.internal.DaggerGenerated;
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
public final class TrashCleanupWorker_Factory {
  private final Provider<CleanupTrashUseCase> cleanupTrashUseCaseProvider;

  public TrashCleanupWorker_Factory(Provider<CleanupTrashUseCase> cleanupTrashUseCaseProvider) {
    this.cleanupTrashUseCaseProvider = cleanupTrashUseCaseProvider;
  }

  public TrashCleanupWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, cleanupTrashUseCaseProvider.get());
  }

  public static TrashCleanupWorker_Factory create(
      Provider<CleanupTrashUseCase> cleanupTrashUseCaseProvider) {
    return new TrashCleanupWorker_Factory(cleanupTrashUseCaseProvider);
  }

  public static TrashCleanupWorker newInstance(Context context, WorkerParameters params,
      CleanupTrashUseCase cleanupTrashUseCase) {
    return new TrashCleanupWorker(context, params, cleanupTrashUseCase);
  }
}
