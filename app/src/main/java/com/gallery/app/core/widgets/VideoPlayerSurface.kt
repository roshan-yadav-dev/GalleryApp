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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private object ThumbnailCache {
    val cache = LruCache<String, List<Bitmap>>(10)
}

@Composable
fun VideoPlayerSurface(
    videoUri: Uri,
    isActive: Boolean = true,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var showControls by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }

    // Overlay Text
    var overlayText by remember { mutableStateOf("") }
    var showTextEntry by remember { mutableStateOf(false) }
    var activeTextStyleIndex by remember { mutableStateOf(0) }

    // Filmstrip Thumbnails
    val thumbnails = remember { mutableStateListOf<Bitmap>() }
    var stripWidthPx by remember { mutableFloatStateOf(1f) }
    var isScrubbing by remember { mutableStateOf(false) }

    // Optimized Low-Latency ExoPlayer Instance
    val exoPlayer = remember {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(1000, 5000, 500, 1000)
            .build()

        ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                setMediaItem(MediaItem.fromUri(videoUri))
                setSeekParameters(SeekParameters.CLOSEST_SYNC)
                repeatMode = Player.REPEAT_MODE_ONE
                prepare()
            }
    }

    // Dynamic Play/Pause state depending on active page state
    LaunchedEffect(isActive, isPlaying) {
        if (isActive && isPlaying) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.playWhenReady = false
            exoPlayer.pause()
        }
    }

    DisposableEffect(videoUri) {
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

    // Position sync loop (runs only when active and playing)
    LaunchedEffect(isActive, isPlaying, isScrubbing) {
        if (isActive && isPlaying && !isScrubbing) {
            while (true) {
                currentPosition = exoPlayer.currentPosition.coerceAtLeast(0L)
                delay(60)
            }
        }
    }

    // Asynchronous Cached Thumbnail Extraction
    LaunchedEffect(videoUri, duration, isActive) {
        if (isActive && duration > 0) {
            val cacheKey = videoUri.toString()
            val cachedList = ThumbnailCache.cache.get(cacheKey)
            if (cachedList != null) {
                thumbnails.clear()
                thumbnails.addAll(cachedList)
            } else {
                withContext(Dispatchers.Default) {
                    val list = mutableListOf<Bitmap>()
                    val retriever = MediaMetadataRetriever()
                    try {
                        retriever.setDataSource(context, videoUri)
                        val count = 10
                        val interval = duration / count
                        for (i in 0 until count) {
                            val timeUs = (i * interval) * 1000L
                            val bmp = if (android.os.Build.VERSION.SDK_INT >= 27) {
                                retriever.getScaledFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC, 80, 60)
                            } else {
                                retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                            }
                            if (bmp != null) list.add(bmp)
                        }
                    } catch (_: Exception) {
                    } finally {
                        try { retriever.release() } catch (_: Exception) {}
                    }
                    if (list.isNotEmpty()) {
                        ThumbnailCache.cache.put(cacheKey, list)
                        withContext(Dispatchers.Main) {
                            thumbnails.clear()
                            thumbnails.addAll(list)
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                showControls = !showControls
                onTap()
            }
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

        // Overlay Controls Layer
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                // Play / Pause Central Button
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
                        .size(64.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Text Overlay Controls Bar
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 115.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showTextEntry) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
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
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (showTextEntry) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.6f))
                                .clickable { showTextEntry = !showTextEntry },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FormatSize, contentDescription = "Add Text", tint = Color.White, modifier = Modifier.size(18.dp))
                        }

                        val styleLabels = listOf("Aa", "AI", "Aa", "A")
                        styleLabels.forEachIndexed { index, label ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (activeTextStyleIndex == index) Color.White else Color.Black.copy(alpha = 0.6f))
                                    .border(
                                        width = if (activeTextStyleIndex == index) 2.dp else 1.dp,
                                        color = if (activeTextStyleIndex == index) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                                    .clickable { activeTextStyleIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (activeTextStyleIndex == index) Color.Black else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Bottom Inline Frame Filmstrip Scrubber Container
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
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

                    val progressRatio = if (duration > 0) (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF141418))
                            .border(2.dp, Color(0xFFFFCC00), RoundedCornerShape(8.dp))
                            .onGloballyPositioned { coords ->
                                stripWidthPx = coords.size.width.toFloat().coerceAtLeast(1f)
                            }
                            .pointerInput(duration) {
                                detectHorizontalDragGestures(
                                    onDragStart = { offset ->
                                        isScrubbing = true
                                        exoPlayer.setSeekParameters(SeekParameters.CLOSEST_SYNC)
                                        if (duration > 0 && stripWidthPx > 0) {
                                            val fraction = (offset.x / stripWidthPx).coerceIn(0f, 1f)
                                            val newPos = (fraction * duration).toLong()
                                            exoPlayer.seekTo(newPos)
                                            currentPosition = newPos
                                        }
                                    },
                                    onDragEnd = {
                                        isScrubbing = false
                                        exoPlayer.setSeekParameters(SeekParameters.EXACT)
                                    },
                                    onDragCancel = {
                                        isScrubbing = false
                                        exoPlayer.setSeekParameters(SeekParameters.EXACT)
                                    },
                                    onHorizontalDrag = { change, _ ->
                                        if (duration > 0 && stripWidthPx > 0) {
                                            val fraction = (change.position.x / stripWidthPx).coerceIn(0f, 1f)
                                            val newPos = (fraction * duration).toLong()
                                            exoPlayer.seekTo(newPos)
                                            currentPosition = newPos
                                        }
                                    }
                                )
                            }
                    ) {
                        if (thumbnails.isNotEmpty()) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                thumbnails.forEach { bmp ->
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = "Video Frame",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    )
                                }
                            }
                        } else {
                            Row(modifier = Modifier.fillMaxSize()) {
                                repeat(10) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(if (it % 2 == 0) Color(0xFF22222A) else Color(0xFF1A1A20))
                                    )
                                }
                            }
                        }

                        // Filmstrip Yellow Trim Highlight Border
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(2.dp, Color(0xFFFFCC00), RoundedCornerShape(8.dp))
                        )

                        // RenderThread / GPU-Accelerated Playhead Indicator Line
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 2.dp)
                                .graphicsLayer {
                                    translationX = progressRatio * (stripWidthPx - 20f)
                                }
                                .padding(vertical = 2.dp)
                                .background(Color.White, RoundedCornerShape(2.dp))
                                .border(1.dp, Color.Red, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}
