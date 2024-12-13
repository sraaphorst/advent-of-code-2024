// Advent of Code 2024, Day 13.
// By Sebastian Raaphorst, 2024.

package day13

import common.aocreader.fetchAdventOfCodeInput
import common.intpos2d.*
import common.runner.timedFunction
import java.math.BigInteger

private typealias BigIntPos = Pair<BigInteger, BigInteger>

data class Machine(val deltaA: IntPos2D, val deltaB: IntPos2D, val prizePos: IntPos2D) {
    /**
     * Let a be the number of times we hit button A, and b be the number of times we hit button B.
     * We want an integer solution to the equation:
     * [dAx  dBx] (a) = (px)
     * [dAy  dBy] (b)   (py)
     */
    fun calculateSolution(adjustment: BigIntPos): BigIntPos? {
        val determinant = deltaA.x() * deltaB.y() - deltaB.x() * deltaA.y()
        if (determinant == 0) return null
        val detBigInteger = determinant.toBigInteger()

        val adjPx = prizePos.x().toBigInteger() + adjustment.first
        val adjPy = prizePos.y().toBigInteger() + adjustment.second
        val abDet = BigIntPos(
            deltaB.y().toBigInteger() * adjPx - deltaB.x().toBigInteger() * adjPy,
            deltaA.x().toBigInteger() * adjPy - deltaA.y().toBigInteger() * adjPx)

        val detX = abDet.first % detBigInteger
        val detY = abDet.second % detBigInteger

        return if (detX == BigInteger.ZERO && detY == BigInteger.ZERO)
            BigIntPos(abDet.first / detBigInteger, abDet.second / detBigInteger)
        else null
    }

    companion object {
        val TokenCostA = 3.toBigInteger()
        val TokenCostB = BigInteger.ONE

        private val MachineRegex = """[XY][+=](\d+)""".toRegex()

        fun parse(input: String): Machine {
            val matches = MachineRegex.findAll(input).map { it.groupValues[1].toInt() }.toList()
            val (deltaA, deltaB, prizePos) = matches.chunked(2).map { IntPos2D(it[0], it[1]) }
            return Machine(deltaA, deltaB, prizePos)
        }
    }
}

fun parse(input: String): List<Machine> =
    input.split("""[\r\n]{2}""".toRegex()).map { Machine.parse(it.trim()) }

fun answer(input: String, adjustment: BigIntPos = BigIntPos(BigInteger.ZERO, BigInteger.ZERO)): BigInteger =
    parse(input)
        .mapNotNull { it.calculateSolution(adjustment) }
        .sumOf { sol -> sol.first * Machine.TokenCostA + sol.second * Machine.TokenCostB }

fun answer1(input: String): BigInteger =
    answer(input)

private val adjustment2 = BigInteger("10000000000000")

fun answer2(input: String): BigInteger =
    answer(input, BigIntPos(adjustment2, adjustment2))

fun main() {
    val input = fetchAdventOfCodeInput(2024, 13)
    println("--- Day 13: Claw Contraption ---")
    timedFunction("Part 1") { answer1(input) } // 28262
    timedFunction("Part 2") { answer2(input) } // 101406661266314
}
