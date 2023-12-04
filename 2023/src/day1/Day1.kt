package day1

import Day

class Day1 : Day {
    private val input = inputFile().readLines()

    override fun problemOne(): Number {
        return input.sumBy {
            val matches = "(\\d)".toRegex().findAll(it)
            val first = matches.first().groupValues[0]
            val last = matches.last().groupValues[0]
            (first + last).toInt()
        }
    }

    override fun problemTwo(): Number {
        return input.sumBy {
            //First regex can't detect overlaps - e.g. oneight produces one
            val first = "(\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex()
                .findAll(it).first().groupValues.first().toDigitString()
            val last = "^.*(\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex().findAll(it)
                .last().groupValues.last().toDigitString()
            (first + last).toInt()
        }
    }

    private fun String.toDigitString() = when (this) {
        ("one") -> "1"
        ("two") -> "2"
        ("three") -> "3"
        ("four") -> "4"
        ("five") -> "5"
        ("six") -> "6"
        ("seven") -> "7"
        ("eight") -> "8"
        ("nine") -> "9"
        else -> {
            this
        }
    }
}
