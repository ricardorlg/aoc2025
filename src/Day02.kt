fun main() {

    fun parseInput(input: List<String>): List<LongRange> {
        return input.flatMap {
            it.split(",", "-")
                .chunked(2) { s ->
                    s[0].toLong()..s[1].toLong()
                }
        }
    }

    fun part1(input: List<String>): Long {
        val data = parseInput(input)
        return data.sumOf { range ->
            range.sumOf { n ->
                val str = n.toString()
                val length = str.length
                val half = length / 2
                n.takeIf { length.isEven() && str.take(half) == str.takeLast(half) } ?: 0
            }
        }
    }

    fun part2(input: List<String>): Long {
        val data = parseInput(input)
        return data.sumOf { range ->
            range.sumOf { n ->
                val str = n.toString()
                val length = str.length
                n.takeIf { (str + str).indexOf(str, 1) < length } ?: 0
            }
        }
    }

    val testInput = readInput("test_input", 2)

    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265)

    val input = readInput("input", 2)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }


}