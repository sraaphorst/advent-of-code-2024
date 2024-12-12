// Advent of Code 2024
// By Sebastian Raaphorst, 2024.

package common.gridalgorithms

import common.intpos2d.*

data class Region(val area: Int, val perimeter: Int, val edges: Int)

fun findRegions(grid: Grid<Char>): List<Region> {
    val visited = mutableSetOf<IntPos2D>()
    val rows = grid.size
    val cols = grid[0].size

    fun neighbours(pos: IntPos2D): List<IntPos2D> =
        Direction.entries.map { dir ->
              pos + dir.delta
        }.filter { pos -> pos.first in 0 until rows && pos.second in 0 until cols }

    fun floodFill(start: IntPos2D): Region {
        val stack = mutableListOf(start)
        var area = 0
        var perimeter = 0
        val symbol = grid[start.first][start.second]
        var corners = 0

        while (stack.isNotEmpty()) {
            val pos = stack.removeLast()
            if (pos !in visited) {
                visited.add(pos)
                area++

                // Calculate perimeter: count neighbors that are not part of the same region,
                // as well as the walls that are out of bounds.
                val neighbours = neighbours(pos)
                val localPerimeter = neighbours.count { pos2 ->
                    grid[pos2.first][pos2.second] != symbol
                }
                val outOfBounds = Direction.entries.map { dir -> pos + dir.delta }
                    .count { pos2 -> pos2.first < 0 || pos2.first >= rows
                            || pos2.second < 0 || pos2.second >= cols }
                perimeter += localPerimeter + outOfBounds

                // Calculate the corners, which will ultimately give us the number of
                // edges. Every corner is a shift in direction, indicating an edge.
                corners += Diagonals.count { (d1, d2) ->
                    val side1 = grid[pos + d1.delta]
                    val side2 = grid[pos + d2.delta]
                    val corner = grid[pos + d1.delta + d2.delta]

                    // Two cases:
                    // 1. The symbol here is different from the corners:
                    //        ? B
                    //        B A
                    // 2. The symbol is the same as the sides but different from the corner:
                    //        B A
                    //        A A
                    (symbol != side1 && symbol != side2) ||
                            (symbol == side1 && symbol == side2 && symbol != corner)
                }

                // Add valid neighbors to the stack
                stack.addAll(
                    neighbours(pos).filter { pos2 ->
                        grid[pos2.first][pos2.second] == symbol && pos2 !in visited
                    }
                )
            }
        }

        // area is A
        // perimeter is b
        return Region(area, perimeter, corners)
    }

    // Iterate over the grid and find all regions
    return (0 until rows).flatMap { x ->
        (0 until cols).mapNotNull { y ->
            if (IntPos2D(x, y) !in visited) floodFill(x to y) else null
        }
    }
}
