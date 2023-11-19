package io.statustracker.track.builder

import io.statustracker.track.status.Status

class StatusDefinition(
    val name: String,
    var next: StatusDefinition? = null
) {
    fun toStatus(): Status = Status(this.name, this.next?.toStatus())
}
