// Advent of Code 2024
// By Sebastian Raaphorst, 2024.

package common.gridalgorithms

import common.intpos2d.Direction
import common.intpos2d.*

typealias Grid<T> = List<List<T>>

operator fun <T> Grid<T>.contains(pos: IntPos2D): Boolean =
    pos.first in this.indices && pos.second in this[pos.first].indices

operator fun <T> Grid<T>.get(pos: IntPos2D): T? =
    if (pos in this) this[pos.first][pos.second] else null

fun <T> Grid<T>.neighbourPositions(pos: IntPos2D): Set<IntPos2D> =
    Direction.entries.map { pos + it.delta }
        .filter { it in this }
        .toSet()

// Get the value neighbours, and not the position neighbours.
fun <T> Grid<T>.neighbourValues(pos: IntPos2D): Set<T> =
    Direction.entries.mapNotNull { this[pos + it.delta] }.toSet()
