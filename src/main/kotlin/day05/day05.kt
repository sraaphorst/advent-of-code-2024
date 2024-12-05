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
//private fun parseOrderingRules(input: String): OrderingRules =
//    input.lines()
//        .filter(String::isNotBlank)
//        .map { line ->
//            val (a, b) = line.trim().split('|').map(String::toInt)
//            b to a
//        }
//        .groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second)
//        .mapValues { (_, b) -> b.toSet() }

private fun parseUpdates(input: String): List<Updates> =
    input.lines()
        .map { line -> line.trim().split(',').map(String::toInt) }

fun parse(input: String): Pair<OrderingRules, List<Updates>> {
    val (rulesString, updatesString) = input.split("\n\n")
    val rules = parseOrderingRules(rulesString)
    val updates = parseUpdates(updatesString)
    return rules to updates
}

/**
 * Determine if the page updates pass, i.e. there are no violations where
 * a page is printed after
 */
//private fun passes(updates: Updates, violationRules: OrderingRules): Boolean =
//    updates.indices.all { idx ->
//        val page = updates[idx]
//        val remainList = updates.drop(idx + 1)
//        val pageViolations = violationRules[page] ?: emptySet()
//        !remainList.any { it in pageViolations }
//    }
//private fun passes(updates: Updates, violationRules: OrderingRules): Boolean =
//    updates.indices.all { idx ->
//        val page = updates[idx]
//        val remainList = updates.drop(idx + 1)
//        val pageViolations = violationRules[page] ?: emptySet()
//        !remainList.any { it in pageViolations }
//    }

//fun passes(updates: List<Int>, orderingRules: Map<Int, Set<Int>>): Boolean =
//    updates.fold(emptySet<Int>()) { seen, page ->
//        // Check if any required pages for `page` are already in `seen`
//        val requiredPages = orderingRules[page] ?: emptySet()
//        if (requiredPages.any { it in seen }) {
//            return null // Signal failure
//        } else {
//            seen + page // Add current page to seen
//        }
//    } != null

// 1 5 18 3 2
// 5 9 3 2
fun passes(updates: Updates, orderingRules: OrderingRules): Boolean =
    // Start at the end and keep track of what pages are not allowed to appear.
    updates.fold(emptySet<Int>()) { seen, page ->
        val notSeenEarlier = orderingRules[page] ?: emptySet()
        if (notSeenEarlier.any { it in seen }) {
            return false
        }
        seen + page
    }.let { true }

fun answer1(orderingRules: OrderingRules, updatesList: List<Updates>): Int =
    updatesList.filter { update -> passes(update, orderingRules) }
        .sumOf { it[it.size / 2] }

fun answer2(input: List<String>): Int =
    TODO()

fun main() {
    val input = readInput({}::class.day()).trim()
    val (orderingRules, updateList) = parse(input)

    println("--- Day 5: Print Queue ---")

    // Answer 1: 4281
    println("Part 1: ${answer1(orderingRules, updateList)}")

    // Answer 2: 83158140
//    println("Part 2: ${answer2(input)}")
}