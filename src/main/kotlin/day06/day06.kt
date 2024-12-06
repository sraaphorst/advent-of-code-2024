// Advent of Code 2024, Day 06.
// By Sebastian Raaphorst, 2024.

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

typealias Orientation = Pair<Direction, Point>
typealias Orientations = Set<Orientation>

private operator fun Point.plus(other: Point): Point =
    (first + other.first) to (second + other.second)

data class MapGrid(val rows: Int,
                   val cols: Int,
                   val boundaries: Set<Point>) {
    fun boundary(point: Point): Boolean =
        point in boundaries

    fun outOfBounds(point: Point) =
        (point.first !in 0 until rows) || (point.second !in 0 until cols)

    // The empty squares where a boundary can be added to the map.
    // Note that we must manually remove the guard's starting location.
    val boundaryCandidates: Set<Point> =
        (0 until rows).flatMap { row ->
            (0 until cols).map { col ->
                Point(row, col)
            }
        }.filter { it !in boundaries }.toSet()
}

data class Guard(private val originalLocation: Point,
                 private val originalDirection: Direction,
                 private val map: MapGrid) {
    /**
     * Continue to move the guard until:
     * 1. The guard moves off the board.
     * 2. A cycle is detected.
     */
    fun move(addedPoint: Point? = null): Set<Point>? {
        tailrec fun aux(visitedPoints: Set<Point> = emptySet(),
                        orientations: Orientations = emptySet(),
                        currentDirection: Direction = originalDirection,
                        currentLocation: Point = originalLocation): Set<Point>? {
            // If we are off the map, then return the number of points.
            if (map.outOfBounds(currentLocation))
                return visitedPoints

            // If the guard has moved at least once and has reached a previous
            // orientation, then she is cycling.
            val currentOrientation = currentDirection to currentLocation

            // Attempt to move the guard.
            // If we have already seen this orientation, then we are cycling.
            // Return null to indicate this.
            if (currentOrientation in orientations) {
                return null
            }

            // Calculate where the guard would go if she kept traveling forward.
            val newLocation = currentLocation + currentDirection.delta

            // If the guard would hit a boundary, record the current orientation, rotate, and
            // do not move to the new location.
            if (map.boundary(newLocation) || (addedPoint != null && newLocation == addedPoint)) {
                return aux(visitedPoints,
                           orientations + currentOrientation,
                           currentDirection.clockwise(),
                           currentLocation)
            }

            // Move the guard forward, recording the location we just passed through.
            return aux(visitedPoints + currentLocation,
                       orientations + currentOrientation,
                       currentDirection,
                       newLocation)

        }

        return aux()
    }
}

/**
 * Make a sparse representation of the items in the way of the guard by representing
 * the points where there are obstacles.
 * The first Point is the guard's starting location.
 */
fun parseProblem(input: String): Guard {
    var location: Point? = null
    val barriers = mutableSetOf<Point>()

    input.trim().lines()
        .withIndex()
        .map { (xIdx, row) ->
            row.withIndex()
                .forEach { (yIdx, symbol) ->
                    when (symbol) {
                        '^' -> location = Point(xIdx, yIdx)
                        '#' -> barriers.add(Point(xIdx, yIdx))
                    }
                }
        }
    val mapRows = input.lines().size
    val colRow = input.lines().first().trim().length
    val map = MapGrid(mapRows, colRow, barriers)
    return Guard(location ?: error("No guard starting position"),
                 Direction.NORTH,
                 map)
}


fun answer1(guard: Guard): Int =
    guard.move()?.size ?: error("Could not calculate")

// The only place we can put a single obstruction are on the guard's initial path:
// Any other locations will not obstruct the guard.
fun answer2(guard: Guard): Int =
    (guard.move() ?: error("Could not calculate")).count { guard.move(it) == null }


fun main() {
    val input = parseProblem(readInput({}::class.day()).trim())

    println("--- Day 6: Guard Gallivant ---")

    // Answer 1: 5208
    println("Part 1: ${answer1(input)}")

    // Answer 2: 1972
    println("Part 2: ${answer2(input)}")
}