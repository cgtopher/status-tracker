package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table

object TrackableTable: Table("trackables") {
    val id = uuid("id")
    val latestStatus = varchar("latest_status", 250)
    val track = uuid("status_id").references(TrackTable.id)

    override val primaryKey = PrimaryKey(id, latestStatus)
}
