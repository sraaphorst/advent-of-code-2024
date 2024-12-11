// Advent of Code 2024, Day 07.
// By Sebastian Raaphorst, 2024.

package day07

import common.aocreader.fetchAdventOfCodeInput
import common.parsing.WhitespaceParser
import common.runner.timedFunction
import java.math.BigInteger

data class Equation(val total: BigInteger, val numbers: List<BigInteger>) {
    /**
     * The numbers list is evaluated left-to-right, and entries can be joined with + or *,
     * or if allowConcat is true, they can also be concatenated.
     */
    private fun canBeMade(allowConcat: Boolean): Boolean {
        // Memoization here does not significantly improve time.
        fun aux(currTotal: BigInteger = numbers.first(),
                remainingNumbers: List<BigInteger> = numbers.drop(1)): Boolean = when {
            currTotal > total -> false
            remainingNumbers.isEmpty() -> currTotal == total
            else -> {
                val nextNumber = remainingNumbers.first()
                val nextRemainingNumbers = remainingNumbers.drop(1)
                aux(currTotal + nextNumber, nextRemainingNumbers) ||
                        aux(currTotal * nextNumber, nextRemainingNumbers) ||
                        (allowConcat && aux("$currTotal$nextNumber".toBigInteger(), nextRemainingNumbers))
            }
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
                .split(WhitespaceParser)
                .map(String::toBigInteger)
            return Equation(total.toBigInteger(), numbers)
        }
    }
}

private fun parse(input: String): List<Equation> =
    input.trim().lines().map(Equation::parseLine)

fun answer1(input: String): BigInteger =
    parse(input).filter(Equation::canBeMadeWithPlusTimes).sumOf(Equation::total)

fun answer2(input: String): BigInteger =
    parse(input).filter(Equation::canBeMadeWithPlusTimesConcat).sumOf(Equation::total)


fun main() {
    val input = fetchAdventOfCodeInput(2024, 7)
    println("--- Day 7: Bridge Repair ---")
    timedFunction("Part 1") { answer1(input) } // 2437272016585
    timedFunction("Part 2") { answer2(input) } // 162987117690649
}
