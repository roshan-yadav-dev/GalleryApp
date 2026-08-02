package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.model.MediaMetadata
import com.gallery.app.core.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaDetailsUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(item: MediaItem): MediaMetadata {
        return mediaRepository.getMediaMetadata(item)
    }
}
