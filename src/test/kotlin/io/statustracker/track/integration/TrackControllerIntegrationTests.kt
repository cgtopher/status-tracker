package io.statustracker.track.integration

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.statustracker.config.integrationTest
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.TrackDTO
import io.statustracker.track.TrackIdentifierDTO
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackControllerIntegrationTests {

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
}