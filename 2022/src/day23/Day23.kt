package day23

import Day
import util.Point2
import util.by

class Day23 : Day {
    private val N = 0 by -1
    private val S = 0 by 1
    private val E = 1 by 0
    private val W = -1 by 0
    private val NW = N + W
    private val NE = N + E
    private val SW = S + W
    private val SE = S + E

    private val startRules = listOf(
        listOf(N, NE, NW) to N,
        listOf(S, SE, SW) to S,
        listOf(W, NW, SW) to W,
        listOf(E, NE, SE) to E
    )

    private val startPositions = inputFile().readLines().filter { it.isNotBlank() }.mapIndexed { y, str ->
        str.mapIndexedNotNull { x, c ->
            if (c == '#') x by y else null
        }
    }.flatten().toSet()

    private var positions = startPositions
    private var rules = startRules
    private var count = 0

    //Returns a list of oldPos -> newPos
    private fun mutated(rules: List<Pair<List<Point2>, Point2>>): List<Pair<Point2, Point2>> =
        positions.map { elf ->
            val noSurroundingElves = elf.diagAdjacent().none { it in positions }
            if (noSurroundingElves) elf to elf
            else {
                val nextPos = rules.firstOrNull { rule ->
                    val ruleNeighbors = rule.first.map { it + elf }
                    ruleNeighbors.none { it in positions }
                }?.second?.plus(elf)
                elf to (nextPos ?: elf)
            }
        }.groupBy { it.second }.flatMap {
            if (it.value.size == 1) it.value else it.value.map { ps -> ps.first to ps.first }
        }.also { count++ }.also { this.rules = rules.drop(1) + rules.take(1) }


    override fun problemOne(): Number {
        repeat(10) {
            positions = mutated(rules).map { it.second }.toSet()
        }
        val minX = positions.map { it.x }.min()!!
        val minY = positions.map { it.y }.min()!!
        val maxX = positions.map { it.x }.max()!!
        val maxY = positions.map { it.y }.max()!!
        val area = (maxX - minX + 1) * (maxY - minY + 1)
        return area - positions.count()
    }

    override fun problemTwo(): Number {
        while (true) {
            val proposedMoves = mutated(rules)
            if (proposedMoves.all { it.first == it.second }) return count
            positions = proposedMoves.map { it.second }.toSet()
        }
    }

    private fun Collection<Point2>.draw() {
        val minX = this.map { it.x }.min()!!
        val minY = this.map { it.y }.min()!!
        val maxX = this.map { it.x }.max()!!
        val maxY = this.map { it.y }.max()!!
        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                print(if (x by y in this) '#' else '.')
            }
            println()
        }
        println()
    }
}
