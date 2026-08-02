package com.gallery.app.domain.usecase

import android.net.Uri
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.FavoritesRepository
import com.gallery.app.core.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val favoritesRepository: FavoritesRepository = mockk()
    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        useCase = ToggleFavoriteUseCase(favoritesRepository)
    }

    @Test
    fun `invoke delegates to favorites repository toggle`() = runTest {
        val dummyUri: Uri = mockk()
        val dummyItem = MediaItem(
            id = 10L,
            uri = dummyUri,
            path = "/storage/emulated/0/DCIM/IMG_10.jpg",
            displayName = "IMG_10.jpg",
            size = 1024L,
            mimeType = "image/jpeg",
            dateAdded = 500L,
            dateModified = 500L
        )

        coEvery { favoritesRepository.toggleFavorite(dummyItem) } returns true

        val result = useCase(dummyItem)

        assertTrue(result)
        coVerify(exactly = 1) { favoritesRepository.toggleFavorite(dummyItem) }
    }
}
