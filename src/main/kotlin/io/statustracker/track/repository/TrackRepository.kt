package io.statustracker.track.repository

import io.statustracker.config.DatabaseException
import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.TrackTable
import io.statustracker.track.ErrorTrack
import io.statustracker.track.Track
import io.statustracker.track.TrackBase
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.slf4j.LoggerFactory
import java.util.UUID

class TrackRepository() {
    private val logger = LoggerFactory.getLogger(TrackRepository::class.java)

    suspend fun createTrack(track: Track, errorTrackId: UUID?) = this.create(track, errorTrackId)
    suspend fun createErrorTrack(errorTrack: ErrorTrack) = this.create(errorTrack)

    private suspend fun create(track: TrackBase, errorTrackId: UUID? = null): TrackDAO {
        val id = UUID.randomUUID()
        val trackDAO = DatabaseFactory.dbQuery {
            this.logger.debug("Saving track {} with id: {}", track.name, id)
            val rows = TrackTable.insert {
                it[TrackTable.id] = id
                it[name] = track.name
                it[deadTTL] = track.deadTTL
                it[endTTL] = track.endTTl
                it[errorTrack] = errorTrackId
            }.resultedValues ?: throw DatabaseException("Problem saving track ${track.name} with id $id")

            return@dbQuery TrackDAO.fromResultRow(rows[0])
        }
        return trackDAO
    }

    suspend fun getById(id: UUID): TrackDAO {
        return DatabaseFactory.dbQuery {
            val result = TrackTable.select { TrackTable.id.eq(id) }.single()
            return@dbQuery TrackDAO.fromResultRow(result)
        }
    }

    suspend fun getByName(name: String): TrackDAO {
        return DatabaseFactory.dbQuery {
            val result = TrackTable.select { TrackTable.name.eq(name) }.single()
            return@dbQuery TrackDAO.fromResultRow(result)
        }
    }
}