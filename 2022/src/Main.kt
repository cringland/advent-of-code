import day3.Day3

fun main(args: Array<String>) {
    val days = listOf(
            { Day3() }
    )
    days.forEachIndexed { i, it -> logDay(i + 1, it) }
}

fun logDay(i: Int, f: () -> Day) {
    println("Day $i:")
    val start = System.currentTimeMillis()
    val day = f()
    println("\tProblem 1: ${day.problemOne()}")
    println("\tProblem 2: ${day.problemTwo()}")
    println("\tSolved in ${System.currentTimeMillis() - start}ms")
}

