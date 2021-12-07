package day7

import java.io.File
import Day
import java.util.*

class Day7 : Day {
    private val input = File("src/day7/input").readText().split(",").map { it.trim().toInt() }
    private val max = input.max()!!
    private val min = input.min()!!

    override fun problemOne(): Int {
        return findCheapestFuel { d1, d2 -> Math.abs(d1 - d2) }
    }

    override fun problemTwo(): Int {
        return findCheapestFuel { d1, d2 ->
            val distance = Math.abs(d1 - d2)
            (0..distance).sum()
        }
    }

    fun findCheapestFuel(fuelCalculator: (Int, Int) -> Int): Int {
        return (min..max).map { i -> input.map { fuelCalculator.invoke(it, i) }.sum() }.min()!!
    }
}
