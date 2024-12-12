// Advent of Code 2024, Day 12
// By Sebastian Raaphorst, 2024.

package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12 {
    companion object {
        val input =
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
    fun `Problem 1 example`() =
        assertEquals(1930L, answer1(input))
}