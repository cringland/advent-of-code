package day19

import Day

class Day19 : Day {
    //    Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 14 clay. Each geode robot costs 2 ore and 16 obsidian.
    enum class Type { ORE, CLAY, OBSIDIAN }
    data class Cost(val oreAmount: Int, val clayAmount: Int = 0, val obsidianAmount: Int = 0)
    data class State(
        val oreAmount: Int = 0,
        val clayAmount: Int = 0,
        val obsidianAmount: Int = 0,
        val geodeAmount: Int = 0,
        val oreRobotAmount: Int = 0,
        val clayRobotAmount: Int = 0,
        val obsidianRobotAmount: Int = 0,
        val geodeRobotAmount: Int = 0,
        val minutesLeft: Int
    )

    data class Blueprint(
        val oreRobotCost: Cost,
        val clayRobotCost: Cost,
        val obsidianRobotCost: Cost,
        val geodeRobotCost: Cost
    ) {
//        private fun calculate(state: State): State {
//            val minsLeft = state.minutesLeft - 1
//
//        }
    }

    private val input = inputFile().readLines().map {
        val list = it.split("costs ", "ore and ").drop(1).map { it.takeWhile { it.isDigit() }.toInt() }
        val cost1 = Cost(list[0])
        val cost2 = Cost(list[1])
        val cost3 = Cost(list[2], clayAmount = list[3])
        val cost4 = Cost(list[4], obsidianAmount = list[5])
        Blueprint(cost1, cost2, cost3, cost4)
    }

    override fun problemOne(): Number {
        return 1
    }

    override fun problemTwo(): Number {
        return 1
    }
}
