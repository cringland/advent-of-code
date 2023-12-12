package day12

import Day

class Day12 : Day {
    private val input = inputFile().readLines().map { line ->
        val split = line.split(" ")
        val nums = split[1].split(",").map { it.toInt() }
        split[0] to nums
    }

    override fun problemOne(): Number {
        return input.map { pair ->
            val str = pair.first
            val nums = pair.second
            possibilities(str, nums, false)
        }.sum()
    }

    override fun problemTwo(): Number {
        return input.mapIndexed { i, pair ->
//            val start = System.currentTimeMillis()
//            println("Start $i")
            val str = (0..4).joinToString("?") { pair.first }
            val nums = (0..4).flatMap { pair.second }
            val possibilities = possibilities(str, nums, false)
//            println("\tDone in ${System.currentTimeMillis() - start}ms")
            possibilities
        }.sum()
    }

    private val mem = mutableMapOf<Triple<String, List<Int>, Boolean>, Long>()
    private fun possibilities(str: String, groupSizes: List<Int>, currentlyCounting: Boolean): Long {
        mem[Triple(str, groupSizes, currentlyCounting)]?.let { return it }

        fun solveAndStore(s: String, g: List<Int>, c: Boolean): Long {
            val p = possibilities(s, g, c)
            mem[Triple(s, g, c)] = p
            return p
        }

        if (str.isEmpty()) {
            return if (groupSizes.isEmpty() || (groupSizes.size == 1 && groupSizes.first() == 0))
                1L else 0L
        }
        if (groupSizes.sum() > str.length) return 0L
        val newStr = str.drop(1)
        when (str.first()) {
            '.' -> {
                return if (currentlyCounting && groupSizes.first() != 0) {
                    0L
                } else if (currentlyCounting && groupSizes.first() == 0) {
                    solveAndStore(newStr, groupSizes.drop(1), false)
                } else {
                    solveAndStore(newStr, groupSizes, false)
                }
            }
            '#' -> {
                if (groupSizes.isEmpty()) return 0L
                val nextGroupSizes = listOf(groupSizes.first() - 1) + groupSizes.drop(1)
                return solveAndStore(newStr, nextGroupSizes, true)
            }

            else -> { //?
                return solveAndStore(".$newStr", groupSizes, currentlyCounting) +
                        solveAndStore("#$newStr", groupSizes, currentlyCounting)
            }
        }
    }
}
