package day11

import Day
import util.by
import kotlin.math.abs

class Day11 : Day {
    private val input = inputFile().readLines()
    private val expandedY = input.mapIndexedNotNull { y, it -> if (it.contains("#")) null else y }
    private val expandedX = input.first().mapIndexedNotNull { x, _ ->
        if (input.none { line2 -> line2[x] == '#' }) x else null
    }
    private val ptPairs = input.mapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') x by y else null
        }
    }.flatten()
        .let { pts ->
            pts.mapIndexed { i, p1 ->
                pts.drop(i + 1).map { p2 -> p1 to p2 }
            }.flatten()
        }

    private fun solve(multiplier: Long) =
        ptPairs.map { pair ->
            val p1 = pair.first
            val p2 = pair.second
            val diff = p1 - p2
            val xRange = p1.x.coerceAtMost(p2.x)..p1.x.coerceAtLeast(p2.x)
            val yRange = p1.y.coerceAtMost(p2.y)..p1.y.coerceAtLeast(p2.y)
            val yCount = expandedY.count { it in yRange }
            val xCount = expandedX.count { it in xRange }
            abs(diff.x) + abs(diff.y) + (yCount.toLong() * multiplier) + (xCount.toLong() * multiplier)
        }.sum()

    override fun problemOne() = solve(1)
    override fun problemTwo() = solve(999999)
}