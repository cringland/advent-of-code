package day2

import java.io.File
import Day

class Day2 : Day {
    private val input = File("src/day2/input").readLines().filter { it.isNotEmpty() }
    private val inputAsPairs = input.map {
        when {
            it.startsWith("forward") -> Pair(getNum(it), 0)
            it.startsWith("down") -> Pair(0, getNum(it))
            else -> Pair(0, -getNum(it))
        }
    }

    override fun problemOne(): Int {
        val pair = inputAsPairs.reduce { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }
        return pair.first * pair.second
    }

    override fun problemTwo(): Int {
        val triple = inputAsPairs.map { Triple(it.first, it.second, 0) }
                .reduce { acc, triple ->
                    val currentAim = acc.second + triple.second
                    val depth = acc.third + (currentAim * triple.first)
                    Triple(acc.first + triple.first, currentAim, depth)
                }
        return triple.first * triple.third
    }

    private fun getNum(value: String): Int {
        return "(\\d*)$".toRegex().find(value)!!.value.toInt()
    }
}
