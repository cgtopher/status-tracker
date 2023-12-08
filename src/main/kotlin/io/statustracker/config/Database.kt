package io.statustracker.config

import io.statustracker.config.tables.DATABASE_TABLES
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(url: String, username: String, password: String) {
        val driverClassName = "org.postgresql.Driver"
        val database = Database.connect(url, driverClassName, username, password)
        //@todo Do with actual migration system, like liquibase
        transaction(database) {
            DATABASE_TABLES.forEach { SchemaUtils.create(it) }
        }
    }


    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
