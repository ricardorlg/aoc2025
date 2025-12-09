import kotlin.math.abs

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
) {

    operator fun plus(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Point3D): Point3D {
        return Point3D(x - other.x, y - other.y, z - other.z)
    }

    operator fun unaryMinus(): Point3D {
        return Point3D(-x, -y, -z)
    }

    operator fun times(scalar: Int): Point3D {
        return Point3D(x * scalar, y * scalar, z * scalar)
    }

    fun squaredDistance(other: Point3D): Long {
        return (x - other.x).toLong() * (x - other.x).toLong() +
                (y - other.y.toLong()) * (y - other.y).toLong() +
                (z - other.z).toLong() * (z - other.z).toLong()
    }

}
