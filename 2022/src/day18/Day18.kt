package day18

import Day
import java.util.ArrayDeque
import java.util.Queue

class Day18 : Day {
    private data class Cube(val x: Int, val y: Int, val z: Int) {
        fun neighbors() = setOf(
            Cube(x + 1, y, z),
            Cube(x - 1, y, z),
            Cube(x, y + 1, z),
            Cube(x, y - 1, z),
            Cube(x, y, z + 1),
            Cube(x, y, z - 1)
        )

        fun isNextTo(other: Cube): Boolean {

            fun Int.nextTo(i: Int): Boolean = i == this - 1 || i == this + 1
            return (x == other.x && y == other.y && z.nextTo(other.z))
                    || (x == other.x && z == other.z && y.nextTo(other.y))
                    || (y == other.y && z == other.z && x.nextTo(other.x))
        }

        companion object {
            fun of(input: String): Cube =
                input.split(",").map(String::toInt).let { Cube(it[0], it[1], it[2]) }
        }
    }

    private val input = inputFile().readLines().map { Cube.of(it) }.toSet()

    override fun problemOne(): Number {
        val temp = input.associateWith {
            val neighbors = input.filter { other -> it.isNextTo(other) }.size
            6 - neighbors
        }
        return temp.values.sum()
    }

    override fun problemTwo(): Number {
        val xRange = input.minBy { it.x }!!.x - 1..input.maxBy { it.x }!!.x + 1
        val yRange = input.minBy { it.y }!!.y - 1..input.maxBy { it.y }!!.y + 1
        val zRange = input.minBy { it.z }!!.z - 1..input.maxBy { it.z }!!.z + 1
        val queue = ArrayDeque<Cube>()
        queue.push(Cube(0, 0, 0))
        val seen = mutableSetOf<Cube>()
        var sidesFound = 0
        while (queue.isNotEmpty()) {
            val current = queue.pop()
            if (current !in seen) {
                current.neighbors()
                    .filter { it.x in xRange && it.y in yRange && it.z in zRange }
                    .forEach { neighbor ->
                        seen.add(current)
                        if (neighbor in input) {
                            sidesFound++
                        } else queue.add(neighbor)
                    }
            }
        }
        return sidesFound
    }
}
