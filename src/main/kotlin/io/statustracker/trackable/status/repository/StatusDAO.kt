package io.statustracker.trackable.status.repository

import io.statustracker.config.tables.StatusTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

data class StatusDAO(
    val name: String,
    val next: String?,
    val trackId: UUID
) {
    companion object {
        fun fromResultRow(row: ResultRow) = StatusDAO(
            row[StatusTable.name],
            row[StatusTable.next],
            row[StatusTable.track]
        )

    }
}