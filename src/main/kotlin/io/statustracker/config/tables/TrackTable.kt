package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table

object TrackTable: Table("tracks") {
    val id = uuid("id")
    val name = varchar("name", 250).uniqueIndex()
    val endTTL = integer("end_ttl")
    val deadTTL = integer("dead_ttl")
    val errorTrack = uuid("error_track_id").nullable().default(null)

    override val primaryKey = PrimaryKey(id)
}
