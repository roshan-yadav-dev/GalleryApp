package com.gallery.app.feature.editor.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallery.app.core.common.DateFormatter
import com.gallery.app.core.domain.model.editor.ClipType
import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import com.gallery.app.core.editor.thumbnail.FrameThumbnailManager

@Composable
fun TimelineView(
    timelineState: TimelineState,
    thumbnailManager: FrameThumbnailManager,
    onSeekTo: (Long) -> Unit,
    onSelectClip: (String?) -> Unit,
    onTrimClip: (Long, Long) -> Unit,
    onZoomChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val zoomPxPerSec = timelineState.zoomPxPerSec
    val totalDurationMs = timelineState.durationMs
    val timelineWidthDp = ((totalDurationMs / 1000f) * zoomPxPerSec).dp.coerceAtLeast(300.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24))
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    if (zoom != 1.0f) {
                        onZoomChange(zoomPxPerSec * zoom)
                    }
                }
            }
    ) {
        // Time Ruler Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .background(Color(0xFF141418))
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .width(timelineWidthDp)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val numIntervals = (totalDurationMs / 1000L).toInt().coerceAtLeast(1)
                val stepSec = when {
                    zoomPxPerSec < 30f -> 10
                    zoomPxPerSec < 80f -> 5
                    else -> 1
                }

                for (sec in 0..numIntervals step stepSec) {
                    val stepWidthDp = (stepSec * zoomPxPerSec).dp
                    Box(
                        modifier = Modifier
                            .width(stepWidthDp)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(10.dp)
                                .background(Color.Gray)
                                .align(Alignment.BottomStart)
                        )
                        Text(
                            text = DateFormatter.formatDuration(sec * 1000L),
                            color = Color.LightGray,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 2.dp)
                        )
                    }
                }
            }
        }

        // Multi-track Canvas Container with Draggable Playhead
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Horizontal Track Scroll Area
            Column(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .width(timelineWidthDp)
                    .fillMaxHeight()
                    .padding(vertical = 4.dp)
            ) {
                timelineState.tracks.forEach { track ->
                    TrackRow(
                        trackTitle = track.title,
                        trackType = track.type,
                        clips = track.clips,
                        selectedClipId = timelineState.selectedClipId,
                        zoomPxPerSec = zoomPxPerSec,
                        thumbnailManager = thumbnailManager,
                        onSelectClip = onSelectClip,
                        onTrimClip = onTrimClip,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 2.dp)
                    )
                }
            }

            // Draggable Playhead Line
            val playheadOffsetDp = ((timelineState.playheadMs / 1000f) * zoomPxPerSec).dp - (scrollState.value / 2.7f).dp
            val playheadClampedOffset = playheadOffsetDp.coerceAtLeast(0.dp)

            Box(
                modifier = Modifier
                    .offset(x = playheadClampedOffset)
                    .width(24.dp)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val deltaMs = ((dragAmount.x / zoomPxPerSec) * 1000f).toLong()
                            onSeekTo((timelineState.playheadMs + deltaMs).coerceIn(0L, totalDurationMs))
                        }
                    }
            ) {
                // Handle Pin at top
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .align(Alignment.TopCenter)
                )
                // Red Vertical Line
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.Red)
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun TrackRow(
    trackTitle: String,
    trackType: TrackType,
    clips: List<EditorClip>,
    selectedClipId: String?,
    zoomPxPerSec: Float,
    thumbnailManager: FrameThumbnailManager,
    onSelectClip: (String?) -> Unit,
    onTrimClip: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackBg = when (trackType) {
        TrackType.VIDEO -> Color(0xFF2A2D34)
        TrackType.AUDIO -> Color(0xFF1E3A2B)
        TrackType.TEXT -> Color(0xFF3D2A1E)
        TrackType.OVERLAY -> Color(0xFF2E1E3D)
    }

    Box(
        modifier = modifier
            .background(trackBg, RoundedCornerShape(4.dp))
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
    ) {
        clips.forEach { clip ->
            val startDp = ((clip.timelineStartMs / 1000f) * zoomPxPerSec).dp
            val widthDp = ((clip.durationMs / 1000f) * zoomPxPerSec).dp.coerceAtLeast(20.dp)

            ClipItem(
                clip = clip,
                isSelected = clip.id == selectedClipId,
                widthDp = widthDp,
                thumbnailManager = thumbnailManager,
                onSelect = { onSelectClip(clip.id) },
                onTrimClip = onTrimClip,
                modifier = Modifier
                    .offset(x = startDp)
                    .width(widthDp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ClipItem(
    clip: EditorClip,
    isSelected: Boolean,
    widthDp: Dp,
    thumbnailManager: FrameThumbnailManager,
    onSelect: () -> Unit,
    onTrimClip: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipBg = when (clip.type) {
        ClipType.VIDEO -> Color(0xFF3F51B5)
        ClipType.AUDIO -> Color(0xFF4CAF50)
        ClipType.TEXT -> Color(0xFFFF9800)
        ClipType.OVERLAY -> Color(0xFF9C27B0)
    }

    val thumbnails = remember { mutableStateMapOf<Long, Bitmap?>() }

    LaunchedEffect(clip.sourceUri, clip.trimStartMs, clip.trimEndMs) {
        if (clip.type == ClipType.VIDEO && clip.sourceUri != Uri.EMPTY) {
            val stepMs = 2000L
            var timeMs = clip.trimStartMs
            while (timeMs <= clip.trimEndMs) {
                val bmp = thumbnailManager.getThumbnail(clip.sourceUri, timeMs, 100, 70)
                thumbnails[timeMs] = bmp
                timeMs += stepMs
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) clipBg.copy(alpha = 0.9f) else clipBg.copy(alpha = 0.6f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.Yellow else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onSelect() }
    ) {
        // Video Thumbnail Filmstrip
        if (clip.type == ClipType.VIDEO) {
            Row(modifier = Modifier.fillMaxSize()) {
                thumbnails.values.forEach { bmp ->
                    if (bmp != null) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(40.dp)
                                .fillMaxHeight()
                                .alpha(0.7f)
                        )
                    }
                }
            }
        }

        // Clip Title Label
        Text(
            text = when (clip.type) {
                ClipType.TEXT -> clip.textConfig?.text ?: "Text"
                ClipType.VIDEO -> "Video Clip"
                ClipType.AUDIO -> "Audio Clip"
                ClipType.OVERLAY -> "Overlay"
            },
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 6.dp)
        )

        // Draggable Trim Handles for Selected Clip
        if (isSelected) {
            // Left Trim Handle
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(Color.Yellow, RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .align(Alignment.CenterStart)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val deltaMs = (dragAmount.x * 20).toLong()
                            onTrimClip(clip.trimStartMs + deltaMs, clip.trimEndMs)
                        }
                    }
            )

            // Right Trim Handle
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(Color.Yellow, RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .align(Alignment.CenterEnd)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val deltaMs = (dragAmount.x * 20).toLong()
                            onTrimClip(clip.trimStartMs, clip.trimEndMs + deltaMs)
                        }
                    }
            )
        }
    }
}
