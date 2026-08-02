package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.TrashItem
import com.gallery.app.core.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrashItemsUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {
    operator fun invoke(): Flow<List<TrashItem>> {
        return trashRepository.getTrashItems()
    }
}
