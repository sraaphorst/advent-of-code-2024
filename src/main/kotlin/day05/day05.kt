// Advent of Code 2024, Day 05.
// By Sebastian Raaphorst, 2024.

package day05

import common.day
import common.middle
import common.readInput

typealias OrderingRules = Map<Int, Set<Int>>
typealias Updates = List<Int>

/**
 * We parse the input failure rules into two distinct structures.
 * The entries are of the form a|b, where if b occurs before a in a list of updates,
 * the process should fail.
 *
 * Thus, this is parsed into a Map<Int, Set<Int>> where all the b from above comprise keys,
 * and any entry a1|b, a2|b, etc. form the set of values {a1, a2, ...} indicate that if an
 * ai occurs after b, the updates fail.
 */
private fun parseViolationRules(input: String): OrderingRules =
    input.lines()
        .filter(String::isNotBlank)
        .map { line ->
            val (a, b) = line.trim().split('|').map(String::toInt)
            b to a
        }
        .groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second)
        .mapValues { (_, b) -> b.toSet() }


private fun parseUpdates(input: String): List<Updates> =
    input.lines()
        .map { line -> line.trim().split(',').map(String::toInt) }

fun parseViolation(input: String): Pair<OrderingRules, List<Updates>> {
    val (rulesString, updatesString) = input.split("\n\n")
    val rules = parseViolationRules(rulesString)
    val updates = parseUpdates(updatesString)
    return rules to updates
}

fun passesViolation(updates: Updates, violationRules: OrderingRules): Boolean =
    updates.fold(emptySet<Int>()) { disallowed, page ->
        if (page in disallowed) return false
        disallowed + (violationRules[page] ?: emptySet())
    }.let { true }

/**
 * Idea:
 * Gather all elements.
 * While there are still elements in the remaining element set:
 * Keep picking the element that doesn't appear in any other violation for remaining elements
 *   and add it to the ordering.
 * Remove it from the remaining element set.
 */
fun reorder(updates: Updates, violationRules: OrderingRules): List<Int> {
    tailrec fun aux(
        reorder: List<Int> = emptyList(),
        remaining: Set<Int> = updates.toSet()
    ): Updates {
        if (remaining.isEmpty()) return reorder
        val disallowed = remaining.flatMap { violationRules[it] ?: emptySet() }
        val candidates = remaining - disallowed
        if (candidates.isEmpty()) throw RuntimeException("No candidate for reordering.")
        val candidate = candidates.first()
        return aux(reorder + candidate, remaining - candidate)
    }
    return aux()
}

fun answer1(violationRules: OrderingRules, updatesList: List<Updates>): Int =
    updatesList.filter { update -> passesViolation(update, violationRules) }
        .sumOf(List<Int>::middle)

fun answer2(violationRules: OrderingRules, updatesList: List<Updates>): Int =
    updatesList.filterNot { passesViolation(it, violationRules) }
        .map { reorder(it, violationRules) }
        .sumOf(List<Int>::middle)


fun main() {
    val input = readInput({}::class.day()).trim()
    val (violationRules, updateList) = parseViolation(input)

    println("--- Day 5: Print Queue ---")

    // Answer 1: 4281
    println("Part 1: ${answer1(violationRules, updateList)}")

    // Answer 2: 5466
    println("Part 2: ${answer2(violationRules, updateList)}")
}