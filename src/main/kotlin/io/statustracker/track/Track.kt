package io.statustracker.track

import io.statustracker.track.status.Status

const val DEFAULT_NAME = "New Track"
const val DEFAULT_TTL = 10000

data class Track(
    val startStatus: Status,
    val name: String = DEFAULT_NAME,
    val endTtl: Int = DEFAULT_TTL,
    val deadTtl: Int = DEFAULT_TTL,
)


