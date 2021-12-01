package day1

import java.io.File

fun main(args: Array<String>) {
    var input = File("src/day1/input").readLines().map(String::toLong)
    var count = countIncrements(input)
    println("Problem 1 Answer: $count")
    
    var slidingWindowSums = input.dropLast(2).mapIndexed { i, value -> value + input[i+1] + input[i+2] }
    var count2 = countIncrements(slidingWindowSums)
    println("Problem 2 Answer: $count2")
}

fun countIncrements(array: List<Long>): Int {
    var count = 0;
    for (i in 0 until array.size-1) {
        if (array[i] < array[i + 1]) {
            count++
        }
    }
    return count;
}
