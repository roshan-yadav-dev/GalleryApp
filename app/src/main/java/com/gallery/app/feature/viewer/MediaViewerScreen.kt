package com.gallery.app.feature.viewer

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.widgets.DetailsBottomSheet
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.core.widgets.PinchZoomViewer
import com.gallery.app.core.widgets.VideoPlayerSurface
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

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
                        // Title left empty so content name is not displayed on screen
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
            // Main Media Pager View (Photos and Videos swiping)
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

            // Left Floating Navigation Arrow (Previous Item)
            if (pagerState.currentPage > 0 && uiState.showSystemBars) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                        contentDescription = "Previous Media",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Right Floating Navigation Arrow (Next Item)
            if (pagerState.currentPage < effectiveItems.size - 1 && uiState.showSystemBars) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = "Next Media",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Bottom Navigation Overlay Column: Before/Next Controller Pill + Actions Row
            AnimatedVisibility(
                visible = uiState.showSystemBars && currentItem != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.85f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Before & Next Content Quick Jumper Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Content Button
                        if (pagerState.currentPage > 0) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2A2D34))
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Prev",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Box(modifier = Modifier.size(1.dp))
                        }

                        // Index Indicator
                        Text(
                            text = "${pagerState.currentPage + 1} of ${effectiveItems.size}",
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Next Content Button
                        if (pagerState.currentPage < effectiveItems.size - 1) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2A2D34))
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Next",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        } else {
                            Box(modifier = Modifier.size(1.dp))
                        }
                    }

                    // Bottom Action Buttons Row (Share, Favorite, Edit, Delete, Info)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (currentItem?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (currentItem?.isFavorite == true) MaterialTheme.colorScheme.tertiary else Color.White,
                                modifier = Modifier.size(24.dp)
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
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(24.dp))
                        }

                        IconButton(onClick = { viewModel.deleteCurrentMedia() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(24.dp))
                        }

                        IconButton(onClick = { viewModel.showDetails() }) {
                            Icon(Icons.Default.Info, contentDescription = "Details", tint = Color.White, modifier = Modifier.size(24.dp))
                        }
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
