package day10

import Day
import util.Point2
import util.by
import kotlin.math.ceil


typealias InputGrid = List<String>

class Day10 : Day {
    private fun InputGrid.get(p: Point2): Char? = this.getOrNull(p.y)?.getOrNull(p.x)

    private val input = inputFile().readLines()
    private val start = input.indexOfFirst { it.contains("S") }.let { y ->
        val x = input[y].indexOf("S")
        x by y
    }

    // Only find 1 so that the loop is directional
    private val firstInLoop = start.adjacent().first {
        val nVal = input.get(it)
        (it.y > start.y && (nVal == 'J' || nVal == 'L' || nVal == '|'))
                || (it.y < start.y && (nVal == '7' || nVal == 'F' || nVal == '|'))
                || (it.x > start.x && (nVal == '7' || nVal == 'J' || nVal == '-'))
                || (it.x < start.x && (nVal == 'L' || nVal == 'F' || nVal == '-'))
    }

    private val loop = let {
        val loop = mutableListOf(start, firstInLoop)
        var cont = true
        while (cont) {
            val p = loop.last()
            when (input[p.y][p.x]) {
                '|' -> listOf(0 by 1, 0 by -1)
                '-' -> listOf(1 by 0, -1 by 0)
                'L' -> listOf(1 by 0, 0 by -1)
                'J' -> listOf(-1 by 0, 0 by -1)
                '7' -> listOf(-1 by 0, 0 by 1)
                'F' -> listOf(1 by 0, 0 by 1)
                else -> throw Exception()
            }.map { it + p }
                .find { it !in loop.takeLast(2) && it != start }
                .let { if (it != null) loop.add(it) else cont = false }
        }
        loop.toList()
    }

    override fun problemOne(): Number {
        return ceil(loop.size.toDouble() / 2).toInt()
    }

    override fun problemTwo(): Number {
        val polygonPts = loop
        val polygonVertices = polygonPts.filterNot { input[it.y][it.x] == '|' || input[it.y][it.x] == '-' }

        return solveP2WithEvenOddRule(polygonPts, polygonVertices)
    }

    private fun solveP2WithEvenOddRule(
        polygonPts: List<Point2>,
        polygonVertices: List<Point2>
    ): Int {
        val innerPts = input.indices
            .flatMap { y -> input[y].indices.map { x -> x by y } }
            .filter { it !in polygonPts }
            .filter { isInPolygon(polygonVertices, it) }
        //        printInner(innerPts)
        return innerPts.size
    }

    // Odd-Even Rule to find if a point is in a Polygon
    // https://stackoverflow.com/questions/11716268/point-in-polygon-algorithm
    private fun isInPolygon(poly: List<Point2>, testPoint: Point2): Boolean {
        var c = false
        var j = poly.size - 1
        for (i in poly.indices) {
            val vertI = poly[i]
            val vertJ = poly[j]
            if (((vertI.y > testPoint.y) != (vertJ.y > testPoint.y))
                && (testPoint.x < (vertJ.x - vertI.x) * (testPoint.y - vertI.y) / (vertJ.y - vertI.y) + vertI.x)
            )
                c = !c
            j = i
        }
        return c
    }

    private fun printInner(inner: List<Point2>) {
        for (y in input.indices) {
            for (x in input[y].indices) {
                print(if (x by y in inner) 'I' else input[y][x])
            }
            println()
        }
    }
}