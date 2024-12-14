// Advent of Code 2024, Day 14.
// By Sebastian Raaphorst, 2024.

package day14

import common.aocreader.fetchAdventOfCodeInput
import common.runner.timedFunction
import common.vec2d.*

data class Room(val cols: Int, val rows: Int)

// In this case, the positions are the cols and the rows as opposed to the normal
// (rows, cols) input.
class Robot(private val startPos: Vec2DInt, val velocity: Vec2DInt) {
    var pos = startPos

    fun move(room: Room) {
        pos = pos + velocity

        // This is toroidal, so we need to modulo the room size.
        pos = Vec2D.int((pos.x + room.cols) % room.cols, (pos.y + room.rows) % room.rows)
    }

    companion object {
        private val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")

        fun parse(input: String): Robot {
            val match = regex.matchEntire(input.trim()) ?: error("Invalid input $input")
            val (x, y, vx, vy) = match.destructured
            return Robot(Vec2D.int(x.toInt(), y.toInt()), Vec2D.int(vx.toInt(), vy.toInt()))
        }
    }
}

fun parse(input: String): Pair<Room, List<Robot>> {
    val lines = input.trim().lines()
    val roomCoords = lines.first().trim().split(",").map(String::toInt)
    val room = Room(roomCoords[0], roomCoords[1])
    val robots = lines.drop(1).map(Robot::parse)
    return Pair(room, robots)
}

fun answer1(input: String): Int {
    val (room, robots) = parse(input)
    (1..100).forEach {
        robots.forEach { robot -> robot.move(room) }
    }

    // Now parse the robots in all quarters.
    val left = robots.filter { it.pos.x in 0 until (room.cols / 2) }
    val right = robots.filter { it.pos.x in (room.cols / 2 + 1) until room.cols }
    val nw = left.filter { it.pos.y in 0 until (room.rows / 2) }
    val sw = left.filter { it.pos.y in (room.rows / 2 + 1) until room.rows }
    val ne = right.filter { it.pos.y in 0 until (room.rows / 2) }
    val se = right.filter { it.pos.y in (room.rows / 2 + 1) until room.rows }
    return nw.size * sw.size * ne.size * se.size
}

// People said that when the robots were all in unique positions was the
// first time the "Easter egg" was found, so we try this, and it works.
fun answer2(input: String): Int {
    val (room, robots) = parse(input)

    tailrec fun aux(timer: Int = 0): Int =
        if (robots.map { it.pos }.toSet().size == robots.size) timer
        else {
            robots.forEach { it.move(room) }
            aux(timer + 1)
        }

    return aux()
}

fun main() {
    val input = fetchAdventOfCodeInput(2024, 14)
    println("--- Day 14: Restroom Redoubt ---")
    timedFunction("Part 1") { answer1("101,103\n" + input) } // 231221760
    timedFunction("Part 2") { answer2("101,103\n" + input) } // 6771
}