package io.statustracker.trackable

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

val trackableService = TrackableService()

fun Application.trackableController() {
    routing {
        post("/trackables") {
            val createTrackableDTO = call.receive<CreateTrackableDTO>()
            val trackable = trackableService.newTrackableById(createTrackableDTO)
            call.respond(HttpStatusCode.Created, trackable)
        }

        put("/trackables") {
            val updateTrackableDTO = call.receive<UpdateTrackableDTO>()
            val trackable = trackableService.updateTrackableStatus(updateTrackableDTO)
            call.respond(trackable)
        }

        get("/trackables/{trackableId}") {
            val trackableId = UUID.fromString(call.parameters["trackableId"])
            val trackable = trackableService.getTrackable(trackableId)
            call.respond(trackable)
        }
    }
}