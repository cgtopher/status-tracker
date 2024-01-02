package io.statustracker.track.builder

import io.statustracker.track.status.Status

class BuilderStatus(
    val name: String,
    var next: BuilderStatus? = null
) {
    fun toStatus(): Status = Status(this.name, this.next?.toStatus())
}
