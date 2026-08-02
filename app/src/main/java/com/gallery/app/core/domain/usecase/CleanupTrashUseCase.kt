package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.repository.TrashRepository
import javax.inject.Inject

class CleanupTrashUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {
    suspend operator fun invoke(): Int {
        return trashRepository.cleanupExpiredTrash()
    }
}
