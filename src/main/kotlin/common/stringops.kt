// Advent of Code
// By Sebastian Raaphorst, 2024.

package common

fun String.countSubstrings(substring: String): Int =
    Regex(Regex.escape(substring)).findAll(this).count()