package com.gallery.app.domain.usecase

import android.net.Uri
import com.gallery.app.core.domain.model.FilterOptions
import com.gallery.app.core.domain.model.MediaItem
import com.gallery.app.core.domain.repository.MediaRepository
import com.gallery.app.core.domain.usecase.GetMediaItemsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMediaItemsUseCaseTest {

    private val mediaRepository: MediaRepository = mockk()
    private lateinit var useCase: GetMediaItemsUseCase

    @Before
    fun setUp() {
        useCase = GetMediaItemsUseCase(mediaRepository)
    }

    @Test
    fun `invoke returns media items list from repository`() = runTest {
        val dummyUri: Uri = mockk()
        val dummyItems = listOf(
            MediaItem(
                id = 1L,
                uri = dummyUri,
                path = "/storage/emulated/0/DCIM/Camera/IMG_001.jpg",
                displayName = "IMG_001.jpg",
                size = 2048L,
                mimeType = "image/jpeg",
                dateAdded = 1000L,
                dateModified = 1000L
            )
        )

        val options = FilterOptions()
        coEvery { mediaRepository.getMediaItems(options) } returns flowOf(dummyItems)

        val result = useCase(options).first()

        assertEquals(1, result.size)
        assertEquals("IMG_001.jpg", result[0].displayName)
    }
}
