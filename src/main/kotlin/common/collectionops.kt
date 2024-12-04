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

fun <T> List<List<T>>.transpose(): List<List<T>> =
    indices.map { col -> map { it[col] } }

fun List<String>.transposeStrings(): List<String> =
    this[0].indices.map { col -> map { it[col] }.joinToString("") }

fun <T> List<List<T>>.reflectHorizontal(): List<List<T>> =
    map(List<T>::reversed)

fun List<String>.reflectHorizontalStrings(): List<String> =
    map(String::reversed)

fun <T> List<List<T>>.reflectVertical(): List<List<T>> =
    reversed()

fun List<String>.reflectVerticalStrings(): List<String> =
    reversed()

fun <T> List<List<T>>.rotateClockwise(): List<List<T>> =
    transpose().map(List<T>::reversed)

fun List<String>.rotateClockwiseStrings(): List<String> =
    transposeStrings().map(String::reversed)

fun <T> List<List<T>>.rotateCounterClockwise(): List<List<T>> =
    transpose().reversed()

fun List<String>.rotateCounterClockwiseStrings(): List<String> =
    transposeStrings().reversed()

fun <T> List<List<T>>.getEastEntries(): List<List<T>> =
    this

fun List<String>.getEastStrings(): List<String> =
    this

fun <T> List<List<T>>.getWestEntries(): List<List<T>> =
    map(List<T>::reversed)

fun List<String>.getWestStrings(): List<String> =
    map(String::reversed)

fun <T> List<List<T>>.getSouthEntries(): List<List<T>> =
    transpose()

fun List<String>.getSouthStrings(): List<String> =
    transposeStrings()

fun <T> List<List<T>>.getNorthEntries(): List<List<T>> =
    getSouthEntries().map(List<T>::reversed)

fun List<String>.getNorthStrings(): List<String> =
    getSouthStrings().map(String::reversed)

fun <T> List<List<T>>.getSEEntries(): List<List<T>> {
    val rows = size
    val cols = this[0].size

    // Get all diagonals starting from the first column
    val leftDiagonals = (0 until rows).map { startRow ->
        generateSequence(startRow to 0) { (row, col) ->
            if (row + 1 < rows && col + 1 < cols) (row + 1 to col + 1) else null
        }.map { (row, col) -> this[row][col] }.toList()
    }

    // Get all diagonals starting from the top row, excluding the first column
    val topDiagonals = (1 until cols).map { startCol ->
        generateSequence(0 to startCol) { (row, col) ->
            if (row + 1 < rows && col + 1 < cols) (row + 1 to col + 1) else null
        }.map { (row, col) -> this[row][col] }.toList()
    }

    return leftDiagonals + topDiagonals
}

fun List<String>.getSEStrings(): List<String> =
    map(String::toList).getSEEntries().map { it.joinToString("") }

fun <T> List<List<T>>.getSWEntries(): List<List<T>> =
    getWestEntries().getSEEntries()

fun List<String>.getSWStrings(): List<String> =
    map(String::toList).getSWEntries().map { it.joinToString("") }

fun <T> List<List<T>>.getNWEntries(): List<List<T>> =
    getSEEntries().map(List<T>::reversed)

fun List<String>.getNWStrings(): List<String> =
    getSEStrings().map(String::reversed)

fun <T> List<List<T>>.getNEEntries(): List<List<T>> =
    getSWEntries().map(List<T>::reversed)

fun List<String>.getNEStrings(): List<String> =
    getSWStrings().map(String::reversed)

fun <T> List<List<T>>.extractBlocks(m: Int, n: Int = m): List<List<List<T>>> {
    // Take m rows.
    val rowBlocks = windowed(m, 1)
    return rowBlocks.flatMap { rows ->
        // Take n columns.
        val columnBlocks = rows.map { it.windowed(n, 1) }
        columnBlocks[0].indices.map { index ->
            // Combine into blocks.
            columnBlocks.map { it[index] }
        }
    }
}
