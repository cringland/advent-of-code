package day3

import java.io.File
import Day

class Day3 : Day {
    private val input = File("src/day3/input").readLines().filter { it.isNotEmpty() }
            .map { it.toList().map(Character::getNumericValue) }

    override fun problemOne(): Int {
        val maxBinary = input.columnCounts()
                .map { map -> if (map[0].orEmpty().size > map[1].orEmpty().size) 0 else 1 }
        return maxBinary.binaryToDecimal() * maxBinary.invertBinary().binaryToDecimal()
    }

    override fun problemTwo(): Int {
        val oxygen = getMostLike { ones, zeros -> ones >= zeros}
        val co2 = getMostLike { ones, zeros -> ones < zeros}
        return oxygen.binaryToDecimal() * co2.binaryToDecimal()
    }
    
    fun getMostLike(onesToZerosCompare: (Int, Int) -> Boolean): List<Int> {
        var list = input
        var i = 0;
        while (list.size > 1) {
            val counts = list.columnCounts()
            val num = if (onesToZerosCompare.invoke(counts[i][1].orEmpty().size, counts[i][0].orEmpty().size)) 1 else 0
            list = list.filter { num == it[i] }
            i++
        }
        return list[0]
    }

    fun List<List<Int>>.columnCounts(): List<Map<Int, List<Int>>> {
        return this.first().mapIndexed { i, _ ->
            this.map({ it[i] }).groupBy { it }
        }
    }

    fun List<Int>.binaryToDecimal(): Int {
        return this.reversed().reduceIndexed { i, sum, value ->
            sum + (value * Math.pow(2.0, i.toDouble())).toInt()
        }
    }

    fun List<Int>.invertBinary(): List<Int> {
        return this.map { if (it == 1) 0 else 1 }
    }
}
