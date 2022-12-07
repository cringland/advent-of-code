package day8

import Day

class Day8 : Day {
    private val input = inputFile().readLines().map { line ->
        val split = line.split("|").map { it.trim().split(" ").map(String::toCharArray).map(CharArray::toList) }
        split[0] to split[1]
    }

    override fun problemOne(): Int {
        return input.map { pair -> pair.second.count { it.size in listOf(2, 4, 3, 7) } }.sum()
    }

    override fun problemTwo(): Int {
        return input.map { pair ->
            val left = pair.first

            val one = left.find { it.size == 2 }!!
            val three = left.find { it.size == 5 && it.containsAll(one) }!!
            val four = left.find { it.size == 4 }!!
            val six = left.find { it.size == 6 && !it.containsAll(one) }!!
            val f = six.find { one.contains(it) }!!
            val c = one.find { it != f }!!
            val d = three.find { four.contains(it) && !one.contains(it) }!!

            fun segSize(size: Int) = { list: List<Char> -> list.size == size }
            fun ((List<Char>) -> Boolean).and(func: ((List<Char>) -> Boolean)) = { list: List<Char> ->
                this.invoke(list) && func.invoke(list)
            }

            val predicateList = mapOf(
                    0 to segSize(6).and { !it.contains(d) },
                    1 to segSize(2),
                    2 to segSize(5).and { it.contains(c) && !it.contains(f) },
                    3 to segSize(5).and { it.containsAll(one) },
                    4 to segSize(4),
                    5 to segSize(5).and { it.contains(f) && !it.contains(c) },
                    6 to segSize(6).and { it.contains(f) && !it.containsAll(one) },
                    7 to segSize(3),
                    8 to segSize(7),
                    9 to segSize(6).and { it.contains(d) && it.contains(c) }
            )

            pair.second.map { value ->
                predicateList.filterValues { pred -> pred.invoke(value) }.keys.first()
            }.joinToString("") { it.toString() }.toInt()
        }.sum()
    }
}
