package com.gallery.app.core.widgets

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.LruCache
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.ui.PlayerView
import com.gallery.app.core.common.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val thumbnailCache = LruCache<String, Bitmap>(30)

@Composable
fun VideoPlayerSurface(
    videoUri: Uri,
    isActive: Boolean,
    onTap: () -> Unit,
    showFilmstripFrames: Boolean = false,
    onToggleFilmstrip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var showControls by remember { mutableStateOf(true) }
    var isScrubbing by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }

    // Text Overlay States
    var overlayText by remember { mutableStateOf("") }
    var showTextEntry by remember { mutableStateOf(false) }
    var activeTextStyleIndex by remember { mutableStateOf(0) }

    // Scrubber track container width
    var containerWidthPx by remember { mutableFloatStateOf(0f) }

    // Filmstrip Frame Thumbnails list
    val filmstripThumbnails = remember { mutableStateListOf<Bitmap>() }

    // Low-Latency ExoPlayer initialization with 500ms min buffer
    val exoPlayer = remember {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(500, 1500, 250, 500)
            .build()

        ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = isActive
            }
    }

    // Dynamic Frame Extraction for Scrubbing Filmstrip
    LaunchedEffect(videoUri, showFilmstripFrames) {
        if (!showFilmstripFrames) return@LaunchedEffect
        withContext(Dispatchers.IO) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, videoUri)
                val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durMs = durationStr?.toLongOrNull() ?: 1000L

                val frameCount = 10
                val intervalUs = (durMs * 1000L) / frameCount

                val extracted = mutableListOf<Bitmap>()
                for (i in 0 until frameCount) {
                    val timeUs = i * intervalUs
                    val cacheKey = "${videoUri}_$timeUs"
                    var bmp = thumbnailCache.get(cacheKey)
                    if (bmp == null) {
                        bmp = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                        if (bmp != null) {
                            thumbnailCache.put(cacheKey, bmp)
                        }
                    }
                    if (bmp != null) {
                        extracted.add(bmp)
                    }
                }
                retriever.release()

                withContext(Dispatchers.Main) {
                    filmstripThumbnails.clear()
                    filmstripThumbnails.addAll(extracted)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Active page playback control
    LaunchedEffect(isActive) {
        if (isActive) {
            exoPlayer.playWhenReady = true
        } else {
            exoPlayer.playWhenReady = false
            exoPlayer.pause()
        }
    }

    // Continuous Position Tracker Listener
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    duration = exoPlayer.duration.coerceAtLeast(0L)
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Smooth UI Position Polling Loop
    LaunchedEffect(isPlaying, isScrubbing) {
        while (isPlaying && !isScrubbing) {
            currentPosition = exoPlayer.currentPosition.coerceAtLeast(0L)
            kotlinx.coroutines.delay(16) // ~60fps smooth sync
        }
    }

    // Root Non-Overlapping Vertical Layout: Preview on top, Scrubber on bottom
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // TOP PREVIEW CANVAS (Dynamically scales inside weight(1f))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Player Surface View
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Transparent Gesture Overlay Box covering the video canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                showControls = !showControls
                                onTap()
                            },
                            onLongPress = {
                                onToggleFilmstrip()
                            }
                        )
                    }
            )

            // Text Overlay Canvas
            if (overlayText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                        .background(
                            when (activeTextStyleIndex) {
                                1 -> Color.Black.copy(alpha = 0.6f)
                                2 -> Color.White.copy(alpha = 0.85f)
                                else -> Color.Transparent
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = overlayText,
                        color = if (activeTextStyleIndex == 2) Color.Black else Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Central Play / Pause Quick Action
            if (showControls) {
                IconButton(
                    onClick = {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Floating Hold-for-2s Hint Badge
            if (!showFilmstripFrames) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Hold 2s for frame timeline",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // BOTTOM TIMELINE & SCRUBBER SECTION (Non-Overlapping)
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .padding(bottom = 8.dp, top = 4.dp)
            ) {
                // Text Overlay Entry Bar
                if (showTextEntry) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .background(Color(0xFF1E1E24), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = overlayText,
                            onValueChange = { overlayText = it },
                            placeholder = { Text("Add text overlay...", color = Color.Gray) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        IconButton(onClick = { showTextEntry = false }) {
                            Text("Done", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Controls Row: Play/Pause Time & Volume & Text Style Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF2A2D34), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                                }
                        )
                        Text(
                            text = "${DateFormatter.formatDuration(currentPosition)} / ${DateFormatter.formatDuration(duration)}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showTextEntry = !showTextEntry },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Add Text",
                                tint = if (showTextEntry) MaterialTheme.colorScheme.primary else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                isMuted = !isMuted
                                exoPlayer.volume = if (isMuted) 0f else 1f
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Mute",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                val progressRatio = if (duration > 0) (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f

                // CONDITION: Video Frames only appear when user long-presses for 2 seconds
                if (showFilmstripFrames) {
                    val trackWidthPx = maxOf(containerWidthPx * 1.5f, 600f)
                    val density = LocalDensity.current
                    val trackWidthDp = with(density) { trackWidthPx.toDp() }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .onGloballyPositioned { coords ->
                                containerWidthPx = coords.size.width.toFloat().coerceAtLeast(1f)
                            }
                            .pointerInput(duration, containerWidthPx) {
                                detectHorizontalDragGestures(
                                    onDragStart = {
                                        isScrubbing = true
                                        exoPlayer.setSeekParameters(SeekParameters.CLOSEST_SYNC)
                                    },
                                    onDragEnd = {
                                        isScrubbing = false
                                        exoPlayer.setSeekParameters(SeekParameters.EXACT)
                                    },
                                    onDragCancel = {
                                        isScrubbing = false
                                        exoPlayer.setSeekParameters(SeekParameters.EXACT)
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        if (duration > 0 && trackWidthPx > 0) {
                                            val currentRatio = (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                                            val deltaRatio = -dragAmount / trackWidthPx
                                            val newRatio = (currentRatio + deltaRatio).coerceIn(0f, 1f)
                                            val newPos = (newRatio * duration).toLong()
                                            exoPlayer.seekTo(newPos)
                                            currentPosition = newPos
                                        }
                                    }
                                )
                            }
                    ) {
                        // Filmstrip Track
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(trackWidthDp)
                                .graphicsLayer {
                                    val halfViewport = containerWidthPx / 2f
                                    translationX = halfViewport - (progressRatio * trackWidthPx)
                                }
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                ) {
                                    val totalTicks = 60
                                    val spacing = size.width / totalTicks
                                    for (i in 0..totalTicks) {
                                        val x = i * spacing
                                        val isMajor = i % 5 == 0
                                        val tickHeight = if (isMajor) size.height * 0.85f else size.height * 0.45f
                                        val color = if (isMajor) Color.White else Color.White.copy(alpha = 0.45f)
                                        val strokeWidth = if (isMajor) 2.dp.toPx() else 1.dp.toPx()

                                        drawLine(
                                            color = color,
                                            start = Offset(x, size.height),
                                            end = Offset(x, size.height - tickHeight),
                                            strokeWidth = strokeWidth
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .border(1.dp, Color.Yellow, RoundedCornerShape(4.dp))
                                ) {
                                    if (filmstripThumbnails.isNotEmpty()) {
                                        filmstripThumbnails.forEach { bmp ->
                                            Image(
                                                bitmap = bmp.asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.DarkGray)
                                        )
                                    }
                                }
                            }
                        }

                        // Stationary Center Playhead Indicator
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(3.dp)
                                .fillMaxHeight()
                                .background(Color.Magenta)
                        )
                    }
                } else {
                    // Standard Slim Progress Bar View
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
