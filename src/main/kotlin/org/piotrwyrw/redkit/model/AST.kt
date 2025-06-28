package org.piotrwyrw.redkit.model

import kotlin.math.max
import kotlin.math.min

data class ProjectStats(val name: String, val flowSize: Int, val areaCount: Int)

enum class TriggerCause {
    ENTER_AREA
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
    lateinit var on: TriggerCause
    lateinit var options: Map<String, Object>
}

class ActionSection {
    lateinit var steps: List<ActionStep>
}

class ActionStep {
    lateinit var type: String
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

data class SelectorArea(val x: Double, val y: Double, val z: Double, val dx: Double, val dy: Double, val dz: Double) {
    override fun toString(): String {
        return "x=$x, y=$y, z=$z, dx=$dx, dy=$dy, dz=$dz"
    }
}

class Area() {
    lateinit var from: Point
    lateinit var to: Point

    constructor(from: Point, to: Point) : this() {
        this.from = from
        this.to = to
    }

    fun boundaryMax(): Point = Point(max(from.x, to.x), max(from.y, to.y), max(from.z, to.z))

    fun boundaryMin(): Point = Point(min(from.x, to.x), min(from.y, to.y), min(from.z, to.z))

    fun selectorArea(): SelectorArea {
        val min = boundaryMin()
        val max = boundaryMax()
        val dx = max.x - min.x
        val dy = max.y - min.y
        val dz = max.z - min.z
        return SelectorArea(min.x, min.y, min.z, dx, dy, dz)
    }

}