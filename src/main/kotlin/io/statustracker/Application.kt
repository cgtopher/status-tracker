package io.statustracker

import io.ktor.serialization.kotlinx.json.*
import io.statustracker.config.healthRoutes
import io.ktor.server.application.*
import io.statustracker.track.trackController
import io.ktor.server.plugins.contentnegotiation.*
import io.statustracker.config.CacheFactory
import io.statustracker.config.DatabaseFactory
import io.statustracker.trackable.trackableController
import kotlinx.serialization.json.Json


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused")
fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    DatabaseFactory.init(
        environment.config.property("db.url").getString(),
        environment.config.property("db.user").getString(),
        environment.config.property("db.password").getString()
    )
    CacheFactory.init(
        environment.config.property("redis.host").getString(),
        environment.config.property("redis.port").getString().toInt()
    )

    healthRoutes()
    trackController()
    trackableController()
}

