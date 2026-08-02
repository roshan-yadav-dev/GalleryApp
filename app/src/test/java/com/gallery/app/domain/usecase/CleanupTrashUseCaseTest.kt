package com.gallery.app.domain.usecase

import com.gallery.app.core.domain.repository.TrashRepository
import com.gallery.app.core.domain.usecase.CleanupTrashUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CleanupTrashUseCaseTest {

    private val trashRepository: TrashRepository = mockk()
    private lateinit var useCase: CleanupTrashUseCase

    @Before
    fun setUp() {
        useCase = CleanupTrashUseCase(trashRepository)
    }

    @Test
    fun `invoke calls cleanupExpiredTrash on repository`() = runTest {
        coEvery { trashRepository.cleanupExpiredTrash() } returns 5

        val cleaned = useCase()

        assertEquals(5, cleaned)
        coVerify(exactly = 1) { trashRepository.cleanupExpiredTrash() }
    }
}
