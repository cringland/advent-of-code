import day1.Day1
import day2.Day2

fun main(args: Array<String>) {
    logDay(Day1(), 1)
    logDay(Day2(), 2)
}

fun logDay(day: Day, i: Int) {
    println("Day $i:")
    println("\tProblem 1: ${day.problemOne()}")
    println("\tProblem 2: ${day.problemTwo()}")
}

