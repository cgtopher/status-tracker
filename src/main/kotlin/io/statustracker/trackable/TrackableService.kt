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

    suspend fun newTrackableById(createTrackableDTO: CreateTrackableDTO): TrackableDTO {
        val track = this.tracksService.getTrackById(createTrackableDTO.trackId)
        val initialStatus = statusService.getStatus(track.startStatus.name, createTrackableDTO.trackId)
        val trackable = Trackable(UUID.randomUUID(), initialStatus, track.deadTTL, track.endTTl)
        this.trackableRepository.log(trackable)
        this.cacheService.setTrackable(trackable)

        return TrackableDTO(
            trackable.id,
            trackable.status
        )
    }

    suspend fun updateTrackableStatus(updateTrackableDTO: UpdateTrackableDTO): TrackableDTO {
        val trackable = this.cacheService.getTrackable(updateTrackableDTO.id)
        if(trackable.status.next != updateTrackableDTO.status) {
            throw TrackableTransitionException("Invalid transition to status ${updateTrackableDTO.status}")
        }
        val newStatus = this.statusService.getStatus(updateTrackableDTO.status, trackable.status.trackId)
        val newTrackable = Trackable(trackable.id, newStatus, trackable.deadTTL, trackable.endTTL)
        this.trackableRepository.log(newTrackable)
        this.cacheService.setTrackable(newTrackable)

        return TrackableDTO(
            newTrackable.id,
            newTrackable.status
        )
    }

    fun getTrackable(trackableId: UUID): TrackableDTO {
        val trackable = this.cacheService.getTrackable(trackableId)
        return TrackableDTO(
            trackable.id,
            trackable.status
        )
    }
}