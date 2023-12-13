package io.statustracker.trackable

import io.statustracker.track.TracksService
import io.statustracker.trackable.repository.TrackableRepository
import io.statustracker.trackable.status.StatusService
import java.util.UUID

class TrackableService(
    private val trackableRepository: TrackableRepository = TrackableRepository(),
    private val cacheService: CacheService = CacheService(),
    private val statusService: StatusService = StatusService(),
    private val tracksService: TracksService = TracksService()
) {
    fun getTrackable(trackableId: UUID): Trackable = cacheService.getTrackable(trackableId)

    suspend fun createTrackable(createTrackableDTO: CreateTrackableDTO): TrackableDTO {
        val track = this.tracksService.getTrack(createTrackableDTO.trackId)
        val initialStatus = statusService.getStatus(track.startStatus.name, createTrackableDTO.trackId)

        return saveTrackable(
            Trackable(initialStatus, track)
        ).toDto()
    }

    suspend fun updateTrackableStatus(id: UUID, updateTrackableDTO: UpdateTrackableDTO): TrackableDTO {
        val trackable = getTrackable(id)
        if(trackable.status.next != updateTrackableDTO.status) {
            throw TrackableException("Invalid transition to status ${updateTrackableDTO.status}")
        }
        val newStatus = this.statusService.getStatus(updateTrackableDTO.status, trackable.status.trackId)

        return saveTrackable(
            Trackable(
                trackable.id,
                trackable.track,
                newStatus,
                trackable.deadTTL,
                trackable.endTTL)
        ).toDto()
    }

    private suspend fun saveTrackable(trackable: Trackable): Trackable {
        this.trackableRepository.log(trackable)
        this.cacheService.setTrackable(trackable)
        return trackable
    }
}