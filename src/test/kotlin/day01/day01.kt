// Advent of Code 2024, Day 01.
// By Sebastian Raaphorst, 2024.

package day01

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private companion object {
        val input =
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(11, answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(31, answer2(input))
}