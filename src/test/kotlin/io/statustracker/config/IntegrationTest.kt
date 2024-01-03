package io.statustracker.config

import com.redis.testcontainers.RedisContainer
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.statustracker.module
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object IntegrationTest {
    val postgres: PostgreSQLContainer<Nothing> = PostgreSQLContainer("postgres:16.1")
    val redis: RedisContainer = RedisContainer(DockerImageName.parse("redis:7.2.3"))

    init {
        postgres.start()
        redis.start()
    }
}

fun integrationTest(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) = testApplication {
        environment {
            config = MapApplicationConfig(
                "ktor.environment" to "dev",
                "ktor.deployment.port" to "8080",
                "db.url" to IntegrationTest.postgres.jdbcUrl,
                "db.user" to IntegrationTest.postgres.username,
                "db.password" to IntegrationTest.postgres.password,
                "redis.host" to IntegrationTest.redis.host,
                "redis.port" to IntegrationTest.redis.firstMappedPort.toString()
            )
        }

        application { module() }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        block(client)
    }