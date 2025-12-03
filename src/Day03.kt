fun main() {

    fun maxJoltage(data: List<Int>, size: Int): Long {
        var lastFoundIndex = 0
        return buildString {
            for (remaining in size - 1 downTo 0) {
                val searchEnd = data.size - remaining
                val (relativeIndex, digit) = data.subList(lastFoundIndex, searchEnd)
                    .withIndex()
                    .maxBy { it.value }
                append(digit)
                lastFoundIndex += relativeIndex + 1
            }
        }.toLong()
    }

    fun part1(input: List<String>): Long {
        return input.map { it.toListOfDigits() }.sumOf { maxJoltage(it, 2) }
    }

    fun part2(input: List<String>): Long {
        return input.map { it.toListOfDigits() }.sumOf { maxJoltage(it, 12) }
    }

    val testInput = readInput("test_input", 3)

    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619)

    val input = readInput("input", 3)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}