package com.gallery.app.feature.albums

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.widgets.DetailsBottomSheet
import com.gallery.app.core.widgets.EmptyStateView
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.core.widgets.MediaCard
import com.gallery.app.core.widgets.SelectionTopBar
import com.gallery.app.core.widgets.StickyHeader

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumDetailScreen(
    onNavigateBack: () -> Unit,
    onMediaClick: (Long) -> Unit,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val gridState = rememberLazyGridState()

    var detailMediaItem by remember { mutableStateOf<MediaItem?>(null) }

    val onShareSelected: () -> Unit = {
        val selectedItems = uiState.allItems.filter { it.id in uiState.selectedItemIds }
        if (selectedItems.isNotEmpty()) {
            val uris = ArrayList(selectedItems.map { it.uri })
            val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "*/*"
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Media"))
        }
    }

    Scaffold(
        topBar = {
            if (uiState.isSelectionMode) {
                SelectionTopBar(
                    selectedCount = uiState.selectedItemIds.size,
                    onCloseSelection = { viewModel.clearSelection() },
                    onSelectAll = { viewModel.selectAll() },
                    onDelete = { viewModel.deleteSelectedItems() },
                    onFavorite = { viewModel.toggleFavoriteSelectedItems() },
                    onShare = onShareSelected
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = uiState.albumName,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        if (uiState.selectedItemIds.isNotEmpty()) {
                            IconButton(onClick = { viewModel.selectAll() }) {
                                Icon(Icons.Default.SelectAll, contentDescription = "Select All")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
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
            when {
                uiState.isLoading -> LoadingStateView()
                uiState.allItems.isEmpty() -> EmptyStateView(message = "No media in this album")
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 110.dp),
                        state = gridState,
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        uiState.groups.forEach { group ->
                            item(
                                key = "header_${group.dateHeader}",
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                StickyHeader(title = group.dateHeader)
                            }

                            items(group.items, key = { it.id }) { item ->
                                val isSelected = uiState.selectedItemIds.contains(item.id)
                                MediaCard(
                                    mediaItem = item,
                                    isSelected = isSelected,
                                    isSelectionMode = uiState.isSelectionMode,
                                    onClick = {
                                        if (uiState.isSelectionMode) {
                                            viewModel.toggleItemSelection(item.id)
                                        } else {
                                            onMediaClick(item.id)
                                        }
                                    },
                                    onLongClick = {
                                        viewModel.toggleItemSelection(item.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            detailMediaItem?.let { item ->
                DetailsBottomSheet(
                    item = item,
                    onDismiss = { detailMediaItem = null }
                )
            }
        }
    }
}
