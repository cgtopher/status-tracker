package io.statustracker.trackable.status.repository

import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.StatusTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.UUID

class StatusRepository {
    suspend fun getStatus(name: String, trackId: UUID): StatusDAO {
        return DatabaseFactory.dbQuery {
            val result = StatusTable
                .select { StatusTable.name.eq(name).and(StatusTable.track.eq(trackId)) }.single()
            return@dbQuery StatusDAO.fromResultRow(result)
        }
    }
}