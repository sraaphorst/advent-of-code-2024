// Advent of Code 2024, Day 08.
// By Sebastian Raaphorst, 2024.

package day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {
    private companion object {
        val input =
            """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """.trimIndent().trim()
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(14, answer1(input))

    @Test
    fun `Problem 2 example`() =
        assertEquals(34, answer2(input))
}