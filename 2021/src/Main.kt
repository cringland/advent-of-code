import day1.Day1
import day2.Day2
import day3.Day3
import day4.Day4
import day5.Day5
import day6.Day6
import day7.Day7
import day8.Day8

fun main(args: Array<String>) {
//    logDay(Day1(), 1)
//    logDay(Day2(), 2)
//    logDay(Day3(), 3)
//    logDay(Day4(), 4)
//    logDay(Day5(), 5)
//    logDay(Day6(), 6)
//    logDay(Day7(), 7)
    logDay(Day8(), 8)
}

fun logDay(day: Day, i: Int) {
    println("Day $i:")
    println("\tProblem 1: ${day.problemOne()}")
    println("\tProblem 2: ${day.problemTwo()}")
}

