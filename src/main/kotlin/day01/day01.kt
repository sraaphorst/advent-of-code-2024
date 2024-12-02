// Advent of Code 2024, Day 01.
// By Sebastian Raaphorst, 2024.

package day01

import common.parseColumns2
import common.toFrequencyMap
import kotlin.math.abs


fun answer1(list1: List<Int>, list2: List<Int>): Int =
    list1.sorted().zip(list2.sorted())
        .sumOf { (first, second) -> abs(first - second) }


fun answer2(list1: List<Int>, list2: List<Int>): Int {
    val freqMap1 = list1.toFrequencyMap()
    val freqMap2 = list2.toFrequencyMap()
    return freqMap1.map { (elem, freq) -> elem * freq * freqMap2.getOrDefault(elem, 0) }.sum()
}

fun main() {
    val input = object {}.javaClass.getResource("/day01.txt")!!.readText()
    val (list1, list2) = parseColumns2(input, String::toInt, String::toInt)

    println("--- Day 1: Historian Hysteria ---")

    // Answer 1: 2031679
    println("Part 1: ${answer1(list1, list2)}")

    // Answer 2: 19678534
    println("Part 2: ${answer2(list1, list2)}")
}