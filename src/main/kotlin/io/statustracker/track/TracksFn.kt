package io.statustracker.track

import io.statustracker.track.builder.TrackBuilderException

fun List<String>.validateNoDupes() {
    if (this.groupBy { it }.filter { it.value.size > 1 }.isNotEmpty()){
        throw TrackBuilderException("All statuses in a track must be unique")
    }
}