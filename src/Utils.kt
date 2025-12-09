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
    if (index == -1) throw IllegalArgumentException("No element matched the predicate.")
    if (index == 0) return emptyList<T>() to this
    if (index == lastIndex) return this to emptyList()
    return take(index) to drop(index + 1)
}

fun String.toIntList() = numberRegex.findAll(this).map { match -> match.value.toInt() }.toList()

fun String.toLongList() = numberRegex.findAll(this).map { match -> match.value.toLong() }.toList()

val numberRegex = "-?\\d+".toRegex()
val whitespaceRegex = "\\s+".toRegex()

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val width = firstOrNull()?.size ?: return emptyList()
    return (0 until width).map { col ->
        map { row -> row[col] }
    }
}

/**
 * Splits the iterable into a list of lists, starting a new list whenever an element matches the [predicate].
 *
 * Example:
 * ```
 * val list = listOf(1, 2, 0, 3, 4)
 * val result = list.split { it == 0 } // [[1, 2], [3, 4]]
 * ```
 */
fun <T> Iterable<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    return split(predicate) { it }
}

/**
 * Splits the iterable into a list of lists, starting a new list whenever an element matches the [predicate],
 * and transforming the non-separator elements using [transform].
 */
inline fun <T, R> Iterable<T>.split(predicate: (T) -> Boolean, transform: (T) -> R): List<List<R>> {
    val result = ArrayList<List<R>>()
    var current = ArrayList<R>()
    for (element in this) {
        if (predicate(element)) {
            result.add(current)
            current = ArrayList()
        } else {
            current.add(transform(element))
        }
    }
    result.add(current)
    return result
}

/**
 * Splits the iterable into a list of lists, starting a new list whenever an element equals the [separator].
 */
fun <T> Iterable<T>.split(separator: T): List<List<T>> = split { it == separator }

fun <T> List<T>.combinations(size: Int): List<List<T>> {
    if (size == 0) return listOf(emptyList())
    if (isEmpty()) return emptyList()

    val head = first()
    val tail = drop(1)

    return tail.combinations(size - 1).map { listOf(head) + it } + tail.combinations(size)
}

fun <T> List<T>.allPairs(): Sequence<Pair<T, T>> {
    return sequence {
        for (i in indices) {
            for (j in (i + 1) until size) {
                yield(get(i) to get(j))
            }
        }
    }
}