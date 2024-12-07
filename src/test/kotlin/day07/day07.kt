// Advent of Code 2024, Day 07.
// By Sebastian Raaphorst, 2024.

package day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private companion object {
        private val input =
            """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """.trimIndent().trim()

        val equations = input.lines().map(Equation::parseLine)
    }

    @Test
    fun `Problem 1 example`() =
        assertEquals(3749L, answer1(equations))

//    @Test
//    fun `Problem 2 example`() =
//        assertEquals(11387, answer2(equations))
}