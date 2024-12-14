// Advent of Code 2024, Day 08.
// By Sebastian Raaphorst, 2024.

package day08

import common.aocreader.fetchAdventOfCodeInput
import common.vec2d.*
import common.runner.timedFunction

private typealias Frequency = Char
private typealias AntennaMap = Map<Frequency, Set<Vec2DInt>>

private data class Grid(val height: Int, val width: Int, val antennae: AntennaMap) {

    private fun inBounds(pos: Vec2DInt): Boolean =
        pos.x in 0 until height && pos.y in 0 until width

    fun calculateAntinodePair(pos1: Vec2DInt, pos2: Vec2DInt): Set<Vec2DInt> {
        val delta = pos1 - pos2
        return setOf(pos1 + delta, pos2 - delta).filter(::inBounds).toSet()
    }

    fun calculateAllAntinodes(pos1: Vec2DInt, pos2: Vec2DInt): Set<Vec2DInt> {
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
        positions: Set<Vec2DInt>,
        calculateAntinodes: (Vec2DInt, Vec2DInt) -> Set<Vec2DInt>
    ): Set<Vec2DInt> =
        positions.withIndex()
            .flatMap { (idx, pos1) ->
                positions.drop(idx + 1).flatMap { pos2 ->
                    calculateAntinodes(pos1, pos2)
                }
            }.toSet()

    fun calculateAntinodeCount(calculateAntinodes: (Vec2DInt, Vec2DInt) -> Set<Vec2DInt>): Int =
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
                        .map { (colIdx, frequency) -> frequency to Vec2D.int(rowIdx, colIdx) }
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
    timedFunction("Part 1") { answer1(input) } // 376
    timedFunction("Part 2") { answer2(input) } // 1352
}
