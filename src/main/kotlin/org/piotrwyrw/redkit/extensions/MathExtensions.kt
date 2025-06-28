package org.piotrwyrw.redkit.extensions

import org.bukkit.Location
import org.piotrwyrw.redkit.model.Point
import kotlin.math.round

fun Location.round() = Location(world, round(x), round(y), round(z))

fun Location.asPoint() = Point(x, y, z)
