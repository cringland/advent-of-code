package day22

import Day
import java.io.File
import java.lang.Long.max
import java.lang.Long.min

class Day22 : Day {

    data class Cuboid(val x1: Long, val x2: Long, val y1: Long, val y2: Long, val z1: Long, val z2: Long) {
        val volume get() = (x2 - x1 + 1) * (y2 - y1 + 1) * (z2 - z1 + 1)

        fun intersect(that: Cuboid): Cuboid? {
            val newCuboid = Cuboid(max(this.x1, that.x1), min(this.x2, that.x2), max(this.y1, that.y1), min(this.y2, that.y2), max(this.z1, that.z1), min(this.z2, that.z2))
            return if (newCuboid.x1 <= newCuboid.x2 && newCuboid.y1 <= newCuboid.y2 && newCuboid.z1 <= newCuboid.z2)
                newCuboid
            else null
        }
    }

    private val pattern = """(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)""".toRegex()

    private val cuboids = File("src/day22/input").readLines()
            .filter { it.isNotEmpty() }
            .map { str ->
                val (on, x1, x2, y1, y2, z1, z2) = pattern.matchEntire(str)!!.destructured
                (on == "on") to Cuboid(x1.toLong(), x2.toLong(), y1.toLong(), y2.toLong(), z1.toLong(), z2.toLong())
            }.fold(mutableListOf<Pair<Boolean, Cuboid>>()) { cuboids, current ->
                cuboids.addAll(cuboids.mapNotNull {
                    val int = it.second.intersect(current.second)
                    if (int != null) !it.first to int else null
                })
                if (current.first) cuboids.add(current)
                cuboids
            }

    override fun problemOne(): Long {
        return cuboids.mapNotNull {
            val int = it.second.intersect(Cuboid(-50, 50, -50, 50, -50, 50))
            if (int != null) it.first to int else null
        }.map { if (it.first) it.second.volume else -1 * it.second.volume }.sum()
    }

    override fun problemTwo(): Long {
        return cuboids.map { if (it.first) it.second.volume else -1 * it.second.volume }.sum()
    }
}

