// Advent of Code 2024, Day 10.
// By Sebastian Raaphorst, 2024.

package day10

import common.aocreader.fetchAdventOfCodeInput
import common.gridalgorithms.*
import common.intpos2d.*
import common.runner.timedFunction

private typealias Trail = List<IntPos2D>
private typealias Trails = Set<List<IntPos2D>>

private fun parse(input: String): List<List<Int>> =
    input.trim().lines()
        .map { line -> line.trim().toList().map { it.digitToIntOrNull() ?: -1 } }

private fun findTrails(grid: List<List<Int>>): Map<IntPos2D, Trails> {
    val zeros = grid.flatMapIndexed { rowIdx, row ->
        row.mapIndexedNotNull { colIdx, height ->
            if (height == 0) IntPos2D(rowIdx, colIdx) else null
        }
    }.toSet()

    // For each 9, we want to find the trails that lead to the peak.
    fun aux(trailSoFar: Trail): Trails {
        val currentPos = trailSoFar.last()
        val (currY, currX) = currentPos
        val currHeight = grid[currY][currX]
        if (currHeight == 9) return setOf(trailSoFar)

        // Try all the valid neighbours.
        val neighbours = grid.neighbourPositions(currentPos)
            .filter { (grid[it] ?: -1L) == currHeight + 1 }
        if (neighbours.isEmpty()) return emptySet()

        return neighbours.flatMap { pos -> aux(trailSoFar + pos) }.toSet()
    }

    return zeros.associate { zero -> zero to aux(listOf(zero)) }
}

private fun countTrailheads(trails: Map<IntPos2D, Trails>): Int =
    trails.values.sumOf { it.map { it.last() }.toSet().size }

private fun countTrails(trails: Map<IntPos2D, Trails>): Int =
    trails.values.sumOf { it.count() }

fun answer1(input: String): Int =
    parse(input).let(::findTrails).let(::countTrailheads)

fun answer2(input: String): Int =
    parse(input).let(::findTrails).let(::countTrails)

fun main() {
    val input = fetchAdventOfCodeInput(2024, 10)
    println("--- Day 10: Hoof It ---")
    timedFunction("Part 1") { answer1(input) } // 719
    timedFunction("Part 2") { answer2(input) } // 1530
}
