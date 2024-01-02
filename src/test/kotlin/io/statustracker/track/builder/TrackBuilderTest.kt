package io.statustracker.track.builder

import io.ktor.util.reflect.*
import io.statustracker.track.Track
import kotlin.test.*


class TrackBuilderTest {

    @Test
    fun `from standard set should return Track`() {
        val result = TrackBuilder.from(setOf("one", "two", "three"))

        assertTrue { result.instanceOf(Track::class) }
        assertEquals("one", result.startStatus.name)
        assertEquals("two", result.startStatus.next?.name)
        assertEquals("three", result.startStatus.next?.next?.name)
    }

    @Test
    fun `from empty set should throw TrackBuilderException`() {
        assertFailsWith(TrackBuilderException::class) { TrackBuilder.from(setOf()) }
    }

    @Test
    fun `builder three steps should return Track`() {
        val result = TrackBuilder.start()
            .step("one")
            .step("two")
            .step("three")
            .build()

        assertTrue { result.instanceOf(Track::class) }
        assertEquals("one", result.startStatus.name)
        assertEquals("two", result.startStatus.next?.name)
        assertEquals("three", result.startStatus.next?.next?.name)
    }

    @Test
    fun `builder three steps as set should return Track`() {
        val result = TrackBuilder.start().step(setOf("one", "two", "three")).build()

        assertTrue { result.instanceOf(Track::class) }
        assertEquals("one", result.startStatus.name)
        assertEquals("two", result.startStatus.next?.name)
        assertEquals("three", result.startStatus.next?.next?.name)
    }


    @Test
    fun `builder name() should return Track`() {
        val name = "Test"
        val result = TrackBuilder.start()
            .name(name)
            .step("one")
            .build()

        assertEquals(result.name, name)
    }

    @Test
    fun `builder endTTL() should return Track`() {
        val endTTL = 1000
        val result = TrackBuilder.start()
            .endTTL(endTTL)
            .step("one")
            .build()

        assertEquals(result.endTtl, endTTL)
    }

    @Test
    fun `builder deadTTL should return Track`() {
        val deadTTL = 1000
        val result = TrackBuilder.start()
            .deadTTL(deadTTL)
            .step("one")
            .build()

        assertEquals(result.deadTtl, deadTTL)
    }
}