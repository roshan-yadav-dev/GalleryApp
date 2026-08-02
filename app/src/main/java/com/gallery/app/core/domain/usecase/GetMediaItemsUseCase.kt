package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaItemsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(filterOptions: FilterOptions = FilterOptions()): Flow<List<MediaItem>> {
        return mediaRepository.getMediaItems(filterOptions)
    }
}
