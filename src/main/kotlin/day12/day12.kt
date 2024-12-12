// Advent of Code 2024, Day 12.
// By Sebastian Raaphorst, 2024.

package day12

import common.aocreader.fetchAdventOfCodeInput
import common.gridalgorithms.*
import common.parsing.Grid
import common.parsing.parseCharGrid
import common.runner.timedFunction

private fun regionCosts(regions: Collection<Region>): Long =
    regions.sumOf { region -> region.area.toLong() * region.perimeter.toLong() }

fun parse(input: String): Grid<Char> =
    parseCharGrid(input)

fun answer1(input: String): Long =
    parse(input).let(::findRegions).let(::regionCosts)

fun answer2(input: String): Long =
    TODO()

fun main() {
    val input = fetchAdventOfCodeInput(2024, 12)
    println("--- Day 12: Garden Groups ---")
    timedFunction("Part 1") { answer1(input) } // 1522850
//    timedFunction("Part 2") { answer2(input) } //
}