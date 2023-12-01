package day14

import Day
import util.Point2
import util.by
import kotlin.math.max
import kotlin.math.min


class Day14 : Day {

    private fun point(str: String) = str.split(",").let { it[0].toInt() by it[1].toInt() }

    private val input = inputFile().readLines().toSet().map { it.split(" -> ").map(::point) }

    override fun problemOne(): Number {
        val cave = input.flatMap { points ->
            points.drop(1).foldIndexed(mutableListOf(points.first())) { i, acc, next ->
                val last = points[i]
                val xs = min(last.x, next.x)..max(last.x, next.x)
                val ys = min(last.y, next.y)..max(last.y, next.y)
                acc.addAll(xs.flatMap { x -> ys.map { y -> x by y } })
                acc
            }
        }.associateWith { '#' }.toMutableMap()
        val lowestY = cave.map { it.key.y }.max()!!
        val start = 500 by 0
        var sandCount = 0
        var currentSand = start
        while (currentSand.y <= lowestY) {
            currentSand = currentSand.x by currentSand.y + 1
            if (currentSand in cave) {
                currentSand = currentSand.x - 1 by currentSand.y
                if (currentSand in cave) {
                    currentSand = currentSand.x + 2 by currentSand.y
                    if (currentSand in cave) {
                        cave[currentSand.x - 1 by currentSand.y - 1] = 'o'
                        currentSand = start
                        sandCount++
                    }
                }
            }
        }
//        cave.draw()
        return sandCount
    }

    fun MutableMap<Point2, Char>.draw() {
        val minX = this.map { it.key.x }.min()!!
        val minY = this.map { it.key.y }.min()!! - 10
        val maxX = this.map { it.key.x }.max()!! + 10
        val maxY = this.map { it.key.y }.max()!! + 10
        println()
        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                print(this.getOrDefault(x by y, '.'))
            }
            println()
        }
        println()
    }

    override fun problemTwo(): Number {
        val cave = input.flatMap { points ->
            points.drop(1).foldIndexed(mutableListOf(points.first())) { i, acc, next ->
                val last = points[i]
                val xs = min(last.x, next.x)..max(last.x, next.x)
                val ys = min(last.y, next.y)..max(last.y, next.y)
                acc.addAll(xs.flatMap { x -> ys.map { y -> x by y } })
                acc
            }
        }.associateWith { '#' }.toMutableMap()
        val minX = cave.map { it.key.x }.min()!! - 1000
        val maxX = cave.map { it.key.x }.max()!! + 1000
        val lowestY = cave.map { it.key.y }.max()!!
        val floor = lowestY + 2
        (minX..maxX).forEach{ cave[it by floor] = '#' }
        val start = 500 by 0
        var sandCount = 0
        var currentSand = start
        while (true) {
            currentSand = currentSand.x by currentSand.y + 1
            if (currentSand in cave) {
                currentSand = currentSand.x - 1 by currentSand.y
                if (currentSand in cave) {
                    currentSand = currentSand.x + 2 by currentSand.y
                    if (currentSand in cave) {
                        cave[currentSand.x - 1 by currentSand.y - 1] = 'o'
                        sandCount++
                        if (currentSand == 501 by 1)
                            break
                        currentSand = start
                    }
                }
            }
        }
        return sandCount
    }
}
