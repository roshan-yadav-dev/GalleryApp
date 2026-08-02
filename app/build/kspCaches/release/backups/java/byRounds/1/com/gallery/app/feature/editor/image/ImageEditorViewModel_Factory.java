package com.gallery.app.feature.editor.image;

import com.gallery.app.core.editor.image.PhotoEditorEngine;
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
public final class ImageEditorViewModel_Factory implements Factory<ImageEditorViewModel> {
  private final Provider<PhotoEditorEngine> editorEngineProvider;

  public ImageEditorViewModel_Factory(Provider<PhotoEditorEngine> editorEngineProvider) {
    this.editorEngineProvider = editorEngineProvider;
  }

  @Override
  public ImageEditorViewModel get() {
    return newInstance(editorEngineProvider.get());
  }

  public static ImageEditorViewModel_Factory create(
      Provider<PhotoEditorEngine> editorEngineProvider) {
    return new ImageEditorViewModel_Factory(editorEngineProvider);
  }

  public static ImageEditorViewModel newInstance(PhotoEditorEngine editorEngine) {
    return new ImageEditorViewModel(editorEngine);
  }
}
