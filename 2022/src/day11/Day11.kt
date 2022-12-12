package day11

import Day

class Day11 : Day {

    data class Monkey(
        var nums: MutableList<Long>,
        val func: (a: Long) -> Long,
        val divBy: Long,
        val ifTrue: Int,
        val ifFalse: Int,
        var total: Long = 0
    )

    override fun problemOne(): Number = monkeyBusiness(20, monkeys()) { it / 3 }

    override fun problemTwo(): Number {
        val monkeys = monkeys()
        val product = monkeys.map { it.divBy }.fold(1L) { acc, i -> acc * i }
        return monkeyBusiness(10000, monkeys) { it % product }
    }

    private fun monkeys() = inputFile().readText().split("\n\n").map {
        val lines = it.lines()
        val nums = lines[1].split(": ")[1].split(", ").map(String::toLong).toMutableList()
        val func = lines[2].split("= ")[1].split(" ").let { sum ->
            val operator: (A: Long, B: Long) -> Long =
                if (sum[1] == "+") { a: Long, b: Long -> a + b } else { a: Long, b: Long -> a * b }
            if (sum[2] == "old") { a: Long -> operator(a, a) } else {
                { a: Long -> operator(a, sum[2].toLong()) }
            }
        }
        val divBy = lines[3].split(" ").last().toLong()
        val ifTrue = lines[4].split(" ").last().toInt()
        val ifFalse = lines[5].split(" ").last().toInt()
        Monkey(nums, func, divBy, ifTrue, ifFalse)
    }

    private fun monkeyBusiness(cycles: Int, monkeys: List<Monkey>, reducer: (Long) -> Long): Number {
        for (i in 0 until cycles) {
            for (mIndex in monkeys.indices) {
                val monkey = monkeys[mIndex]
                for (item in monkey.nums.toList()) {
                    monkey.total++
                    monkey.nums.removeAt(0)
                    val value = reducer(monkey.func(item))
                    if ((value % monkey.divBy) == 0L) {
                        monkeys[monkey.ifTrue].nums.add(value)
                    } else {
                        monkeys[monkey.ifFalse].nums.add(value)
                    }
                }
            }
        }
        return monkeys.map { it.total }.sortedDescending().let { it[0] * it[1] }
    }
}