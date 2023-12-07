package io.statustracker.trackable

import kotlin.Exception

open class TrackableException(msg: String, exception: Exception? = null) : Exception(msg, exception)
open class TrackableConfigurationException(msg: String, exception: Exception? = null): Exception(msg, exception)
open class TrackableNotFoundException(msg: String, exception: Exception? = null): Exception(msg, exception)