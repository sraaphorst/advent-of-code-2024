// Advent of Code 2024.
// By Sebastian Raaphorst, 2024.

package common.gridalgorithms

import common.vec2d.*

data class Region(val area: Int, val perimeter: Int, val edges: Int)

fun findRegions(grid: Grid<Char>): List<Region> {
    val visited = mutableSetOf<Vec2D<Int>>()
    val rows = grid.size
    val cols = grid[0].size

    fun neighbours(pos: Vec2DInt): List<Vec2DInt> =
        Direction.entries.map { dir ->
              pos + dir.delta
        }.filter { pos -> pos.x in 0 until rows && pos.y in 0 until cols }

    fun floodFill(start: Vec2DInt): Region {
        val stack = mutableListOf(start)
        var area = 0
        var perimeter = 0
        val symbol = grid[start.x][start.y]
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
                    grid[pos2.x][pos2.y] != symbol
                }
                val outOfBounds = Direction.entries.map { dir -> pos + dir.delta }
                    .count { pos2 -> pos2.x < 0 || pos2.x >= rows
                            || pos2.y < 0 || pos2.y >= cols }
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
                        grid[pos2.x][pos2.y] == symbol && pos2 !in visited
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
            val vec = Vec2D.int(x, y)
            if (vec !in visited) floodFill(vec) else null
        }
    }
}
