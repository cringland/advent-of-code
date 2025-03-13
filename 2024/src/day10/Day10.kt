package day10

import Day
import util.Point2
import util.by

class Day10 : Day {

    private val input = inputFile().readLines()
    private fun inBounds(pt: Point2): Boolean = pt.y >= 0 && pt.y < input.size && pt.x < input[pt.y].length && pt.x >= 0
    private val zeroPoints = input.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == '0') x by y else null
            }.filterNotNull()
    }.flatten()

    override fun problemOne(): Number {
        fun go(pt: Point2): Set<Point2> {
            val currentNum = input[pt.y][pt.x].toString().toInt()
            if (currentNum == 9) return setOf(pt)
            return pt.adjacent().filter { inBounds(it) && (input[it.y][it.x].toString().toInt() == currentNum + 1)  }.flatMap { go(it) }.toSet()
        }
        return zeroPoints.flatMap {start ->
            go(start)
        }.count()
    }

    override fun problemTwo(): Number {
        fun go(pt: Point2): Long {
            val currentNum = input[pt.y][pt.x].toString().toInt()
            if (currentNum == 9) return 1
            return pt.adjacent().filter { inBounds(it) && (input[it.y][it.x].toString().toInt() == currentNum + 1)  }.map {
                go(it)
            }.sum()
        }
        return zeroPoints.map {start ->
            go(start)
        }.sum()
    }
}
