package io.statustracker.trackable

import io.statustracker.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateTrackableDTO(
    @Serializable(with = UUIDSerializer::class)
    val trackId: UUID
)