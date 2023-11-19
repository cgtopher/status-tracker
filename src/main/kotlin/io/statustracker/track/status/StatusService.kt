package io.statustracker.track.status

import io.statustracker.track.status.repository.StatusRepository
import java.util.*

class StatusService {
    private val statusRepository = StatusRepository()

    suspend fun saveStatus(status: Status, trackId: UUID) {
        this.statusRepository.saveStatusGroup(status, trackId)
    }

    suspend fun getStatusesForTrack(trackId: UUID): Status {
        val statusDAOs = this.statusRepository.getStatuses(trackId)
        val endStatus = statusDAOs
                .find { it.next == null }
        var statusHead: Status = endStatus
            ?.let { Status(it.name) }
            ?: throw StatusException("Malformed statuses, no end status found")
        var name = endStatus.name

        val lookup: MutableMap<String?, String> = statusDAOs.filter { it.next != null }.associate { it.next to it.name }.toMutableMap()
        while (lookup.isNotEmpty()) {
            name = lookup.remove(name) ?: throw StatusException("Malformed statuses, not linked properly")
            val statusDAO = statusDAOs.first{ it.name == name }
            statusHead = Status(statusDAO.name, statusHead)
        }

        return statusHead
    }
}