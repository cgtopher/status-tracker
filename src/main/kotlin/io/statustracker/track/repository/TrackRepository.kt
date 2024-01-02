package io.statustracker.track.repository

import io.statustracker.DatabaseException
import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.TrackTable
import io.statustracker.track.Track
import io.statustracker.track.TrackNotFoundException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.slf4j.LoggerFactory
import java.util.UUID

class TrackRepository() {
    private val logger = LoggerFactory.getLogger(TrackRepository::class.java)

    suspend fun create(track: Track): TrackDAO {
        val id = UUID.randomUUID()
        val trackDAO = DatabaseFactory.dbQuery {
            this.logger.debug("Saving track {} with id: {}", track.name, id)
            val rows = TrackTable.insert {
                it[TrackTable.id] = id
                it[name] = track.name
                it[deadTTL] = track.deadTtl
                it[endTTL] = track.endTtl
            }.resultedValues ?: throw DatabaseException("Problem saving track ${track.name} with id $id")

            return@dbQuery TrackDAO.fromResultRow(rows[0])
        }
        return trackDAO
    }

    suspend fun getById(id: UUID): TrackDAO {
        return DatabaseFactory.dbQuery {
            val result = TrackTable.select { TrackTable.id.eq(id) }.singleOrNull()
            return@dbQuery mapResult(result)
        }
    }

    private fun mapResult(result: ResultRow?): TrackDAO {
        if(result == null) {
            throw TrackNotFoundException("Unable to find track")
        }
        return TrackDAO.fromResultRow(result)
    }
}