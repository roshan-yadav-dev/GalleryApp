package com.gallery.app.feature.editor;

import com.gallery.app.core.common.DispatcherProvider;
import com.gallery.app.core.editor.engine.GestureEngine;
import com.gallery.app.core.editor.engine.RenderingEngine;
import com.gallery.app.core.editor.engine.SelectionEngine;
import com.gallery.app.core.editor.engine.TimelineEngine;
import com.gallery.app.core.editor.export.FFmpegExportEngine;
import com.gallery.app.core.editor.history.TimelineHistoryManager;
import com.gallery.app.core.editor.player.VideoEditorPlayerManager;
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
public final class VideoEditorViewModel_Factory implements Factory<VideoEditorViewModel> {
  private final Provider<VideoEditorPlayerManager> playerManagerProvider;

  private final Provider<TimelineEngine> timelineEngineProvider;

  private final Provider<SelectionEngine> selectionEngineProvider;

  private final Provider<GestureEngine> gestureEngineProvider;

  private final Provider<RenderingEngine> renderingEngineProvider;

  private final Provider<TimelineHistoryManager> historyManagerProvider;

  private final Provider<FFmpegExportEngine> exportEngineProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public VideoEditorViewModel_Factory(Provider<VideoEditorPlayerManager> playerManagerProvider,
      Provider<TimelineEngine> timelineEngineProvider,
      Provider<SelectionEngine> selectionEngineProvider,
      Provider<GestureEngine> gestureEngineProvider,
      Provider<RenderingEngine> renderingEngineProvider,
      Provider<TimelineHistoryManager> historyManagerProvider,
      Provider<FFmpegExportEngine> exportEngineProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.playerManagerProvider = playerManagerProvider;
    this.timelineEngineProvider = timelineEngineProvider;
    this.selectionEngineProvider = selectionEngineProvider;
    this.gestureEngineProvider = gestureEngineProvider;
    this.renderingEngineProvider = renderingEngineProvider;
    this.historyManagerProvider = historyManagerProvider;
    this.exportEngineProvider = exportEngineProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public VideoEditorViewModel get() {
    return newInstance(playerManagerProvider.get(), timelineEngineProvider.get(), selectionEngineProvider.get(), gestureEngineProvider.get(), renderingEngineProvider.get(), historyManagerProvider.get(), exportEngineProvider.get(), dispatchersProvider.get());
  }

  public static VideoEditorViewModel_Factory create(
      Provider<VideoEditorPlayerManager> playerManagerProvider,
      Provider<TimelineEngine> timelineEngineProvider,
      Provider<SelectionEngine> selectionEngineProvider,
      Provider<GestureEngine> gestureEngineProvider,
      Provider<RenderingEngine> renderingEngineProvider,
      Provider<TimelineHistoryManager> historyManagerProvider,
      Provider<FFmpegExportEngine> exportEngineProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new VideoEditorViewModel_Factory(playerManagerProvider, timelineEngineProvider, selectionEngineProvider, gestureEngineProvider, renderingEngineProvider, historyManagerProvider, exportEngineProvider, dispatchersProvider);
  }

  public static VideoEditorViewModel newInstance(VideoEditorPlayerManager playerManager,
      TimelineEngine timelineEngine, SelectionEngine selectionEngine, GestureEngine gestureEngine,
      RenderingEngine renderingEngine, TimelineHistoryManager historyManager,
      FFmpegExportEngine exportEngine, DispatcherProvider dispatchers) {
    return new VideoEditorViewModel(playerManager, timelineEngine, selectionEngine, gestureEngine, renderingEngine, historyManager, exportEngine, dispatchers);
  }
}
