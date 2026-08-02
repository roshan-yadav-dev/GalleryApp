package com.gallery.app.feature.editor

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.core.domain.model.editor.ExportStatus
import com.gallery.app.core.domain.model.editor.OverlayType
import com.gallery.app.core.domain.model.editor.TextStyleConfig
import com.gallery.app.core.editor.thumbnail.FrameThumbnailManager
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.feature.editor.components.ClipPropertySheet
import com.gallery.app.feature.editor.components.ExportProgressDialog
import com.gallery.app.feature.editor.components.PlaybackControls
import com.gallery.app.feature.editor.components.TimelineView
import com.gallery.app.feature.editor.components.VideoPreviewCanvas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditorScreen(
    videoUriString: String,
    onNavigateBack: () -> Unit,
    thumbnailManager: FrameThumbnailManager,
    viewModel: VideoEditorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showTextInputDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }

    LaunchedEffect(videoUriString) {
        val uri = Uri.parse(videoUriString)
        viewModel.loadVideo(context, uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Editor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.exportVideo() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null)
                        Text(" Export", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141418))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0F0F12))
        ) {
            if (uiState.isLoading) {
                LoadingStateView()
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Immersive Preview Canvas (Top half)
                    VideoPreviewCanvas(
                        exoPlayer = viewModel.playerManager.initializePlayer(),
                        timelineState = uiState.timelineState,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    // Control Bar
                    PlaybackControls(
                        isPlaying = uiState.timelineState.isPlaying,
                        isLooping = uiState.timelineState.isLooping,
                        canUndo = uiState.canUndo,
                        canRedo = uiState.canRedo,
                        hasSelectedClip = uiState.timelineState.selectedClip != null,
                        showSafeGuides = uiState.timelineState.showSafeGuides,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onStepBack = { viewModel.stepFrame(forward = false) },
                        onStepForward = { viewModel.stepFrame(forward = true) },
                        onSplitClip = { viewModel.splitSelectedClip() },
                        onDuplicateClip = { viewModel.duplicateSelectedClip() },
                        onDeleteClip = { viewModel.deleteSelectedClip() },
                        onRotateClip = { viewModel.rotateSelectedClip() },
                        onFlipClip = { viewModel.flipSelectedClip(horizontal = true) },
                        onFilterClick = { /* Handled in inspector */ },
                        onAddText = { showTextInputDialog = true },
                        onToggleLoop = { viewModel.toggleLooping() },
                        onToggleGuides = { viewModel.toggleSafeGuides() },
                        onUndo = { viewModel.undo() },
                        onRedo = { viewModel.redo() }
                    )

                    // Selected Clip Property Bar
                    ClipPropertySheet(
                        selectedClip = uiState.timelineState.selectedClip,
                        onVolumeChange = { viewModel.setClipVolume(it) },
                        onSpeedChange = { viewModel.setClipSpeed(it) },
                        onFilterChange = { viewModel.setClipFilter(it) },
                        onColorAdjustmentsChange = { viewModel.setClipColorAdjustments(it) }
                    )

                    // Interactive Multi-Track Timeline (Bottom half)
                    TimelineView(
                        timelineState = uiState.timelineState,
                        thumbnailManager = thumbnailManager,
                        onSeekTo = { viewModel.seekTo(it) },
                        onSelectClip = { viewModel.selectClip(it) },
                        onTrimClip = { start, end -> viewModel.trimSelectedClip(start, end) },
                        onZoomChange = { viewModel.setZoom(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }
            }

            // Export Progress Modal Dialog
            ExportProgressDialog(
                exportProgress = uiState.exportProgress,
                onDismiss = {
                    if (uiState.exportProgress.status == ExportStatus.SUCCESS) {
                        onNavigateBack()
                    }
                }
            )

            // Text Input Dialog
            if (showTextInputDialog) {
                AlertDialog(
                    onDismissRequest = { showTextInputDialog = false },
                    title = { Text("Add Text Overlay") },
                    text = {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Text content") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (textInput.isNotBlank()) {
                                viewModel.addOverlay(OverlayType.TEXT, TextStyleConfig(text = textInput))
                                textInput = ""
                            }
                            showTextInputDialog = false
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTextInputDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
