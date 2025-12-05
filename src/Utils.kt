import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, day: Int) = Path("inputs/day${day}/$name.txt").readText().trim().lines()

/**
 * Reads a single line from the given input txt file.
 */
fun readInputString(name: String, day: Int) = Path("inputs/day${day}/$name.txt").readText().trim()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

inline fun executeWithTime(part1: Boolean = true, block: () -> Any) {
    val (result, duration) = measureTimedValue(block)
    if (part1) {
        println("Part 1")
    } else {
        println("Part 2")
    }
    println("--------------------")
    println("Execution time: $duration")
    println("Result: $result")
    println("--------------------")
}

fun Int.isOdd() = this % 2 != 0
fun Int.isEven() = this % 2 == 0
fun String.toListOfDigits() = map { it.digitToInt() }
fun <T> List<T>.breakOn(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val index = indexOfFirst(predicate)
    if(index == -1) throw IllegalArgumentException("No element matched the predicate.")
    if (index == 0) return emptyList<T>() to this
    if (index == lastIndex) return this to emptyList()
    return take(index) to drop(index + 1)
}