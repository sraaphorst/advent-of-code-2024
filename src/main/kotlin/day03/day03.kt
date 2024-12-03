// Advent of Code 2024, Day 02.
// By Sebastian Raaphorst, 2024.

package day03

import common.day
import common.readInput

private val regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

fun extractMul(input: String): List<Pair<Int, Int>> =
    regex.findAll(input)
        .map { matchResult ->
            val (a, b) = matchResult.destructured
            Pair(a.toInt(), b.toInt())
        }.toList()


fun answer1(muls: List<Pair<Int, Int>>): Int =
    muls.sumOf { (first, second) -> first * second }


fun answer2(mult: List<Pair<Int, Int>>): Int =
    TODO()

fun main() {
    val input = readInput({}::class.day())
    val muls = extractMul(input)

    println("--- Day 3: Mull It Over ---")

    // Answer 1: 173785482
    println("Part 1: ${answer1(muls)}")

    // Answer 2: 430
//    println("Part 2: ${answer2(reports)}")
}