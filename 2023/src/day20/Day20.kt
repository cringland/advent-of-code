package day20

import Day
import util.lcm

var low = 0L
var high = 0L

enum class PulseType { HIGH, LOW }
data class Pulse(val type: PulseType, val from: String)

fun high(from: String) = Pulse(PulseType.HIGH, from).also { high++ }
fun low(from: String) = Pulse(PulseType.LOW, from).also { low++ }
class Day20 : Day {
    interface Module {
        fun dests(): List<String>
        fun name(): String
        fun handle(pulse: Pulse): List<Pair<String, Pulse>>
    }

    class FlipFlopModule(val name: String, val dest: List<String>) : Module {
        var on = false
        override fun dests() = dest
        override fun name() = name
        override fun handle(pulse: Pulse): List<Pair<String, Pulse>> {
            if (pulse.type == PulseType.HIGH) return emptyList()
            on = !on
            return dest.map { it to if (on) high(name) else low(name) }
        }
    }

    class ConjunctionModule(val name: String, val dest: List<String>, val inputs: List<String>) : Module {
        private val inputsMap = inputs.associateWith { PulseType.LOW }.toMutableMap()
        override fun dests() = dest
        override fun name() = name
        override fun handle(pulse: Pulse): List<Pair<String, Pulse>> {
            inputsMap[pulse.from] = pulse.type
            return dest.map { it to if (inputsMap.values.all(PulseType.HIGH::equals)) low(name) else high(name) }
        }
    }

    private val inputLines = inputFile().readLines().map { it.replace(" ", "") }
    private val broadcaster = inputLines.first { it.startsWith("b") }
        .substringAfter(">")
        .split(",")

    private fun generateModules() = inputLines.filterNot { it.startsWith("b") }
        .associate {
            val name = it.substringBefore("-").drop(1)
            val dest = it.substringAfter(">").split(",")
            val module = if (it.startsWith('%')) FlipFlopModule(name, dest) else {
                val inputs = inputLines.map { line ->
                    line.substringBefore("-").drop(1) to line.substringAfter(">").split(",")
                }
                    .filter { pair -> pair.second.contains(name) }
                    .map { pair -> pair.first }
                ConjunctionModule(name, dest, inputs)
            }
            name to module
        }

    override fun problemOne(): Number {
        val modules = generateModules()
        repeat(1000) { i ->
            var signals = broadcaster.map { it to low("broadcaster") }
            while (signals.isNotEmpty()) {
                signals = signals.mapNotNull {
                    modules[it.first]?.handle(it.second)
                }.flatten()
            }
        }
        return (1000 + low) * high
    }

    override fun problemTwo(): Number {
        low = 0L
        high = 0L
        val modules = generateModules()
        val lastConj = modules.values.first { it.dests().contains("rx") } as ConjunctionModule
        val cycles = lastConj.inputs.associateWith { 0L }.toMutableMap()
        var presses = 0L
        while (cycles.any { it.value == 0L }) {
            presses++
            var signals = broadcaster.map { it to low("broadcaster") }
            while (signals.isNotEmpty()) {
                signals = signals.mapNotNull {
                    if (it.first in cycles && it.second.type == PulseType.LOW) cycles[it.first] = presses
                    modules[it.first]?.handle(it.second)
                }.flatten()
            }
        }
        return cycles.values.toList().lcm()
    }
}
