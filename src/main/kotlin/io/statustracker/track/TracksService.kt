package io.statustracker.track

import io.statustracker.track.builder.ErrorTrackBuilder
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
        trackBuilder.onError().also { errorTrackBuilder ->
            val builder: ErrorTrackBuilder = trackDTO.errorTrack?.let {
                prepareErrorTrackBuilder(trackDTO.errorTrack, errorTrackBuilder)
            } ?: errorTrackBuilder.steps(setOf( DEFAULT_ERROR_TRACK_STATUS))
            builder.buildError()
        }

        val track = trackBuilder.build()
        this.logger.info("Track {} built", track.name)

        val trackDAO = saveTrack(track)
        return TrackIdentifierDTO(trackDAO.id)
    }

    private fun prepareTrackBuilder(trackDTO: TrackDTO): TrackBuilder {
        trackDTO.statuses.validateNoDupes()
        return TrackBuilder.start()
            .name(trackDTO.name)
            .steps(trackDTO.statuses.toSet())
            .endTTL(trackDTO.endTTL)
            .deadTTL(trackDTO.deadTTL)
    }

    private fun prepareErrorTrackBuilder(
        errorTrackDTO: ErrorTrackDTO,
        errorTrackBuilder: ErrorTrackBuilder
    ): ErrorTrackBuilder {
        errorTrackDTO.statuses.validateNoDupes()
        return errorTrackBuilder
            .name(errorTrackDTO.name)
            .steps(errorTrackDTO.statuses.toSet())
            .endTTL(errorTrackDTO.endTTL)
            .deadTTL(errorTrackDTO.deadTTL)
    }

    private suspend fun saveTrack(track: Track): TrackDAO {
        val errorTrackDAO = track.errorTrack?.let {
            val dao = trackRepository.createErrorTrack(track.errorTrack)
            statusService.saveStatus(track.errorTrack.startStatus, dao.id)
            dao
        }

        val trackDAO = trackRepository.createTrack(track, errorTrackDAO?.id)
        statusService.saveStatus(track.startStatus, trackDAO.id)

        return trackDAO
    }

    suspend fun getTrack(trackId: UUID): Track {
        val trackDAO = this.trackRepository.getById(trackId)
        return buildTrackFromDAO(trackDAO)
    }

    private suspend fun buildTrackFromDAO(trackDAO: TrackDAO): Track {
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