package day4

import Day
import util.*
import kotlin.math.pow

class Day4 : Day {
    private val input = inputFile().readLines()

    override fun problemOne(): Number {
        return input.mapIndexed { y, row ->
            row.mapIndexed() { x, c ->
                if (c == 'X') {
                    val startPoint = x by y
                    fun findWord(dir: Point2): Boolean {
                        val nextPts = listOf(startPoint + dir, startPoint + dir + dir, startPoint + dir + dir + dir)
                        if (nextPts.any { it.x < 0 || it.x >= row.length || it.y < 0 || it.y >= input.size })
                            return false
                        return nextPts.map { input[it.y][it.x] }.joinToString("").toUpperCase() == "MAS"
                    }
                    (diagPts + cardinalPts).count { findWord(it) }
                } else 0
            }
        }.flatten().sum()
    }

    private val validGrids = setOf("MSMS","SMSM","MMSS","SSMM")
    private fun isCross(y: Int, x: Int): Boolean {
        val gridInfo = listOf(input[y][x], input[y][x + 2], input[y + 2][x], input[y + 2][x + 2]).joinToString("")
        return gridInfo in validGrids
    }

    override fun problemTwo(): Number {
        return input.dropLast(2).mapIndexed { y, row ->
            row.dropLast(2).filterIndexed() { x, _ ->
                input[y + 1][x + 1] == 'A' && isCross(y,x)
            }.count()
        }.sum()
    }
}
