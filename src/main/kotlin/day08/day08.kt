// Advent of Code 2024, Day 08.
// By Sebastian Raaphorst, 2024.

package day08

import common.day
import common.readInput

typealias Frequency = Char
typealias Position = Pair<Int, Int>
typealias AntennaMap = Map<Frequency, Set<Position>>

private operator fun Position.minus(other: Position): Position =
    Position(this.first - other.first, this.second - other.second)

private operator fun Position.plus(other: Position): Position =
    Position(this.first + other.first, this.second + other.second)

data class Grid(val height: Int, val width: Int, val antennae: AntennaMap) {
    /**
     * Calculate the slope of the line that the points are on, namely:
     * pos1 - pos2
     * Then pos1 = pos2 + delta, and pos2 = pos1 - delta, so we want the
     * other two candidates, namely:
     * 1. pos1 + delta
     * 2. pos2 - delta
     * and if they are in the grid, then they contribute.
     */
    private fun calculateAntinodePair(pos1: Position, pos2: Position): List<Position> {
        val delta = pos1 - pos2
        return listOf(pos1 + delta, pos2 - delta)
            .filter { (row, col) -> row in (0 until height) && col in (0 until width) }
            .also { it.forEach { t -> println("For $pos1 + $pos2 -> $t") }}
    }

    /**
     * Calculate the slope of the line that the antinodes are on, namely
     * pos1 - pos2
     * and then continue to calculate all possible other candidates (including themselves).
     */
    private fun calculateAllAntinodes(pos1: Position, pos2: Position): Set<Position> {
        val delta = pos1 - pos2
        tailrec fun aux(newAntinodes: Set<Position> = setOf(pos1, pos2),
                        antinodes: Set<Position> = emptySet()): Set<Position> = when {
                            newAntinodes.isEmpty() -> antinodes
                            else -> {
                                val possibleNewAntinodes = newAntinodes.flatMap { listOf(it + delta, it - delta) }
                                    .toSet()
                                    .filter { (row, col) -> row in (0 until height) && col in (0 until width) }
                                    .toSet()
                                aux(possibleNewAntinodes, antinodes + newAntinodes)
                            }
                        }
        return aux()
    }

    /**
     * For a given set of positions corresponding to a frequency, calculate
     * the set of generated antinodes.
     */
    private fun calculateAntinodesForFrequency(positions: Set<Position>): Set<Position> =
        positions.withIndex()
            .flatMap { (idx, pos1) -> positions.drop(idx + 1)
                .flatMap { pos2 ->
                    calculateAntinodePair(pos1, pos2)
                }
            }.toSet()

    private fun calculateAntinodesForFrequency2(positions: Set<Position>): Set<Position> =
        positions.withIndex()
            .flatMap { (idx, pos1) -> positions.drop(idx + 1)
                .flatMap { pos2 ->
                    calculateAllAntinodes(pos1, pos2)
                }
            }.toSet()

    fun calculateAntinodeCount(): Int =
        antennae.values
            .flatMap(::calculateAntinodesForFrequency)
            .toSet()
            .size

    fun calculateAntinodeCount2() : Int =
        antennae.values
            .flatMap(::calculateAntinodesForFrequency2)
            .toSet()
            .size

    companion object {
        private fun parseMap(input: String): AntennaMap =
            input.lines()
                .withIndex()
                .flatMap { (rowIdx, row) ->
                    row.withIndex()
                        .filter { (_, frequency) -> frequency != '.' }
                        .map { (colIdx, frequency) -> frequency to Pair(rowIdx, colIdx) }
                }.groupBy({ it.first }, { it.second })
                .mapValues { (_, positions) -> positions.toSet() }

        fun parse(input: String) =
            Grid(input.lines().size, input.lines().get(0).length, parseMap(input))
    }
}

fun answer1(input: String): Int =
    Grid.parse(input).calculateAntinodeCount()

fun answer2(input: String): Int =
    Grid.parse(input).calculateAntinodeCount2()


fun main() {
    val input = readInput({}::class.day()).trim()

    println("--- Day 8: Resonant Collinearity ---")

    // Part 1: 376
    println("Part 1: ${answer1(input)}")

    // Part 2:
//    println("Part 2: ${answer2(input)}")
}
