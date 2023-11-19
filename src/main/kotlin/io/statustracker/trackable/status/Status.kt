package io.statustracker.trackable.status

import io.statustracker.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Status(
    @Serializable(with = UUIDSerializer::class)
    val trackId: UUID,
    val current: String,
    val next: String? = null,
)