package day6

import Day

class Day6 : Day {
    private val input = inputFile().readLines().map { str ->
        str.extractNums()
    }

    override fun problemOne(): Number {
        return input.let {
            it[0].mapIndexed { i, num -> num.toLong() to it[1][i].toLong() }
        }.map { race ->
            val time = race.first
            val distToBeat = race.second
            (1 until time).filter { speed ->
                dist(time, speed) > distToBeat
            }.size
        }.fold(1) { total, it -> total * it }
    }

    override fun problemTwo(): Number {
        val pair = input.map {
            it.joinToString("").toLong()
        }.zipWithNext().single()
        return (1 until pair.first).filter { speed ->
            dist(pair.first, speed) > pair.second
        }.size
    }

    private fun dist(totalTime: Long, speed: Long): Long {
        val timeLeft = totalTime - speed
        return timeLeft * speed
    }

    private fun String.extractNums() = "(\\d+)".toRegex().findAll(this).map { it.value }.toList()
}
