package com.gallery.app.core.editor.engine

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GestureEngine @Inject constructor() {

    fun calculateSnapOffset(
        currentMs: Long,
        snapTargetsMs: List<Long>,
        snapThresholdMs: Long = 150L
    ): Long {
        var closestTarget = currentMs
        var minDiff = Long.MAX_VALUE

        for (target in snapTargetsMs) {
            val diff = Math.abs(currentMs - target)
            if (diff < minDiff && diff <= snapThresholdMs) {
                minDiff = diff
                closestTarget = target
            }
        }
        return closestTarget
    }

    fun calculatePinchZoom(currentZoom: Float, zoomFactor: Float): Float {
        return (currentZoom * zoomFactor).coerceIn(15f, 300f)
    }
}
