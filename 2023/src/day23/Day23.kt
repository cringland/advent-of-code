package day23

import Day
import day17.Day17
import util.Dir
import util.Point2
import util.by
import java.util.*
import kotlin.math.abs

private fun Point2.inBounds(input: List<String>) = this.y in input.indices && this.x in input[this.y].indices
class Day23 : Day {
    private val input = inputFile().readLines()
    private val start = input.first().indexOf('.') by 0
    private val end = input.last().indexOf('.') by input.size - 1

    data class Hike(
        val x: Int,
        val y: Int,
//        val path: List<Point2>, // Not needed but useful for debugging
        val pathLength: Long
    ){

        fun state() = x by y
    }

    override fun problemOne(): Number {
        return findLargestPath(start, end) { it.neigbours() }!!
    }

    override fun problemTwo(): Number {
        val decPoints = input.findDecisionPoints()
        val decPointToDecPointMap = decPoints.associateWith { point1 ->
            decPoints
                .filter { it != point1 }
                .mapNotNull { point2 ->
                    findLargestPath(
                        from = point1,
                        to = point2,
                        filterFunc = { it.x by it.y == point2 || it.x by it.y !in decPoints }) { it.neigboursP2() }?.let { point2 to it }
                }
                .toMap()
        }
        return findLargestPath(start, end) {
            decPointToDecPointMap[it.x by it.y]!!.toList().map { pointToLength -> Hike(pointToLength.first.x, pointToLength.first.y, it.pathLength + pointToLength.second) }.toSet()
        }!!
    }

    private fun findLargestPath(
        from: Point2,
        to: Point2,
        filterFunc: (Hike) -> Boolean = { _ -> true },
        neighbourFunc: (Hike) -> Set<Hike>
    ): Long? {
        val startPoint = Hike(from.x, from.y, 0)
        val hikeLengths = mutableSetOf<Long>()
        fun Hike.recurse(currentlySeen: Set<Point2>): Unit = neighbourFunc(this)
            .filter { it.x by it.y !in currentlySeen }
            .filter(filterFunc)
            .forEach {
                if (it.x by it.y == to) hikeLengths += it.pathLength
                else it.recurse(currentlySeen + (it.x by it.y))
            }
        startPoint.recurse(setOf(from))
        return hikeLengths.max()
    }

    private fun List<String>.findDecisionPoints(): Set<Point2> {
        return this.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if (c != '#') {
                    (x by y).let { p ->
                        if (p.adjacent()
                                .filter { it.inBounds(this) }
                                .filter { this[it.y][it.x] != '#' }.size > 2
                        ) {
                            p
                        } else null
                    }
                } else null
            }
        }.flatten().toSet() + start + end
    }

    private fun Hike.neigboursP2(): Set<Hike> {
        val thisPoint = this.x by this.y
        return thisPoint.adjacentWithDir()
            .asSequence()
            .filter { it.second.inBounds(input) }
            .map { it.second to input[it.second.y][it.second.x] }
            .filter { it.second != '#' }
            .map { Hike(it.first.x, it.first.y, this.pathLength + 1) }
            .toSet()
    }

    private fun Hike.neigbours(): Set<Hike> {
        val thisPoint = this.x by this.y
        return thisPoint.adjacentWithDir()
            .filter { it.second.inBounds(input) }
            .map { Triple(it.first, it.second, input[it.second.y][it.second.x]) }
            .mapNotNull {
                val (dir, point, char) = it
                when (char) {
                    '#' -> null
                    '^' -> if (dir == Dir.D) null else Hike(point.x, point.y - 1, this.pathLength + 2)
                    '>' -> if (dir == Dir.L) null else Hike(point.x + 1, point.y, this.pathLength + 2)
                    '<' -> if (dir == Dir.R) null else Hike(point.x - 1, point.y, this.pathLength + 2)
                    'v' -> if (dir == Dir.U) null else Hike(point.x, point.y + 1, this.pathLength + 2)
                    else -> Hike(point.x, point.y, this.pathLength + 1)
                }
            }.toSet()
    }

}
