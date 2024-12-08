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
    private fun calculateAntinodePair(pos1: Position, pos2: Position): Set<Position> {
        val delta = pos1 - pos2
        return listOf(pos1 + delta, pos2 - delta)
            .filter(::inGrid)
            .toSet()
    }

    private fun inGrid(pos: Position): Boolean =
        pos.first in (0 until height) && pos.second in (0 until width)

    /**
     * Calculate the slope of the line that the antinodes are on, namely
     * pos1 - pos2
     * and then continue to calculate all possible other candidates (including themselves).
     */
    private fun calculateAllAntinodes(pos1: Position, pos2: Position): Set<Position> {
        val delta = pos1 - pos2
        val antinodes = mutableSetOf<Position>()
        val currAntinodes = mutableSetOf<Position>(pos1, pos2)
        while (currAntinodes.isNotEmpty()) {
            antinodes += currAntinodes
            val nextAntinodes = mutableSetOf<Position>()

            for (antinode in currAntinodes) {
                val candidate1 = antinode - delta
                if (inGrid(candidate1) && candidate1 !in antinodes) nextAntinodes.add(candidate1)
                val candidate2 = antinode + delta
                if (inGrid(candidate2) && candidate2 !in antinodes) nextAntinodes.add(candidate2)
            }
            currAntinodes.clear()
            currAntinodes += nextAntinodes
        }
        return antinodes
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

    // Part 2: 1352
    println("Part 2: ${answer2(input)}")
}
