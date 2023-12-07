package io.statustracker.trackable

import io.statustracker.config.UUIDSerializer
import io.statustracker.track.Track
import io.statustracker.trackable.status.Status
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Trackable(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val track: String,
    val status: Status,
    val deadTTL: Int,
    val endTTL: Int
)  {
    constructor(initialStatus: Status, track: Track) : this(
        UUID.randomUUID(),
        track.name,
        initialStatus,
        track.deadTtl,
        track.endTtl
    )
}

