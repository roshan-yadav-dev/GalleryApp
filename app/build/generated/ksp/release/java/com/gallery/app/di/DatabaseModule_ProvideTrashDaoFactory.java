package com.gallery.app.di;

import com.gallery.app.core.database.GalleryDatabase;
import com.gallery.app.core.database.dao.TrashDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTrashDaoFactory implements Factory<TrashDao> {
  private final Provider<GalleryDatabase> databaseProvider;

  public DatabaseModule_ProvideTrashDaoFactory(Provider<GalleryDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TrashDao get() {
    return provideTrashDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTrashDaoFactory create(
      Provider<GalleryDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTrashDaoFactory(databaseProvider);
  }

  public static TrashDao provideTrashDao(GalleryDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTrashDao(database));
  }
}
