package day17

import Day
import util.Point2
import util.by
import java.util.*

private fun Point2.inBounds(input: List<List<Any>>) = this.y in input.indices && this.x in input[this.y].indices
enum class Dir { L, R, D, U }

class Day17 : Day {
    private val input = inputFile().readLines().map { line -> line.map { it.toString().toLong() } }
    private val firstPoint = 0 by 0
    private val lastPoint = input.first().size - 1 by input.size - 1
    private val p1List = (1..3).map { (1..it).toList() }
    private val p2List = (4..10).map { (1..it).toList() }

    override fun problemOne(): Number {
        return dijkstra(firstPoint, lastPoint)
    }

    override fun problemTwo(): Number {
        return dijkstra(firstPoint, lastPoint, true)
    }

    data class Temp(
        val x: Int,
        val y: Int,
//        val path: List<Point2>, // Not needed but useful for debugging
        val heatLoss: Long,
        val prevDir: Dir
    ) : Comparable<Temp> {

        fun state() = Triple(x, y, prevDir)
        override fun compareTo(other: Temp): Int {
            return (this.heatLoss - other.heatLoss).toInt()
        }
    }

    fun dijkstra(from: Point2, to: Point2, p2: Boolean = false): Long {
        val processed = mutableSetOf<Triple<Int, Int, Dir>>()
        val toProcess = PriorityQueue<Temp>()
//        toProcess.offer(Temp(from.x, from.y, listOf(from), 0, Dir.U))
//        toProcess.offer(Temp(from.x, from.y, listOf(from), 0, Dir.R))
        toProcess.offer(Temp(from.x, from.y, 0, Dir.U))
        toProcess.offer(Temp(from.x, from.y, 0, Dir.R))

        while (toProcess.isNotEmpty()) {
            val u = toProcess.poll()
            if (u.state() in processed) continue
            processed.add(u.state())
            if (u.x by u.y == to) {
                return u.heatLoss
            } else {
                u.neigbours(p2)
                    .forEach {
                        toProcess.offer(it)
                    }
            }
        }
        throw Exception("No Path Found")
    }

    private fun Temp.neigbours(p2: Boolean): List<Temp> {
        fun paths(dir: Dir, func: (Int) -> Point2) = (if (p2) p2List else p1List)
            .map { path -> path.map { func(it) } }
            .filter { list -> list.last().inBounds(input) }
            .map { points ->
                val newHeatLoss = heatLoss + points.map { input[it.y][it.x] }.sum()
//                Temp(points.last().x, points.last().y, this.path + points, newHeatLoss, dir)
                Temp(points.last().x, points.last().y,  newHeatLoss, dir)
            }

        val right by lazy { paths(Dir.R) { (this.x + it) by this.y } }
        val left by lazy { paths(Dir.L) { (this.x - it) by this.y } }
        val up by lazy { paths(Dir.U) { this.x by (this.y - it) } }
        val down by lazy { paths(Dir.D) { this.x by (this.y + it) } }
        return when (prevDir) {
            Dir.U, Dir.D -> right + left
            else -> up + down
        }
    }

    private fun List<Point2>.printPath() {
        input.forEachIndexed { y, s ->
            s.indices.forEach { x ->
                if (x by y in this) print("X") else print(input[y][x])
            }
            println()
        }
        println()
    }
}
