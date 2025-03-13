package day2

import Day
import java.lang.Integer.max

class Day2 : Day {
    private val input = inputFile().readLines().map {
        val matches = "(\\d+)".toRegex().findAll(it)
        matches.toList().map { it.groupValues[0].toInt() }
    }


    override fun problemOne(): Number {
        return input.count { isSafe(it) }
    }

    override fun problemTwo(): Number {
        return input
            .map { list ->
                List(list.size) { i ->
                    val temp = list.toMutableList()
                    temp.removeAt(i)
                    temp.toList()
                }
            }.count { it.any { list ->
                isSafe(list)
            } }
    }

    private fun isSafe(list: List<Int>): Boolean {
        val isSorted = list == list.sorted() || list == list.sortedDescending()
        return if (!isSorted) false
        else {
            var prevVal = list.first()
            list.drop(1).all {
                val works = (it >= prevVal-3 && it < prevVal) || (it > prevVal && it <= prevVal + 3)
                prevVal = it
                works
            }
        }
    }
}
