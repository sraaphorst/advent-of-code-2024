// Advent of Code 2024, Day 11.
// By Sebastian Raaphorst, 2024.

package day11

import common.aocreader.fetchAdventOfCodeInput
import common.parsing.WhitespaceParser
import common.runner.timedFunction
import day11.StoneRules.cacheSize
import java.util.concurrent.ConcurrentHashMap


private fun parse(input: String): MutableMap<Long, Long> =
    input.split(WhitespaceParser)
        .map(String::toLong)
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()

// The key is that the order of the stones have no relevance.
private object StoneRules {
    private val memoization: MutableMap<Long, List<Long>> = ConcurrentHashMap()

    private fun rule(stone: Long): List<Long> = memoization.computeIfAbsent(stone) {
        when {
            it == 0L -> listOf(1L)
            it.toString().length % 2 == 0 -> {
                val middle = it.toString().length / 2
                listOf(
                    it.toString().substring(0, middle).toLong(),
                    it.toString().substring(middle).toLong()
                )
            }
            else -> listOf(it * 2024)
        }
    }

    fun blink(stones: Map<Long, Long>): MutableMap<Long, Long> =
        stones.flatMap { (stone, count) ->
            rule(stone).map { transformed -> transformed to count }
        }.groupingBy { it.first }
            .fold(0L) { acc, (_, count) -> acc + count }
            .toMutableMap()

    fun cacheSize(): Int = memoization.size
}

private fun answer(input: String, blinks: Int): Long =
    (1..blinks).fold(parse(input)) { stones, _ -> StoneRules.blink(stones) }
        .values.sum()

fun answer1(input: String): Long =
    answer(input, 25)

fun answer2(input: String): Long =
    answer(input, 75)

fun main() {
    val input = fetchAdventOfCodeInput(2024, 11)
    println("--- Day 11: Plutonian Pebbles ---")
    timedFunction("Part 1") { answer1(input) }
    println("\tCache size: ${cacheSize()}")
    timedFunction("Part 2") { answer2(input) }
    println("\tCache size: ${cacheSize()}")
}