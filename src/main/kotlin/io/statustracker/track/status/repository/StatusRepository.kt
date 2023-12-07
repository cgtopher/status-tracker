package io.statustracker.track.status.repository

import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.StatusTable
import io.statustracker.track.status.Status
import io.statustracker.track.status.StatusNotFoundException
import io.statustracker.track.status.buildStatusStack
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.slf4j.LoggerFactory
import java.util.*

class StatusRepository() {
    private val logger = LoggerFactory.getLogger(StatusRepository::class.java)

    suspend fun saveStatusGroup(status: Status, trackId: UUID): StatusDAO {
        val statusStack = buildStatusStack(status)
        this.logger.debug("Initial status stack size: {}", statusStack.size)

        val firstStatus = DatabaseFactory.dbQuery {
            // Ensure that we have at least one status
            var statusDAO = StatusDAO(UUID.randomUUID(), statusStack.peek(), null)
            do {
                statusStack.pop()
                this.logger.info("Saving status: {}", statusDAO.name)
                StatusTable.insert {
                    it[id] = statusDAO.id
                    it[name] =  statusDAO.name
                    it[next] = statusDAO.next
                    it[track] = trackId
                }
                if (!statusStack.isEmpty()) {
                    // We peek here and pop on top of the loop because the first assignment is needed above the loop
                    statusDAO = StatusDAO(UUID.randomUUID(), statusStack.peek(), statusDAO.name)
                    this.logger.debug("Status stack size: {}", statusStack.size)
                }
            } while (!statusStack.isEmpty())

            return@dbQuery statusDAO
        }

        return firstStatus
    }

    suspend fun getStatuses(trackId: UUID): List<StatusDAO> {
        return DatabaseFactory.dbQuery {
            val statusDAOs = StatusTable.select { StatusTable.track eq trackId }.map {
                StatusDAO.fromResultRow(it)
            }

            if(statusDAOs.isEmpty()) {
                throw StatusNotFoundException("Mal-configured track, no statuses found")
            }

            return@dbQuery statusDAOs
        }
    }
}