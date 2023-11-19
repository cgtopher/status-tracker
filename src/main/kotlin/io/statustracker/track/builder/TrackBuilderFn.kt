package io.statustracker.track.builder

import io.statustracker.track.status.Status


internal fun buildStatuses(statuses: Array<String>): Status {
    var cur: Status? = null
    var i = statuses.size - 1
    // Build statuses from end to start, effectively a linked list
    while (i >= 0) {
        cur = Status(statuses[i], cur)
        i--
    }

    return cur ?: throw TrackBuilderException("All TrackPaths must have at least one status")
}
