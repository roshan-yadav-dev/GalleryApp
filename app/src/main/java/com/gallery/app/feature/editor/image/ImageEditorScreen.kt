package com.gallery.app.feature.editor.image

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallery.app.core.domain.model.editor.ImageFilterType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageEditorScreen(
    imageUriString: String,
    onNavigateBack: () -> Unit,
    viewModel: ImageEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val previewBitmap by viewModel.previewBitmap.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(imageUriString) {
        if (imageUriString.isNotEmpty()) {
            viewModel.loadMedia(Uri.parse(imageUriString))
        }
    }

    LaunchedEffect(uiState.savedUri) {
        if (uiState.savedUri != null) {
            onNavigateBack()
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Photo Editor", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.undo() }, enabled = viewModel.canUndo()) {
                        Icon(
                            Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (viewModel.canUndo()) Color.White else Color.Gray
                        )
                    }
                    IconButton(onClick = { viewModel.redo() }, enabled = viewModel.canRedo()) {
                        Icon(
                            Icons.AutoMirrored.Filled.Redo,
                            contentDescription = "Redo",
                            tint = if (viewModel.canRedo()) Color.White else Color.Gray
                        )
                    }
                    IconButton(onClick = { viewModel.saveImage() }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Main Bitmap Canvas Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (previewBitmap != null) {
                        Image(
                            bitmap = previewBitmap!!.asImageBitmap(),
                            contentDescription = "Edited Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                // Bottom Tool Panel
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E24))
                ) {
                    // Active Tool Controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        when (selectedTab) {
                            0 -> TransformControls(
                                onRotate = { viewModel.rotateRight() },
                                onFlipH = { viewModel.flipHorizontal() },
                                onFlipV = { viewModel.flipVertical() }
                            )
                            1 -> AdjustmentSliders(
                                brightness = uiState.brightness,
                                contrast = uiState.contrast,
                                saturation = uiState.saturation,
                                onBrightnessChange = { viewModel.updateBrightness(it) },
                                onContrastChange = { viewModel.updateContrast(it) },
                                onSaturationChange = { viewModel.updateSaturation(it) }
                            )
                            2 -> FilterSelector(
                                activeFilter = uiState.activeFilter,
                                onSelectFilter = { viewModel.setFilter(it) }
                            )
                        }
                    }

                    // Category Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0xFF141418),
                        contentColor = Color.White
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Transform") },
                            icon = { Icon(Icons.Default.RotateRight, contentDescription = null) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Adjust") },
                            icon = { Icon(Icons.Default.Tune, contentDescription = null) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Filters") },
                            icon = { Icon(Icons.Default.Flip, contentDescription = null) }
                        )
                    }
                }
            }

            // Saving Indicator Overlay
            if (uiState.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Saving image...", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransformControls(
    onRotate: () -> Unit,
    onFlipH: () -> Unit,
    onFlipV: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onRotate() }
        ) {
            Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = Color.White, modifier = Modifier.size(32.dp))
            Text("Rotate 90°", color = Color.LightGray, fontSize = 12.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onFlipH() }
        ) {
            Icon(Icons.Default.Flip, contentDescription = "Flip H", tint = Color.White, modifier = Modifier.size(32.dp))
            Text("Flip H", color = Color.LightGray, fontSize = 12.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onFlipV() }
        ) {
            Icon(Icons.Default.Flip, contentDescription = "Flip V", tint = Color.White, modifier = Modifier.size(32.dp))
            Text("Flip V", color = Color.LightGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AdjustmentSliders(
    brightness: Float,
    contrast: Float,
    saturation: Float,
    onBrightnessChange: (Float) -> Unit,
    onContrastChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit
) {
    var activeAdjustTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Brightness",
                color = if (activeAdjustTab == 0) MaterialTheme.colorScheme.primary else Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { activeAdjustTab = 0 }
            )
            Text(
                text = "Contrast",
                color = if (activeAdjustTab == 1) MaterialTheme.colorScheme.primary else Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { activeAdjustTab = 1 }
            )
            Text(
                text = "Saturation",
                color = if (activeAdjustTab == 2) MaterialTheme.colorScheme.primary else Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { activeAdjustTab = 2 }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        when (activeAdjustTab) {
            0 -> Slider(
                value = brightness,
                onValueChange = onBrightnessChange,
                valueRange = -0.5f..0.5f,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
            )
            1 -> Slider(
                value = contrast,
                onValueChange = onContrastChange,
                valueRange = 0.5f..2.0f,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
            )
            2 -> Slider(
                value = saturation,
                onValueChange = onSaturationChange,
                valueRange = 0.0f..2.0f,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
private fun FilterSelector(
    activeFilter: ImageFilterType,
    onSelectFilter: (ImageFilterType) -> Unit
) {
    val filters = ImageFilterType.entries.toTypedArray()
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.forEach { filter ->
            val isSelected = filter == activeFilter
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color(0xFF2A2D34),
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelectFilter(filter) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter.displayName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
