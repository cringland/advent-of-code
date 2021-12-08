package day8

import Day
import java.io.File

class Day8 : Day {
    private val input = File("src/day8/input").readLines().map { line ->
        val split = line.split("|").map { it.trim().split(" ").map(String::toCharArray).map(CharArray::toList) }
        split[0] to split[1]
    }

    override fun problemOne(): Int {
        return input.map { pair -> pair.second.count { it.size in listOf(2, 4, 3, 7) } }.sum()
    }

    override fun problemTwo(): Int {
        return input.map { pair ->
            val left = pair.first

            val predicateList = mutableMapOf<Int, (List<Char>) -> Boolean>()

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

            predicateList[0] = segSize(6).and { !it.contains(d) }
            predicateList[1] = segSize(2)
            predicateList[2] = segSize(5).and { it.contains(c) && !it.contains(f) }
            predicateList[3] = segSize(5).and { it.containsAll(one) }
            predicateList[4] = segSize(4)
            predicateList[5] = segSize(5).and { it.contains(f) && !it.contains(c) }
            predicateList[6] = segSize(6).and { it.contains(f) && !it.containsAll(one) }
            predicateList[7] = segSize(3)
            predicateList[8] = segSize(7)
            predicateList[9] = segSize(6).and { it.contains(d) && it.contains(c) }

            pair.second.map { value ->
                predicateList.filterValues { pred -> pred.invoke(value) }.keys.first()
            }.joinToString("") { it.toString() }.toInt()
        }.sum()
    }
}
