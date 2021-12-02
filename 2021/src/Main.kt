import day1.Day1

fun main(args: Array<String>) {
    logDay(Day1(), 1)
}

fun logDay(day: Day, i: Int) {
    println("Day $i:")
    println("\tProblem 1: ${day.problemOne()}")
    println("\tProblem 2: ${day.problemTwo()}")
}

