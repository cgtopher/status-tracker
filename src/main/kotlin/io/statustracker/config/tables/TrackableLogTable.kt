package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object TrackableLogTable: Table("trackable_logs") {
    val id = uuid("id")
    val latestStatus = varchar("latest_status", 250)
    val trackable = uuid("trackable_id").references(TrackableTable.id)
    @Suppress("unused")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)

    init {
        index(isUnique = true, latestStatus, trackable)
    }
}
