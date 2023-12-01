package day24

import Day

class Day24 : Day {
    private val input = inputFile().readLines()

    override fun problemOne(): Number {
        return input.map {
            val matches = "(\\d)".toRegex().findAll(it)
            val first = matches.first().groupValues[0]
            val last = matches.last().groupValues[0]
            (first + last).toInt()
        }.sum()
    }

    override fun problemTwo(): Number {
        return input.map {
            val first = "(\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex()
                .findAll(it)!!.first().groupValues.first().toDigitString()
            val last = "^.*(\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex().findAll(it)!!.last().groupValues.last().toDigitString()
            //First regex can't detect overlaps
//            println("${it} - $first - $last")
            (first + last).toInt()
        }.sum()
    }

    fun String.toDigitString(): String {
        return when(this) {
            ("one") -> "1"
            ("two") -> "2"
            ("three") -> "3"
            ("four") -> "4"
            ("five") -> "5"
            ("six") -> "6"
            ("seven") -> "7"
            ("eight") -> "8"
            ("nine") -> "9"
            else -> {this}
        }
    }
}
