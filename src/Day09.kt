import java.awt.Polygon
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Long {
        return input
            .map {
                val (col, row) = it.toIntList()
                Point2D(row, col)
            }.allPairs()
            .maxOf { (p1, p2) ->
                val height = abs(p1.row - p2.row) + 1
                val width = abs(p1.column - p2.column) + 1
                width.toLong() * height
            }
    }

    fun part2WithJavaArea(input: List<String>): Long {

        fun makeRectangle(p1: Point2D, p2: Point2D): Rectangle2D {
            val lowerRow = minOf(p1.row, p2.row)
            val lowerCol = minOf(p1.column, p2.column)
            val height = abs(p1.row - p2.row)
            val width = abs(p1.column - p2.column)
            return Rectangle2D.Double(lowerCol.toDouble(), lowerRow.toDouble(), width.toDouble(), height.toDouble())
        }

        val polygon = Polygon()
        val points = input.map {
            val (col, row) = it.toIntList()
            polygon.addPoint(col, row)
            Point2D(row, col)
        }
        val area = Area(polygon)
        return points
            .allPairs()
            .filter { (p1, p2) ->
                val rectangle = makeRectangle(p1, p2)
                area.contains(rectangle)
            }.maxOf { (p1, p2) ->
                val height = abs(p1.row - p2.row) + 1
                val width = abs(p1.column - p2.column) + 1
                width.toLong() * height
            }
    }

    fun part2WithAABB(input: List<String>): Long {

        fun makeBoundingBox(p1: Point2D, p2: Point2D): Pair<Point2D, Point2D> {
            val minRow = minOf(p1.row, p2.row)
            val minCol = minOf(p1.column, p2.column)
            val maxRow = maxOf(p1.row, p2.row)
            val maxCol = maxOf(p1.column, p2.column)
            return Pair(Point2D(minRow, minCol), Point2D(maxRow, maxCol))
        }

        fun Pair<Point2D, Point2D>.intersects(other: Pair<Point2D, Point2D>): Boolean {
            return first.column < other.second.column &&
                    second.column > other.first.column &&
                    first.row < other.second.row &&
                    second.row > other.first.row
        }

        val points = input.map {
            val (col, row) = it.toIntList()
            Point2D(row, col)
        }

        val edgeBoundingBoxes = (points + points.first())
            .zipWithNext()
            .map { (p1, p2) -> makeBoundingBox(p1, p2) }

        return points
            .allPairs()
            .filter { (p1, p2) ->
                val candidateBox = makeBoundingBox(p1, p2)
                edgeBoundingBoxes.none { edgeBox -> candidateBox.intersects(edgeBox) }
            }
            .maxOf { (p1, p2) ->
                val height = abs(p1.row - p2.row) + 1
                val width = abs(p1.column - p2.column) + 1
                width.toLong() * height
            }
    }

    val testInput = readInput("test_input", 9)

    check(part1(testInput) == 50L)
    check(part2WithAABB(testInput) == 24L)

    val input = readInput("input", 9)
    executeWithTime(true) { part1(input) }
    executeWithTime(false) { part2WithAABB(input) }
}

