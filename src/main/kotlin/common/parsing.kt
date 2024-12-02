// Advent of Code
// By Sebastian Raaphorst, 2024.

package common

private val WhitespaceParser = Regex("""\s+""")

/**
 * Parse two columns into two lists.
 */
fun <C1, C2> parseColumns(input: String,
                          toC1: (String) -> C1,
                          toC2: (String) -> C2): Pair<List<C1>, List<C2>> =
    input.split('\n')
        .map { line ->
            line.trim().split(WhitespaceParser)
                .let { toC1(it[0]) to toC2(it[1]) }
        }.unzip()

/**
 * Parse a grid of values and return as a list of lists.
 * The lines must be separated by a newline, and the entries within the line with whitespace.
 * The grid can be ragged.
 */
fun <T> parseGrid(input: String, toElem: (String) -> T): List<List<T>> =
    input.split('\n')
        .map { line ->
            line.trim()
                .split(WhitespaceParser)
                .map { toElem(it) } }


