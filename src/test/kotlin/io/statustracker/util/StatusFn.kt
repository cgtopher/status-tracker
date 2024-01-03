package io.statustracker.util

import kotlin.random.Random

fun getRandomStatuses(listSize: Int, minStringSize: Int, maxStringSize: Int): List<String> {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    // iterate up to specified list size
    val statuses = (0..<listSize).map {
        // random string size from min to max specified
        (minStringSize..Random.nextInt(minStringSize,maxStringSize)).map {
            // random character from pool
            Random.nextInt(0, charPool.size).let { charPool[it] }
        }.joinToString("")
    }.toSet() // ensure uniqueness

    return statuses.toList()
}