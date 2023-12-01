package day15

import Day
import util.Point2
import util.by
import java.io.File
import java.util.*

class Day15 : Day {

    data class Temp(val x: Int, val y: Int, val distance: Int) : Comparable<Temp> {
        override fun compareTo(other: Temp): Int {
            return this.distance - other.distance
        }
    }

    private val input = inputFile().readLines().filter(String::isNotEmpty).map { column ->
        column.toCharArray().map(Character::getNumericValue)
    }

    private val pointToAdjs: Map<Point2, List<Pair<Point2, Int>>> = pointToAdjsMap(input)

    override fun problemOne(): Int {
        val last = input.size - 1
        return dijkstraDistance(pointToAdjs, 0 by 0, last by last)
    }

    override fun problemTwo(): Int {
        val grid = replicatedInput()
        val pointToAdjs2: Map<Point2, List<Pair<Point2, Int>>> = pointToAdjsMap(grid)

        val last = grid.size - 1
        return dijkstraDistance(pointToAdjs2, 0 by 0, last by last)
    }

    fun dijkstraDistance(graph: Map<Point2, List<Pair<Point2, Int>>>, from: Point2, to: Point2): Int {
        val processed = mutableSetOf<Point2>()
        val toProcess = PriorityQueue<Temp>()
        toProcess.offer(Temp(from.x, from.y, 0))

        while (toProcess.isNotEmpty()) {
            val u = toProcess.poll()
            processed.add(u.x by u.y)
            if (u.x by u.y == to) {
                return u.distance
            }
            graph[u.x by u.y]!!.forEach { adj ->
                if(adj.first !in processed) {
                    processed.add(adj.first)
                    val dist = u.distance + adj.second
                    toProcess.offer(Temp(adj.first.x, adj.first.y, dist))
                }
            }
        }
        throw RuntimeException("???")
    }

    private fun pointToAdjsMap(grid: List<List<Int>>): Map<Point2, List<Pair<Point2, Int>>> {
        val pointToAdjs2: Map<Point2, List<Pair<Point2, Int>>> = grid.mapIndexed { x, column ->
            column.mapIndexed { y, _ ->
                val p = x by y
                p to p.adjacent().mapNotNull { adjP ->
                    val value = grid.getOrNull(adjP)
                    if (value == null) null else adjP to value
                }
            }
        }.flatten().toMap()
        return pointToAdjs2
    }

    private fun replicatedInput(): MutableList<MutableList<Int>> {
        val grid = MutableList(input.size * 5) { MutableList(input.size * 5) { 0 } }
        repeat(5) { k ->
            for (y in input.indices) {
                for (x in input[y].indices) {
                    val newVal = input[y][x] + k
                    grid[y + (k * input.size)][x] = if ((newVal) > 9) newVal - 9 else newVal
                }
            }
        }

        repeat(5) { k ->
            for (y in grid.indices) {
                for (x in input.indices) {
                    val newVal = grid[y][x] + k
                    grid[y][x + (k * input.size)] = if ((newVal) > 9) newVal - 9 else newVal
                }
            }
        }
        return grid
    }

    private fun List<List<Int>>.getOrNull(p: Point2): Int? = this.getOrNull(p.x, p.y)
    private fun List<List<Int>>.getOrNull(x: Int, y: Int): Int? =
            this.getOrNull(x)?.getOrNull(y)
}
