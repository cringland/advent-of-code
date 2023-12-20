package day17

import Day
import util.Point2
import util.by
import java.util.*
import kotlin.concurrent.fixedRateTimer

private fun Point2.inBounds(input: List<String>) = this.y in input.indices && this.x in input[this.y].indices

class Day17 : Day {
    private val input = inputFile().readLines()

    override fun problemOne(): Number {
        return dijkstra(0 by 0, input.first().length - 1 by input.size - 1)
    }

    override fun problemTwo(): Number {
        return 1
    }

    data class Temp(val x: Int, val y: Int, val path: List<Point2>, val heatLoss: Long, val prevDir: Char, val from: Point2) :
        Comparable<Temp> {

        fun state() = (x by y) to prevDir
        override fun compareTo(other: Temp): Int {
            return (this.heatLoss - other.heatLoss).toInt()
        }
    }

    fun dijkstra(from: Point2, to: Point2): Long {
        val processed = mutableSetOf<Pair<Point2, Char>>()
        val toProcess = PriorityQueue<Temp>()
        toProcess.offer(Temp(from.x, from.y, listOf(from), 0, 'U', 0 by 0))

        while (toProcess.isNotEmpty()) {
            val u = toProcess.poll()
            processed.add(u.state())

            if (u.x by u.y == to) {
                u.path.printPath()
                println()
                return u.heatLoss
            } else {

            u.neigbours()
                .filter { it.state() !in processed }
                .forEach {
                    toProcess.offer(
                        it
                    )
                }
            }
        }
        throw Exception("No Path Found")
    }

    private fun Temp.neigbours(): List<Temp> {
        val rightRange = listOf(listOf(this.x + 1), listOf(this.x + 1, this.x + 2))
            .filter { list -> list.all { (it by y).inBounds(input) } }
            .map { xes ->
                val newPathBit = xes.map { it by y }
                val newHeatLoss = heatLoss + newPathBit.sumBy {
                    input[it.y][it.x].toString().toInt()
                }.toLong()
                Temp(xes.last(), y, this.path + newPathBit, newHeatLoss, 'R',this.x by this.y)
            }
        val leftRange = listOf(listOf(this.x - 1), listOf(this.x - 1, this.x - 2))
            .filter { list -> list.all { (it by y).inBounds(input) } }
            .map { xes ->
                val newPathBit = xes.map { it by y }
                val newHeatLoss = heatLoss + newPathBit.sumBy {
                    input[it.y][it.x].toString().toInt()
                }.toLong()
                Temp(xes.last(), y, this.path + newPathBit, newHeatLoss, 'L',this.x by this.y)
            }
        val upRange = listOf(listOf(this.y - 1), listOf(this.y - 1, this.y - 2))
            .filter { list -> list.all { (x by it).inBounds(input) } }
            .map { ys ->
                val newPathBit = ys.map { x by it }
                val newHeatLoss = heatLoss + newPathBit.sumBy {
                    input[it.y][it.x].toString().toInt()
                }.toLong()
                Temp(this.x, ys.last(), this.path + newPathBit, newHeatLoss, 'U',this.x by this.y)
            }
        val downRange = listOf(listOf(this.y + 1), listOf(this.y + 1, this.y + 2))
            .filter { list -> list.all { (x by it).inBounds(input) } }
            .map { ys ->
                val newPathBit = ys.map { x by it }
                val newHeatLoss = heatLoss + newPathBit.sumBy {
                    input[it.y][it.x].toString().toInt()
                }.toLong()
                Temp(this.x, ys.last(), this.path + newPathBit, newHeatLoss, 'D',this.x by this.y)
            }

        return when (prevDir) {
            'U' -> rightRange + downRange + leftRange
            'R' -> upRange + downRange + leftRange
            'D' -> upRange + rightRange + leftRange
            else -> upRange + rightRange + downRange
        }
    }

    fun List<Point2>.printPath() {
        input.forEachIndexed { y, s ->
            s.indices.forEach { x ->
                if (x by y in this) print("X") else print(input[y][x])
            }
            println()
        }
        println()
    }
}
