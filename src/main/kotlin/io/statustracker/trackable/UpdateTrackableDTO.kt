package io.statustracker.trackable

import io.statustracker.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UpdateTrackableDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val status: String
)