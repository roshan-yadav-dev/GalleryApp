package com.gallery.app.core.editor.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoEditorPlayerManager @Inject constructor(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var currentUri: Uri? = null

    private val _playheadPositionMs = MutableStateFlow(0L)
    val playheadPositionMs: StateFlow<Long> = _playheadPositionMs.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var positionPollerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    fun initializePlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                setSeekParameters(SeekParameters.EXACT)
                repeatMode = Player.REPEAT_MODE_ONE
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        _isPlaying.value = playing
                        if (playing) {
                            startPositionPoller()
                        } else {
                            stopPositionPoller()
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            _isPlaying.value = false
                            stopPositionPoller()
                        }
                    }
                })
            }
        }
        return exoPlayer!!
    }

    fun prepareSource(uri: Uri) {
        val player = initializePlayer()
        if (currentUri != uri) {
            currentUri = uri
            val mediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)
            player.prepare()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.let { player ->
            player.seekTo(positionMs.coerceAtLeast(0L))
            _playheadPositionMs.value = positionMs
        }
    }

    fun stepFrame(forward: Boolean) {
        exoPlayer?.let { player ->
            val delta = if (forward) 40L else -40L // ~25 fps frame step
            val newPos = (player.currentPosition + delta).coerceAtLeast(0L)
            player.seekTo(newPos)
            _playheadPositionMs.value = newPos
        }
    }

    fun setLooping(enabled: Boolean) {
        exoPlayer?.repeatMode = if (enabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }

    fun play() {
        exoPlayer?.playWhenReady = true
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    fun togglePlayPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                pause()
            } else {
                play()
            }
        }
    }

    fun setSpeed(speed: Float) {
        exoPlayer?.setPlaybackSpeed(speed.coerceIn(0.25f, 4.0f))
    }

    fun setVolume(volume: Float) {
        exoPlayer?.volume = volume.coerceIn(0f, 1f)
    }

    private fun startPositionPoller() {
        stopPositionPoller()
        positionPollerJob = scope.launch {
            while (true) {
                exoPlayer?.let { player ->
                    if (player.isPlaying) {
                        _playheadPositionMs.value = player.currentPosition
                    }
                }
                delay(33L) // ~30 fps update
            }
        }
    }

    private fun stopPositionPoller() {
        positionPollerJob?.cancel()
        positionPollerJob = null
    }

    fun release() {
        stopPositionPoller()
        exoPlayer?.release()
        exoPlayer = null
        currentUri = null
    }
}
