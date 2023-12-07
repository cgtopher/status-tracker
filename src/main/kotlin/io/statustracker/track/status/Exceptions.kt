package io.statustracker.track.status

open class StatusException(msg: String, exception: Exception? = null) : Exception(msg, exception)
open class StatusNotFoundException(msg: String, exception: Exception? = null): Exception(msg, exception)