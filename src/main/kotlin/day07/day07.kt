// Advent of Code 2024, Day 07.
// By Sebastian Raaphorst, 2024.

package day07

import common.day
import common.readInput
import java.math.BigInteger

data class Equation(val total: BigInteger, val numbers: List<BigInteger>) {
    /**
     * The numbers list is evaluated left-to-right, and entries can be joined with + or *,
     * or if allowConcat is true, they can also be concatenated.
     */
    private fun canBeMade(allowConcat: Boolean): Boolean {
        fun aux(currTotal: BigInteger = numbers.first(),
            remainingNumbers: List<BigInteger> = numbers.drop(1)): Boolean {
            // currTotal can only ever increase, so if we already passed total, there is no
            // reason to continue.
            if (currTotal > total) return false

            // If we have used up all the numbers and have achieved total, success.
            if (remainingNumbers.isEmpty()) return currTotal == total

            // Calculate all of the possible extensions.
            val nextNumber = remainingNumbers.first()
            val nextRemainingNumbers = remainingNumbers.drop(1)
            return aux(currTotal + nextNumber, nextRemainingNumbers) ||
                    aux(currTotal * nextNumber, nextRemainingNumbers) ||
                    (allowConcat && aux("$currTotal$nextNumber".toBigInteger(), nextRemainingNumbers))
        }
        return aux()
    }

    fun canBeMadeWithPlusTimes() =
        canBeMade(false)

    fun canBeMadeWithPlusTimesConcat() =
        canBeMade(true)

    companion object {
        fun parseLine(input: String): Equation {
            val (total, remaining) = input.split(":")
            val numbers = remaining
                .trim()
                .split(Regex("""\s+"""))
                .map(String::toBigInteger)
            return Equation(total.toBigInteger(), numbers)
        }
    }
}

fun parse(input: String): List<Equation> =
    input.trim().lines().map(Equation::parseLine)

fun answer1(equations: List<Equation>): BigInteger =
    equations.filter(Equation::canBeMadeWithPlusTimes).sumOf(Equation::total)

fun answer2(equations: List<Equation>): BigInteger =
    equations.filter(Equation::canBeMadeWithPlusTimesConcat).sumOf(Equation::total)


fun main() {
    val input = readInput({}::class.day()).trim()
    val equations = parse(input)

    println("--- Day 7: Bridge Repair ---")

    // Part 1: 2437272016585
    println("Part 1: ${answer1(equations)}")

    // Part 2: 162987117690649
    println("Part 2: ${answer2(equations)}")
}
