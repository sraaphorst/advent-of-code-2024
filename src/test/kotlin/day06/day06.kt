// Advent of Code 2024, Day 06.
// By Sebastian Raaphorst, 2024.

package day06

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private companion object {
        private val input =
            """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """.trimIndent().trim()
        val guard = parseProblem(input)
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(41, answer1(guard))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(6, answer2(guard))
    }
}