// Advent of Code
// By Sebastian Raaphorst, 2024.

package common

import kotlin.reflect.KClass

/**
 * Extract the name of the day.
 */
fun KClass<*>.day(): String =
    this.java.packageName ?: throw RuntimeException("No package name found")

/**
 * Read the input for the day.
 */
fun readInput(day: String): String =
    {}::class.java.getResource("/$day.txt")!!.readText()
