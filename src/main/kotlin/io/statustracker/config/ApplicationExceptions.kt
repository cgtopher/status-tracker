package io.statustracker.config

open class ApplicationException(msg: String, exception: Exception? = null) : Exception(msg, exception)
open class DatabaseException(msg: String, exception: Exception? = null) : Exception(msg, exception)
open class NotFoundException(msg: String, exception: Exception? = null) : Exception(msg, exception)
