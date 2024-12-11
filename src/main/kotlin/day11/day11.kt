// Advent of Code 2024, Day 11.
// By Sebastian Raaphorst, 2024.

package day11

import common.aocreader.fetchAdventOfCodeInput
import common.parsing.WhitespaceParser

private fun parse(input: String): MutableMap<Long, Long> {
    val result = mutableMapOf<Long, Long>()
    input.split(WhitespaceParser).map(String::toLong).forEach { num ->
        result[num] = result.getOrDefault(num, 0) + 1
    }
    return result
}

// The key is that the order of the stones have no relevance.
private object StoneRules {
    private val memoization: MutableMap<Long, List<Long>> = mutableMapOf()

    private fun rule(stone: Long): List<Long> =
        memoization.computeIfAbsent(stone) { num ->
            if (num == 0L) return@computeIfAbsent listOf(1L)

            val asString = num.toString()
            if (asString.length % 2 == 0) {
                val middle = asString.length / 2
                val firstHalf = asString.substring(0, middle).toLong()
                val secondHalf = asString.substring(middle).toLong()
                return@computeIfAbsent listOf(firstHalf, secondHalf)
            }

            return@computeIfAbsent listOf(stone * 2024)
        }

    fun blink(stones: MutableMap<Long, Long>): MutableMap<Long, Long> {
        val newStones = mutableMapOf<Long, Long>()
        for ((stone, count) in stones) {
            val transformed = rule(stone)
            for (tStone in transformed) {
                newStones[tStone] = newStones.getOrDefault(tStone, 0L) + count
            }
        }
        return newStones
    }
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

    // Part 1:185894, memoization cache has size 1381.
    println("Part 1: ${answer1(input)}")

    // Part 2: 221632504974231, memoization cache has size 3896.
    println("Part 2: ${answer2(input)}")
}
