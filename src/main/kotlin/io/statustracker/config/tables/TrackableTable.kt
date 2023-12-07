package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table

object TrackableTable: Table("trackables") {
    val id = uuid("id")
    val trackId = uuid("track_id").references(TrackTable.id)

    override val primaryKey = PrimaryKey(id)
}