package com.gallery.app.feature.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.R
import com.gallery.app.core.domain.model.GridSize
import com.gallery.app.core.domain.model.SortBy
import com.gallery.app.core.domain.model.SortDirection
import com.gallery.app.core.widgets.EmptyStateView
import com.gallery.app.core.widgets.LoadingStateView
import com.gallery.app.core.widgets.MediaCard
import com.gallery.app.core.widgets.SelectionTopBar
import com.gallery.app.core.widgets.StickyHeader
import com.gallery.app.feature.viewer.MediaViewerScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
    onEditVideo: (String) -> Unit = {},
    onEditImage: (String) -> Unit = {},
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var activeViewerMediaId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            if (uiState.isSelectionMode) {
                SelectionTopBar(
                    selectedCount = uiState.selectedItemIds.size,
                    onCloseSelection = { viewModel.clearSelection() },
                    onSelectAll = { viewModel.selectAll() },
                    onShare = { /* Share action */ },
                    onFavorite = { viewModel.toggleFavoriteSelectedItems() },
                    onDelete = { viewModel.deleteSelectedItems() }
                )
            } else {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.nav_gallery)) },
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            Text(
                                text = "Grid Size",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                            DropdownMenuItem(
                                text = { Text("Small Grid") },
                                onClick = {
                                    viewModel.setGridSize(GridSize.SMALL)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Medium Grid") },
                                onClick = {
                                    viewModel.setGridSize(GridSize.MEDIUM)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Large Grid") },
                                onClick = {
                                    viewModel.setGridSize(GridSize.LARGE)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Name") },
                                onClick = {
                                    viewModel.setSortOrder(SortBy.NAME, SortDirection.ASCENDING)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Date") },
                                onClick = {
                                    viewModel.setSortOrder(SortBy.DATE, SortDirection.DESCENDING)
                                    showMenu = false
                                }
                            )
                        }
                    }
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
                uiState.allItems.isEmpty() -> EmptyStateView(message = stringResource(id = R.string.gallery_empty))
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(uiState.gridSize.columns),
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        uiState.groups.forEach { group ->
                            item(span = { GridItemSpan(uiState.gridSize.columns) }) {
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
                                            activeViewerMediaId = item.id
                                        }
                                    },
                                    onLongClick = {
                                        viewModel.toggleItemSelection(item.id)
                                    },
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Inline Fullscreen Media Viewer Overlay (Swiping left/right across all gallery media)
            AnimatedVisibility(
                visible = activeViewerMediaId != null,
                enter = fadeIn() + scaleIn(initialScale = 0.95f),
                exit = fadeOut() + scaleOut(targetScale = 0.95f)
            ) {
                if (activeViewerMediaId != null) {
                    BackHandler {
                        activeViewerMediaId = null
                    }
                    MediaViewerScreen(
                        initialMediaId = activeViewerMediaId,
                        mediaItemsList = uiState.allItems,
                        onNavigateBack = { activeViewerMediaId = null },
                        onEditVideo = { uri ->
                            activeViewerMediaId = null
                            onEditVideo(uri)
                        },
                        onEditImage = { uri ->
                            activeViewerMediaId = null
                            onEditImage(uri)
                        }
                    )
                }
            }
        }
    }
}
