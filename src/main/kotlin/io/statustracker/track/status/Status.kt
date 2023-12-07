package io.statustracker.track.status

data class Status(
    val name: String,
    val next: Status? = null
) {
    fun toList(): List<String> {
        val statuses = mutableListOf<String>()
        var curStatus: Status? = this
        while (curStatus != null) {
            statuses.add(curStatus.name)
            curStatus = curStatus.next
        }
        return statuses
    }
}
