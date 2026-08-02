package com.gallery.app.di;

import com.gallery.app.core.database.GalleryDatabase;
import com.gallery.app.core.database.dao.FavoriteDao;
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
public final class DatabaseModule_ProvideFavoriteDaoFactory implements Factory<FavoriteDao> {
  private final Provider<GalleryDatabase> databaseProvider;

  public DatabaseModule_ProvideFavoriteDaoFactory(Provider<GalleryDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FavoriteDao get() {
    return provideFavoriteDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideFavoriteDaoFactory create(
      Provider<GalleryDatabase> databaseProvider) {
    return new DatabaseModule_ProvideFavoriteDaoFactory(databaseProvider);
  }

  public static FavoriteDao provideFavoriteDao(GalleryDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFavoriteDao(database));
  }
}
