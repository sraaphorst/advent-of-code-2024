// Advent of Code 2024, Day 01.
// By Sebastian Raaphorst, 2024.

package common

fun <C1, C2> parseColumns2(input: String,
                           toC1: (String) -> C1,
                           toC2: (String) -> C2): Pair<List<C1>, List<C2>> =
    input.split('\n')
            .map { line ->
                line.trim().split("\\s+".toRegex())
                    .let { toC1(it[0]) to toC2(it[1]) }
            }.unzip()


fun <T> Collection<T>.toFrequencyMap(): Map<T, Int> =
    groupingBy { it }.eachCount()