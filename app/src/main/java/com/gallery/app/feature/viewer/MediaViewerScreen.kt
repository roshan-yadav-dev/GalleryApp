package com.gallery.app.feature.viewer

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gallery.app.core.common.DateFormatter
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

    val thumbnailListState = rememberLazyListState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onPageChanged(page)
            thumbnailListState.animateScrollToItem(page)
        }
    }

    val currentItem = effectiveItems.getOrNull(pagerState.currentPage)

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            AnimatedVisibility(
                visible = uiState.showSystemBars,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                var showOverflowMenu by remember { mutableStateOf(false) }

                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = currentItem?.let { DateFormatter.formatDateHeader(it.dateAdded) } ?: "Media",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (currentItem != null) {
                                Text(
                                    text = DateFormatter.formatTime(currentItem.dateAdded),
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showOverflowMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = showOverflowMenu,
                            onDismissRequest = { showOverflowMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Details") },
                                onClick = {
                                    showOverflowMenu = false
                                    viewModel.showDetails()
                                }
                            )
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
            // Main Media Swipe Pager (Horizontal Pager)
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

            // Bottom System Overlay: Filmstrip Thumbnails & Action Toolbar
            AnimatedVisibility(
                visible = uiState.showSystemBars,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.75f))
                ) {
                    // 1. Horizontal Filmstrip Thumbnail Navigation Strip
                    LazyRow(
                        state = thumbnailListState,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        itemsIndexed(effectiveItems, key = { _, item -> item.id }) { index, item ->
                            val isSelected = (index == pagerState.currentPage)
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(item.uri)
                                        .size(120, 120)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                if (item.isVideo) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(2.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Videocam,
                                            contentDescription = "Video",
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 2. Bottom Action Toolbar Row (Share, Favorite, Edit, Delete, Info)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
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
                            Icon(Icons.Default.Edit, contentDescription = "Edit Media", tint = Color.White, modifier = Modifier.size(24.dp))
                        }

                        // Delete
                        IconButton(onClick = { viewModel.deleteCurrentMedia() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(24.dp))
                        }

                        // Info
                        IconButton(onClick = { viewModel.showDetails() }) {
                            Icon(Icons.Default.Info, contentDescription = "Details", tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }

            // Bottom EXIF Metadata Sheet
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
