package day8

import Day
import util.Point2
import util.by
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Day8 : Day {

    private val input = inputFile().readLines()
    private fun inBounds(pt: Point2): Boolean = pt.y >= 0 && pt.y < input.size && pt.x < input[pt.y].length && pt.x >= 0
    private val charToPts = input.mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            c to (x by y)
        }
    }.flatten().filter { it.first != '.' }.groupBy({ it.first }, { it.second })

    fun List<Point2>.printPoints(c: Char) {
        (0..input.size-1).forEach { y ->
            (0..input[0].length-1).forEach { x ->
                if (x by y in this) print("#") else if (input[y][x] == c) print(c) else print('.')
            }
            println()
        }
    }

    fun List<List<Boolean>>.printPoints() {
        (0..input.size-1).forEach { y ->
            (0..input[0].length-1).forEach { x ->
                if (this[y][x]) print("#") else print(input[y][x])
            }
            println()
        }
    }

    private val charToLines = charToPts.map { entry ->
        entry.key to entry.value.mapIndexed { i, p1 ->
            entry.value.drop(1+i).map { p1 to it }
        }.flatten()
    }


    override fun problemOne(): Number {

        val pts = charToLines.map { pair ->
            val c = pair.first
            c to pair.second.flatMap {
                val p1 = it.first
                val p2 = it.second
                val yDiff = p2.y - p1.y
                val y1 = p1.y - yDiff
                val y2 = p2.y + yDiff
                val xDiff = max(p1.x, p2.x) - min(p1.x, p2.x)
                if (xDiff == 0) listOf(p2.x by y1, p2.x by y2)
                else if (p1.x > p2.x) listOf((p1.x + xDiff) by y1, (p2.x - xDiff) by y2)
                else listOf((p1.x - xDiff) by y1, (p2.x + xDiff) by y2)
            }
        }
        return pts.flatMap { it.second }.toSet().count { inBounds(it) }

    }

    override fun problemTwo(): Number {
        val lineEquations: List<(Int, Int) -> Boolean> = charToLines.flatMap { pair ->
            pair.second.map {
                val p1 = it.first
                val p2 = it.second
                val m = (p1.y - p2.y).toDouble() / (p1.x - p2.x).toDouble()
                val c = p1.y - (m * p1.x)
                { x, y -> (y - p1.y).toDouble() == (m * (x - p1.x).toDouble()) }
            }
        }
        val pts = input.mapIndexed { y, row ->
            row.mapIndexed {x, _ ->
                lineEquations.any { it(x,y) }
            }
        }
        pts.printPoints()
        return pts.flatten().count { it }
    }


}
