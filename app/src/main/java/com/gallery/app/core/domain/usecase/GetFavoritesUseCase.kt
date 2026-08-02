package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> {
        return favoritesRepository.getFavoriteMediaItems()
    }
}
