package io.statustracker.config

import io.ktor.http.*
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ExceptionDTO(
    val message: String,
    @Serializable(with = HttpStatusCodeSerializer::class)
    val code: HttpStatusCode,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateTime: LocalDateTime = LocalDateTime.now()
)