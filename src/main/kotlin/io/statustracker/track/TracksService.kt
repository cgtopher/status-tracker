package io.statustracker.track

import io.statustracker.track.status.StatusService
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
        val trackBuilder = prepareTrackBuilder(trackDTO)

        val track = trackBuilder.build()
        this.logger.info("Track {} built", track.name)

        val trackDAO = saveTrack(track)
        return TrackIdentifierDTO(trackDAO.id)
    }

    private fun prepareTrackBuilder(trackDTO: TrackDTO): TrackBuilder {
        trackDTO.statuses.validateNoDupes()
        return TrackBuilder.start()
            .name(trackDTO.name)
            .step(trackDTO.statuses.toSet())
            .endTTL(trackDTO.endTTL)
            .deadTTL(trackDTO.deadTTL)
    }


    private suspend fun saveTrack(track: Track): TrackDAO {
        val trackDAO = trackRepository.create(track)
        statusService.saveStatus(track.startStatus, trackDAO.id)

        return trackDAO
    }

    suspend fun getTrack(trackId: UUID): Track {
        val trackDAO = this.trackRepository.getById(trackId)
        return buildTrackFromDAO(trackDAO)
    }

    private suspend fun buildTrackFromDAO(trackDAO: TrackDAO): Track {
        val status = this.statusService.getStatusesForTrack(trackDAO.id)
        return Track(
            status,
            trackDAO.name,
            trackDAO.endTTL,
            trackDAO.deadTTL
        )
    }



}