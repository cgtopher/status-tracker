package io.statustracker.track

import io.statustracker.track.status.StatusService
import io.statustracker.track.status.toList
import io.statustracker.track.builder.TrackBuilder
import io.statustracker.track.repository.TrackDAO
import io.statustracker.track.repository.TrackRepository
import org.slf4j.LoggerFactory
import java.util.UUID


class TracksService(
    private val trackRepository: TrackRepository = TrackRepository(),
    private val statusService: StatusService = StatusService()
) {

    private val logger = LoggerFactory.getLogger(TracksService::class.java)

    suspend fun createTrack(trackDTO: TrackDTO): TrackIdentifierDTO {
        trackDTO.statuses.validateNoDupes()

        val trackBuilder = TrackBuilder.start()
            .name(trackDTO.name)
            .steps(trackDTO.statuses.toSet())
            .endTTL(trackDTO.endTTL)
            .deadTTL(trackDTO.deadTTL)

        if(trackDTO.errorTrack != null) {
            val errorDTO = trackDTO.errorTrack
            errorDTO.statuses.validateNoDupes()
            trackBuilder.onError()
                .name(errorDTO.name)
                .steps(errorDTO.statuses.toSet())
                .endTTL(trackDTO.endTTL)
                .deadTTL(trackDTO.deadTTL)
                .buildError()
        } else {
            trackBuilder.onError()
                .steps(setOf(DEFAULT_ERROR_TRACK_STATUS))
                .buildError()
        }

        val track = trackBuilder.build()
        this.logger.info("Track {} built", track.name)

        val errorTrackDAO = track.errorTrack?.let {
            val dao = this.trackRepository.createErrorTrack(track.errorTrack)
            this.statusService.saveStatus(track.errorTrack.startStatus, dao.id)
            dao
        }

        val trackDAO = this.trackRepository.createTrack(track, errorTrackDAO?.id)
        this.statusService.saveStatus(track.startStatus, trackDAO.id)

        return TrackIdentifierDTO(trackDAO.id)
    }

    suspend fun getTrackById(trackId: UUID): Track {
        val trackDAO = this.trackRepository.getById(trackId)
        return this.buildTrack(trackDAO)
    }

    suspend fun getTrackByName(name: String): Track {
        val trackDAO = this.trackRepository.getByName(name)
        return this.buildTrack(trackDAO)
    }

    suspend fun getTrackDTO(trackId: UUID): TrackDTO {
        val track = this.getTrackById(trackId)
        return TrackDTO(
            track.name,
            track.startStatus.toList(),
            track.endTTl,
            track.deadTTL,
            track.errorTrack?.let { ErrorTrackDTO(
                it.name,
                it.startStatus.toList(),
                it.endTTl,
                it.deadTTL
            ) }
        )
    }

    private suspend fun buildTrack(trackDAO: TrackDAO): Track {
        val status = this.statusService.getStatusesForTrack(trackDAO.id)
        val errorTrack = trackDAO.errorTrackId?.let { errorTrackId ->
            val dao = this.trackRepository.getById(errorTrackId)
            val errorStatus = this.statusService.getStatusesForTrack(errorTrackId)
            ErrorTrack(
                errorStatus,
                dao.name,
                dao.endTTL,
                dao.deadTTL
            )
        }

        return Track(
            status,
            trackDAO.name,
            errorTrack,
            trackDAO.endTTL,
            trackDAO.deadTTL
        )
    }



}