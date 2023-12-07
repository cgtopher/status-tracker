package io.statustracker.track.builder

abstract class TrackBuilderBase<T> {
    protected var startStatus: StatusDefinition? = null
    private var cur: StatusDefinition? = null
    protected var name: String? = null
    protected var endTTL: Int? = null
    protected var deadTTL: Int? = null

    fun addStep(status: String) {
        val newStatus = StatusDefinition(status)
        if(startStatus == null) {
            this.startStatus = newStatus
        } else {
            this.cur?.next = newStatus
        }

        this.cur = newStatus
    }

    fun addSteps(statuses: Set<String>) {
        statuses.forEach { this.addStep(it) }
    }

    abstract fun step(status: String): T
    abstract fun steps(statuses: Set<String>): T
    abstract fun name(name: String): T
    abstract fun deadTTL(deadTTL: Int?): T
    abstract fun endTTL(endTTL: Int?): T
}