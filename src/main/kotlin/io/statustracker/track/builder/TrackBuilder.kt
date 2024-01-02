package io.statustracker.track.builder

import io.statustracker.track.DEFAULT_NAME
import io.statustracker.track.DEFAULT_TTL
import io.statustracker.track.Track

class TrackBuilder {
    private var startStatus: BuilderStatus? = null
    private var cur: BuilderStatus? = null
    private var name: String? = null
    private var endTTL: Int? = null
    private var deadTTL: Int? = null

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
            Track(it.toStatus(), this.name ?: DEFAULT_NAME, this.endTTL ?: DEFAULT_TTL, this.deadTTL ?: DEFAULT_TTL)
        } ?: throw TrackBuilderException("Tracks must have at least one status")
    }

    fun step(status: String): TrackBuilder {
        this.addStep(status)
        return this
    }

    fun step(statuses: Set<String>): TrackBuilder {
        statuses.forEach { this.addStep(it) }
        return this
    }

    private fun addStep(status: String) {
        val newStatus = BuilderStatus(status)
        if(startStatus == null) {
            this.startStatus = newStatus
        } else {
            this.cur?.next = newStatus
        }

        this.cur = newStatus
    }

    fun name(name: String): TrackBuilder {
        this.name = name
        return this
    }

    fun deadTTL(deadTTL: Int?): TrackBuilder {
        this.deadTTL = deadTTL
        return this
    }

    fun endTTL(endTTL: Int?): TrackBuilder {
        this.endTTL = endTTL
        return this
    }
}

