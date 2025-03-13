package day1

import Day
import kotlin.math.absoluteValue

class Day1 : Day {
    private val input = inputFile().readLines()
    private val pairs = input.map {
        val matches = "(\\d+)".toRegex().findAll(it)
        val first = matches.first().groupValues[0].toInt()
        val last = matches.last().groupValues[0].toInt()
        first to last
    }

    override fun problemOne(): Number {
        val left = pairs.map { it.first }.sorted()
        val right = pairs.map { it.second }.sorted()
        return left.mapIndexed { i, x ->
           val y = right[i]
            (y - x).absoluteValue
        }.sum()
    }

    override fun problemTwo(): Number {
        val groups = pairs.groupBy { it.second }
        return pairs.sumBy { it.first * groups.getOrDefault(it.first, emptyList()).size }
    }
}
