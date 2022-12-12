package day10

import Day

class Day10 : Day {
    private val input = inputFile().readLines()
    private val values = getVals(240)

    override fun problemOne(): Number {
        return listOf(20, 60, 100, 140, 180, 220).map { values[it - 2] * it }.sum()
    }

    override fun problemTwo(): String {
        val list = values.dropLast(1).toMutableList()
        list.add(0, 1)
        val temp = list.mapIndexed { cycle, regx ->
            val newLine = if ((cycle + 1) % 40 == 0) "\n" else ""
            if (((regx - 1)..(regx + 1)).contains((cycle) % 40)) "#$newLine" else ".$newLine"
        }.joinToString(separator = "") { it }
        return "\n$temp"
    }

    private fun getVals(cycles: Int): List<Int> {
        val vals = mutableListOf<Int>()
        var reg = 1
        var cycle = 1
        var i = 0
        while (cycle in 1..cycles) {
            val cmd = input[i]
            i++
            vals.add(reg)
            if (cmd == "noop") {
                cycle++
            } else {
                cycle++
                val num = cmd.split(" ")[1].toInt()
                reg += num
                cycle++
                vals.add(reg)
            }
        }
        return vals.toList()
    }
}