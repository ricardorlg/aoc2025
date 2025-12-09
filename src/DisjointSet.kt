class DisjointSet<T>(elements: List<T>) {
    private val parent = elements.associateWith { it }.toMutableMap()
    private val size = elements.associateWith { 1L }.toMutableMap()
    var numberOfComponents = elements.size
        private set

    fun find(item: T): T {
        if (parent[item] != item) {
            parent[item] = find(parent[item]!!)
        }
        return parent[item]!!
    }

    fun union(item1: T, item2: T): Boolean {
        val root1 = find(item1)
        val root2 = find(item2)
        if (root1 == root2) return false

        val size1 = size[root1]!!
        val size2 = size[root2]!!

        if (size1 < size2) {
            parent[root1] = root2
            size[root2] = size1 + size2
        } else {
            parent[root2] = root1
            size[root1] = size1 + size2
        }
        numberOfComponents--
        return true
    }

    fun componentSizes(): List<Long> =
        parent.keys.filter { parent[it] == it }.map { size[it]!! }
}