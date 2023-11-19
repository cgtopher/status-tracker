package io.statustracker.track.builder

import io.statustracker.track.DEFAULT_NAME
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.ErrorTrack
import io.statustracker.track.Track


abstract class TrackBuilderBase<T> {
    protected var startStatus: StatusDefinition? = null
    private var cur: StatusDefinition? = null
    protected var name: String? = null
    protected var endTTL: Int? = null
    protected var deadTTL: Int? = null

    fun addStep(status: String) {
        val newStatus = StatusDefinition(status)
        if(startStatus == null) {
            this.startStatus = newStatus
        } else {
            this.cur?.next = newStatus
        }

        this.cur = newStatus
    }

    fun addSteps(statuses: Set<String>) {
        statuses.forEach { this.addStep(it) }
    }

    abstract fun step(status: String): T
    abstract fun steps(statuses: Set<String>): T
    abstract fun name(name: String): T
    abstract fun deadTTL(deadTTL: Int?): T
    abstract fun endTTL(endTTL: Int?): T
}

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

class ErrorTrackBuilder(private val mainTrack: TrackBuilder) : TrackBuilderBase<ErrorTrackBuilder>() {
    companion object {
        fun from(statuses: Set<String>): ErrorTrack {
            val startStatus = buildStatuses(statuses.toTypedArray())
            return ErrorTrack(startStatus)
        }
    }

    fun buildError(): TrackBuilder {
        val errorTrack = this.startStatus?.let {
            ErrorTrack(it.toStatus(), this.name ?: DEFAULT_NAME, this.endTTL ?: DEFAULT_TTL, this.deadTTL ?: DEFAULT_TTL)
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
