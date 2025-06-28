package org.piotrwyrw.redkit.extensions

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.piotrwyrw.redkit.RedKit
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun Location.particleLine(toX: Double, toY: Double, toZ: Double, color: Color) {
    val from = clone()
    val to = Location(this.world, toX, toY, toZ)
    val diff = to.toVector().subtract(from.toVector())
    val length = diff.length()
    if (length == 0.0) return

    val dir = diff.normalize().multiply(0.1)
    val dust = DustOptions(color, 0.4f)
    val point = from.clone()

    var t = 0.0
    while (t < length) {
        world!!.spawnParticle(Particle.REDSTONE, point, 0, 0.0, 0.0, 0.0, dust)
        point.add(dir)
        t += 0.1
    }
}
fun Location.particleLine(to: Location, color: Color) = this.particleLine(to.x, to.y, to.z, color)

fun minimum(a: Location, b: Location): Location {
    return Location(a.world, min(a.x, b.x), min(a.y, b.y), min(a.z, b.z))
}

fun maximum(a: Location, b: Location): Location {
    return Location(a.world, max(a.x, b.x), max(a.y, b.y), max(a.z, b.z))
}

fun Location.particleBox(to: Location, color: Color) {
    val min = minimum(this, to)
    val max = maximum(this, to)

    val a = Location(this.world, min.x, max.y, min.z)
    val b = Location(this.world, max.x, max.y, min.z)
    val c = Location(this.world, max.x, min.y, min.z)
    val d = Location(this.world, min.x, min.y, min.z)

    val corners = arrayOf(a, b, c, d)
    val count = corners.size
    corners.forEachIndexed { i, it ->
        val next = corners[(i + 1) % count]
        it.particleLine(next, color)
        Location(it.world, it.x, it.y, max.z).particleLine(next.x, next.y, max.z, color)
        it.particleLine(Location(it.world, it.x, it.y, max.z), color)
    }
}

fun Location.particleSuzan() {
    val vertices = mutableListOf<List<Double>>()
    val faces = mutableListOf<List<Int>>()

    File(RedKit.getInstance().dataFolder, "suzan.obj").readLines()
        .filter { it.startsWith("v ") || it.startsWith("f ") }
        .forEach {
            if (it.startsWith("v ")) {
                val parts = it.split("\\s+".toRegex()).drop(1).map { s -> s.toDouble() }
                if (parts.size == 3) vertices.add(parts)
            } else {
                val indices = it.split("\\s+".toRegex()).drop(1).map { s -> s.toInt() - 1 }
                faces.add(indices)
            }
        }

    for (face in faces) {
        for (i in face.indices) {
            val fromVert = vertices[face[i]]
            val toVert = vertices[face[(i + 1) % face.size]]
            val from = clone().add(fromVert[0], fromVert[1], fromVert[2])
            val to = clone().add(toVert[0], toVert[1], toVert[2])
            from.particleLine(to, Color.RED)
        }
    }
}