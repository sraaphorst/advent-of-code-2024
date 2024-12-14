// Advent of Code 2024, Day 13.
// By Sebastian Raaphorst, 2024.

package day13

import common.aocreader.fetchAdventOfCodeInput
import common.vec2d.*
import common.runner.timedFunction
import java.math.BigInteger


data class Machine(val deltaA: Vec2DInt, val deltaB: Vec2DInt, val prizePos: Vec2DInt) {
    /**
     * Let a be the number of times we hit button A, and b be the number of times we hit button B.
     * We want an integer solution to the equation:
     * [dAx  dBx] (a) = (px)
     * [dAy  dBy] (b)   (py)
     */
    fun calculateSolution(adjustment: Vec2DBigInt): Vec2DBigInt? {
        val determinant = deltaA.x * deltaB.y - deltaB.x * deltaA.y
        if (determinant == 0) return null

        // Adjusted prize pos.
        val adjPrizePos = prizePos.toBigInteger() + adjustment

        // Rows of the inverted matrix * determinant
        val detR1 = Vec2D.int(deltaB.y, -deltaB.x)
        val detR2 = Vec2D.int(-deltaA.y, deltaA.x)

        val abDet = Vec2D.bigInt(
            deltaB.y.toBigInteger() * adjPrizePos.x - deltaB.x.toBigInteger() * adjPrizePos.y,
            deltaA.x.toBigInteger() * adjPrizePos.y - deltaA.y.toBigInteger() * adjPrizePos.x)

        // Mod these by determinant, and if zero, divide
        val detBigInt = determinant.toBigInteger()
        return if (abDet % detBigInt == Vec2DBigIntZero)
            abDet / detBigInt
        else
            null
    }

    companion object {
        val TokenCost = Vec2D.bigInt(3.toBigInteger(), 1.toBigInteger())

        private val MachineRegex = """[XY][+=](\d+)""".toRegex()

        fun parse(input: String): Machine {
            val matches = MachineRegex.findAll(input).map { it.groupValues[1].toInt() }.toList()
            val (deltaA, deltaB, prizePos) = matches.chunked(2).map { Vec2D.int(it[0], it[1]) }
            return Machine(deltaA, deltaB, prizePos)
        }
    }
}

fun parse(input: String): List<Machine> =
    input.split("""[\r\n]{2}""".toRegex()).map { Machine.parse(it.trim()) }

fun answer(input: String, adjustment: Vec2DBigInt = Vec2DBigIntZero): BigInteger =
    parse(input)
        .mapNotNull { it.calculateSolution(adjustment) }
        .sumOf { sol -> sol dot Machine.TokenCost}

fun answer1(input: String): BigInteger =
    answer(input)

private val adjustment2 = BigInteger("10000000000000")

fun answer2(input: String): BigInteger =
    answer(input, Vec2D.bigInt(adjustment2, adjustment2))

fun main() {
    val input = fetchAdventOfCodeInput(2024, 13)
    println("--- Day 13: Claw Contraption ---")
    timedFunction("Part 1") { answer1(input) } // 28262
    timedFunction("Part 2") { answer2(input) } // 101406661266314
}
