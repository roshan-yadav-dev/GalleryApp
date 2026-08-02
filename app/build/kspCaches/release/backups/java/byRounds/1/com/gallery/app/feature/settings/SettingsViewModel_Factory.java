package com.gallery.app.feature.settings;

import com.gallery.app.core.domain.usecase.GetSettingsUseCase;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<GetSettingsUseCase> getSettingsUseCaseProvider;

  private final Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider;

  public SettingsViewModel_Factory(Provider<GetSettingsUseCase> getSettingsUseCaseProvider,
      Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider) {
    this.getSettingsUseCaseProvider = getSettingsUseCaseProvider;
    this.updateSettingsUseCaseProvider = updateSettingsUseCaseProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(getSettingsUseCaseProvider.get(), updateSettingsUseCaseProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<GetSettingsUseCase> getSettingsUseCaseProvider,
      Provider<UpdateSettingsUseCase> updateSettingsUseCaseProvider) {
    return new SettingsViewModel_Factory(getSettingsUseCaseProvider, updateSettingsUseCaseProvider);
  }

  public static SettingsViewModel newInstance(GetSettingsUseCase getSettingsUseCase,
      UpdateSettingsUseCase updateSettingsUseCase) {
    return new SettingsViewModel(getSettingsUseCase, updateSettingsUseCase);
  }
}
