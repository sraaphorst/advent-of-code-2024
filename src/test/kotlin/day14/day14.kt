// Advent of Code 2024, Day 14.
// By Sebastian Raaphorst, 2024.

package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14 {
    companion object {
        val input1 =
            """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(12, answer1(input1))
    }
}