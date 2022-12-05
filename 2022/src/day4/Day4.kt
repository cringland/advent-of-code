package day4

import Day
import java.io.File

class Day4 : Day {
    private val input = File("src/day4/input").readLines()
            .map {
                it.split("-", ",").map(String::toInt)
            }

    override fun problemOne(): Int {
        return input.filter { 
            (it[0] >= it[2] && it[1] <= it[3]) || (it[2] >= it[0] && it[3] <= it[1]) 
        }.size
    }

    override fun problemTwo(): Int {
        return input.filter {
            it[0] <= it[3] && it[1] >= it[2]
        }.size
    }
}
