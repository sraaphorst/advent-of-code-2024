// Advent of Code 2024, Day 08.
// By Sebastian Raaphorst, 2024.

package day08

import common.aocreader.fetchAdventOfCodeInput
import common.intpos2d.*

private typealias Frequency = Char
private typealias AntennaMap = Map<Frequency, Set<IntPos2D>>

private data class Grid(val height: Int, val width: Int, val antennae: AntennaMap) {

    private fun inBounds(pos: IntPos2D): Boolean =
        pos.first in 0 until height && pos.second in 0 until width

    fun calculateAntinodePair(pos1: IntPos2D, pos2: IntPos2D): Set<IntPos2D> {
        val delta = pos1 - pos2
        return setOf(pos1 + delta, pos2 - delta).filter(::inBounds).toSet()
    }

    fun calculateAllAntinodes(pos1: IntPos2D, pos2: IntPos2D): Set<IntPos2D> {
        val delta = pos1 - pos2
        val antinodes = mutableSetOf(pos1, pos2)
        val queue = mutableSetOf(pos1, pos2)

        while (queue.isNotEmpty()) {
            val nextQueue = queue.flatMap { antinode ->
                listOf(antinode - delta, antinode + delta)
                    .filter { inBounds(it) && it !in antinodes }
            }.toMutableSet()
            antinodes += nextQueue
            queue.clear()
            queue += nextQueue
        }
        return antinodes
    }

    private fun calculateAntinodesForFrequency(
        positions: Set<IntPos2D>,
        calculateAntinodes: (IntPos2D, IntPos2D) -> Set<IntPos2D>
    ): Set<IntPos2D> =
        positions.withIndex()
            .flatMap { (idx, pos1) ->
                positions.drop(idx + 1).flatMap { pos2 ->
                    calculateAntinodes(pos1, pos2)
                }
            }.toSet()

    fun calculateAntinodeCount(calculateAntinodes: (IntPos2D, IntPos2D) -> Set<IntPos2D>): Int =
        antennae.values
            .flatMap { calculateAntinodesForFrequency(it, calculateAntinodes) }
            .toSet()
            .size

    companion object {
        private fun parseMap(input: String): AntennaMap =
            input.lines()
                .withIndex()
                .flatMap { (rowIdx, row) ->
                    row.withIndex()
                        .filter { (_, frequency) -> frequency != '.' }
                        .map { (colIdx, frequency) -> frequency to IntPos2D(rowIdx, colIdx) }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, positions) -> positions.toSet() }

        fun parse(input: String): Grid =
            Grid(input.lines().size, input.lines()[0].length, parseMap(input))
    }
}

fun answer1(input: String): Int =
    Grid.parse(input).let { grid ->
        grid.calculateAntinodeCount(grid::calculateAntinodePair)
    }

fun answer2(input: String): Int =
    Grid.parse(input).let { grid ->
        grid.calculateAntinodeCount(grid::calculateAllAntinodes)
    }


fun main() {
    val input = fetchAdventOfCodeInput(2024, 8)

    println("--- Day 8: Resonant Collinearity ---")

    // Part 1: 376
    println("Part 1: ${answer1(input)}")

    // Part 2: 1352
    println("Part 2: ${answer2(input)}")
}
