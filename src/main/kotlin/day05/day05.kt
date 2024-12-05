// Advent of Code 2024, Day 05.
// By Sebastian Raaphorst, 2024.

package day05

import common.day
import common.readInput

typealias OrderingRules = Map<Int, Set<Int>>
typealias Updates = List<Int>

/**
 * We parse the input into two distinct structures.
 * 1. The first structure comprises the page ordering rules and is made into a:
 *    Map<Int, Set<Int>>
 *    where for entry a|b in the input, page a must be printed before page b.
 *    We store these as keys b with list entries a.
 *    If we encounter b and any entry a is printed after it, it fails.
 */
private fun parseOrderingRules(input: String): OrderingRules =
    input.lines()
        .filter(String::isNotBlank)
        .map { line ->
            val (a, b) = line.trim().split('|').map(String::toInt)
            a to b
        }
        .groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second)
        .mapValues { it.value.toSet() }

// These are failure rules: for entry a, if any entry in its list appears after it, failure.
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

fun parseOrdering(input: String): Pair<OrderingRules, List<Updates>> {
    val (rulesString, updatesString) = input.split("\n\n")
    val rules = parseOrderingRules(rulesString)
    val updates = parseUpdates(updatesString)
    return rules to updates
}

fun parseViolation(input: String): Pair<OrderingRules, List<Updates>> {
    val (rulesString, updatesString) = input.split("\n\n")
    val rules = parseViolationRules(rulesString)
    val updates = parseUpdates(updatesString)
    return rules to updates
}

fun passesOrdering(updates: Updates, orderingRules: OrderingRules): Boolean =
    // Start at the end and check to see if any pages appear that were not allowed to be
    // seen closer to the end of the list.
    updates.fold(emptySet<Int>()) { seen, page ->
        val notSeenEarlier = orderingRules[page] ?: emptySet()
        if (notSeenEarlier.any { it in seen }) { return false }
        seen + page
    }.let { true }

fun passesViolation(updates: Updates, violationRules: OrderingRules): Boolean =
    updates.fold(emptySet<Int>()) { disallowed, page ->
        if (page in disallowed) return false
        disallowed + (violationRules[page] ?: emptySet())
    }.let { true }

fun passesViolation2(updates: Updates, violationRules: OrderingRules): Boolean =
    updates.indices.all { idx ->
        val page = updates[idx]
        val remainList = updates.drop(idx + 1)
        val pageViolations = violationRules[page] ?: emptySet()
        !remainList.any { it in pageViolations }
    }


fun answer1(orderingRules: OrderingRules, updatesList: List<Updates>): Int =
    updatesList.filter { update -> passesOrdering(update, orderingRules) }
        .sumOf { it[it.size / 2] }

fun answer1b(violationRules: OrderingRules, updatesList: List<Updates>): Int =
    updatesList.filter { update -> passesViolation(update, violationRules) }
        .sumOf { it[it.size / 2] }

fun answer2(input: List<String>): Int =
    TODO()

fun main() {
    val input = readInput({}::class.day()).trim()
    val (orderingRules, updateList) = parseOrdering(input)
    val (violationRules, updateList2) = parseViolation(input)

    println("--- Day 5: Print Queue ---")

    // Answer 1: 4281
    println("Part 1: ${answer1(orderingRules, updateList)}")
    println("Part 1: ${answer1(orderingRules, updateList)}")

    // Answer 2: 83158140
//    println("Part 2: ${answer2(input)}")
}