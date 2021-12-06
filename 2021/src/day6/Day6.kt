package day6

import java.io.File
import Day
import java.util.*

class Day6 : Day {
    private val initialState = File("src/day6/input").readText().split(",").map { it.trim().toInt() }

    override fun problemOne(): Int {
        var state = initialState
        repeat(80) {
            state = state.flatMap { if (it == 0) listOf(6, 8) else listOf(it - 1) }
        }
        return state.size
    }

    override fun problemTwo(): Long {
        val state = LongArray(9).apply {
            for (fish in initialState) {
                this[fish]++
            }
        }.toMutableList()

        repeat(256) {
            val newBirths = state[0]
            Collections.rotate(state, -1)
            state[6] = state[6] + newBirths
        }
        return state.sum()
    }
}
