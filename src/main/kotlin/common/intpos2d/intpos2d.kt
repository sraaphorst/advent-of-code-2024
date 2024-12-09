// Advent of Code 2024
// By Sebastian Raaphorst, 2024.

package common.intpos2d

typealias IntPos2D = Pair<Int, Int>

operator fun IntPos2D.plus(other: IntPos2D) =
    IntPos2D(this.first + other.first, this.second + other.second)

operator fun IntPos2D.minus(other: IntPos2D) =
    IntPos2D(this.first - other.first, this.second - other.second)

operator fun IntPos2D.unaryMinus(): IntPos2D =
    IntPos2D(-this.first, -this.second)

operator fun Int.times(pos: IntPos2D): IntPos2D =
    IntPos2D(this * pos.first, this * pos.second)

operator fun IntPos2D.times(factor: Int) =
    factor * this

enum class Direction(val delta: IntPos2D) {
    NORTH(IntPos2D(-1, 0)),
    EAST(IntPos2D(0, 1)),
    SOUTH(IntPos2D(1, 0)),
    WEST(IntPos2D(0, -1));

    fun clockwise(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun counterClockwise(): Direction = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    fun opposite(): Direction = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}
