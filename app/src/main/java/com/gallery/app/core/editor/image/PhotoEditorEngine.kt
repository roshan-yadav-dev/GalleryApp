package com.gallery.app.core.editor.image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.gallery.app.core.common.DispatcherProvider
import com.gallery.app.core.domain.model.editor.ImageFilterType
import com.gallery.app.core.domain.model.editor.ImageEditorState
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoEditorEngine @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider
) {

    suspend fun loadBitmap(uri: Uri, maxDimension: Int = 1920): Bitmap? = withContext(dispatchers.io) {
        try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeStream(stream, null, options)
                
                var inSampleSize = 1
                val (w, h) = options.outWidth to options.outHeight
                while (w / inSampleSize > maxDimension || h / inSampleSize > maxDimension) {
                    inSampleSize *= 2
                }

                val decodeOptions = BitmapFactory.Options().apply {
                    this.inSampleSize = inSampleSize
                }

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream, null, decodeOptions)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to load bitmap from uri: %s", uri)
            null
        }
    }

    suspend fun applyEdits(
        sourceBitmap: Bitmap,
        state: ImageEditorState
    ): Bitmap = withContext(dispatchers.default) {
        val matrix = Matrix()

        // Apply Rotations and Flips
        if (state.rotationDegrees != 0f) {
            matrix.postRotate(state.rotationDegrees)
        }
        val sx = if (state.flipHorizontal) -1f else 1f
        val sy = if (state.flipVertical) -1f else 1f
        if (sx != 1f || sy != 1f) {
            matrix.postScale(sx, sy)
        }

        var transformed = Bitmap.createBitmap(
            sourceBitmap, 0, 0, sourceBitmap.width, sourceBitmap.height, matrix, true
        )

        // Apply Adjustments & Color Filter via ColorMatrix
        val cm = ColorMatrix()

        // 1. Saturation
        if (state.saturation != 1f) {
            cm.setSaturation(state.saturation)
        }

        // 2. Brightness & Contrast
        val contrastCm = ColorMatrix(floatArrayOf(
            state.contrast, 0f, 0f, 0f, state.brightness * 255f,
            0f, state.contrast, 0f, 0f, state.brightness * 255f,
            0f, 0f, state.contrast, 0f, state.brightness * 255f,
            0f, 0f, 0f, 1f, 0f
        ))
        cm.postConcat(contrastCm)

        // 3. Preset Color Filters
        val filterCm = getFilterColorMatrix(state.activeFilter)
        if (filterCm != null) {
            cm.postConcat(filterCm)
        }

        val resultBitmap = Bitmap.createBitmap(
            transformed.width, transformed.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(resultBitmap)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }
        canvas.drawBitmap(transformed, 0f, 0f, paint)

        if (transformed != sourceBitmap && transformed != resultBitmap) {
            transformed.recycle()
        }

        resultBitmap
    }

    suspend fun saveEditedImage(
        editedBitmap: Bitmap,
        outputFileName: String = "Edited_${System.currentTimeMillis()}.jpg"
    ): Uri? = withContext(dispatchers.io) {
        try {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, outputFileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GalleryAPP_Edited")
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let { destUri ->
                context.contentResolver.openOutputStream(destUri)?.use { outStream ->
                    editedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, outStream)
                }
            }
            uri
        } catch (e: Exception) {
            Timber.e(e, "Error saving edited photo to MediaStore")
            null
        }
    }

    private fun getFilterColorMatrix(filter: ImageFilterType): ColorMatrix? {
        return when (filter) {
            ImageFilterType.NONE -> null
            ImageFilterType.GRAYSCALE -> ColorMatrix().apply { setSaturation(0f) }
            ImageFilterType.SEPIA -> ColorMatrix(floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
            ImageFilterType.VINTAGE -> ColorMatrix(floatArrayOf(
                0.9f, 0f, 0f, 0f, 20f,
                0f, 0.7f, 0f, 0f, 15f,
                0f, 0f, 0.5f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            ))
            ImageFilterType.WARM -> ColorMatrix(floatArrayOf(
                1.2f, 0f, 0f, 0f, 30f,
                0f, 1.0f, 0f, 0f, 10f,
                0f, 0f, 0.8f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
            ImageFilterType.COOL -> ColorMatrix(floatArrayOf(
                0.8f, 0f, 0f, 0f, 0f,
                0f, 1.0f, 0f, 0f, 10f,
                0f, 0f, 1.2f, 0f, 30f,
                0f, 0f, 0f, 1f, 0f
            ))
            ImageFilterType.INVERT -> ColorMatrix(floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
    }
}
