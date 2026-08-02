package com.gallery.app.feature.viewer

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.core.widgets.DetailsBottomSheet
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.core.widgets.PinchZoomViewer
import com.gallery.app.core.widgets.VideoPlayerSurface

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaViewerScreen(
    onNavigateBack: () -> Unit,
    onEditVideo: (String) -> Unit = {},
    onEditImage: (String) -> Unit = {},
    viewModel: MediaViewerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    if (uiState.isLoading || uiState.mediaItems.isEmpty()) {
        LoadingStateView()
        return
    }

    val pagerState = rememberPagerState(
        initialPage = uiState.currentIndex,
        pageCount = { uiState.mediaItems.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onPageChanged(page)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            AnimatedVisibility(
                visible = uiState.showSystemBars,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = uiState.currentMediaItem?.displayName ?: "",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        val currentItem = uiState.currentMediaItem
                        IconButton(
                            onClick = {
                                if (currentItem != null) {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, currentItem.uri)
                                        type = currentItem.mimeType
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Media"))
                                }
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                        }
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (currentItem?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (currentItem?.isFavorite == true) MaterialTheme.colorScheme.tertiary else Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                val uriStr = currentItem?.uri?.toString() ?: return@IconButton
                                if (currentItem.isVideo) {
                                    onEditVideo(uriStr)
                                } else {
                                    onEditImage(uriStr)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Media", tint = Color.White)
                        }
                        IconButton(onClick = { viewModel.showDetails() }) {
                            Icon(Icons.Default.Info, contentDescription = "Details", tint = Color.White)
                        }
                        IconButton(onClick = { viewModel.deleteCurrentMedia() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.5f)
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = uiState.mediaItems[page]
                if (item.isVideo) {
                    VideoPlayerSurface(
                        videoUri = item.uri,
                        isActive = (page == pagerState.currentPage),
                        onTap = { viewModel.toggleSystemBars() }
                    )
                } else {
                    PinchZoomViewer(
                        mediaItem = item,
                        onTap = { viewModel.toggleSystemBars() }
                    )
                }
            }

            // Bottom EXIF Sheet
            if (uiState.showDetailsSheet && uiState.currentMetadata != null) {
                DetailsBottomSheet(
                    metadata = uiState.currentMetadata!!,
                    sheetState = sheetState,
                    onDismissRequest = { viewModel.hideDetails() }
                )
            }
        }
    }
}
