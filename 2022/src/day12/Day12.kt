package day12

import Day
import util.Point2
import util.by
import java.util.*

private fun Char.canClimbTo(that: Char): Boolean {
    val actThis = if (this == 'S') 'a' else if (this == 'E') 'z' else this
    val actThat = if (that == 'S') 'a' else if (that == 'E') 'z' else that
    return actThat <= actThis || actThat == (actThis + 1)
}

private operator fun List<CharArray>.get(p: Point2) = this[p.y][p.x]

private fun List<CharArray>.coordsFor(c: Char): Point2 {
    val y = this.indexOfFirst { it.contains(c) }
    val x = this[y].indexOf(c)
    return x by y
}

private fun Point2.inBounds(input: List<CharArray>) =
    !((this.y >= input.size || this.y < 0) || (this.x >= input[this.y].size || this.x < 0))

class Day12 : Day {


    private val input = inputFile().readLines().map { it.toCharArray() }
    private val startPoint = input.coordsFor('S')
    private val endPoint = input.coordsFor('E')

    override fun problemOne(): Number {
        return dijkstra(startPoint, endPoint)!!.size
    }

    override fun problemTwo(): Number {
        val aPoints = input.mapIndexed { y, arr ->
            arr.mapIndexed { x, value ->
                if (value == 'a') x by y
                else null
            }.filterNotNull()
        }.flatten()
        return aPoints.mapNotNull { dijkstra(it, endPoint)?.size }.min()!!
    }

    data class Temp(val x: Int, val y: Int, val path: List<Point2>) : Comparable<Temp> {
        override fun compareTo(other: Temp): Int {
            return this.path.size - other.path.size
        }
    }

    fun dijkstra(from: Point2, to: Point2): List<Point2>? {
        val processed = mutableSetOf<Point2>()
        val toProcess = PriorityQueue<Temp>()
        toProcess.offer(Temp(from.x, from.y, listOf()))

        while (toProcess.isNotEmpty()) {
            val u = toProcess.poll()
            processed.add(u.x by u.y)
            if (u.x by u.y == to) {
                return u.path
            }
            (u.x by u.y).adjacent().forEach { adj ->
                if (adj.inBounds(input) && adj !in processed && input[u.x by u.y].canClimbTo(input[adj])) {
                    val path = u.path + adj
                    processed.add(adj)
                    toProcess.offer(Temp(adj.x, adj.y, path))
                }
            }
        }
        return null
    }
}
