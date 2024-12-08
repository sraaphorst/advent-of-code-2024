// Advent of Code 2024, Day 05.
// By Sebastian Raaphorst, 2024.

package day05

import common.day
import common.collectionops.middle
import common.readInput

typealias OrderingRules = Map<Int, Set<Int>>
typealias Updates = List<Int>

/**
 * Parse the violation rules into a map where:
 * - Keys are the integers `b` that must not precede certain `a` values.
 * - Values are sets of `a` values that cause a violation if they follow the key.
 */
private fun parseViolationRules(input: String): OrderingRules =
    input.lines()
        .filter(String::isNotBlank)
        .map { line ->
            val (a, b) = line.trim().split('|').map(String::toInt)
            b to a
        }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, values) -> values.toSet() }

/**
 * Parse updates as lists of integers from input lines.
 */
private fun parseUpdates(input: String): List<Updates> =
    input.lines().map { line ->
        line.trim().split(',').map(String::toInt)
    }

/**
 * Parse the violation rules and updates from the input string.
 * The input is split into two sections separated by a blank line.
 */
private fun parseViolation(input: String): Pair<OrderingRules, List<Updates>> {
    val (rulesString, updatesString) = input.split("\n\n")
    return parseViolationRules(rulesString) to parseUpdates(updatesString)
}

/**
 * Check if a given sequence of updates passes the violation rules.
 * Returns `true` if no violations occur, otherwise `false`.
 */
private fun passesViolation(updates: Updates, violationRules: OrderingRules): Boolean {
    val disallowed = mutableSetOf<Int>()
    for (page in updates) {
        if (page in disallowed) return false
        disallowed += violationRules[page] ?: emptySet()
    }
    return true
}

/**
 * Reorder the updates to satisfy violation rules.
 * Uses a recursive approach to build the ordering.
 */
private fun reorder(updates: Updates, violationRules: OrderingRules): Updates {
    tailrec fun aux(
        reordered: Updates = emptyList(),
        remaining: Set<Int> = updates.toSet()
    ): Updates {
        if (remaining.isEmpty()) return reordered
        val disallowed = remaining.flatMap { violationRules[it] ?: emptySet() }.toSet()
        val candidates = remaining - disallowed
        val candidate = candidates.firstOrNull()
            ?: throw RuntimeException("No candidate for reordering.")
        return aux(reordered + candidate, remaining - candidate)
    }
    return aux()
}

/**
 * Part 1: Sum the "middle" values of updates that pass the violation rules.
 */
fun answer1(input: String): Int =
    parseViolation(input).let { (violationRules, updatesList) ->
        updatesList
            .filter { passesViolation(it, violationRules) }
            .sumOf(List<Int>::middle)
    }

/**
 * Part 2: Sum the "middle" values of reordered updates that fail the violation rules.
 */
fun answer2(input: String): Int =
    parseViolation(input).let { (violationRules, updatesList) ->
        updatesList
            .filterNot { passesViolation(it, violationRules) }
            .map { reorder(it, violationRules) }
            .sumOf(List<Int>::middle)
    }

fun main() {
    val input = readInput({}::class.day()).trim()

    println("--- Day 5: Print Queue ---")

    // Part 1: 4281
    println("Part 1: ${answer1(input)}")

    // Part 2: 5466
    println("Part 2: ${answer2(input)}")
}
