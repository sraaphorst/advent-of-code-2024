// Advent of Code 2024.
// By Sebastian Raaphorst, 2024.

package common.gridalgorithms

import common.vec2d.Direction
import common.vec2d.*

typealias Grid<T> = List<List<T>>

operator fun <T> Grid<T>.contains(pos: Vec2DInt): Boolean =
    pos.x in this.indices && pos.y in this[pos.x].indices

operator fun <T> Grid<T>.get(pos: Vec2DInt): T? =
    if (pos in this) this[pos.x][pos.y] else null

fun <T> Grid<T>.neighbourPositions(pos: Vec2DInt): Set<Vec2DInt> =
    Direction.entries.map { pos + it.delta }
        .filter { it in this }
        .toSet()

// Get the value neighbours, and not the position neighbours.
fun <T> Grid<T>.neighbourValues(pos: Vec2DInt): Set<T> =
    Direction.entries.mapNotNull { this[pos + it.delta] }.toSet()
