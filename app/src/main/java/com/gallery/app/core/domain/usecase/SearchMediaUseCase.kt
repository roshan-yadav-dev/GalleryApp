package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(query: String): Flow<List<MediaItem>> {
        return mediaRepository.getMediaItems(FilterOptions(searchQuery = query))
    }
}
