import day1.Day1
import day2.Day2
import day3.Day3
import day4.Day4
import day5.Day5
import day6.Day6

fun main(args: Array<String>) {
    val currentDay = 6
    val days = listOf(
            { Day1() },
            { Day2() },
            { Day3() },
            { Day4() },
            { Day5() },
            { Day6() }
    )
//    days.forEachIndexed { i, it -> logDay(i + 1, it) }
    logDay(currentDay - 1, days[currentDay - 1])
}

fun logDay(i: Int, f: () -> Day) {
    println("Day $i:")
    val start = System.currentTimeMillis()
    val day = f()
    println("\tProblem 1: ${day.problemOne()}")
    println("\tProblem 2: ${day.problemTwo()}")
    println("\tSolved in ${System.currentTimeMillis() - start}ms")
}

