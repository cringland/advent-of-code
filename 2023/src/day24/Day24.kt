package day24

import Day
import util.Point2

class Day24 : Day {
    data class Point3(val x: Double, val y: Double, val z: Double)

    data class Hailstone(val pos: Point3, val velocity: Point3) {
        companion object {
            fun of(line: String): Hailstone {
                val (p, v) = line.replace(" ", "").split("@")
                    .map { pText ->
                        pText.split(",").map { it.toDouble() }
                    }.map { Point3(it[0], it[1], it[2]) }
                return Hailstone(p, v)
            }
        }

        val m = velocity.y / velocity.x
        val b = pos.y - (m * pos.x)

        fun isPastX(value: Double): Boolean = velocity.x > 0 && value >= pos.x || velocity.x < 0 && value <= pos.x
        fun isPastY(value: Double): Boolean = velocity.y > 0 && value >= pos.y || velocity.y < 0 && value <= pos.y
        fun isPastZ(value: Double): Boolean = velocity.z > 0 && value >= pos.z || velocity.z < 0 && value <= pos.z
        fun intersection2D(other: Hailstone): Pair<Double, Double>? {
            if ((this.m - other.m) == 0.0) return null
            val intersectX = (-this.b + other.b) / (this.m - other.m)
            if (!isPastX(intersectX) || !other.isPastX(intersectX)) return null
            val intersectY = (m * intersectX) + b
            if (!isPastY(intersectY) || !other.isPastY(intersectY)) return null
            return intersectX to intersectY
        }
    }

    private val input = inputFile().readLines().map { Hailstone.of(it) }

    override fun problemOne(): Number {
        val range = if (input.size > 200) 200000000000000.00 .. 400000000000000.00 else 7.0 .. 27.0
        val intersections = input.mapIndexed { i, stone1 ->
            input.drop(i).mapNotNull { stone2 ->
                stone1.intersection2D(stone2)
            }
        }.flatten().filter { it.first in range && it.second in range }
        return intersections.size
    }

    override fun problemTwo(): Number {
        return 1
    }
}
