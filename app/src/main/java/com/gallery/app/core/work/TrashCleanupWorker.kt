package com.gallery.app.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gallery.app.core.domain.usecase.CleanupTrashUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class TrashCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cleanupTrashUseCase: CleanupTrashUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val cleanedCount = cleanupTrashUseCase()
            Timber.d("TrashCleanupWorker: Cleaned up $cleanedCount expired trash items.")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "TrashCleanupWorker failed")
            Result.retry()
        }
    }
}
