package io.statustracker.track

import io.statustracker.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TrackDTO(
    val name: String,
    val statuses: List<String>,
    val endTTL: Int? = null,
    val deadTTL: Int? = null,
)

fun Track.toDto(): TrackDTO {
    return TrackDTO(
        this.name,
        this.startStatus.toList(),
        this.endTtl,
        this.deadTtl,
    )
}

@Serializable
data class TrackIdentifierDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)