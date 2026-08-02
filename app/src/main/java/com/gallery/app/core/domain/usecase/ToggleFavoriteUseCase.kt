package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.FavoritesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(mediaItem: MediaItem): Boolean {
        return favoritesRepository.toggleFavorite(mediaItem)
    }
}
