package io.statustracker.config.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object TrackableLogTable: Table("trackable_logs") {
    val id = uuid("id")
    val latestStatus = varchar("latest_status", 250).index("idx_latest_status_trackable_id")
    val trackable = uuid("trackable_id").references(TrackableTable.id).index("idx_latest_status_trackable_id")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
