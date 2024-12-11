// Advent of Code 2024, Day 06.
// By Sebastian Raaphorst, 2024.

package day06

import common.aocreader.fetchAdventOfCodeInput
import common.intpos2d.*
import common.runner.timedFunction

private typealias Orientation = Pair<Direction, IntPos2D>

private data class MapGrid(val rows: Int,
                           val cols: Int,
                           val boundaries: Set<IntPos2D>) {
    fun isBoundary(point: IntPos2D): Boolean =
        point in boundaries

    fun isInBounds(point: IntPos2D): Boolean =
        point.first in 0 until rows && point.second in 0 until cols
}

private data class Guard(val startPosition: IntPos2D,
                         val map: MapGrid) {
    /**
     * Simulate the guard's path and return either:
     * 1. A set of visited points if it escapes.
     * 2. Null if it enters a loop.
     */
    fun move(addedPoint: IntPos2D? = null): Set<IntPos2D>? {
        val visitedPoints = mutableSetOf<IntPos2D>()
        val visitedStates = mutableSetOf<Orientation>()

        var currentPosition = startPosition
        var currentDir = Direction.NORTH

        while (map.isInBounds(currentPosition)) {
            visitedPoints.add(currentPosition)

            // If we return to a state we already visited, we have detected a cycle.
            val state = currentDir to currentPosition
            if (state in visitedStates) return null
            visitedStates.add(state)

            // Move forward or turn if hitting a boundary
            val nextPosition = currentPosition + currentDir.delta
            if (map.isBoundary(nextPosition) || (addedPoint != null && nextPosition == addedPoint))
                currentDir = currentDir.clockwise()
            else
                currentPosition = nextPosition
        }
        return visitedPoints
    }
}

/**
 * Parse the input into a Guard and MapGrid.
 */
private fun parse(input: String): Guard {
    var startPosition: IntPos2D? = null
    val barriers = mutableSetOf<IntPos2D>()

    input.trim().lines().forEachIndexed { x, row ->
        row.forEachIndexed { y, ch -> when (ch) {
            '^' -> startPosition = IntPos2D(x, y)
            '#' -> barriers.add(IntPos2D(x, y))
        } }
    }

    val rows = input.lines().size
    val cols = input.lines().first().length
    val map = MapGrid(rows, cols, barriers)
    return Guard(startPosition ?: error("No start position found"), map)
}

fun answer1(input: String): Int =
    parse(input).move()?.size ?: error("Could not calculate")

fun answer2(input: String): Int =
    parse(input).let { guard ->
        guard.move()?.count { guard.move(it) == null } ?: error("Could not calculate")
    }

fun main() {
    val input = fetchAdventOfCodeInput(2024, 6)
    println("--- Day 6: Guard Gallivant ---")
    timedFunction("Part 1") { answer1(input) } // 5208
    timedFunction("Part 2") { answer2(input) } // 1972
}
