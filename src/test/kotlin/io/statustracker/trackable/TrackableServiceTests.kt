package io.statustracker.trackable

import io.mockk.every
import io.mockk.mockk
import io.statustracker.track.Track
import io.statustracker.track.TracksService
import io.statustracker.track.status.Status
import io.statustracker.trackable.repository.TrackableRepository
import io.statustracker.trackable.status.StatusService
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class TrackableServiceTests {
    private val mockTrackableRepository = mockk<TrackableRepository>()
    private val mockCacheService = mockk<CacheService>()
    private val mockStatusService = mockk<StatusService>()
    private val mockTracksService = mockk<TracksService>()

    private val service = TrackableService(mockTrackableRepository, mockCacheService, mockStatusService, mockTracksService)

    private val validTrackId = UUID.randomUUID()
    private val invalidTrackId = UUID.randomUUID()

    @Test
    fun newTrackableById_validTrackId_shouldReturnTrackable() {

    }
}