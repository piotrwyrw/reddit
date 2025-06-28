package org.piotrwyrw.redkit.model

data class ProjectStats(val name: String, val flowSize: Int, val areaCount: Int)

enum class FlowType {
    EVENT
}

enum class EventType {
    ENTER_AREA,
    LEAVE_AREA
}

enum class SubjectType {
    PLAYER
}

enum class StepType {
    MESSAGE,
    EFFECT,
    TELEPORT,
    KILL
}

class Project {
    lateinit var flow: MutableList<FlowSection>
    lateinit var areas: MutableMap<String, Area>
}

class FlowSection {
    lateinit var trigger: TriggerSection
    lateinit var action: ActionSection
}

class TriggerSection {
    lateinit var type: FlowType
    lateinit var options: Map<String, Object>
}

class ActionSection {
    lateinit var steps: List<ActionStep>
}

class ActionStep {
    lateinit var type: StepType
    lateinit var options: Map<String, Object>
}

class Point() {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    constructor(x: Double, y: Double, z: Double) : this() {
        this.x = x
        this.y = y
        this.z = z
    }
}

class Area() {
    lateinit var from: Point
    lateinit var to: Point

    constructor(from: Point, to: Point) : this() {
        this.from = from
        this.to = to
    }
}