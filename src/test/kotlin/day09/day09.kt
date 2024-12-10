// Advent of Code 2024, Day 09.
// By Sebastian Raaphorst, 2024.

package day09

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private companion object {
        val input =
            """
            2333133121414131402
            """.trimIndent().trim()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(1928.toBigInteger(), answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(2858.toBigInteger(), answer2(input))
}