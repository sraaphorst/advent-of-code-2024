// Advent of Code 2024, Day 03.
// By Sebastian Raaphorst, 2024.

package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private companion object {
        val input1 =
            """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """.trimIndent()

        val input2 =
            """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(161, answer1(input1))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(48, answer2(input2))
    }
}