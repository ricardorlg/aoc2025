import Machine.Buttons
import com.microsoft.z3.Context
import com.microsoft.z3.Status.SATISFIABLE


data class Machine(
    val lightDiagram: String,
    val buttons: List<Buttons>,
    val joltageRequirements: List<Int>,
) {

    data class Buttons(
        val lightsToToggle: List<Int>,
    ) {
        fun toggle(state: String): String {
            return buildString {
                state.forEachIndexed { index, c ->
                    if (lightsToToggle.contains(index)) {
                        append(if (c == '#') '.' else '#')
                    } else {
                        append(c)
                    }
                }
            }
        }
    }

    fun solveForDiagram(): Long {
        var currentStates = setOf(".".repeat(lightDiagram.length))
        var steps = 0L

        while (lightDiagram !in currentStates) {
            currentStates = currentStates.flatMap { state ->
                buttons.map { it.toggle(state) }
            }.toSet()
            steps++
        }
        return steps
    }

    fun solveForJoltageRequirements(): Long {
        Context().use { context ->
            val solver = context.mkOptimize()

            val buttonPushes = Array(buttons.size) { i ->
                context.mkIntConst("x$i").also { solver.Add(context.mkGe(it, context.mkInt(0))) }
            }

            val totalPushes = context.mkAdd(*buttonPushes)
                .also {
                    solver.MkMinimize(it)
                }


            joltageRequirements.forEachIndexed { i, joltage ->
                val relevantButtons = buttons.indices
                    .filter { i in buttons[it].lightsToToggle }
                    .map { buttonPushes[it] }
                    .toTypedArray()
                solver.Add(context.mkEq(context.mkAdd(*relevantButtons), context.mkInt(joltage)))
            }

            assert(solver.Check() == SATISFIABLE) { "No solution found" }
            return solver.model.eval(totalPushes, false).toString().toLong()
        }
    }
}

fun main() {

    fun machines(input: List<String>) = input.map { line ->
        val parts = line.split(" ")
        val lightDiagram = parts.first().substringAfter('[').substringBefore(']')
        val buttons = parts.subList(1, parts.lastIndex).map { Buttons(it.toIntList()) }
        val joltageRequirements = parts.last().toIntList()
        Machine(lightDiagram, buttons, joltageRequirements)
    }


    fun part1(manual: List<String>): Long {
        return machines(manual).sumOf { it.solveForDiagram() }
    }

    fun part2(manual: List<String>): Long {
        return machines(manual).sumOf { it.solveForJoltageRequirements() }
    }

    val testInput = readInput("test_input", 10)


    check(part1(testInput) == 7L)
    check(part2(testInput) == 33L)

    val input = readInput("input", 10)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2(input) }
}

