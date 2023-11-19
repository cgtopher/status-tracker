package io.statustracker.config

import io.statustracker.config.tables.DATABASE_TABLES
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    //@todo: remove creds obviously
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://127.0.0.1:5432/statustracker"
        val database = Database.connect(jdbcURL, driverClassName, "postgres", "postgres")
        //@todo Do with actual migration system, like liquibase
        transaction(database) {
            DATABASE_TABLES.forEach { SchemaUtils.create(it) }
        }
    }


    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
