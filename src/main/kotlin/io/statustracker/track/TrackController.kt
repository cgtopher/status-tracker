package io.statustracker.track

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

private val tracksService = TracksService()

fun Application.trackController() {
    routing {
        accept(ContentType.Any) {
            post("/tracks") {
                val trackDTO = call.receive<TrackDTO>()
                val trackIdentifierDTO = tracksService.createTrack(trackDTO)
                call.respond<TrackIdentifierDTO>(HttpStatusCode.Created, trackIdentifierDTO)
            }

            get("/tracks/{trackId}") {
                val trackId = UUID.fromString(call.parameters["trackId"])
                val trackDTO = tracksService.getTrackDTO(trackId)
                call.respond<TrackDTO>(trackDTO)
            }
        }
    }
}