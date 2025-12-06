fun main() {

    fun calculate(operator: String, values: List<Long>): Long = when (operator) {
        "+" -> values.sum()
        "*" -> values.fold(1L) { acc, v -> acc * v }
        else -> error("Unknown operator $operator")
    }

    fun part1(input: List<String>): Long {
        val operations = input.last().split(whitespaceRegex).iterator()
        val problemsBoard = Grid(input.dropLast(1))

        return problemsBoard
            .allRowsData()
            .map { it.toLongList() }
            .transpose()
            .sumOf { problem ->
                calculate(operations.next(), problem)
            }
    }

    fun part2(input: List<String>): Long {
        val operations = input.last().split(whitespaceRegex).iterator()
        val problemsBoard = Grid(input.dropLast(1))

        return problemsBoard
            .allColumnsData()
            .split(String::isBlank) { it.trim().toLong() }
            .sumOf { problem ->
                calculate(operations.next(), problem)
            }
    }


    val testInput = readInput("test_input", 6)

    check(part1(testInput) == 4277556L)
    check(part2(testInput) == 3263827L)

    val input = readInput("input", 6)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}