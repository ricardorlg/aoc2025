fun main() {

    fun solve(part1: Boolean, input: List<String>): Int {
        val startPos = 50
        val modulus = 100

        return input.fold(startPos to 0) { (currentPos, totalHits), instruction ->
            val direction = instruction.first()
            val steps = instruction.drop(1).toInt()
            val delta = if (direction == 'L') -1 else 1
            val amount = steps * delta

            if (part1) {
                val nextPos = (currentPos + amount).mod(modulus)
                val hit = if (nextPos == 0) 1 else 0
                nextPos to (totalHits + hit)
            } else {
                var hits = 0
                val completeTurns = amount / (modulus * delta)
                hits += completeTurns
                val pendingTurns = amount % (modulus * delta)
                if (delta == -1 && currentPos != 0 && currentPos + pendingTurns <= 0) {
                    hits += 1
                }
                if (delta == 1 && currentPos + pendingTurns >= modulus) {
                    hits += 1
                }
                val nextPos = (currentPos + amount).mod(modulus)
                nextPos to (totalHits + hits)
            }
        }.second
    }

    val testInput = readInput("test_input", 1)

    check(solve(true, testInput) == 3)
    check(solve(false, testInput) == 6)

    val input = readInput("input", 1)
    executeWithTime(true) { solve(true, input) }
    executeWithTime(false) { solve(false, input) }
}
