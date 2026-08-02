package com.gallery.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.data.repository.AlbumRepositoryImpl;
import com.gallery.app.core.data.repository.FavoritesRepositoryImpl;
import com.gallery.app.core.data.repository.MediaRepositoryImpl;
import com.gallery.app.core.data.repository.SettingsRepositoryImpl;
import com.gallery.app.core.data.repository.TrashRepositoryImpl;
import com.gallery.app.core.data.source.local.MediaStoreDataSource;
import com.gallery.app.core.database.GalleryDatabase;
import com.gallery.app.core.database.dao.AlbumDao;
import com.gallery.app.core.database.dao.FavoriteDao;
import com.gallery.app.core.database.dao.TrashDao;
import com.gallery.app.core.datastore.DataStoreManager;
import com.gallery.app.core.domain.usecase.DeletePermanentlyUseCase;
import com.gallery.app.core.domain.usecase.GetAlbumsUseCase;
import com.gallery.app.core.domain.usecase.GetFavoritesUseCase;
import com.gallery.app.core.domain.usecase.GetMediaDetailsUseCase;
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase;
import com.gallery.app.core.domain.usecase.GetSettingsUseCase;
import com.gallery.app.core.domain.usecase.GetTrashItemsUseCase;
import com.gallery.app.core.domain.usecase.MoveToTrashUseCase;
import com.gallery.app.core.domain.usecase.RestoreFromTrashUseCase;
import com.gallery.app.core.domain.usecase.SearchMediaUseCase;
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase;
import com.gallery.app.core.domain.usecase.UpdateSettingsUseCase;
import com.gallery.app.core.editor.engine.GestureEngine;
import com.gallery.app.core.editor.engine.RenderingEngine;
import com.gallery.app.core.editor.engine.SelectionEngine;
import com.gallery.app.core.editor.engine.TimelineEngine;
import com.gallery.app.core.editor.export.FFmpegExportEngine;
import com.gallery.app.core.editor.history.TimelineHistoryManager;
import com.gallery.app.core.editor.image.PhotoEditorEngine;
import com.gallery.app.core.editor.player.VideoEditorPlayerManager;
import com.gallery.app.core.editor.thumbnail.FrameThumbnailManager;
import com.gallery.app.core.permissions.PermissionManager;
import com.gallery.app.core.storage.MediaStoreObserver;
import com.gallery.app.core.storage.StorageAccessFrameworkHelper;
import com.gallery.app.di.AppModule_ProvideContextFactory;
import com.gallery.app.di.AppModule_ProvideDispatcherProviderFactory;
import com.gallery.app.di.DatabaseModule_ProvideAlbumDaoFactory;
import com.gallery.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.gallery.app.di.DatabaseModule_ProvideFavoriteDaoFactory;
import com.gallery.app.di.DatabaseModule_ProvideTrashDaoFactory;
import com.gallery.app.feature.albums.AlbumDetailViewModel;
import com.gallery.app.feature.albums.AlbumDetailViewModel_HiltModules;
import com.gallery.app.feature.albums.AlbumsViewModel;
import com.gallery.app.feature.albums.AlbumsViewModel_HiltModules;
import com.gallery.app.feature.editor.VideoEditorViewModel;
import com.gallery.app.feature.editor.VideoEditorViewModel_HiltModules;
import com.gallery.app.feature.editor.image.ImageEditorViewModel;
import com.gallery.app.feature.editor.image.ImageEditorViewModel_HiltModules;
import com.gallery.app.feature.favorites.FavoritesViewModel;
import com.gallery.app.feature.favorites.FavoritesViewModel_HiltModules;
import com.gallery.app.feature.gallery.GalleryViewModel;
import com.gallery.app.feature.gallery.GalleryViewModel_HiltModules;
import com.gallery.app.feature.search.SearchViewModel;
import com.gallery.app.feature.search.SearchViewModel_HiltModules;
import com.gallery.app.feature.settings.SettingsViewModel;
import com.gallery.app.feature.settings.SettingsViewModel_HiltModules;
import com.gallery.app.feature.trash.TrashViewModel;
import com.gallery.app.feature.trash.TrashViewModel_HiltModules;
import com.gallery.app.feature.viewer.MediaViewerViewModel;
import com.gallery.app.feature.viewer.MediaViewerViewModel_HiltModules;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerGalleryApplication_HiltComponents_SingletonC {
  private DaggerGalleryApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public GalleryApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements GalleryApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements GalleryApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements GalleryApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements GalleryApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements GalleryApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements GalleryApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements GalleryApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public GalleryApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends GalleryApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends GalleryApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends GalleryApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends GalleryApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(10).put(LazyClassKeyProvider.com_gallery_app_feature_albums_AlbumDetailViewModel, AlbumDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_albums_AlbumsViewModel, AlbumsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_favorites_FavoritesViewModel, FavoritesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_gallery_GalleryViewModel, GalleryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_editor_image_ImageEditorViewModel, ImageEditorViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_viewer_MediaViewerViewModel, MediaViewerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_trash_TrashViewModel, TrashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_gallery_app_feature_editor_VideoEditorViewModel, VideoEditorViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPermissionManager(instance, singletonCImpl.permissionManagerProvider.get());
      MainActivity_MembersInjector.injectDataStoreManager(instance, singletonCImpl.dataStoreManagerProvider.get());
      MainActivity_MembersInjector.injectThumbnailManager(instance, singletonCImpl.frameThumbnailManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_gallery_app_feature_trash_TrashViewModel = "com.gallery.app.feature.trash.TrashViewModel";

      static String com_gallery_app_feature_albums_AlbumsViewModel = "com.gallery.app.feature.albums.AlbumsViewModel";

      static String com_gallery_app_feature_gallery_GalleryViewModel = "com.gallery.app.feature.gallery.GalleryViewModel";

      static String com_gallery_app_feature_editor_VideoEditorViewModel = "com.gallery.app.feature.editor.VideoEditorViewModel";

      static String com_gallery_app_feature_settings_SettingsViewModel = "com.gallery.app.feature.settings.SettingsViewModel";

      static String com_gallery_app_feature_favorites_FavoritesViewModel = "com.gallery.app.feature.favorites.FavoritesViewModel";

      static String com_gallery_app_feature_editor_image_ImageEditorViewModel = "com.gallery.app.feature.editor.image.ImageEditorViewModel";

      static String com_gallery_app_feature_viewer_MediaViewerViewModel = "com.gallery.app.feature.viewer.MediaViewerViewModel";

      static String com_gallery_app_feature_search_SearchViewModel = "com.gallery.app.feature.search.SearchViewModel";

      static String com_gallery_app_feature_albums_AlbumDetailViewModel = "com.gallery.app.feature.albums.AlbumDetailViewModel";

      @KeepFieldType
      TrashViewModel com_gallery_app_feature_trash_TrashViewModel2;

      @KeepFieldType
      AlbumsViewModel com_gallery_app_feature_albums_AlbumsViewModel2;

      @KeepFieldType
      GalleryViewModel com_gallery_app_feature_gallery_GalleryViewModel2;

      @KeepFieldType
      VideoEditorViewModel com_gallery_app_feature_editor_VideoEditorViewModel2;

      @KeepFieldType
      SettingsViewModel com_gallery_app_feature_settings_SettingsViewModel2;

      @KeepFieldType
      FavoritesViewModel com_gallery_app_feature_favorites_FavoritesViewModel2;

      @KeepFieldType
      ImageEditorViewModel com_gallery_app_feature_editor_image_ImageEditorViewModel2;

      @KeepFieldType
      MediaViewerViewModel com_gallery_app_feature_viewer_MediaViewerViewModel2;

      @KeepFieldType
      SearchViewModel com_gallery_app_feature_search_SearchViewModel2;

      @KeepFieldType
      AlbumDetailViewModel com_gallery_app_feature_albums_AlbumDetailViewModel2;
    }
  }

  private static final class ViewModelCImpl extends GalleryApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AlbumDetailViewModel> albumDetailViewModelProvider;

    private Provider<AlbumsViewModel> albumsViewModelProvider;

    private Provider<FavoritesViewModel> favoritesViewModelProvider;

    private Provider<GalleryViewModel> galleryViewModelProvider;

    private Provider<ImageEditorViewModel> imageEditorViewModelProvider;

    private Provider<MediaViewerViewModel> mediaViewerViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TrashViewModel> trashViewModelProvider;

    private Provider<VideoEditorViewModel> videoEditorViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetMediaItemsUseCase getMediaItemsUseCase() {
      return new GetMediaItemsUseCase(singletonCImpl.mediaRepositoryImplProvider.get());
    }

    private GetAlbumsUseCase getAlbumsUseCase() {
      return new GetAlbumsUseCase(singletonCImpl.albumRepositoryImplProvider.get());
    }

    private ToggleFavoriteUseCase toggleFavoriteUseCase() {
      return new ToggleFavoriteUseCase(singletonCImpl.favoritesRepositoryImplProvider.get());
    }

    private MoveToTrashUseCase moveToTrashUseCase() {
      return new MoveToTrashUseCase(singletonCImpl.trashRepositoryImplProvider.get());
    }

    private GetFavoritesUseCase getFavoritesUseCase() {
      return new GetFavoritesUseCase(singletonCImpl.favoritesRepositoryImplProvider.get());
    }

    private GetSettingsUseCase getSettingsUseCase() {
      return new GetSettingsUseCase(singletonCImpl.settingsRepositoryImplProvider.get());
    }

    private UpdateSettingsUseCase updateSettingsUseCase() {
      return new UpdateSettingsUseCase(singletonCImpl.settingsRepositoryImplProvider.get());
    }

    private GetMediaDetailsUseCase getMediaDetailsUseCase() {
      return new GetMediaDetailsUseCase(singletonCImpl.mediaRepositoryImplProvider.get());
    }

    private SearchMediaUseCase searchMediaUseCase() {
      return new SearchMediaUseCase(singletonCImpl.mediaRepositoryImplProvider.get());
    }

    private GetTrashItemsUseCase getTrashItemsUseCase() {
      return new GetTrashItemsUseCase(singletonCImpl.trashRepositoryImplProvider.get());
    }

    private RestoreFromTrashUseCase restoreFromTrashUseCase() {
      return new RestoreFromTrashUseCase(singletonCImpl.trashRepositoryImplProvider.get());
    }

    private DeletePermanentlyUseCase deletePermanentlyUseCase() {
      return new DeletePermanentlyUseCase(singletonCImpl.trashRepositoryImplProvider.get());
    }

    private VideoEditorPlayerManager videoEditorPlayerManager() {
      return new VideoEditorPlayerManager(singletonCImpl.provideContextProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.albumDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.albumsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.favoritesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.galleryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.imageEditorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.mediaViewerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.trashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.videoEditorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(10).put(LazyClassKeyProvider.com_gallery_app_feature_albums_AlbumDetailViewModel, ((Provider) albumDetailViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_albums_AlbumsViewModel, ((Provider) albumsViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_favorites_FavoritesViewModel, ((Provider) favoritesViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_gallery_GalleryViewModel, ((Provider) galleryViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_editor_image_ImageEditorViewModel, ((Provider) imageEditorViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_viewer_MediaViewerViewModel, ((Provider) mediaViewerViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_trash_TrashViewModel, ((Provider) trashViewModelProvider)).put(LazyClassKeyProvider.com_gallery_app_feature_editor_VideoEditorViewModel, ((Provider) videoEditorViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_gallery_app_feature_gallery_GalleryViewModel = "com.gallery.app.feature.gallery.GalleryViewModel";

      static String com_gallery_app_feature_viewer_MediaViewerViewModel = "com.gallery.app.feature.viewer.MediaViewerViewModel";

      static String com_gallery_app_feature_settings_SettingsViewModel = "com.gallery.app.feature.settings.SettingsViewModel";

      static String com_gallery_app_feature_favorites_FavoritesViewModel = "com.gallery.app.feature.favorites.FavoritesViewModel";

      static String com_gallery_app_feature_search_SearchViewModel = "com.gallery.app.feature.search.SearchViewModel";

      static String com_gallery_app_feature_editor_image_ImageEditorViewModel = "com.gallery.app.feature.editor.image.ImageEditorViewModel";

      static String com_gallery_app_feature_albums_AlbumDetailViewModel = "com.gallery.app.feature.albums.AlbumDetailViewModel";

      static String com_gallery_app_feature_albums_AlbumsViewModel = "com.gallery.app.feature.albums.AlbumsViewModel";

      static String com_gallery_app_feature_trash_TrashViewModel = "com.gallery.app.feature.trash.TrashViewModel";

      static String com_gallery_app_feature_editor_VideoEditorViewModel = "com.gallery.app.feature.editor.VideoEditorViewModel";

      @KeepFieldType
      GalleryViewModel com_gallery_app_feature_gallery_GalleryViewModel2;

      @KeepFieldType
      MediaViewerViewModel com_gallery_app_feature_viewer_MediaViewerViewModel2;

      @KeepFieldType
      SettingsViewModel com_gallery_app_feature_settings_SettingsViewModel2;

      @KeepFieldType
      FavoritesViewModel com_gallery_app_feature_favorites_FavoritesViewModel2;

      @KeepFieldType
      SearchViewModel com_gallery_app_feature_search_SearchViewModel2;

      @KeepFieldType
      ImageEditorViewModel com_gallery_app_feature_editor_image_ImageEditorViewModel2;

      @KeepFieldType
      AlbumDetailViewModel com_gallery_app_feature_albums_AlbumDetailViewModel2;

      @KeepFieldType
      AlbumsViewModel com_gallery_app_feature_albums_AlbumsViewModel2;

      @KeepFieldType
      TrashViewModel com_gallery_app_feature_trash_TrashViewModel2;

      @KeepFieldType
      VideoEditorViewModel com_gallery_app_feature_editor_VideoEditorViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.gallery.app.feature.albums.AlbumDetailViewModel 
          return (T) new AlbumDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getMediaItemsUseCase(), viewModelCImpl.getAlbumsUseCase(), viewModelCImpl.toggleFavoriteUseCase(), viewModelCImpl.moveToTrashUseCase());

          case 1: // com.gallery.app.feature.albums.AlbumsViewModel 
          return (T) new AlbumsViewModel(viewModelCImpl.getAlbumsUseCase());

          case 2: // com.gallery.app.feature.favorites.FavoritesViewModel 
          return (T) new FavoritesViewModel(viewModelCImpl.getFavoritesUseCase());

          case 3: // com.gallery.app.feature.gallery.GalleryViewModel 
          return (T) new GalleryViewModel(viewModelCImpl.getMediaItemsUseCase(), viewModelCImpl.getSettingsUseCase(), viewModelCImpl.updateSettingsUseCase(), viewModelCImpl.toggleFavoriteUseCase(), viewModelCImpl.moveToTrashUseCase());

          case 4: // com.gallery.app.feature.editor.image.ImageEditorViewModel 
          return (T) new ImageEditorViewModel(singletonCImpl.photoEditorEngineProvider.get());

          case 5: // com.gallery.app.feature.viewer.MediaViewerViewModel 
          return (T) new MediaViewerViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getMediaItemsUseCase(), viewModelCImpl.getMediaDetailsUseCase(), viewModelCImpl.toggleFavoriteUseCase(), viewModelCImpl.moveToTrashUseCase());

          case 6: // com.gallery.app.feature.search.SearchViewModel 
          return (T) new SearchViewModel(viewModelCImpl.searchMediaUseCase());

          case 7: // com.gallery.app.feature.settings.SettingsViewModel 
          return (T) new SettingsViewModel(viewModelCImpl.getSettingsUseCase(), viewModelCImpl.updateSettingsUseCase());

          case 8: // com.gallery.app.feature.trash.TrashViewModel 
          return (T) new TrashViewModel(viewModelCImpl.getTrashItemsUseCase(), viewModelCImpl.restoreFromTrashUseCase(), viewModelCImpl.deletePermanentlyUseCase());

          case 9: // com.gallery.app.feature.editor.VideoEditorViewModel 
          return (T) new VideoEditorViewModel(viewModelCImpl.videoEditorPlayerManager(), singletonCImpl.timelineEngineProvider.get(), singletonCImpl.selectionEngineProvider.get(), singletonCImpl.gestureEngineProvider.get(), singletonCImpl.renderingEngineProvider.get(), new TimelineHistoryManager(), singletonCImpl.fFmpegExportEngineProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends GalleryApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends GalleryApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends GalleryApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<Context> provideContextProvider;

    private Provider<PermissionManager> permissionManagerProvider;

    private Provider<DataStoreManager> dataStoreManagerProvider;

    private Provider<DispatcherProvider> provideDispatcherProvider;

    private Provider<FrameThumbnailManager> frameThumbnailManagerProvider;

    private Provider<MediaStoreDataSource> mediaStoreDataSourceProvider;

    private Provider<MediaStoreObserver> mediaStoreObserverProvider;

    private Provider<GalleryDatabase> provideDatabaseProvider;

    private Provider<StorageAccessFrameworkHelper> storageAccessFrameworkHelperProvider;

    private Provider<MediaRepositoryImpl> mediaRepositoryImplProvider;

    private Provider<AlbumRepositoryImpl> albumRepositoryImplProvider;

    private Provider<FavoritesRepositoryImpl> favoritesRepositoryImplProvider;

    private Provider<TrashRepositoryImpl> trashRepositoryImplProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<PhotoEditorEngine> photoEditorEngineProvider;

    private Provider<TimelineEngine> timelineEngineProvider;

    private Provider<SelectionEngine> selectionEngineProvider;

    private Provider<GestureEngine> gestureEngineProvider;

    private Provider<RenderingEngine> renderingEngineProvider;

    private Provider<FFmpegExportEngine> fFmpegExportEngineProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(ImmutableMap.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>of());
    }

    private FavoriteDao favoriteDao() {
      return DatabaseModule_ProvideFavoriteDaoFactory.provideFavoriteDao(provideDatabaseProvider.get());
    }

    private TrashDao trashDao() {
      return DatabaseModule_ProvideTrashDaoFactory.provideTrashDao(provideDatabaseProvider.get());
    }

    private AlbumDao albumDao() {
      return DatabaseModule_ProvideAlbumDaoFactory.provideAlbumDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideContextProvider = DoubleCheck.provider(new SwitchingProvider<Context>(singletonCImpl, 1));
      this.permissionManagerProvider = DoubleCheck.provider(new SwitchingProvider<PermissionManager>(singletonCImpl, 0));
      this.dataStoreManagerProvider = DoubleCheck.provider(new SwitchingProvider<DataStoreManager>(singletonCImpl, 2));
      this.provideDispatcherProvider = DoubleCheck.provider(new SwitchingProvider<DispatcherProvider>(singletonCImpl, 4));
      this.frameThumbnailManagerProvider = DoubleCheck.provider(new SwitchingProvider<FrameThumbnailManager>(singletonCImpl, 3));
      this.mediaStoreDataSourceProvider = DoubleCheck.provider(new SwitchingProvider<MediaStoreDataSource>(singletonCImpl, 6));
      this.mediaStoreObserverProvider = DoubleCheck.provider(new SwitchingProvider<MediaStoreObserver>(singletonCImpl, 7));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<GalleryDatabase>(singletonCImpl, 8));
      this.storageAccessFrameworkHelperProvider = DoubleCheck.provider(new SwitchingProvider<StorageAccessFrameworkHelper>(singletonCImpl, 9));
      this.mediaRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MediaRepositoryImpl>(singletonCImpl, 5));
      this.albumRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<AlbumRepositoryImpl>(singletonCImpl, 10));
      this.favoritesRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<FavoritesRepositoryImpl>(singletonCImpl, 11));
      this.trashRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<TrashRepositoryImpl>(singletonCImpl, 12));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 13));
      this.photoEditorEngineProvider = DoubleCheck.provider(new SwitchingProvider<PhotoEditorEngine>(singletonCImpl, 14));
      this.timelineEngineProvider = DoubleCheck.provider(new SwitchingProvider<TimelineEngine>(singletonCImpl, 15));
      this.selectionEngineProvider = DoubleCheck.provider(new SwitchingProvider<SelectionEngine>(singletonCImpl, 16));
      this.gestureEngineProvider = DoubleCheck.provider(new SwitchingProvider<GestureEngine>(singletonCImpl, 17));
      this.renderingEngineProvider = DoubleCheck.provider(new SwitchingProvider<RenderingEngine>(singletonCImpl, 18));
      this.fFmpegExportEngineProvider = DoubleCheck.provider(new SwitchingProvider<FFmpegExportEngine>(singletonCImpl, 19));
    }

    @Override
    public void injectGalleryApplication(GalleryApplication galleryApplication) {
      injectGalleryApplication2(galleryApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private GalleryApplication injectGalleryApplication2(GalleryApplication instance) {
      GalleryApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.gallery.app.core.permissions.PermissionManager 
          return (T) new PermissionManager(singletonCImpl.provideContextProvider.get());

          case 1: // android.content.Context 
          return (T) AppModule_ProvideContextFactory.provideContext(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.gallery.app.core.datastore.DataStoreManager 
          return (T) new DataStoreManager(singletonCImpl.provideContextProvider.get());

          case 3: // com.gallery.app.core.editor.thumbnail.FrameThumbnailManager 
          return (T) new FrameThumbnailManager(singletonCImpl.provideContextProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 4: // com.gallery.app.core.common.DispatcherProvider 
          return (T) AppModule_ProvideDispatcherProviderFactory.provideDispatcherProvider();

          case 5: // com.gallery.app.core.data.repository.MediaRepositoryImpl 
          return (T) new MediaRepositoryImpl(singletonCImpl.provideContextProvider.get(), singletonCImpl.mediaStoreDataSourceProvider.get(), singletonCImpl.mediaStoreObserverProvider.get(), singletonCImpl.favoriteDao(), singletonCImpl.trashDao(), singletonCImpl.storageAccessFrameworkHelperProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 6: // com.gallery.app.core.data.source.local.MediaStoreDataSource 
          return (T) new MediaStoreDataSource(singletonCImpl.provideContextProvider.get());

          case 7: // com.gallery.app.core.storage.MediaStoreObserver 
          return (T) new MediaStoreObserver(singletonCImpl.provideContextProvider.get());

          case 8: // com.gallery.app.core.database.GalleryDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.gallery.app.core.storage.StorageAccessFrameworkHelper 
          return (T) new StorageAccessFrameworkHelper(singletonCImpl.provideContextProvider.get());

          case 10: // com.gallery.app.core.data.repository.AlbumRepositoryImpl 
          return (T) new AlbumRepositoryImpl(singletonCImpl.mediaStoreDataSourceProvider.get(), singletonCImpl.mediaStoreObserverProvider.get(), singletonCImpl.albumDao(), singletonCImpl.provideDispatcherProvider.get());

          case 11: // com.gallery.app.core.data.repository.FavoritesRepositoryImpl 
          return (T) new FavoritesRepositoryImpl(singletonCImpl.mediaStoreDataSourceProvider.get(), singletonCImpl.mediaStoreObserverProvider.get(), singletonCImpl.favoriteDao(), singletonCImpl.provideDispatcherProvider.get());

          case 12: // com.gallery.app.core.data.repository.TrashRepositoryImpl 
          return (T) new TrashRepositoryImpl(singletonCImpl.trashDao(), singletonCImpl.storageAccessFrameworkHelperProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 13: // com.gallery.app.core.data.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(singletonCImpl.dataStoreManagerProvider.get());

          case 14: // com.gallery.app.core.editor.image.PhotoEditorEngine 
          return (T) new PhotoEditorEngine(singletonCImpl.provideContextProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          case 15: // com.gallery.app.core.editor.engine.TimelineEngine 
          return (T) new TimelineEngine();

          case 16: // com.gallery.app.core.editor.engine.SelectionEngine 
          return (T) new SelectionEngine();

          case 17: // com.gallery.app.core.editor.engine.GestureEngine 
          return (T) new GestureEngine();

          case 18: // com.gallery.app.core.editor.engine.RenderingEngine 
          return (T) new RenderingEngine();

          case 19: // com.gallery.app.core.editor.export.FFmpegExportEngine 
          return (T) new FFmpegExportEngine(singletonCImpl.provideContextProvider.get(), singletonCImpl.provideDispatcherProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
