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
