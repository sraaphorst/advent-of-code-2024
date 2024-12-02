// Advent of Code 2024, Day 02.
// By Sebastian Raaphorst, 2024.

package day02

import common.parseGrid

private fun isReportSafe(report: List<Int>): Boolean {
    val diffs = report.zipWithNext()
        .map { (first, second) -> first - second }
    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}

private fun dropIndex(list: List<Int>, idx: Int): List<Int> =
    list.take(idx) + list.takeLast(list.size - idx - 1)

private fun isReportAlmostSafe(report: List<Int>): Boolean =
    report.indices
        .any { idx -> isReportSafe(dropIndex(report, idx)) }


fun answer1(reports: List<List<Int>>): Int =
    reports.count(::isReportSafe)


fun answer2(reports: List<List<Int>>,): Int =
    reports.count(::isReportAlmostSafe)

fun main() {
    val input = object {}.javaClass.getResource("/day02.txt")!!.readText()
    val reports = parseGrid(input, String::toInt)

    println("--- Day 2: Red-Nosed Reports ---")

    // Answer 1: 379
    println("Part 1: ${answer1(reports)}")

    // Answer 2: 430
    println("Part 2: ${answer2(reports)}")
}