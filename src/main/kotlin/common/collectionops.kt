// Advent of Code
// By Sebastian Raaphorst, 2024.

package common

/**
 * Given a Collection, convert it into a map with the frequencies of each element.
 */
fun <T> Collection<T>.toFrequencyMap(): Map<T, Int> =
    groupingBy { it }.eachCount()

/**
 * Given a List, return all the sublists without one element as a sequence.
 */
fun <T> List<T>.allListDrops(): Sequence<List<T>> =
    indices.asSequence()
        .map { idx -> this.take(idx) + this.takeLast(this.size - idx - 1) }