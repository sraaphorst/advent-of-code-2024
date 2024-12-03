// Advent of Code 2024, Day 03.
// By Sebastian Raaphorst, 2024.

package day03

import common.day
import common.readInput

/**
 * We want to turn off processing for substrings of the form:
 * 1. don't()...do()
 * 2. don't()... to the end of the input
 * These might span multiple lines, so we eliminate all newlines first.
 */
private fun preprocess(input: String): String =
    input.replace(Regex("""[\r\n]+"""), "")
        .replace(Regex("""don't\(\).*?do\(\)"""), "")
        .replace(Regex("""don't\(\).*"""), "")

/**
 * Calculates the sum of products from mul(a, b) patterns in the input.
 */
private fun calculateMul(input: String): Int =
    Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(input)
        .sumOf { it.destructured.let { (a, b) -> a.toInt() * b.toInt() } }

fun answer1(input: String): Int =
    calculateMul(input)

fun answer2(input: String): Int =
    answer1(preprocess(input))

fun main() {
    val input = readInput({}::class.day())

    println("--- Day 3: Mull It Over ---")

    // Answer 1: 173785482
    println("Part 1: ${answer1(input)}")

    // Answer 2: 83158140
    println("Part 2: ${answer2(input)}")
}