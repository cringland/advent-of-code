package day14

import Day
import java.io.File

class Day14 : Day {
    private val input = File("src/day14/input").readLines()
    private val startState: Map<String, Long> = input.first().zipWithNext { a, b -> "" + a + b }
            .fold(mutableMapOf<String, Long>()) { map, it ->
                map.add(it, 1)
            }.toMap()

    private val rules = input.drop(2).map {
        val (a, b) = it.split(" -> ")
        val split = a.split("")
        a to listOf(split[1] + b, b + split[2])
    }.toMap()

    override fun problemOne(): Long {
        val counts = runFor(10)
        return (counts.values.max()!! - counts.values.min()!!) / 2
    }

    override fun problemTwo(): Long {
        val counts = runFor(40)
        return (counts.values.max()!! - counts.values.min()!!) / 2
    }

    private fun runFor(i: Int): Map<Char, Long> {
        var state = startState
        repeat(i) {
            state = mutate(state)
        }
        return getCounts(state).toMap()
    }

    private fun <T> MutableMap<T, Long>.add(s: T, l: Number): MutableMap<T, Long> {
        if (this[s] != null)
            this[s] = this[s]!! + l.toLong()
        else
            this[s] = l.toLong()
        return this
    }

    private fun getCounts(state: Map<String, Long>): MutableMap<Char, Long> {
        return state.entries.fold(mutableMapOf<Char, Long>()) { map, entry ->
            entry.key.forEach { c ->
                map.add(c, entry.value)
            }
            map
        }.add(input.first().first(), 1).add(input.first().last(), 1)
    }

    private fun mutate(state: Map<String, Long>): Map<String, Long> {
        val newState = state.toMutableMap()
        state.forEach { entry ->
            val oldVal = entry.key
            val newVals = rules[oldVal]
            if (newVals != null) {
                newState[oldVal] = newState[oldVal]!! - entry.value
                newVals.forEach { newState.add(it, entry.value) }
            }
        }
        return newState.toMap()
    }
}
