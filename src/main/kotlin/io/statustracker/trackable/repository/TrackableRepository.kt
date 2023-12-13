package io.statustracker.trackable.repository

import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.TrackTable
import io.statustracker.config.tables.TrackableLogTable
import io.statustracker.config.tables.TrackableTable
import io.statustracker.trackable.Trackable
import io.statustracker.trackable.TrackableConfigurationException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.*

class TrackableRepository {
    suspend fun log(trackable: Trackable) {
        DatabaseFactory.dbQuery {
            val trackableReference = TrackableTable.select { TrackableTable.id.eq(trackable.id) }.singleOrNull()

            if(trackableReference == null) {
                val trackRow = TrackTable.select { TrackTable.name.eq(trackable.track) }.singleOrNull()
                if(trackRow == null) {
                    throw TrackableConfigurationException("Track ${trackable.track} not found, unable to log interaction")
                }
                TrackableTable.insert {
                    it[id] = trackable.id
                    it[trackId] = trackRow[TrackTable.id]
                }
            }


            TrackableLogTable.insert {
                it[id] = UUID.randomUUID()
                it[latestStatus] = trackable.status.current
                it[trackableId] = trackable.id
            }
        }
    }
}