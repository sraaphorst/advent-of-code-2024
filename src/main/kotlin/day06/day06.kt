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

private operator fun Point.plus(other: Point): Point =
    (first + other.first) to (second + other.second)

data class MapGrid(val rows: Int,
                   val cols: Int,
                   val boundaries: Set<Point>) {
    fun boundary(point: Point): Boolean =
        point in boundaries

    fun outOfBounds(point: Point) =
        (point.first !in 0 until rows) || (point.second !in 0 until cols)
}

data class Guard(private var location: Point,
                 private var direction: Direction,
                 private val map: MapGrid) {
    // The locations that were visited.
    private val visited = mutableSetOf<Point>(location)

    /**
     * Move the guard and determine if the guard can continue to move.
     */
    fun move(): Boolean {
        val newLocation = location + direction.delta

        // Did the guard move out of bounds?
        if (map.outOfBounds(newLocation)) {
            return false
        }

        // Did the guard hit a boundary, in which case, we rotate?
        if (map.boundary(newLocation))
            direction = direction.clockwise()
        else {
            location = newLocation
            visited.add(location)
        }
        return true
    }

    fun spacesVisited(): Int =
        visited.size

    fun debug() {
        println("Map: rows=${map.rows}, cols=${map.cols}")
        (0 until map.rows).forEach { row ->
            (0 until map.cols).forEach { col ->
                val point = Point(row, col)
                if (map.boundary(point))
                    print('#')
                else if (point in visited)
                    print('X')
                else
                    print('.')
            }
            println()
        }
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


fun answer1(guard: Guard): Int {
    tailrec fun aux(): Int =
        if (!guard.move()) guard.spacesVisited()
        else aux()
    return aux()
}

//fun answer2(input: String): Int = TODO()


fun main() {
    val input = parseProblem(readInput({}::class.day()).trim())

    println("--- Day 6: Guard Gallivant ---")

    // Answer 1: 5208
    println("Part 1: ${answer1(input)}")

    // Answer 2:
//    println("Part 2: ${answer2(input)}")
}