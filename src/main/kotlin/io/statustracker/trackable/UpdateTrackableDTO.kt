package io.statustracker.trackable

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTrackableDTO(
    val status: String
)