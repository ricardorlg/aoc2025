fun main() {

    fun countBeamSplitting(start: Point2D, grid: Grid): Int {
        val queue = mutableListOf(start)
        val seen = mutableSetOf<Point2D>()
        while (queue.isNotEmpty()) {
            val currentPoint = queue.removeFirst()
            val nextSplit = (currentPoint.row + 1 until grid.rows)
                .asSequence()
                .map { Point2D(it, currentPoint.column) }
                .find { grid[it] == '^' }

            if (nextSplit != null && seen.add(nextSplit)) {
                queue.add(nextSplit.move(Direction.DOWN_LEFT))
                queue.add(nextSplit.move(Direction.DOWN_RIGHT))
            }
        }
        return seen.size
    }

    fun getAllTimelines(start: Point2D, grid: Grid): Long {
        val memo = mutableMapOf<Point2D, Long>()
        fun countPathsFrom(point: Point2D): Long {
            val cached = memo[point]
            if (cached != null) return cached
            val down = point.move(Direction.DOWN)
            return when (grid[down]) {
                null -> 1L
                '.', 'S' -> countPathsFrom(down)
                else -> countPathsFrom(point.move(Direction.DOWN_LEFT)) +
                        countPathsFrom(point.move(Direction.DOWN_RIGHT))
            }.also { memo[point] = it }

        }
        return countPathsFrom(start)
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val startPoint = grid.points.first { grid[it] == 'S' }
        return countBeamSplitting(startPoint, grid)
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)
        val startPoint = grid.points.first { grid[it] == 'S' }
        return getAllTimelines(startPoint, grid)
    }


    val testInput = readInput("test_input", 7)

    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("input", 7)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}
