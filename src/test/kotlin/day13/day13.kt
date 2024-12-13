// Advent of Code 2024, Day 13.
// By Sebastian Raaphorst, 2024.

package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13 {
    companion object {
        val input1 =
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(480, answer1(input1))
    }

//    @Test
//    fun `Problem2 example`() {
//        assertEquals(80, answer2(input1))
//        assertEquals(1206, answer2(input2))
//    }
}