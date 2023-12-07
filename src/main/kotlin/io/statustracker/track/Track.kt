package io.statustracker.track

import io.statustracker.track.status.Status

const val DEFAULT_NAME = "New Track"
const val DEFAULT_TTL = 10000
const val DEFAULT_ERROR_TRACK_STATUS = "ERROR"

open class TrackBase(
    open val startStatus: Status,
    open val name: String,
    open val endTtl: Int,
    open val deadTtl: Int
)

data class Track(
    override val startStatus: Status,
    override val name: String = DEFAULT_NAME,
    val errorTrack: ErrorTrack? = null,
    override val endTtl: Int = DEFAULT_TTL,
    override val deadTtl: Int = DEFAULT_TTL,
) : TrackBase(startStatus, name, endTtl, deadTtl)

data class ErrorTrack(
    override val startStatus: Status,
    override val name: String = DEFAULT_NAME,
    override val endTtl: Int = DEFAULT_TTL,
    override val deadTtl: Int = DEFAULT_TTL
) : TrackBase(startStatus, name, endTtl, deadTtl = deadTtl)


