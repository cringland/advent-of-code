package day15

import Day
import java.io.File
import java.util.*

class Day15 : Day {

    data class Point(val x: Int, val y: Int)

    infix fun Int.comma(that: Int): Point = Point(this, that)

    data class Temp(val x: Int, val y: Int, val distance: Int) : Comparable<Temp> {
        override fun compareTo(other: Temp): Int {
            return this.distance - other.distance
        }
    }
    private val dirs = listOf<(Int, Int) -> Point>(
            { x, y -> Point(x - 1, y) },
            { x, y -> Point(x + 1, y) },
            { x, y -> Point(x, y - 1) },
            { x, y -> Point(x, y + 1) })


    private val input = File("src/day15/input").readLines().filter(String::isNotEmpty).map { column ->
        column.toCharArray().map(Character::getNumericValue)
    }

    private val pointToAdjs: Map<Point, List<Pair<Point, Int>>> = pointToAdjsMap(input)

    override fun problemOne(): Int {
        val last = input.size - 1
        val distances = dijkstra(pointToAdjs, 0 comma 0, last comma last)
        return distances[last comma last]!!
    }

    override fun problemTwo(): Int {
        val grid = replicatedInput()
        val pointToAdjs2: Map<Point, List<Pair<Point, Int>>> = pointToAdjsMap(grid)

        val last = grid.size - 1
        val distances = dijkstra(pointToAdjs2, 0 comma 0, last comma last)
        return distances[last comma last]!!
    }

    fun dijkstra(graph: Map<Point, List<Pair<Point, Int>>>, from: Point, to: Point): Map<Point, Int> {
        val distances = graph.map { it.key to Int.MAX_VALUE }.toMap().toMutableMap()
        val processed = mutableSetOf<Point>()
        val toProcess = PriorityQueue<Temp>()
        toProcess.offer(Temp(0,0,0))
        
        distances[from] = 0

        while(toProcess.isNotEmpty()) {
//            val u = distances.filterNot { it.key in processed }.minBy { it.value }!!
            val u = toProcess.poll()
            processed.add(u.x comma u.y)
            graph[u.x comma u.y]!!.forEach { adj ->
                val currentDist = distances[adj.first]
                if (currentDist == null || u.distance + adj.second < currentDist) {
                    val dist = u.distance + adj.second
                    distances[adj.first] = dist
                    toProcess.offer(Temp(adj.first.x, adj.first.y, dist))
                }
                if(adj.first == to) {
                    return distances
                }
            }
        }
        throw RuntimeException("???")
    }

    private fun pointToAdjsMap(grid: List<List<Int>>): Map<Point, List<Pair<Point, Int>>> {
        val pointToAdjs2: Map<Point, List<Pair<Point, Int>>> = grid.mapIndexed { x, column ->
            column.mapIndexed { y, _ ->
                (x comma y) to grid.getAdjacentValues(x, y)
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

    private fun List<List<Int>>.getAdjacentValues(x: Int, y: Int): List<Pair<Point, Int>> {
        return dirs.map { it(x, y) }
                .mapNotNull {
                    val value = this.getOrNull(it.x, it.y)
                    if (value == null) null else it to value
                }
    }

    private fun List<List<Int>>.getOrNull(x: Int, y: Int) =
            this.getOrNull(x)?.getOrNull(y)
}
