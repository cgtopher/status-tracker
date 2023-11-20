package io.statustracker

import io.ktor.serialization.kotlinx.json.*
import io.statustracker.config.healthRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.statustracker.track.trackController
import io.ktor.server.plugins.contentnegotiation.*
import io.statustracker.config.CacheFactory
import io.statustracker.config.DatabaseFactory
import io.statustracker.trackable.trackableController
import kotlinx.serialization.json.Json

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    DatabaseFactory.init()
    CacheFactory.init()

    healthRoutes()
    trackController()
    trackableController()
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
