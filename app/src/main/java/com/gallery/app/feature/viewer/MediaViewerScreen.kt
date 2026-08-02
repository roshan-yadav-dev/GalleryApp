package com.gallery.app.feature.viewer

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.widgets.DetailsBottomSheet
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.core.widgets.PinchZoomViewer
import com.gallery.app.core.widgets.VideoPlayerSurface

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaViewerScreen(
    initialMediaId: Long? = null,
    mediaItemsList: List<MediaItem>? = null,
    onNavigateBack: () -> Unit,
    onEditVideo: (String) -> Unit = {},
    onEditImage: (String) -> Unit = {},
    viewModel: MediaViewerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    val effectiveItems = mediaItemsList ?: uiState.mediaItems
    if (effectiveItems.isEmpty()) {
        if (uiState.isLoading) {
            LoadingStateView()
        }
        return
    }

    val startIndex = if (initialMediaId != null) {
        effectiveItems.indexOfFirst { it.id == initialMediaId }.coerceAtLeast(0)
    } else {
        uiState.currentIndex.coerceIn(0, (effectiveItems.size - 1).coerceAtLeast(0))
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { effectiveItems.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onPageChanged(page)
        }
    }

    val currentItem = if (pagerState.currentPage in effectiveItems.indices) {
        effectiveItems[pagerState.currentPage]
    } else {
        uiState.currentMediaItem
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
                            text = currentItem?.displayName ?: "",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
            // Main Media Pager View (Supports both Photos and Videos swiping seamlessly left/right)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = effectiveItems[page]
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

            // Bottom Action Navigation Bar Overlay
            AnimatedVisibility(
                visible = uiState.showSystemBars && currentItem != null && !currentItem.isVideo,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Share
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
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    // Favorite
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (currentItem?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (currentItem?.isFavorite == true) MaterialTheme.colorScheme.tertiary else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Edit
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
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    // Delete
                    IconButton(onClick = { viewModel.deleteCurrentMedia() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    // Info / EXIF Details
                    IconButton(onClick = { viewModel.showDetails() }) {
                        Icon(Icons.Default.Info, contentDescription = "Details", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }
            }

            // Bottom EXIF Details Sheet
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
