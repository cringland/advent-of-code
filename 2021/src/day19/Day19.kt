package day19

import Day
import java.io.File
import kotlin.math.abs

data class Point3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(that: Point3): Point3 = Point3(this.x + that.x, this.y + that.y, this.z + that.z)
    operator fun minus(that: Point3): Point3 = Point3(this.x - that.x, this.y - that.y, this.z - that.z)
}

class Day19 : Day {

    private val flips: List<(Point3) -> Point3> = listOf(
            { it -> it },
            { it -> Point3(it.x, -it.y, -it.z) },
            { it -> Point3(it.x, -it.z, it.y) },
            { it -> Point3(-it.y, -it.z, it.x) },
            { it -> Point3(it.y, -it.z, -it.x) },
            { it -> Point3(-it.x, -it.z, -it.y) }
    )

    private val rotations: List<(Point3) -> Point3> = listOf(
            { it -> it },
            { it -> Point3(-it.y, it.x, it.z) },
            { it -> Point3(-it.x, -it.y, it.z) },
            { it -> Point3(it.y, -it.x, it.z) }
    )

    private val input = File("src/day19/input").readText().split("--- scanner \\d+ ---".toRegex())
            .filter { it.isNotEmpty() }
            .map {
                it.split("\n").filter(String::isNotEmpty).map { str ->
                    val (a, b, c) = str.split(",")
                    Point3(a.toInt(), b.toInt(), c.toInt())
                }.toSet()
            }

    data class Result(val transformed: List<Set<Point3>>, val scanners: Set<Point3>)

    private val result = transform(input)

    private fun transform(input: List<Set<Point3>>): Result {
        val transformed = mutableListOf(input.first())
        val toFind = input.drop(1).toMutableList()
        val scanners = mutableSetOf(Point3(0, 0, 0))
        var i = 0
        while (toFind.isNotEmpty()) {
            val removeIndices = mutableListOf<Int>()
            toFind.forEachIndexed { index, init ->
                flip@ for (flipper in flips) {
                    for (rotator in rotations) {
                        val mutated = init.map { rotator(flipper(it)) }.toSet()
                        val currentSolved = transformed[i]
                        for (thisOne in mutated) {
                            for (thatOne in currentSolved) {
                                val diff = thatOne - thisOne
                                val translated = mutated.map { it + diff }.toSet()
                                if (translated.intersect(currentSolved).size >= 12) {
                                    transformed.add(translated)
                                    removeIndices.add(index)
                                    scanners.add(diff)
                                    break@flip
                                }
                            }
                        }
                    }
                }
            }
            i++
//            println("Removing $removeIndices")
            removeIndices.sortedDescending().forEach { toFind.removeAt(it) }
//            println("${toFind.size} left to find")
        }
        return Result(transformed, scanners)
    }

    override fun problemOne(): Int {
        return result.transformed.flatten().toSet().size
    }

    override fun problemTwo(): Int {
        var maxManhattan = 0
        result.scanners.forEach { p1 ->
            result.scanners.forEach { p2 ->
                if (p1 != p2) {
                    val diff = p1 - p2
                    val absDiff = Point3(abs(diff.x), abs(diff.y), abs(diff.z))
                    val manhatDiff = absDiff.x + absDiff.y + absDiff.z
                    if (maxManhattan < manhatDiff) maxManhattan = manhatDiff
                }
            }
        }
        return maxManhattan
    }
}

