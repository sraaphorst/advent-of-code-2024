// Advent of Code 2024.
// By Sebastian Raaphorst, 2024.

package common.vec2d

import java.math.BigInteger

private enum class NumberType { INT, LONG, BIG_INTEGER }

private interface Arithmetic<T : Number> {
    val type: NumberType
    fun add(a: T, b: T): T
    fun unaryMinus(a: T): T
    fun subtract(a: T, b: T): T
    fun multiply(a: T, b: T): T
    fun divide(a: T, b: T): T
    fun modulo(a: T, b: T): T
    fun pair(a: T, b: T): Pair<T, T>
}

private object IntArithmetic : Arithmetic<Int> {
    override val type = NumberType.INT
    override fun add(a: Int, b: Int) = a + b
    override fun unaryMinus(a: Int): Int = -a
    override fun subtract(a: Int, b: Int) = a - b
    override fun multiply(a: Int, b: Int) = a * b
    override fun divide(a: Int, b: Int) = a / b
    override fun modulo(a: Int, b: Int) = a % b
    override fun pair(a: Int, b: Int): Pair<Int, Int> = Pair(a, b)
}

private object LongArithmetic : Arithmetic<Long> {
    override val type = NumberType.LONG
    override fun add(a: Long, b: Long) = a + b
    override fun unaryMinus(a: Long): Long = -a
    override fun subtract(a: Long, b: Long) = a - b
    override fun multiply(a: Long, b: Long) = a * b
    override fun divide(a: Long, b: Long) = a / b
    override fun modulo(a: Long, b: Long) = a % b
    override fun pair(a: Long, b: Long): Pair<Long, Long> = Pair(a, b)
}

private object BigIntegerArithmetic : Arithmetic<BigInteger> {
    override val type = NumberType.BIG_INTEGER
    override fun add(a: BigInteger, b: BigInteger) = a + b
    override fun unaryMinus(a: BigInteger): BigInteger = -a
    override fun subtract(a: BigInteger, b: BigInteger) = a - b
    override fun multiply(a: BigInteger, b: BigInteger) = a * b
    override fun divide(a: BigInteger, b: BigInteger) = a / b
    override fun modulo(a: BigInteger, b: BigInteger) = a % b
    override fun pair(a: BigInteger, b: BigInteger): Pair<BigInteger, BigInteger> = Pair(a, b)
}

class Vec2D<T : Number> private constructor(
    private val arithmetic: Arithmetic<T>,
    val x: T,
    val y: T
) {
    init {
        require(arithmetic.type != NumberType.BIG_INTEGER || x is BigInteger) {
            "Mismatched type: ${arithmetic.type} expected, got ${x::class.simpleName}"
        }
    }

    operator fun plus(other: Vec2D<T>): Vec2D<T> {
        require(arithmetic.type == other.arithmetic.type) {
            "Type mismatch: ${arithmetic.type} vs ${other.arithmetic.type}"
        }
        return Vec2D(arithmetic, arithmetic.add(x, other.x), arithmetic.add(y, other.y))
    }

    operator fun unaryMinus(): Vec2D<T> =
        Vec2D(arithmetic, arithmetic.unaryMinus(x), arithmetic.unaryMinus(y))

    operator fun minus(other: Vec2D<T>): Vec2D<T> {
        require(arithmetic.type == other.arithmetic.type) {
            "Type mismatch: ${arithmetic.type} vs ${other.arithmetic.type}"
        }
        return Vec2D(arithmetic, arithmetic.subtract(x, other.x), arithmetic.subtract(y, other.y))
    }

    operator fun times(other: Vec2D<T>): Vec2D<T> {
        require(arithmetic.type == other.arithmetic.type) {
            "Type mismatch: ${arithmetic.type} vs ${other.arithmetic.type}"
        }
        return Vec2D(arithmetic, arithmetic.multiply(x, y), arithmetic.multiply(other.x, other.y))
    }

    operator fun div(other: Vec2D<T>): Vec2D<T> {
        require(arithmetic.type == other.arithmetic.type) {
            "Type mismatch: ${arithmetic.type} vs ${other.arithmetic.type}"
        }
        return Vec2D(arithmetic, arithmetic.divide(x, other.x), arithmetic.divide(y, other.y))
    }

    override fun toString(): String = "Vec2D(x=$x, y=$y)"

    // Scalar ops.
    operator fun times(scalar: T): Vec2D<T> =
        Vec2D(arithmetic, arithmetic.multiply(x, scalar), arithmetic.multiply(y, scalar))

    operator fun rem(modulus: T): Vec2D<T> =
        Vec2D(arithmetic, arithmetic.modulo(x, modulus), arithmetic.modulo(y, modulus))

    operator fun div(scalar: T): Vec2D<T> =
        Vec2D(arithmetic, arithmetic.divide(x, scalar), arithmetic.divide(y, scalar))

    infix fun dot(other: Vec2D<T>): T =
        arithmetic.add(arithmetic.multiply(x, other.x), arithmetic.multiply(y, other.y))

    // Destructing:
    operator fun component1(): T = x
    operator fun component2(): T = y

    fun toInt(): Vec2D<Int> =
        Vec2D(IntArithmetic, x.toInt(), y.toInt())
    fun toLong(): Vec2D<Long> =
        Vec2D(LongArithmetic, x.toLong(), y.toLong())
    fun toBigInteger(): Vec2D<BigInteger> = when (arithmetic.type) {
        NumberType.INT -> Vec2D(BigIntegerArithmetic, BigInteger.valueOf(x.toLong()), BigInteger.valueOf(y.toLong()))
        NumberType.LONG -> Vec2D(BigIntegerArithmetic, BigInteger.valueOf(x.toLong()), BigInteger.valueOf(y.toLong()))
        NumberType.BIG_INTEGER -> this as Vec2D<BigInteger>
    }


    companion object {
        fun int(x: Int, y: Int) = Vec2D(IntArithmetic, x, y)
        fun long(x: Long, y: Long) = Vec2D(LongArithmetic, x, y)
        fun bigInt(x: BigInteger, y: BigInteger) = Vec2D(BigIntegerArithmetic, x, y)
    }
}

operator fun <T : Number> T.times(vec: Vec2D<T>): Vec2D<T> =
    vec * this

typealias Vec2DInt = Vec2D<Int>
typealias Vec2DLong = Vec2D<Long>
typealias Vec2DBigInt = Vec2D<BigInteger>

val Vec2DIntZero = Vec2DInt.int(0, 0)
val Vec2DLongZero = Vec2DLong.long(0, 0)
val Vec2DBigIntZero = Vec2DInt.bigInt(BigInteger.ZERO, BigInteger.ZERO)

// Vector conversion ops
// Int -> Long
//inline operator fun <reified T: Number> Vec2D<T>.plus(other: Vec2D<Long>): Vec2D<Long> =
//    Vec2D.long(this.x.toLong() + other.x, this.y.toLong() + other.y)
//operator fun Vec2D<Int>.minus(other: Vec2D<Long>): Vec2D<Long> =
//    Vec2D.long(this.x.toLong() - other.x, this.y.toLong() - other.y)
//operator fun Vec2D<Int>.times(other: Vec2DLong): Vec2DLong =
//    Vec2D.long(this.x.toLong() * other.x, this.y.toLong() * other.y)
//
//operator fun Vec2DLong.plus(other: Vec2DInt): Vec2DLong =
//    other + this
//operator fun Vec2DLong.minus(other: Vec2DInt): Vec2DLong =
//    -(other - this)
//operator fun Vec2DLong.times(other: Vec2DInt): Vec2DLong =
//    other * this
//
//// Int -> BigInt
//operator fun Vec2DInt.plus(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() + other.x, this.y.toBigInteger() + other.y)
//operator fun Vec2DInt.minus(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() - other.x, this.y.toBigInteger() - other.y)
//operator fun Vec2DInt.times(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() * other.x, this.y.toBigInteger() * other.y)
//
//operator fun Vec2DBigInt.plus(other: Vec2DInt): Vec2DBigInt =
//    other + this
//operator fun Vec2DBigInt.minus(other: Vec2DInt): Vec2DBigInt =
//    -(other - this)
//operator fun Vec2DBigInt.times(other: Vec2DInt): Vec2DBigInt =
//    other * this
//
//// Long -> BigInt
//operator fun Vec2DLong.plus(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() + other.x, this.y.toBigInteger() + other.y)
//operator fun Vec2DLong.minus(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() - other.x, this.y.toBigInteger() - other.y)
//operator fun Vec2DLong.times(other: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.x.toBigInteger() * other.x, this.y.toBigInteger() * other.y)
//
//operator fun Vec2DBigInt.plus(other: Vec2DLong): Vec2DBigInt =
//    other + this
//operator fun Vec2DBigInt.minus(other: Vec2DLong): Vec2DBigInt =
//    -(other - this)
//operator fun Vec2DBigInt.times(other: Vec2DLong): Vec2DBigInt =
//    other * this
//
//// Scalar conversion ops.
//operator fun Int.plus(vec: Vec2DLong): Vec2DLong =
//    Vec2D.long(this.toLong() + vec.x, this.toLong() + vec.y)
//operator fun Int.times(vec: Vec2DLong): Vec2DLong =
//    Vec2D.long(this.toLong() * vec.x, this.toLong() * vec.y)
//
//operator fun Int.plus(vec: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.toBigInteger() + vec.x, this.toBigInteger() + vec.y)
//operator fun Int.times(vec: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.toBigInteger() * vec.x, this.toBigInteger() * vec.y)
//
//operator fun Long.plus(vec: Vec2DInt): Vec2DLong =
//    Vec2D.long(this + vec.x.toLong(), this + vec.y.toLong())
//operator fun Long.times(vec: Vec2DInt): Vec2DLong =
//    Vec2D.long(this * vec.x.toLong(), this * vec.y.toLong())
//
//operator fun Long.plus(vec: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.toBigInteger() + vec.x, this.toBigInteger() + vec.y)
//operator fun Long.times(vec: Vec2DBigInt): Vec2DBigInt =
//    Vec2D.bigInt(this.toBigInteger() * vec.x, this.toBigInteger() * vec.y)
//
//operator fun BigInteger.plus(vec: Vec2DInt): Vec2DBigInt =
//    Vec2D.bigInt(this + vec.x.toBigInteger(), this + vec.y.toBigInteger())
//operator fun BigInteger.times(vec: Vec2DInt): Vec2DBigInt =
//    Vec2D.bigInt(this * vec.x.toBigInteger(), this * vec.y.toBigInteger())
//
//operator fun BigInteger.plus(vec: Vec2DLong): Vec2DBigInt =
//    Vec2D.bigInt(this + vec.x.toBigInteger(), this + vec.y.toBigInteger())
//operator fun BigInteger.times(vec: Vec2DLong): Vec2DBigInt =
//    Vec2D.bigInt(this * vec.x.toBigInteger(), this * vec.y.toBigInteger())
//
//// Modulus and division
//operator fun Vec2DLong.rem(modulus: Int): Vec2DLong =
//    Vec2D.long(this.x % modulus, this.y % modulus)
//operator fun Vec2DLong.div(denominator: Int): Vec2DLong =
//    Vec2D.long(this.x / denominator, this.y / denominator)
//
//operator fun Vec2DBigInt.rem(modulus: Int): Vec2DBigInt =
//    Vec2D.bigInt(this.x % modulus.toBigInteger(), this.y % modulus.toBigInteger())
//operator fun Vec2DBigInt.div(denominator: Int): Vec2DBigInt =
//    Vec2D.bigInt(this.x % denominator.toBigInteger(), this.y % denominator.toBigInteger())
//
//operator fun Vec2DBigInt.rem(modulus: Long): Vec2DBigInt =
//    Vec2D.bigInt(this.x % modulus.toBigInteger(), this.y % modulus.toBigInteger())
//operator fun Vec2DBigInt.div(denominator: Long): Vec2DBigInt =
//    Vec2D.bigInt(this.x % denominator.toBigInteger(), this.y % denominator.toBigInteger())


enum class Direction(val delta: Vec2D<Int>) {
    NORTH(Vec2D.int(-1, 0)),
    EAST(Vec2D.int(0, 1)),
    SOUTH(Vec2D.int(1, 0)),
    WEST(Vec2D.int(0, -1));

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

val Diagonals: Set<Pair<Direction, Direction>> = setOf(
    Pair(Direction.NORTH, Direction.WEST),
    Pair(Direction.WEST, Direction.SOUTH),
    Pair(Direction.SOUTH, Direction.EAST),
    Pair(Direction.EAST, Direction.NORTH)
)
