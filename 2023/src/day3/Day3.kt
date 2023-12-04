package day3

import Day
import util.Point2
import util.by


class Day3 : Day {
    data class PosRange(val x1: Int, val x2: Int, val y: Int, val value: Int) {
        fun adjacents(): List<Point2> = (x1 - 1..x2 + 1).flatMap { x ->
            (y - 1..y + 1).map {
                x by it
            }
        }
    }

    private val input = inputFile().readLines()
    private val posRanges = input.mapIndexed { y, str ->
        val ranges = mutableListOf<PosRange>()
        var x = 0
        while (x < str.length) {
            if (str[x].isDigit()) {
                val number = str.substring(x).takeWhile { it.isDigit() }
                ranges.add(PosRange(x, x + number.length - 1, y, number.toInt()))
                x += number.length
            } else x++
        }
        ranges
    }.flatten()


    override fun problemOne(): Number {
        val correctNumbers = adjacentMatches { c -> !(c.isDigit() || c == '.')}
        return correctNumbers.sumBy { it.first.value }
    }

    override fun problemTwo(): Number {
        val correctNumbers = adjacentMatches { c -> c == '*' }
        return correctNumbers
            .groupBy { it.second }
            .filter { it.value.size == 2 }
            .values.sumBy { it.first().first.value * it[1].first.value }
    }

    private fun adjacentMatches(charCheck: (Char) -> Boolean) = posRanges.mapNotNull {
        it.adjacents().firstOrNull { p ->
            if (p.y < 0 || p.x < 0 || p.y >= input.size || p.x >= input[p.y].length)
                false
            else {
                charCheck(input[p.y][p.x])
            }
        }?.let { p -> it to (p.x by p.y) }
    }
}
