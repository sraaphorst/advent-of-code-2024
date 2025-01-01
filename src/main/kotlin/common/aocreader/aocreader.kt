// Advent of Code 2024.
// By Sebastian Raaphorst, 2024.

package common.aocreader

import java.net.HttpURLConnection
import java.net.URI

private fun getCookie(): String =
    System.getenv("AOC_SESSION") ?: {}::class.java.getResource("/cookie")!!.readText().trim()

fun fetchAdventOfCodeInput(year: Int, day: Int): String {
    val sessionCookie = getCookie()
    val url = URI("https://adventofcode.com/$year/day/$day/input").toURL()
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Cookie", "session=$sessionCookie")
    connection.setRequestProperty("User-Agent", "AoC Kotlin Client")

    return if (connection.responseCode == 200) {
        connection.inputStream.bufferedReader().use { it.readText().trim() }
    } else {
        throw Exception("Failed to fetch input: HTTP ${connection.responseCode}")
    }
}
