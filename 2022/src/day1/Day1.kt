package day1

import Day
import java.io.File

class Day1 : Day {
    private val input = File("src/day1/input").readText()
            .split("\n\n").map {  
                it.lines()
                        .filter(String::isNotEmpty)
                        .sumBy(String::toInt)
            }
            .sortedDescending()
    

    override fun problemOne(): Int {
        return input[0]
    }

    override fun problemTwo(): Int {
        return input[0] + input[1] + input[2]
    }
}
