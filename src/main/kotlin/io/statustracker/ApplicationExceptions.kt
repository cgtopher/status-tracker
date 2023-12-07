package io.statustracker
import io.ktor.http.*
import io.statustracker.config.HttpStatusCodeSerializer
import io.statustracker.config.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

open class ApplicationException(msg: String, exception: Exception? = null) : Exception(msg, exception)
open class DatabaseException(msg: String, exception: Exception? = null) : Exception(msg, exception)

@Serializable
data class ExceptionDTO(
    val message: String,
    @Serializable(with = HttpStatusCodeSerializer::class)
    val code: HttpStatusCode,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateTime: LocalDateTime = LocalDateTime.now()
)
