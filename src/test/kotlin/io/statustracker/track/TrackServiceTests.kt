package io.statustracker.track

import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import io.statustracker.track.repository.TrackDAO
import io.statustracker.track.repository.TrackRepository
import io.statustracker.track.status.StatusService
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TrackServiceTests {
    private val trackDAO = TrackDAO(
        UUID.randomUUID(),
        "Track",
        100,
        100
    )

    private val mockTrackRepository = mockk<TrackRepository>() {
        coEvery { create(any()) } returns trackDAO
    }
    private val mockStatusService = mockk<StatusService>() {
        coEvery { saveStatus(any(), any()) } returns Unit
    }

    private val service = TracksService(mockTrackRepository, mockStatusService)

    @Test
    fun `createTrack minimum input should return TrackIdentifierDTO`() = runBlocking {
        val trackDTO = TrackDTO(trackDAO.name, listOf("one", "two", "three"))

        val result = service.createTrack(trackDTO)

        assertTrue { result.instanceOf(TrackIdentifierDTO::class) }
        assertEquals(result.id, trackDAO.id)

        coVerifyAll {
            mockTrackRepository.create(any())
            mockStatusService.saveStatus(any(), trackDAO.id)
        }
    }

    @Test
    fun `createTrack full input should return TrackIdentifierDTO`() = runBlocking {
        val trackDTO = TrackDTO(
            trackDAO.name,
            listOf("one", "two", "three"),
            100,
            100
        )

        val result = service.createTrack(trackDTO)

        assertTrue { result.instanceOf(TrackIdentifierDTO::class) }
        assertEquals(result.id, trackDAO.id)

        coVerifyAll {
            mockTrackRepository.create(any())
            mockStatusService.saveStatus(any(), trackDAO.id)
        }
    }

}