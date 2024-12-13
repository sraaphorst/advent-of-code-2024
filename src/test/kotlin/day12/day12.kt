// Advent of Code 2024, Day 12
// By Sebastian Raaphorst, 2024.

package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12 {
    companion object {
        val input1 =
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """.trimIndent()

        val input2 =
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """.trimIndent().trim()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(140, answer1(input1))
        assertEquals(1930, answer1(input2))
    }

    @Test
    fun `Problem2 example`() {
        assertEquals(80, answer2(input1))
        assertEquals(1206, answer2(input2))
    }
}