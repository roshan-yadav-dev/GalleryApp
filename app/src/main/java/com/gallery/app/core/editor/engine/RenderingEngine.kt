package com.gallery.app.core.editor.engine

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import com.gallery.app.core.domain.model.editor.ColorAdjustmentConfig
import com.gallery.app.core.domain.model.editor.VideoFilterType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RenderingEngine @Inject constructor() {

    fun createColorFilter(
        filterType: VideoFilterType,
        adjustments: ColorAdjustmentConfig
    ): ColorMatrixColorFilter {
        val cm = ColorMatrix()

        // 1. Saturation
        if (adjustments.saturation != 1.0f) {
            cm.setSaturation(adjustments.saturation)
        }

        // 2. Brightness & Contrast
        val contrastCm = ColorMatrix(
            floatArrayOf(
                adjustments.contrast, 0f, 0f, 0f, adjustments.brightness * 255f,
                0f, adjustments.contrast, 0f, 0f, adjustments.brightness * 255f,
                0f, 0f, adjustments.contrast, 0f, adjustments.brightness * 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        cm.postConcat(contrastCm)

        // 3. Preset Filter Matrix
        getFilterColorMatrix(filterType)?.let { filterCm ->
            cm.postConcat(filterCm)
        }

        return ColorMatrixColorFilter(cm)
    }

    private fun getFilterColorMatrix(filterType: VideoFilterType): ColorMatrix? {
        return when (filterType) {
            VideoFilterType.NONE -> null
            VideoFilterType.GRAYSCALE -> ColorMatrix().apply { setSaturation(0f) }
            VideoFilterType.SEPIA -> ColorMatrix(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            VideoFilterType.VINTAGE -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 20f,
                    0f, 0.7f, 0f, 0f, 15f,
                    0f, 0f, 0.5f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            VideoFilterType.WARM -> ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 30f,
                    0f, 1.0f, 0f, 0f, 10f,
                    0f, 0f, 0.8f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            VideoFilterType.COOL -> ColorMatrix(
                floatArrayOf(
                    0.8f, 0f, 0f, 0f, 0f,
                    0f, 1.0f, 0f, 0f, 10f,
                    0f, 0f, 1.2f, 0f, 30f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            VideoFilterType.INVERT -> ColorMatrix(
                floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            VideoFilterType.BLACK_WHITE -> ColorMatrix().apply { setSaturation(0f) }
            VideoFilterType.VIBRANT -> ColorMatrix().apply { setSaturation(1.4f) }
        }
    }
}
