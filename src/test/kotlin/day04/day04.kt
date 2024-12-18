// Advent of Code 2024, Day 04.
// By Sebastian Raaphorst, 2024.

package day04

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {
    private companion object {
        val input =
            """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(18, answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(9, answer2(input))
}