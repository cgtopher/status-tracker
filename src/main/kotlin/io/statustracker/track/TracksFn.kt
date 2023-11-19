package io.statustracker.track

fun List<String>.validateNoDupes() = this.groupBy { it }.filter { it.value.size > 1 }.isNotEmpty()