package io.statustracker.track.status

import io.ktor.util.reflect.*
import java.util.Stack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StatusFnTests {
    @Test
    fun `buildStatusStack three nested statuses should return Stack of String`() {
        val subject = Status("one", Status("two", Status("three")))
        val result = buildStatusStack(subject)

        assertTrue { result.instanceOf(Stack::class) }
        assertEquals("three", result.pop())
        assertEquals("two", result.pop())
        assertEquals("one", result.pop())
        assertEquals(0, result.size)
    }
}