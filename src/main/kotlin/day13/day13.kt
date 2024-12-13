// Advent of Code 2024, Day 13.
// By Sebastian Raaphorst, 2024.

package day13

import common.aocreader.fetchAdventOfCodeInput
import common.intpos2d.*
import common.runner.timedFunction

data class Machine(val deltaA: IntPos2D, val deltaB: IntPos2D, val prizePos: IntPos2D) {
    /**
     * Let a be the number of times we hit button A, and b be the number of times we hit button B.
     * We want an integer solution to the equation:
     * [dAx  dBx] (a) = (px)
     * [dAy  dBy] (b)   (py)
     */
    fun calculateSolution(): IntPos2D? {
        val determinant = deltaA.x() * deltaB.y() - deltaB.x() * deltaA.y()
        if (determinant == 0) return null

        val abDet = IntPos2D(
            deltaB.y() * prizePos.x() - deltaB.x() * prizePos.y(),
            deltaA.x() * prizePos.y() - deltaA.y() * prizePos.x())

        return if (abDet % determinant != Zero2D) null else abDet / determinant
    }



    companion object {
        val TokenCost = IntPos2D(3, 1)

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

fun answer1(input: String): Int =
    parse(input)
        .mapNotNull(Machine::calculateSolution)
        .sumOf { it dot Machine.TokenCost }

//fun answer2(input: String): Int =
//    parse(input).let(::findRegions).let(::regionCosts2)

fun main() {
    val input = fetchAdventOfCodeInput(2024, 13)
    println("--- Day 13: Claw Contraption ---")
    timedFunction("Part 1") { answer1(input) } // 28262
//    timedFunction("Part 2") { answer2(input) } // 953738
}