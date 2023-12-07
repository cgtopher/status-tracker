package io.statustracker.track.builder

import io.statustracker.track.DEFAULT_NAME
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.ErrorTrack

class ErrorTrackBuilder(private val mainTrack: TrackBuilder) : TrackBuilderBase<ErrorTrackBuilder>() {
    companion object {
        fun from(statuses: Set<String>): ErrorTrack {
            val startStatus = buildStatuses(statuses.toTypedArray())
            return ErrorTrack(startStatus)
        }
    }

    fun buildError(): TrackBuilder {
        val errorTrack = this.startStatus?.let {
            ErrorTrack(
                it.toStatus(),
                this.name ?: DEFAULT_NAME,
                this.endTTL ?: DEFAULT_TTL,
                this.deadTTL ?: DEFAULT_TTL
            )
        } ?: throw TrackBuilderException("ErrorTracks must have at least one status")

        this.mainTrack.errorTrack(errorTrack)
        return mainTrack
    }

    override fun step(status: String): ErrorTrackBuilder {
        this.addStep(status)
        return this
    }

    override fun steps(statuses: Set<String>): ErrorTrackBuilder {
        this.addSteps(statuses)
        return this
    }

    override fun name(name: String): ErrorTrackBuilder {
        this.name = name
         return this
    }

    override fun deadTTL(deadTTL: Int?): ErrorTrackBuilder {
        this.deadTTL = deadTTL
        return this
    }

    override fun endTTL(endTTL: Int?): ErrorTrackBuilder {
        this.endTTL = endTTL
        return this
    }
}