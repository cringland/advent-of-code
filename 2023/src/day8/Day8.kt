package day8

import Day
import util.lcm
import java.util.concurrent.locks.Condition
import kotlin.math.max

class Day8 : Day {
    private val input = inputFile().readLines()
    private val instructions = input[0]
    private val map = input.drop(2).map {
        it.replace("[\\(\\) ]".toRegex(), "")
    }.associate {
        val key = it.substringBefore("=")
        val values = it.substringAfter("=").split(",").let { vals -> vals[0] to vals[1] }
        key to values
    }

    override fun problemOne(): Number {
        return solve("AAA") { it == "ZZZ" }
    }

    override fun problemTwo(): Number {
        val values = map.keys.filter { it.endsWith('A') }
            .map { startKey -> solve(startKey) { it.endsWith('Z') } }
        return values.lcm()
    }

    private fun solve(startKey: String, endCondition: (String) -> Boolean): Long {
        var currentKey = startKey
        var index = 0
        var count = 0L
        while (!endCondition(currentKey)) {
            if (index == instructions.length) index = 0
            currentKey = if (instructions[index] == 'R') map[currentKey]!!.second
            else map[currentKey]!!.first
            count++
            index++
        }
        return count
    }
}
