package io.statustracker.track

import io.mockk.mockk
import io.statustracker.track.repository.TrackRepository
import io.statustracker.track.status.StatusService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test

class TrackServiceTests {
    private val mockTrackRepository = mockk<TrackRepository>()
    private val mockStatusService = mockk<StatusService>()

    private val service = TracksService(mockTrackRepository, mockStatusService)

    @Before
    fun setup() {

    }

    @Test
    fun `createTrack minimum input should return TrackIdentifierDTO`() = runBlocking {

    }
}