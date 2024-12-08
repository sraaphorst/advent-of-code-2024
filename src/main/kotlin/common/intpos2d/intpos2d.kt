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
