// Advent of Code 2024, Day 09.
// By Sebastian Raaphorst, 2024.

package day10

import common.aocreader.fetchAdventOfCodeInput
import common.intpos2d.*
import common.parsing.parseGrid

private typealias Trail = List<IntPos2D>
private typealias Trails = Set<List<IntPos2D>>

private enum class Direction(val delta: IntPos2D) {
    NORTH(IntPos2D(-1, 0)),
    EAST(IntPos2D(0, 1)),
    SOUTH(IntPos2D(1, 0)),
    WEST(IntPos2D(0, -1)),
}

private fun parse(input: String): List<List<Int>> =
    input.trim().lines()
        .map { line -> line.trim().toList().map { it.digitToInt() } }

private fun findTrailheads(grid: List<List<Int>>): Map<IntPos2D, Trails> {
    val height = grid.size
    val width = grid[0].size

    // Instead of going from zeros to nines, we go from nines to zeros
    // to count the number of trails to the peak.
    // Find the nines, i.e. the ending positions of the map.
    val nines = grid.flatMapIndexed { rowIdx, row ->
        row.mapIndexedNotNull { colIdx, height ->
            if (height == 9) IntPos2D(rowIdx, colIdx) else null
        }
    }.toSet()

    // We need to track the number of trails to each nine.
    val trails: MutableMap<IntPos2D, Trails> = mutableMapOf()

    // For each 9, we want to find the trails that lead to the peak.
    fun aux(trailSoFar: Trail): Trails {
        val currentPos = trailSoFar.last()
        val (currY, currX) = currentPos
        val currHeight = grid[currY][currX]
        if (currHeight == 0) return setOf(trailSoFar)

        // Try all the valid neighbours.
        val neighbours = Direction.entries
            .map { currentPos + it.delta }
            .filter { coords -> coords.first in 0 until height && coords.second in 0 until width }
            .filter { coords -> grid[coords.first][coords.second] == currHeight - 1 }
        if (neighbours.isEmpty()) return emptySet()

        return neighbours.flatMap { pos -> aux(trailSoFar + pos) }.toSet()
    }

    nines.forEach { nine ->
        val allTrails = aux(listOf(nine))
        trails[nine] = allTrails
    }

    return trails
}

fun countTrailheads(trails: Map<IntPos2D, Trails>): Int =
    trails.values.flatten().toSet().size

fun answer1(input: String): Int =
    parse(input).let(::findTrailheads).let(::countTrailheads)

fun answer2(input: String): Int =
    TODO()

fun main() {
    val input = fetchAdventOfCodeInput(2024, 10)

    println("--- Day 10: Resonant Collinearity ---")

    // Part 1:
    println("Part 1: ${answer1(input)}")

    // Part 2:
    println("Part 2: ${answer2(input)}")
}
