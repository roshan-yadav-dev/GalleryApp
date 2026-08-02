package com.gallery.app.feature.editor

import android.net.Uri
import com.gallery.app.core.domain.model.editor.ClipType
import com.gallery.app.core.domain.model.editor.EditorClip
import com.gallery.app.core.domain.model.editor.EditorTrack
import com.gallery.app.core.domain.model.editor.TimelineState
import com.gallery.app.core.domain.model.editor.TrackType
import com.gallery.app.core.editor.history.TimelineHistoryManager
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VideoEditorViewModelTest {

    private lateinit var historyManager: TimelineHistoryManager
    private val mockUri = mockk<Uri>(relaxed = true)

    @Before
    fun setUp() {
        historyManager = TimelineHistoryManager()
    }

    @Test
    fun `test TimelineHistoryManager undo and redo stacks`() {
        val state1 = TimelineState(playheadMs = 100L)
        val state2 = TimelineState(playheadMs = 500L)

        historyManager.pushState(state1)
        assertFalse(historyManager.canUndo())

        historyManager.pushState(state2)
        assertTrue(historyManager.canUndo())
        assertFalse(historyManager.canRedo())

        val undoneState = historyManager.undo(state2)
        assertEquals(state1, undoneState)
        assertTrue(historyManager.canRedo())

        val redoneState = historyManager.redo()
        assertEquals(state2, redoneState)
    }

    @Test
    fun `test clip duration calculation`() {
        val clip = EditorClip(
            id = "clip_1",
            trackId = "track_1",
            type = ClipType.VIDEO,
            sourceUri = mockUri,
            sourceDurationMs = 10000L,
            timelineStartMs = 1000L,
            timelineEndMs = 6000L,
            trimStartMs = 0L,
            trimEndMs = 5000L
        )

        assertEquals(5000L, clip.durationMs)
        assertEquals(5000L, clip.trimDurationMs)
    }

    @Test
    fun `test timeline state overall duration calculation`() {
        val clip1 = EditorClip(
            id = "clip_1",
            trackId = "track_1",
            type = ClipType.VIDEO,
            sourceUri = mockUri,
            sourceDurationMs = 10000L,
            timelineStartMs = 0L,
            timelineEndMs = 8000L
        )

        val track = EditorTrack(
            id = "track_1",
            type = TrackType.VIDEO,
            title = "Video Track",
            clips = listOf(clip1)
        )

        val state = TimelineState(tracks = listOf(track))
        assertEquals(8000L, state.durationMs)
    }
}
