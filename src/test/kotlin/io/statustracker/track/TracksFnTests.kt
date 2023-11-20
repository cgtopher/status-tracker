package io.statustracker.track

import io.ktor.util.reflect.*
import io.statustracker.track.builder.TrackBuilderException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TracksFnTests {
    @Test
    fun `no dupes should return Unit`() {
        val subject = listOf("one", "two", "three")
        val result = subject.validateNoDupes()
        assertTrue { result.instanceOf(Unit::class) }
    }

    @Test
    fun `dupes should throw TrackBuilderException`() {
        val subject = listOf("one", "one", "two", "three")
        assertFailsWith(TrackBuilderException::class) {
            subject.validateNoDupes()
        }
    }

    @Test
    fun `empty list should return Unit`() {
        val subject = listOf<String>()
        val result = subject.validateNoDupes()
        assertTrue { result.instanceOf(Unit::class) }
    }
}