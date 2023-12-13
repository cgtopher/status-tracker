package io.statustracker.trackable.repository

import io.statustracker.config.tables.TrackableLogTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID


data class TrackableDAO(
    val id: UUID,
    val latestStatus: String,
    val trackId: UUID
) {
    companion object {
        fun fromResultRow(row: ResultRow): TrackableDAO {
            return TrackableDAO(
                row[TrackableLogTable.id],
                row[TrackableLogTable.latestStatus],
                row[TrackableLogTable.trackableId]
            )
        }
    }
}