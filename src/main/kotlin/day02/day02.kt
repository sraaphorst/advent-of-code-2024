// Advent of Code 2024, Day 02.
// By Sebastian Raaphorst, 2024.

package day02

import common.collectionops.allListDrops
import common.day
import common.parsing.parseGrid
import common.readInput

private const val Lower = 1
private const val Upper = 3

private fun isReportSafe(report: List<Int>): Boolean {
    val diffs = report.zipWithNext()
        .map { (first, second) -> first - second }
    return diffs.all { it in Lower..Upper } || diffs.all { it in -Upper..-Lower }
}

private fun isReportAlmostSafe(report: List<Int>): Boolean =
    report.allListDrops()
        .any(::isReportSafe)


fun answer1(input: String): Int =
    parseGrid(input, String::toInt).count(::isReportSafe)


fun answer2(input: String): Int =
    parseGrid(input, String::toInt).count(::isReportAlmostSafe)

fun main() {
    val input = readInput({}::class.day())

    println("--- Day 2: Red-Nosed Reports ---")

    // Answer 1: 379
    println("Part 1: ${answer1(input)}")

    // Answer 2: 430
    println("Part 2: ${answer2(input)}")
}