package io.statustracker.track.builder

import io.ktor.util.reflect.*
import io.statustracker.track.Track
import kotlin.test.*


class TrackBuilderTest {

    @Test
    fun from_standardSet_shouldReturnTrack() {
        val result = TrackBuilder.from(setOf("one", "two", "three"))

        assertTrue { result.instanceOf(Track::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }

    @Test
    fun from_emptySet_shouldThrowTrackBuilderException() {
        assertFailsWith(TrackBuilderException::class) { TrackBuilder.from(setOf()) }
    }

    @Test
    fun builder_threeSteps_shouldReturnTrack() {
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
    fun builder_threeStepsAsSet_shouldReturnTrack() {
        val result = TrackBuilder.start().steps(setOf("one", "two", "three")).build()

        assertTrue { result.instanceOf(Track::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }

    @Test
    fun builder_threeStepsWithError_shouldReturnTrack() {
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
    fun builder_Name_shouldReturnTrack() {
        val name = "Test"
        val result = TrackBuilder.start()
            .name(name)
            .step("one")
            .build()

        assertEquals(result.name, name)
    }

    @Test
    fun builder_endTTL_shouldReturnTrack() {
        val endTTL = 1000
        val result = TrackBuilder.start()
            .endTTL(endTTL)
            .step("one")
            .build()

        assertEquals(result.endTTl, endTTL)
    }

    @Test
    fun builder_deadTTL_shouldReturnTrack() {
        val deadTTL = 1000
        val result = TrackBuilder.start()
            .deadTTL(deadTTL)
            .step("one")
            .build()

        assertEquals(result.deadTTL, deadTTL)
    }
}