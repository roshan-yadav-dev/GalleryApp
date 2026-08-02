package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.TrashItem
import com.gallery.app.core.domain.repository.TrashRepository
import javax.inject.Inject

class RestoreFromTrashUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {
    suspend operator fun invoke(trashItem: TrashItem): Boolean {
        return trashRepository.restoreFromTrash(trashItem)
    }
}
