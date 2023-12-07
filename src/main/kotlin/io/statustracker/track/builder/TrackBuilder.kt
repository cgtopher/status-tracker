package io.statustracker.track.builder

import io.statustracker.track.DEFAULT_NAME
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.ErrorTrack
import io.statustracker.track.Track

class TrackBuilder : TrackBuilderBase<TrackBuilder>() {
    private var errorTrack: ErrorTrack? = null

    companion object {
        // Builds from string array of status names with defaults set
        fun from(statuses: Set<String>): Track {
            val startStatus = buildStatuses(statuses.toTypedArray())
            return Track(startStatus)
        }

        fun start() = TrackBuilder()
    }

    fun build(): Track {
        return this.startStatus?.let {
            Track(it.toStatus(), this.name ?: DEFAULT_NAME, this.errorTrack, this.endTTL ?: DEFAULT_TTL, this.deadTTL ?: DEFAULT_TTL)
        } ?: throw TrackBuilderException("Tracks must have at least one status")
    }

    fun onError() = ErrorTrackBuilder(this)
    fun errorTrack(errorTrack: ErrorTrack) {
        this.errorTrack = errorTrack
    }

    override fun step(status: String): TrackBuilder {
        this.addStep(status)
        return this
    }

    override fun steps(statuses: Set<String>): TrackBuilder {
        this.addSteps(statuses)
        return this
    }

    override fun name(name: String): TrackBuilder {
        this.name = name
        return this
    }

    override fun deadTTL(deadTTL: Int?): TrackBuilder {
        this.deadTTL = deadTTL
        return this
    }

    override fun endTTL(endTTL: Int?): TrackBuilder {
        this.endTTL = endTTL
        return this
    }
}

