package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table

object StatusTable: Table("statuses") {
    val id = uuid("id")
    val name = varchar("name", 250)
    val next = varchar("next", 250).nullable()
    val track = uuid("track_id").references(TrackTable.id)

    val trackNameIndex = index(true, track, name)

    override val primaryKey = PrimaryKey(id, name)
}
