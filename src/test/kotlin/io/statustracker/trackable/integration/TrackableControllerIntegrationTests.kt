package io.statustracker.trackable.integration

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.statustracker.config.integrationTest
import io.statustracker.track.TrackDTO
import io.statustracker.track.TrackIdentifierDTO
import io.statustracker.trackable.CreateTrackableDTO
import io.statustracker.trackable.TrackableDTO
import io.statustracker.trackable.UpdateTrackableDTO
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackableControllerIntegrationTests {
    private val TIMES_TO_POLL = 1000

    @Test
    fun `track with three statuses and sequential transitions`() = integrationTest { client ->
        val track = TrackDTO("Three statuses sequential test", listOf("one", "two", "three"))

        val trackIdentifierRequest = client.post("/tracks") {
            contentType(ContentType.Application.Json)
            setBody(track)
        }

        val trackIdentifier = trackIdentifierRequest.body<TrackIdentifierDTO>()

        val createTrackableDTO = CreateTrackableDTO(trackIdentifier.id)
        val initialTrackableRequest = client.post("/trackables") {
            contentType(ContentType.Application.Json)
            setBody(createTrackableDTO)
        }
        val initialTrackable = initialTrackableRequest.body<TrackableDTO>()

        assertEquals(trackIdentifier.id, initialTrackable.status.trackId)
        assertEquals("one", initialTrackable.status.current)
        assertEquals("two", initialTrackable.status.next)

        val statusTwoTrackableRequest = client.post("/trackables/${initialTrackable.id}") {
            contentType(ContentType.Application.Json)
            setBody(UpdateTrackableDTO("two"))
        }
        val statusTwoTrackable = statusTwoTrackableRequest.body<TrackableDTO>()

        assertEquals(initialTrackable.id, statusTwoTrackable.id)
        assertEquals(trackIdentifier.id, statusTwoTrackable.status.trackId)
        assertEquals("two", statusTwoTrackable.status.current)
        assertEquals("three", statusTwoTrackable.status.next)

        val statusThreeTrackableRequest = client.post("/trackables/${initialTrackable.id}") {
            contentType(ContentType.Application.Json)
            setBody(UpdateTrackableDTO("three"))
        }
        val statusThreeTrackable = statusThreeTrackableRequest.body<TrackableDTO>()

        assertEquals(initialTrackable.id, statusThreeTrackable.id)
        assertEquals(trackIdentifier.id, statusThreeTrackable.status.trackId)
        assertEquals("three", statusThreeTrackable.status.current)
        assertEquals(null, statusThreeTrackable.status.next)
    }

    @Test
    fun `GET trackable polling`() = integrationTest { client ->
        val status = "status"
        val track = TrackDTO("Single status poll test", listOf(status))

        val trackIdentifierRequest = client.post("/tracks") {
            contentType(ContentType.Application.Json)
            setBody(track)
        }

        val trackIdentifier = trackIdentifierRequest.body<TrackIdentifierDTO>()
        val createTrackableDTO = CreateTrackableDTO(trackIdentifier.id)
        val trackableRequest = client.post("/trackables") {
            contentType(ContentType.Application.Json)
            setBody(createTrackableDTO)
        }
        val trackable = trackableRequest.body<TrackableDTO>()

        for (i in 0..< TIMES_TO_POLL) {
            val request = client.get("trackables/${trackable.id}") {
                contentType(ContentType.Application.Json)
            }
            assertEquals(HttpStatusCode.OK, request.status)

            val checkTrackable = request.body<TrackableDTO>()
            assertEquals(trackable.id, checkTrackable.id)
            assertEquals(trackable.status.current, checkTrackable.status.current)
            assertEquals(trackable.status.next, checkTrackable.status.next)
            assertEquals(trackIdentifier.id, checkTrackable.status.trackId)
        }

    }
}