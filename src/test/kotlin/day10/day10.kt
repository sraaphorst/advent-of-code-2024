// Advent of Code 2024, Day 10.
// By Sebastian Raaphorst, 2024.

package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {
    private companion object {
        val input =
            """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """.trimIndent().trim()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(36, answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(TODO(), answer2(input))
}