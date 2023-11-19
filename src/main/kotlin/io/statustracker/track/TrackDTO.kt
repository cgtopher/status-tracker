package io.statustracker.track

import io.statustracker.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TrackDTO(
    val name: String,
    val endTTL: Int? = null,
    val deadTTL: Int? = null,
    val statuses: List<String>,
    val errorTrack: ErrorTrackDTO?
)

@Serializable
data class ErrorTrackDTO(
    val name: String,
    val endTTL: Int? = null,
    val deadTTL: Int? = null,
    val statuses: List<String>,
)

@Serializable
data class TrackIdentifierDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)