// Advent of Code 2024, Day 07.
// By Sebastian Raaphorst, 2024.

// NOTE: Trying to use pure FP in this question made part 2 run extremely slowly.
// Mutable data structures are needed to avoid having to continuously copy structures.

package day07

import common.day
import common.readInput

data class Equation(val total: Long, val numbers: List<Long>) {
    private fun canBeMade(allowConcat: Boolean): Boolean {
        fun aux(currTotal: Long = numbers.first(),
            remainingNumbers: List<Long> = numbers.drop(1)): Boolean {
            if (remainingNumbers.isEmpty()) return currTotal == total
            val nextNumber = remainingNumbers.first()
            val nextRemainingNumbers = remainingNumbers.drop(1)
            return aux(currTotal + nextNumber, nextRemainingNumbers) ||
                    aux(currTotal * nextNumber, nextRemainingNumbers) ||
                    (allowConcat && return aux("$currTotal$nextNumber".toLong(), remainingNumbers))
        }
        return aux()
    }

    fun canBeMadeWithPlusTimes() =
        canBeMade(false)

    fun canBeMadeWithPlusTimesConcat() =
        canBeMade(true)

    companion object {
        fun parseLine(input: String): Equation {
            println("Parsing $input")
            val total = input.takeWhile(Char::isDigit).toLong()
            val numbers = input.dropWhile { it != ':' }
                .drop(1)
                .trim()
                .split(Regex("""\s+"""))
                .also(::println)
                .filter(String::isNotBlank)
                .map(String::toLong)
            return Equation(total, numbers)
        }
    }
}

fun parse(input: String): List<Equation> =
    input.trim().lines().map(Equation::parseLine)

fun answer1(equations: List<Equation>): Long =
    equations.filter(Equation::canBeMadeWithPlusTimes).sumOf(Equation::total)

fun answer2(equations: List<Equation>): Long =
    equations.filter(Equation::canBeMadeWithPlusTimesConcat).sumOf(Equation::total)


fun main() {
    val input = readInput({}::class.day()).trim()
    val equations = parse(input)

    println("--- Day 7:  ---")

    // Part 1: 2437272016585
    println("Part 1: ${answer1(equations)}")

    // Part 2:
//    println("Part 2: ${answer2(input)}")
}
