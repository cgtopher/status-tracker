package io.statustracker.trackable

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.statustracker.ExceptionDTO
import java.util.*

val trackableService = TrackableService()

fun Application.trackableController() {
    routing {
        post("/trackables") {
            try {
                val createTrackableDTO = call.receive<CreateTrackableDTO>()
                val trackable = trackableService.newTrackableById(createTrackableDTO)
                call.respond<TrackableDTO>(HttpStatusCode.Created, trackable)
            } catch (e: TrackableException) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.InternalServerError,
                    ExceptionDTO(
                        e.message ?: "Problem creating trackable",
                        HttpStatusCode.InternalServerError
                    )
                )
            } catch (e: Exception) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.InternalServerError,
                    ExceptionDTO(
                        e.message ?: "Internal Server Error",
                        HttpStatusCode.InternalServerError
                    )
                )
            }
        }

        put("/trackables") {
            try {
                val updateTrackableDTO = call.receive<UpdateTrackableDTO>()
                val trackable = trackableService.updateTrackableStatus(updateTrackableDTO)
                call.respond<TrackableDTO>(trackable)
            } catch (e: TrackableNotFoundException) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.NotFound,
                    ExceptionDTO(
                        e.message ?: "Cant find trackable",
                        HttpStatusCode.NotFound
                    )
                )
            } catch (e: TrackableException) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.BadRequest,
                    ExceptionDTO(
                        e.message ?: "Problem with trackable",
                        HttpStatusCode.BadRequest
                    )
                )
            } catch (e: Exception) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.InternalServerError,
                    ExceptionDTO(
                        e.message ?: "Internal Server Error",
                        HttpStatusCode.InternalServerError
                    )
                )
            }
        }

        get("/trackables/{trackableId}") {
            try {
                val trackableId = UUID.fromString(call.parameters["trackableId"])
                val trackable = trackableService.getTrackable(trackableId)
                call.respond<TrackableDTO>(trackable.toDto())
            } catch (e: TrackableNotFoundException) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.NotFound,
                    ExceptionDTO(
                        e.message ?: "Unable to find trackable",
                        HttpStatusCode.NotFound
                    )
                )
            } catch (e: Exception) {
                call.respond<ExceptionDTO>(
                    HttpStatusCode.InternalServerError,
                    ExceptionDTO(
                        e.message ?: "Internal Server Error",
                        HttpStatusCode.InternalServerError
                    )
                )
            }
        }
    }
}