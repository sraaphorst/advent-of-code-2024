// Advent of Code 2024, Day 11.
// By Sebastian Raaphorst, 2024.

package day11

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// Note: no problem 2 test today.
class Day11Test {
    private companion object {
        val input =
            """
            125 17
            """.trimIndent().trim()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(55312L, answer1(input))
}