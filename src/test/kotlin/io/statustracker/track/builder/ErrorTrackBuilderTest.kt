package io.statustracker.track.builder

import io.ktor.util.reflect.*
import io.statustracker.track.ErrorTrack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class ErrorTrackBuilderTest {

    @Test
    fun from_standardSet_shouldReturnErrorTrack() {
        val result = ErrorTrackBuilder.from(setOf("one", "two", "three"))

        assertTrue { result.instanceOf(ErrorTrack::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }

    @Test
    fun builder_threeSteps_shouldReturnErrorTrack() {
        val mainTrack = TrackBuilder.start().step("one")

        ErrorTrackBuilder(mainTrack).step("one").step("two").step("three").buildError()

        val result = mainTrack.build().errorTrack


        assertNotNull(result)
        assertTrue { result.instanceOf(ErrorTrack::class) }
        assertEquals(result.startStatus.name, "one")
        assertEquals(result.startStatus.next?.name, "two")
        assertEquals(result.startStatus.next?.next?.name, "three")
    }
}