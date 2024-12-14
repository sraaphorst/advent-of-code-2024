// Advent of Code 2024.
// By Sebastian Raaphorst, 2024.

package common.runner

inline fun <T> timedFunction(functionName: String, block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("$functionName: $result (${(endTime - startTime) / 1_000_000.0} ms)")
    return result
}
