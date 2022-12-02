package day2

import Day
import java.io.File

class Day2 : Day {
    private val input = File("src/day2/input").readLines().map { it.split(" ") }
    override fun problemOne(): Int {
        return input.fold(0, { acc, rule ->
            val one = rule[0]
            val two = rule[2]
            val result = when {
                (one == "A" && two == "X") || (one == "B" && two == "Y") || (one == "C" && two == "Z") -> 3
                (one == "A" && two == "Y") || (one == "B" && two == "Z") || (one == "C" && two == "X") -> 6
                else -> 0
            }
            val otherScore = when (two) {
                "X" -> 1
                "Y" -> 2
                else -> 3
            }
            acc + result + otherScore
        })
    }

    override fun problemTwo(): Int {
        return input.fold(0, { acc, rule ->
            val one = rule[0]
            val two = rule[2]
            val otherScore = when {
                (one == "A" && two == "Y") || (one == "B" && two == "X") || (one == "C" && two == "Z") -> 1
                (one == "A" && two == "Z") || (one == "B" && two == "Y") || (one == "C" && two == "X") -> 2 
                else -> 3
            }
            val result = when (two) {
                "X" -> 0
                "Y" -> 3
                else -> 6
            }
            acc + result + otherScore
        })
    }
}
