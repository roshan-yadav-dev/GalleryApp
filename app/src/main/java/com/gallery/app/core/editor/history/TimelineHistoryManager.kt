package com.gallery.app.core.editor.history

import com.gallery.app.core.domain.model.editor.TimelineState
import java.util.ArrayDeque
import javax.inject.Inject

class TimelineHistoryManager @Inject constructor() {
    private val undoStack = ArrayDeque<TimelineState>()
    private val redoStack = ArrayDeque<TimelineState>()
    private val maxHistorySize = 30

    fun pushState(state: TimelineState) {
        if (undoStack.isNotEmpty() && undoStack.peek() == state) {
            return
        }
        undoStack.push(state)
        if (undoStack.size > maxHistorySize) {
            undoStack.removeLast()
        }
        redoStack.clear()
    }

    fun canUndo(): Boolean = undoStack.size > 1

    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun undo(currentState: TimelineState): TimelineState? {
        if (!canUndo()) return null
        val current = undoStack.pop()
        redoStack.push(current)
        return undoStack.peek()
    }

    fun redo(): TimelineState? {
        if (!canRedo()) return null
        val state = redoStack.pop()
        undoStack.push(state)
        return state
    }

    fun clear() {
        undoStack.clear()
        redoStack.clear()
    }
}
