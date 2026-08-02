package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.TrashRepository
import javax.inject.Inject

class MoveToTrashUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {
    suspend operator fun invoke(mediaItem: MediaItem): Boolean {
        return trashRepository.moveToTrash(mediaItem)
    }
}
