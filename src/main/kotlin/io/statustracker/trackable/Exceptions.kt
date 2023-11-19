package io.statustracker.trackable

import kotlin.Exception

open class TrackableTransitionException(msg: String, exception: Exception? = null) : Exception(msg, exception)