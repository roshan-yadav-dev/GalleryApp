package com.gallery.app.core.domain.usecase

import com.gallery.app.core.domain.model.Album
import com.gallery.app.core.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    operator fun invoke(): Flow<List<Album>> {
        return albumRepository.getAlbums()
    }
}
