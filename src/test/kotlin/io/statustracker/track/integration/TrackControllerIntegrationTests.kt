package io.statustracker.track.integration

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.statustracker.config.integrationTest
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.TrackDTO
import io.statustracker.track.TrackIdentifierDTO
import io.statustracker.util.getRandomStatuses
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackControllerIntegrationTests {
    private val STATUS_LIST_SIZE = 10000
    private val STATUS_STRING_SIZE_MIN = 1
    private val STATUS_STRING_SIZE_MAX = 42

    @Test
    fun `create and fetch track with minimal inputs`() = integrationTest { client ->
        val trackName = "test"
        val statuses = listOf("one")

        val createResponse = client.post("/tracks") {
            contentType(ContentType.Application.Json)
            setBody(
                TrackDTO(
                    trackName,
                    statuses
                )
            )
        }

        assertEquals(HttpStatusCode.Created, createResponse.status)

        val trackIdentifier = createResponse.body<TrackIdentifierDTO>()

        val fetchResponse = client.get("/tracks/${trackIdentifier.id}")
        val track = fetchResponse.body<TrackDTO>()
        assertEquals(trackName, track.name)
        assertEquals(statuses, track.statuses)
        assertEquals(DEFAULT_TTL, track.endTTL)
        assertEquals(DEFAULT_TTL, track.deadTTL)
    }


    @Test
    fun `track with many randomized statuses`() = integrationTest { client ->
        val statuses = getRandomStatuses(STATUS_LIST_SIZE, STATUS_STRING_SIZE_MIN, STATUS_STRING_SIZE_MAX)
        val trackName = "Test many statuses"

        val createResponse = client.post("/tracks") {
            contentType(ContentType.Application.Json)
            setBody(
                TrackDTO(
                    trackName,
                    statuses
                )
            )
        }

        assertEquals(HttpStatusCode.Created, createResponse.status)
        val trackIdentifier = createResponse.body<TrackIdentifierDTO>()

        val fetchResponse = client.get("/tracks/${trackIdentifier.id}")
        val track = fetchResponse.body<TrackDTO>()
        assertEquals(statuses.size, track.statuses.size)
    }
}