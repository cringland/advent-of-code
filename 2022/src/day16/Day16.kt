package day16

import Day


class Day16 : Day {
    data class Valve(val id: String, val rate: Int, val leadsTo: List<String>)

    private val valves = inputFile().readLines().associate {
        val list =
            it.split("Valve ", " has flow rate=", "; tunnels lead to valves ", "; tunnel leads to valve ", ", ").drop(1)
        val key = list[0]
        val rate = list[1].toInt()
        val leadsTo = list.drop(2)
        key to Valve(key, rate, leadsTo)
    }

    private val totalNonZero = valves.filter { it.value.rate > 0 }.keys.size

    private val idToDistances = valves.keys.map { valve ->
        val distances = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
        val toVisit = mutableListOf(valve)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeAt(0)
            valves[current]!!.leadsTo.forEach { neighbour ->
                val newDistance = distances[current]!! + 1
                if (newDistance < distances.getValue(neighbour)) {
                    distances[neighbour] = newDistance
                    toVisit.add(neighbour)
                }
            }
        }
        distances
    }.associateBy { it.keys.first() }



    fun find(
        currentValve: Valve,
        timeLeft: Int,
        total: Long,
        turnedOnIds: Set<String>
    ): Pair<Long, Set<String>> {
        if (timeLeft == 0 || turnedOnIds.size == totalNonZero) return total to turnedOnIds

        if (currentValve.rate > 0 && currentValve.id !in turnedOnIds) {
            return find(
                currentValve,
                timeLeft - 1,
                total + (currentValve.rate * (timeLeft - 1)),
                turnedOnIds + currentValve.id
            )
        }

        return currentValve.leadsTo.fold(0L to emptySet()) { acc, it ->
            val valve = valves[it]!!
            val found = find(valve, timeLeft - 1, total, turnedOnIds)
            if (acc.first > found.first) acc else found
        }
    }

    override fun problemOne(): Number {
        val p = find(valves["AA"]!!, 30, 0, setOf())
        return p.first
    }

    override fun problemTwo(): Number {
        return 1
    }
}
