package io.statustracker.track

import io.statustracker.track.builder.TrackBuilderException

/**
 * Validates that a list of strings does not contain duplicate values.
 * If duplicates are found, it throws a TrackBuilderException.
 *
 * @throws TrackBuilderException if duplicates are found
 */
fun List<String>.validateNoDupes() {
    if (this.groupBy { it }.filter { it.value.size > 1 }.isNotEmpty()){
        throw TrackBuilderException("All statuses in a track must be unique")
    }
}