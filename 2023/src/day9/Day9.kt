package day9

import Day

class Day9 : Day {
    private val input = inputFile().readLines()
        .map { it.split(" ").map { n -> n.toInt() } }
        .map { list ->
            val diffsLists = mutableListOf(list)
            while (diffsLists.last().any { it != 0 }) {
                diffsLists += diffsLists.last().zipWithNext().map { it.second - it.first }
            }
            diffsLists
        }

    override fun problemOne(): Number {
        return input.sumBy { diffLists -> diffLists.sumBy { it.last() } }
    }

    override fun problemTwo(): Number {
        return input.sumBy { diffLists -> diffLists.reversed().fold(0) { num, it -> it.first() - num }}
    }
}