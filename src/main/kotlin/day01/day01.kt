// Advent of Code 2024, Day 01.
// By Sebastian Raaphorst, 2024.

package day01

import common.aocreader.fetchAdventOfCodeInput
import common.parsing.parseColumns
import common.collectionops.toFrequencyMap
import kotlin.math.abs

fun answer1(input: String): Int =
    parseColumns(input, String::toInt, String::toInt)
        .let { (list1, list2) ->
            list1.sorted().zip(list2.sorted())
                .sumOf { (first, second) -> abs(first - second) }
        }

fun answer2(input: String): Int =
    parseColumns(input, String::toInt, String::toInt)
        .let { (list1, list2) ->
            val freqMap1 = list1.toFrequencyMap()
            val freqMap2 = list2.toFrequencyMap()
            freqMap1.map { (elem, freq) -> elem * freq * freqMap2.getOrDefault(elem, 0) }.sum()
        }

fun main() {
    val input = fetchAdventOfCodeInput(2024, 1)

    println("--- Day 1: Historian Hysteria ---")

    // Answer 1: 2031679
    println("Part 1: ${answer1(input)}")

    // Answer 2: 19678534
    println("Part 2: ${answer2(input)}")
}