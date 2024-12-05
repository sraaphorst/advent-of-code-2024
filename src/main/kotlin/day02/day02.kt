// Advent of Code 2024, Day 02.
// By Sebastian Raaphorst, 2024.

package day02

import common.allListDrops
import common.day
import common.parseGrid
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


fun answer1(reports: List<List<Int>>): Int =
    reports.count(::isReportSafe)


fun answer2(reports: List<List<Int>>): Int =
    reports.count(::isReportAlmostSafe)

fun main() {
    val input = readInput({}::class.day())
    val reports = parseGrid(input, String::toInt)

    println("--- Day 2: Red-Nosed Reports ---")

    // Answer 1: 379
    println("Part 1: ${answer1(reports)}")

    // Answer 2: 430
    println("Part 2: ${answer2(reports)}")
}