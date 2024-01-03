package io.statustracker

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.statustracker.config.integrationTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HealthRouteIntegrationTests {
    @Test
    fun testRoot() = integrationTest {client ->
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Ok", response.bodyAsText())
    }
}