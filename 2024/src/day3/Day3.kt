package day3

import Day
import util.Point2
import util.by
import kotlin.math.max


class Day3 : Day {
    private val input = inputFile().readText()

    override fun problemOne(): Number {
        return doMuls(input)
    }

    override fun problemTwo(): Number {
        var total = 0L
        var current = input
        while(true) {
            val dontIndex = current.indexOf("don't()") + 1
            if (dontIndex == 0) {
                total += doMuls(current.substring(0))
                break
            }
            total += doMuls(current.substring(0, dontIndex))
            val doIndex = current.substring(dontIndex).indexOf("do()") + 1
            if (doIndex == 0) {
                break
            }
            current = current.substring(dontIndex).substring(doIndex)
        }
        return total
    }

    private fun doMuls(str: String): Long {
        val matches = "(mul\\(\\d+,\\d+\\))".toRegex().findAll(str)
        return matches.toList().sumBy {
            val nums = "(\\d+)".toRegex().findAll(it.groupValues[0])
            val numOne = nums.first().groupValues[0].toInt()
            val numTwo = nums.last().groupValues[0].toInt()
            numOne * numTwo
        }.toLong()
    }
}
