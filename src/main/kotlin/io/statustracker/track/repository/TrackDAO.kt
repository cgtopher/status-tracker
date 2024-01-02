package io.statustracker.track.repository

import io.statustracker.config.tables.TrackTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*


data class TrackDAO(
    val id: UUID,
    val name: String,
    val endTTL: Int,
    val deadTTL: Int
) {
    companion object {
        fun fromResultRow(row: ResultRow) =
            TrackDAO(
                row[TrackTable.id],
                row[TrackTable.name],
                row[TrackTable.endTTL],
                row[TrackTable.deadTTL]
            )
    }
}