package io.statustracker.trackable

import io.ktor.util.reflect.*
import io.mockk.*
import io.statustracker.track.Track
import io.statustracker.track.TracksService
import io.statustracker.trackable.repository.TrackableRepository
import io.statustracker.trackable.status.Status
import io.statustracker.trackable.status.StatusService
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TrackableServiceTests {
    private val validTrackId = UUID.randomUUID()
    private val trackableId = UUID.randomUUID()
    private val invalidTrackableId = UUID.randomUUID()

    private val trackableName = "foo"
    private val firstStatusName = "bar"
    private val secondStatusName = "baz"

    private val singleStatus = Status(UUID.randomUUID(), firstStatusName)
    private val doubleStatus = Status(validTrackId, firstStatusName, secondStatusName)
    private val mockTrackableRepository = mockk<TrackableRepository>() {
        coEvery { log(any()) } returns Unit
    }

    private val mockCacheService = mockk<CacheService>() {
        every { setTrackable(any()) } returns Unit
        every { getTrackable(trackableId) } returns Trackable(trackableId, doubleStatus, 100, 100)
        every { getTrackable(invalidTrackableId) } returns Trackable(trackableId, singleStatus, 100, 100)
    }

    private val mockStatusService = mockk<StatusService>() {
        coEvery { getStatus(any(), any()) } returns singleStatus
    }

    private val mockTracksService = mockk<TracksService>() {
        coEvery { getTrackById(validTrackId) } returns Track(io.statustracker.track.status.Status(trackableName), "name")
    }

    private val service = TrackableService(mockTrackableRepository, mockCacheService, mockStatusService, mockTracksService)


    @Test
    fun `newTrackableById valid track id should return TrackDTO`() = runBlocking{

        val result = service.newTrackableById(CreateTrackableDTO(validTrackId))

        assertTrue { result.instanceOf(TrackableDTO::class) }
        assertTrue { result.status.current.equals(firstStatusName) }
        coVerifyAll {
            mockTracksService.getTrackById(validTrackId)
            mockStatusService.getStatus(trackableName, validTrackId)
            mockTrackableRepository.log(any())
            mockCacheService.setTrackable(any())
        }
    }


    @Test
    fun `updateTrackableStatus valid status should return TrackDTO`() = runBlocking {
        coEvery { mockStatusService.getStatus(secondStatusName, validTrackId) } returns Status(validTrackId, secondStatusName, null)

        val result = service.updateTrackableStatus(UpdateTrackableDTO(trackableId, doubleStatus.next!!))

        assertTrue { result.instanceOf(TrackableDTO::class) }
        assertEquals(trackableId, result.id)
        assertEquals(result.status.current, secondStatusName)
        assertEquals(result.status.next, null)
    }

    @Test
    fun `updateTrackableStatus invalid status should throw TrackableTransitionException`(): Unit = runBlocking {
        assertFailsWith(TrackableTransitionException::class) {
            service.updateTrackableStatus(UpdateTrackableDTO(trackableId, "invalid_status"))
        }
    }

    @Test
    fun `updateTrackableStatus attempting transition at end of track should throw TrackableTransitionException`(): Unit = runBlocking {
        assertFailsWith(TrackableTransitionException::class) {
            service.updateTrackableStatus(UpdateTrackableDTO(invalidTrackableId, doubleStatus.next!!))
        }
    }

    @Test
    fun `getTrackable should return TrackableDTO`() = runBlocking {
        val result = service.getTrackable(trackableId)

        assertTrue { result.instanceOf(TrackableDTO::class) }
        assertEquals(trackableId, result.id)
        assertEquals(doubleStatus.current, result.status.current)
        assertEquals(doubleStatus.next, result.status.next)
        assertEquals(validTrackId, result.status.trackId)
    }

}