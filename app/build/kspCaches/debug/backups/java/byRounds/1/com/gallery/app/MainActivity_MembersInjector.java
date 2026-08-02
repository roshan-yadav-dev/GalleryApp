package com.gallery.app;

import com.gallery.app.core.datastore.DataStoreManager;
import com.gallery.app.core.editor.thumbnail.FrameThumbnailManager;
import com.gallery.app.core.permissions.PermissionManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PermissionManager> permissionManagerProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<FrameThumbnailManager> thumbnailManagerProvider;

  public MainActivity_MembersInjector(Provider<PermissionManager> permissionManagerProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FrameThumbnailManager> thumbnailManagerProvider) {
    this.permissionManagerProvider = permissionManagerProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.thumbnailManagerProvider = thumbnailManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<PermissionManager> permissionManagerProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FrameThumbnailManager> thumbnailManagerProvider) {
    return new MainActivity_MembersInjector(permissionManagerProvider, dataStoreManagerProvider, thumbnailManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPermissionManager(instance, permissionManagerProvider.get());
    injectDataStoreManager(instance, dataStoreManagerProvider.get());
    injectThumbnailManager(instance, thumbnailManagerProvider.get());
  }

  @InjectedFieldSignature("com.gallery.app.MainActivity.permissionManager")
  public static void injectPermissionManager(MainActivity instance,
      PermissionManager permissionManager) {
    instance.permissionManager = permissionManager;
  }

  @InjectedFieldSignature("com.gallery.app.MainActivity.dataStoreManager")
  public static void injectDataStoreManager(MainActivity instance,
      DataStoreManager dataStoreManager) {
    instance.dataStoreManager = dataStoreManager;
  }

  @InjectedFieldSignature("com.gallery.app.MainActivity.thumbnailManager")
  public static void injectThumbnailManager(MainActivity instance,
      FrameThumbnailManager thumbnailManager) {
    instance.thumbnailManager = thumbnailManager;
  }
}
