package io.statustracker.track.status

import java.util.*

fun buildStatusStack(status: Status): Stack<String> {
    val statusStack: Stack<String> = Stack()
    var curStatus: Status? = status
    while (curStatus != null) {
        statusStack.push(curStatus.name)
        curStatus = curStatus.next
    }

    return statusStack
}

fun Status.toList(): List<String> {
    val statuses = mutableListOf<String>()
    var curStatus: Status? = this
    while (curStatus != null) {
        statuses.add(curStatus.name)
        curStatus = curStatus.next
    }
    return statuses
}