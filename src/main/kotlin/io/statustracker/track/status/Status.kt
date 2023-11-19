package io.statustracker.track.status

data class Status(
    val name: String,
    val next: Status? = null
)
