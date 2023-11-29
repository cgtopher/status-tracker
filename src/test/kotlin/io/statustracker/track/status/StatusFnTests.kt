package io.statustracker.track.status

import io.ktor.util.reflect.*
import java.util.EmptyStackException
import java.util.Stack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
        assertFailsWith(EmptyStackException::class) { result.pop() }
    }

    @Test
    fun `buildStatusStack one status should return Stack of one String`() {
        val subject = Status("Only")
        val result = buildStatusStack(subject)

        assertTrue { result.instanceOf(Stack::class) }
        assertEquals("Only", result.pop())
        assertFailsWith(EmptyStackException::class) { result.pop() }
    }

    @Test
    fun `Status toList three nested statuses should return List of three Strings`() {
        val subject = Status("one", Status("two", Status("three")))
        val result = subject.toList()

        assertTrue { result.instanceOf(List::class) }
        assertEquals(result.size, 3)
        assertEquals(result[0], "one")
        assertEquals(result[1], "two")
        assertEquals(result[2], "three")
    }
}