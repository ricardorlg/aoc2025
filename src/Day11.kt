fun main() {

    fun parseInput(input: List<String>) = input.associate { line ->
        val (k, v) = line.split(":").map { it.trim() }
        k to v.split(" ")
    }

    fun countPathsFromNodeToOther(graph: Map<String, List<String>>, start: String, end: String): Long {
        val memo = mutableMapOf<String, Long>()

        fun countFrom(current: String): Long =
            memo.getOrPut(current) {
                if (current == end) 1L
                else graph[current].orEmpty().sumOf { next -> countFrom(next) }
            }

        return countFrom(start)
    }


    fun part1(input: List<String>): Long {
        val graph = parseInput(input)
        return countPathsFromNodeToOther(graph, "you", "out")
    }

    fun part2(input: List<String>): Long {
        val graph = parseInput(input)

        val startNode = "svr"
        val endNode = "out"
        val dacNode = "dac"
        val fftNode = "fft"

        data class State(val node: String, val seenDac: Boolean, val seenFft: Boolean)

        val memo = mutableMapOf<State, Long>()

        fun countPaths(state: State): Long =
            memo.getOrPut(state) {
                if (state.node == endNode) {
                    if (state.seenDac && state.seenFft) 1L else 0L
                } else {
                    graph[state.node].orEmpty().sumOf { next ->
                        val nextState = State(
                            node = next,
                            seenDac = state.seenDac || next == dacNode,
                            seenFft = state.seenFft || next == fftNode
                        )
                        countPaths(nextState)
                    }
                }
            }

        return countPaths(State(startNode, seenDac = false, seenFft = false))
    }

    /**
     * Alternative solution for part 2.
     * The number of paths between nodes A to C that pass to B is equal to the product of the number of paths from A to B and the number of paths from B to C.
     */
    fun part2Alternative(input: List<String>): Long {
        val graph = parseInput(input)
        val dacToFFT = countPathsFromNodeToOther(graph, "dac", "fft")
        // If there are paths from dac to fft, the order must be svr -> dac -> fft -> out
        // We can't have both dac-to-fft and fft-to-dac paths, as it would imply a cycle.
        if (dacToFFT > 0) {
            val svrToDac = countPathsFromNodeToOther(graph, "svr", "dac")
            val fftToOut = countPathsFromNodeToOther(graph, "fft", "out")
            return svrToDac * dacToFFT * fftToOut
        } else {
            val svrToFFT = countPathsFromNodeToOther(graph, "svr", "fft")
            val fftToDac = countPathsFromNodeToOther(graph, "fft", "dac")
            val dacToOut = countPathsFromNodeToOther(graph, "dac", "out")
            return svrToFFT * fftToDac * dacToOut
        }
    }

    val testInput = readInput("test_input", 11)
    val testInput2 = readInput("test_input_2", 11)

    check(part1(testInput) == 5L)
    check(part2(testInput2) == 2L)

    val input = readInput("input", 11)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}
