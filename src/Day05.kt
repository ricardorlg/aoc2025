import kotlin.math.max

fun main() {

    fun parseInput(input: List<String>, needIds: Boolean): Pair<List<LongRange>, List<Long>> {
        return input.breakOn { it.isBlank() }.let { (rangeLines, idsLines) ->
            val ranges = rangeLines
                .map { line ->
                    val (start, end) = line.split("-").map(String::toLong)
                    start..end
                }
            val ids = if (needIds) idsLines.map(String::toLong) else emptyList()
            ranges to ids
        }
    }


    fun part1(input: List<String>): Int {
        val (freshIdRanges, availableIngredients) = parseInput(input, true)
        return availableIngredients.count { id ->
            freshIdRanges.any { id in it }
        }
    }

    fun part2(input: List<String>): Long {
        val (freshIdRanges, _) = parseInput(input, false)
        return freshIdRanges.sortedBy { it.first }
            .fold(0L to Long.MIN_VALUE) { (sum, lastEnd), range ->
                val start = max(lastEnd, range.first)
                if (start <= range.last) (sum + range.last - start + 1) to range.last + 1 else (sum to lastEnd)
            }.first
    }


    val testInput = readInput("test_input", 5)

    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)

    val input = readInput("input", 5)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}