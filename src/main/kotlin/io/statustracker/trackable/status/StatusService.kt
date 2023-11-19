package io.statustracker.trackable.status

import io.statustracker.trackable.status.repository.StatusRepository
import java.util.UUID

class StatusService(
    private val statusRepository: StatusRepository = StatusRepository()
) {

    suspend fun getStatus(name: String, trackId: UUID): Status {
        val dao = this.statusRepository.getStatus(name, trackId)
        return Status(dao.trackId, dao.name, dao.next)
    }
}