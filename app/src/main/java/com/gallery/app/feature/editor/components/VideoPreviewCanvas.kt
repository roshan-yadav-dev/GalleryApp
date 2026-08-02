package com.gallery.app.feature.editor.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.gallery.app.core.domain.model.editor.OverlayType
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import kotlin.math.roundToInt

@OptIn(UnstableApi::class)
@Composable
fun VideoPreviewCanvas(
    exoPlayer: ExoPlayer?,
    timelineState: TimelineState,
    onTogglePlayPause: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var previewScale by remember { mutableFloatStateOf(1.0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTogglePlayPause() },
                    onDoubleTap = { previewScale = 1.0f }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    previewScale = (previewScale * zoom).coerceIn(0.5f, 3.0f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(previewScale)
        ) {
            if (exoPlayer != null) {
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
                    update = { view ->
                        if (view.player != exoPlayer) {
                            view.player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Live Text & Overlay Rendering based on current playhead
            val currentPlayhead = timelineState.playheadMs
            val activeClips = timelineState.tracks.flatMap { it.clips }.filter {
                currentPlayhead in it.timelineStartMs..it.timelineEndMs
            }.sortedBy { it.transformConfig?.layerIndex ?: 0 }

            activeClips.forEach { clip ->
                val transform = clip.transformConfig
                val textCfg = clip.textConfig

                if (clip.overlayType == OverlayType.TEXT || textCfg != null) {
                    val textContent = textCfg?.text ?: "Text Overlay"
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textContent,
                            color = Color.White,
                            fontSize = (textCfg?.fontSizeSp ?: 24).sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .rotate(transform?.rotationDeg ?: 0f)
                                .scale(transform?.scale ?: 1f)
                                .alpha(transform?.opacity ?: 1f)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Safe Area Guides Overlay
            if (timelineState.showSafeGuides) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .border(1.dp, Color.Cyan.copy(alpha = 0.5f))
                )
            }
        }
    }
}
