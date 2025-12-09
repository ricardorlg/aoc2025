fun main() {

    fun parseInput(input: List<String>): List<Point3D> = input.map {
        val (x, y, z) = it.toIntList()
        Point3D(x, y, z)
    }

    fun List<Point3D>.sortedByDistance(): Sequence<Pair<Point3D, Point3D>> {
        val comparator = compareBy<Pair<Point3D, Point3D>> { (p1, p2) -> p1.squaredDistance(p2) }
        return allPairs()
            .map { (p1, p2) -> p1 to p2 }
            .sortedWith(comparator)
    }

    fun part1(input: List<String>, maxNumberOfConnections: Int): Long {
        val junctionBoxes = parseInput(input)
        val circuits = DisjointSet(junctionBoxes)

        junctionBoxes
            .sortedByDistance()
            .take(maxNumberOfConnections)
            .forEach { (box1, box2) -> circuits.union(box1, box2) }

        return circuits.componentSizes().sortedDescending().take(3).reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val junctionBoxes = parseInput(input)
        val circuits = DisjointSet(junctionBoxes)

        return junctionBoxes
            .sortedByDistance()
            .first { (box1, box2) -> circuits.union(box1, box2) && circuits.numberOfComponents == 1 }
            .let { (box1, box2) -> box1.x.toLong() * box2.x }
    }

    val testInput = readInput("test_input", 8)

    part1(testInput, 10).println()
    check(part1(testInput, 10) == 40L)
    check(part2(testInput) == 25272L)

    val input = readInput("input", 8)
    executeWithTime(true) { part1(input, 1000) }
    executeWithTime(false) { part2(input) }
}

