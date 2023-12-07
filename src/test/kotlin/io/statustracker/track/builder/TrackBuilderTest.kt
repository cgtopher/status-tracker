package io.statustracker.track.builder

import io.ktor.util.reflect.*
import io.statustracker.track.Track
import kotlin.test.*


class TrackBuilderTest {

    @Test
    fun `from standard set should return Track`() {
        val result = TrackBuilder.from(setOf("one", "two", "three"))

        assertTrue { result.instanceOf(Track::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
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
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }

    @Test
    fun `builder three steps as set should return Track`() {
        val result = TrackBuilder.start().steps(setOf("one", "two", "three")).build()

        assertTrue { result.instanceOf(Track::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }

    @Test
    fun `builder three steps with error should return Track`() {
        val track = TrackBuilder.start()
            .step("one")
            .step("two")
            .step("three")
            .onError()
                .step("one")
                .buildError()
            .build()

        val result = track.errorTrack

        assertNotNull(result)
        assertEquals(result.startStatus.name, "one")
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