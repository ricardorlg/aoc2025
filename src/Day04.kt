fun main() {

    fun Grid.forkliftsHaveAccess(point: Point2D): Boolean {
        val neighbors = getAdjacentItems(point)
        return this[point] == '@' && neighbors.count { it == '@' } < 4
    }

    fun part1(input: List<String>): Int {
        val rollsOfPaper = Grid(input)
        return rollsOfPaper.points.count {
            rollsOfPaper.forkliftsHaveAccess(it)
        }
    }

    fun part2(input: List<String>): Int {
        val rollsOfPaper = Grid(input)
        return generateSequence {
            rollsOfPaper
                .points
                .filter { rollsOfPaper.forkliftsHaveAccess(it) }
                .onEach { rollsOfPaper[it] = 'x' }
                .size.takeIf { it > 0 }
        }.sum()
    }


    val testInput = readInput("test_input", 4)

    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    val input = readInput("input", 4)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}