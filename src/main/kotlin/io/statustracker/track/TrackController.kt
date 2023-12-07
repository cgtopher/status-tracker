package io.statustracker.track

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.statustracker.ExceptionDTO
import io.statustracker.track.builder.TrackBuilderException
import java.util.*


fun Application.trackController() {
    val tracksService = TracksService()

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
                        ExceptionDTO(e.message ?: "Invalid Track Request", HttpStatusCode.BadRequest)
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ExceptionDTO(e.message ?: "Internal Server Error", HttpStatusCode.InternalServerError)
                    )
                }
            }

            get("/tracks/{trackId}") {
                try {
                    val trackId = UUID.fromString(call.parameters["trackId"])
                    val track = tracksService.getTrack(trackId)
                    call.respond<TrackDTO>(track.toDto())
                } catch (e: TrackNotFoundException) {
                    call.respond<ExceptionDTO>(
                        HttpStatusCode.NotFound,
                        ExceptionDTO(e.message ?: "Internal Server Error", HttpStatusCode.InternalServerError)
                    )
                } catch (e: Exception) {
                    call.respond<ExceptionDTO>(
                        HttpStatusCode.InternalServerError,
                        ExceptionDTO(e.message ?: "Internal Server Error", HttpStatusCode.InternalServerError)
                    )
                }
            }
        }
    }
}