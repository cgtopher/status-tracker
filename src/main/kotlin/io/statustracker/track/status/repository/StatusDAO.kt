package io.statustracker.track.status.repository

import io.statustracker.config.tables.StatusTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID


data class StatusDAO(
    val id: UUID,
    val name: String,
    val next: String?,
) {
    companion object {
        fun fromResultRow(row: ResultRow) = StatusDAO(
            row[StatusTable.id],
            row[StatusTable.name],
            row[StatusTable.next],
        )
    }
}