package day1

import Day
import java.io.File

class Day1 : Day {
    private val input = File("src/day1/input").readText()
            .split("\n\n").map {  
                it.lines()
                        .filter { s -> s.isNotEmpty() }
                        .map(String::toLong) 
            }
            .map { it.sum() }
            .sortedDescending()
    

    override fun problemOne(): Long {
        return input[0]
    }

    override fun problemTwo(): Long {
        return input[0] + input[1] + input[2]
    }
}
