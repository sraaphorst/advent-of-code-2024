// Advent of Code 2024, Day 02.
// By Sebastian Raaphorst, 2024.

package day02

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {
    private companion object {
        val input =
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(2, answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(4, answer2(input))
}