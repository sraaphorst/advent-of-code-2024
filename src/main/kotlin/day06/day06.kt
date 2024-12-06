// Advent of Code 2024, Day 06.
// By Sebastian Raaphorst, 2024.

// NOTE: Trying to use pure FP in this question made part 2 run extremely slowly.
// Mutable data structures are needed to avoid having to continuously copy structures.

package day06

import common.day
import common.readInput

typealias Point = Pair<Int, Int>

/**
 * The direction that the guard is facing and moves in.
 */
enum class Direction(val delta: Point) {
    NORTH(-1 to 0),
    SOUTH(1 to 0),
    EAST(0 to 1),
    WEST(0 to -1);

    fun clockwise(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}

private operator fun Point.plus(other: Point): Point =
    (first + other.first) to (second + other.second)

typealias Orientation = Pair<Direction, Point>

data class MapGrid(val rows: Int,
                   val cols: Int,
                   val boundaries: Set<Point>) {
    fun isBoundary(point: Point): Boolean =
        point in boundaries

    fun isOutOfBounds(point: Point): Boolean =
        point.first !in 0 until rows || point.second !in 0 until cols
}

data class Guard(val startPosition: Point,
                 val map: MapGrid) {
    /**
     * Simulate the guard's path and return either:
     * 1. A set of visited points if it escapes.
     * 2. Null if it enters a loop.
     */
    fun move(addedPoint: Point? = null): Set<Point>? {
        val visitedPoints = mutableSetOf<Point>()
        val visitedStates = mutableSetOf<Orientation>()

        var currentPosition = startPosition
        var currentDir = Direction.NORTH

        while (!map.isOutOfBounds(currentPosition)) {
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
fun parseProblem(input: String): Guard {
    var startPosition: Point? = null
    val barriers = mutableSetOf<Point>()

    input.trim().lines().forEachIndexed { x, row ->
        row.forEachIndexed { y, ch -> when (ch) {
            '^' -> startPosition = Point(x, y)
            '#' -> barriers.add(Point(x, y))
        } }
    }

    val rows = input.lines().size
    val cols = input.lines().first().length
    val map = MapGrid(rows, cols, barriers)
    return Guard(startPosition ?: error("No start position found"), map)
}

fun answer1(guard: Guard): Int =
    guard.move()?.size ?: error("Could not calculate")

fun answer2(guard: Guard): Int {
    val originalPath = guard.move() ?: error("Could not calculate")

    // We want the number of nulls, i.e. the number of times the guard gets in a cycle.
    return originalPath.count { candidatePoint ->
        guard.move(candidatePoint) == null
    }
}

fun main() {
    val input = parseProblem(readInput({}::class.day()).trim())

    println("--- Day 6: Guard Gallivant ---")

    // Part 1: 5208
    println("Part 1: ${answer1(input)}")

    // Part 2: 1972
    println("Part 2: ${answer2(input)}")
}
