// Advent of Code 2024, Day 04.
// By Sebastian Raaphorst, 2024.

package day04

import common.countSubstrings
import common.day
import common.collectionops.*
import common.readInput

private const val XMAS = "XMAS"

private val Transforms = listOf(
    List<String>::getEastStrings,
    List<String>::getWestStrings,
    List<String>::getSouthStrings,
    List<String>::getNorthStrings,
    List<String>::getSEStrings,
    List<String>::getSWStrings,
    List<String>::getNEStrings,
    List<String>::getNWStrings,
)

/**
 * Count the number of times XMAS appears in the specified input.
 */
private fun countXmas(input: List<String>): Int =
    input.sumOf { it.countSubstrings(XMAS) }

/**
 * Calculates the sum of products from mul(a, b) patterns in the input.
 */
private fun countXmases(input: List<String>): Int =
    Transforms.sumOf { transform -> countXmas(transform(input)) }

/**
 * Count the number of X-MASes in the puzzle. These always have an A in the middle of a
 * 3x3 input, with an M in two adjacent corners and an S in the other two adjacent corners.
 * Example:
 *  M.S
 *  .A.
 *  M.S
 *  This could be tidied up but since we only have four cases, we leave it as is.
 */
private fun countXXmases(input: List<String>): Int =
    input.map(String::toList)
        .extractBlocks(3, 3)
        .count { block -> block.size == 3 && block.all { it.size == 3 } && block[1][1] == 'A' &&
                ((block[0][0] == 'M' && block[0][2] == 'M' && block[2][0] == 'S' && block[2][2] == 'S') ||
                        (block[0][0] == 'M' && block[2][0] == 'M' && block[0][2] == 'S' && block[2][2] == 'S') ||
                        (block[0][0] == 'S' && block[0][2] == 'S' && block[2][0] == 'M' && block[2][2] == 'M') ||
                        (block[0][0] == 'S' && block[2][0] == 'S' && block[0][2] == 'M' && block[2][2] == 'M'))

        }

fun answer1(input: String): Int =
    countXmases(input.lines())

fun answer2(input: String): Int =
    countXXmases(input.lines())

fun main() {
    val input = readInput({}::class.day())

    println("--- Day 4: Ceres Search ---")

    // Answer 1: 2370
    println("Part 1: ${answer1(input)}")

    // Answer 2: 1908
    println("Part 2: ${answer2(input)}")
}