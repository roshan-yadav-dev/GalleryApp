package com.gallery.app.feature.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    isLooping: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    hasSelectedClip: Boolean,
    showSafeGuides: Boolean,
    onTogglePlayPause: () -> Unit,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onSplitClip: () -> Unit,
    onDuplicateClip: () -> Unit,
    onDeleteClip: () -> Unit,
    onRotateClip: () -> Unit,
    onFlipClip: () -> Unit,
    onFilterClick: () -> Unit,
    onAddText: () -> Unit,
    onToggleLoop: () -> Unit,
    onToggleGuides: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF141418))
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Actions: Undo / Redo
        Row {
            IconButton(onClick = onUndo, enabled = canUndo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = "Undo",
                    tint = if (canUndo) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onRedo, enabled = canRedo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Redo,
                    contentDescription = "Redo",
                    tint = if (canRedo) Color.White else Color.Gray
                )
            }
        }

        // Center Actions: Playback
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onStepBack) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Step Back", tint = Color.White)
            }
            IconButton(
                onClick = onTogglePlayPause,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onStepForward) {
                Icon(Icons.Default.SkipNext, contentDescription = "Step Forward", tint = Color.White)
            }
            IconButton(onClick = onToggleLoop) {
                Icon(
                    Icons.Default.Loop,
                    contentDescription = "Loop",
                    tint = if (isLooping) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }

        // Right Actions (Scrollable toolbar)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSplitClip, enabled = hasSelectedClip) {
                Icon(
                    Icons.Default.ContentCut,
                    contentDescription = "Split Clip",
                    tint = if (hasSelectedClip) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onDuplicateClip, enabled = hasSelectedClip) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = "Duplicate Clip",
                    tint = if (hasSelectedClip) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onRotateClip, enabled = hasSelectedClip) {
                Icon(
                    Icons.AutoMirrored.Filled.RotateRight,
                    contentDescription = "Rotate",
                    tint = if (hasSelectedClip) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onFlipClip, enabled = hasSelectedClip) {
                Icon(
                    Icons.Default.Flip,
                    contentDescription = "Flip Horizontal",
                    tint = if (hasSelectedClip) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onFilterClick, enabled = hasSelectedClip) {
                Icon(
                    Icons.Default.Filter,
                    contentDescription = "Filters",
                    tint = if (hasSelectedClip) Color.White else Color.Gray
                )
            }
            IconButton(onClick = onAddText) {
                Icon(Icons.Default.TextFields, contentDescription = "Add Text", tint = Color.White)
            }
            IconButton(onClick = onToggleGuides) {
                Icon(
                    Icons.Default.GridOn,
                    contentDescription = "Guides",
                    tint = if (showSafeGuides) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
            IconButton(onClick = onDeleteClip, enabled = hasSelectedClip) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Clip",
                    tint = if (hasSelectedClip) Color.Red else Color.Gray
                )
            }
        }
    }
}
