package io.statustracker.trackable.repository

import io.statustracker.config.DatabaseFactory
import io.statustracker.config.tables.TrackableTable
import io.statustracker.trackable.Trackable
import org.jetbrains.exposed.sql.insert

class TrackableRepository {
    suspend fun log(trackable: Trackable) {
        DatabaseFactory.dbQuery {
            TrackableTable.insert {
                it[id] = trackable.id
                it[latestStatus] = trackable.status.current
                it[track] = trackable.status.trackId
            }
        }
    }
}