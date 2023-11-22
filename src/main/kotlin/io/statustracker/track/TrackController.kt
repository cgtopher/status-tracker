package io.statustracker.track

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.statustracker.config.ExceptionDTO
import io.statustracker.track.builder.TrackBuilderException
import java.util.*

private val tracksService = TracksService()

fun Application.trackController() {
    routing {
        accept(ContentType.Any) {
            post("/tracks") {
                try {
                    val trackDTO = call.receive<TrackDTO>()
                    val trackIdentifierDTO = tracksService.createTrack(trackDTO)
                    call.respond<TrackIdentifierDTO>(HttpStatusCode.Created, trackIdentifierDTO)
                } catch (e: TrackBuilderException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionDTO("Invalid track request: ${e.message}", HttpStatusCode.BadRequest)
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ExceptionDTO("Server error: ${e.message}", HttpStatusCode.InternalServerError)
                    )
                }
            }

            get("/tracks/{trackId}") {
                val trackId = UUID.fromString(call.parameters["trackId"])
                val trackDTO = tracksService.getTrackDTO(trackId)
                call.respond<TrackDTO>(trackDTO)
            }
        }
    }
}