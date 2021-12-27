package day23

import Day
import java.io.File
import java.util.*
import kotlin.math.abs

class Day23 : Day {


    data class Move(val cost: Long, val result: State)
    data class Total(val cost: Long, val state: State) : Comparable<Total> {
        override fun compareTo(other: Total): Int {
            return this.cost.compareTo(other.cost)
        }
    }

    data class State(val a: List<Char>, val b: List<Char>, val c: List<Char>, val d: List<Char>, val hall: List<Char>) {

        companion object {
            fun getCost(c: Char): Long = when (c) {
                'A' -> 1L
                'B' -> 10L
                'C' -> 100L
                'D' -> 1000L
                else -> throw RuntimeException("What is this $c?")
            }

            fun <T> List<T>.replacing(i: Int, value: T): List<T> {
                val newList = this.toMutableList()
                newList[i] = value
                return newList.toList()
            }

            fun doorIndex(c: Char) = when (c) {
                'A' -> 2
                'B' -> 4
                'C' -> 6
                'D' -> 8
                else -> throw RuntimeException("What is this $c?")
            }

            fun isDoor(i: Int) = i in listOf(2, 4, 6, 8)

            fun <T> List<T>.containsNone(values: List<T>): Boolean {
                return this.none { values.contains(it) }
            }
        }

        fun getMoves(): List<Move> {
            val moves = mutableListOf<Move>()

            fun movesToHall(list: List<Char>, c: Char, stateMaker: (list: List<Char>, hall: List<Char>) -> State) {
                val index = list.indexOfFirst { it != '.' }
                val allDotsOrCorrect = list.all { it == c || it == '.' }
                if (!allDotsOrCorrect && index > -1) {
                    val newList = list.replacing(index, '.')
                    val distToHall = index + 1
                    val character = list[index]
                    val doorIndex = doorIndex(c)
                    for (i in doorIndex downTo 0) {
                        if (isDoor(i)) continue
                        if (hall[i] != '.') break
                        val cost = (doorIndex - i + distToHall) * getCost(character)
                        val newHall = hall.replacing(i, character)
                        moves.add(Move(cost, stateMaker(newList, newHall)))
                    }
                    for (i in doorIndex until hall.size) {
                        if (isDoor(i)) continue
                        if (hall[i] != '.') break
                        val cost = (i - doorIndex + distToHall) * getCost(character)
                        val newHall = hall.replacing(i, character)
                        moves.add(Move(cost, stateMaker(newList, newHall)))
                    }
                }
            }

            movesToHall(a, 'A') { list, newHall -> this.copy(a = list, hall = newHall) }
            movesToHall(b, 'B') { list, newHall -> this.copy(b = list, hall = newHall) }
            movesToHall(c, 'C') { list, newHall -> this.copy(c = list, hall = newHall) }
            movesToHall(d, 'D') { list, newHall -> this.copy(d = list, hall = newHall) }

            for (i in hall.indices) {
                val ch = hall[i]
                if (ch == '.') continue
                val doorIndex = doorIndex(ch)
                fun hallwayClear() = (if (i < doorIndex) hall.subList(i + 1, doorIndex) else hall.subList(doorIndex, i - 1)).all { it == '.' }

                fun movesFromHall(list: List<Char>, stateMaker: (list: List<Char>, hall: List<Char>) -> State) {
                    val notChars = listOf('A', 'B', 'C', 'D').filterNot { it == ch }
                    if (list.containsNone(notChars) && hallwayClear()) {
                        val lastDotIndex = list.lastIndexOf('.')
                        val newAList = list.replacing(lastDotIndex, ch)
                        val newHall = hall.replacing(i, '.')
                        val dist = (lastDotIndex + 1) + abs(doorIndex - i)
                        val cost = dist * getCost(ch)
                        moves.add(Move(cost, stateMaker(newAList, newHall)))
                    }
                }
                when (ch) {
                    'A' -> movesFromHall(a) { list, newHall -> this.copy(a = list, hall = newHall) }
                    'B' -> movesFromHall(b) { list, newHall -> this.copy(b = list, hall = newHall) }
                    'C' -> movesFromHall(c) { list, newHall -> this.copy(c = list, hall = newHall) }
                    'D' -> movesFromHall(d) { list, newHall -> this.copy(d = list, hall = newHall) }
                    else -> throw RuntimeException("oops?")
                }

            }


            return moves
        }

        fun isComplete(): Boolean = a.all { it == 'A' } && b.all { it == 'B' } && c.all { it == 'C' } && d.all { it == 'D' }
    }

    private val input = File("src/day23/input").readLines().filter { it.isNotEmpty() }
            .mapNotNull { line ->
                "(\\w)".toRegex().findAll(line).toList().map { it.value.first() }.let {
                    if (it.isEmpty())
                        null
                    else
                        State(listOf(it[0]), listOf(it[1]), listOf(it[2]), listOf(it[3]), listOf())
                }
            }.let {
                val f = it.first()
                val s = it[1]
                State(f.a + s.a, f.b + s.b, f.c + s.c, f.d + s.d, List(11) { '.' })
            }

    override fun problemOne(): Long {
        return findShortest(input)

    }

    override fun problemTwo(): Long {
        fun List<Char>.inserting(c1: Char, c2: Char): List<Char> = listOf(this.first(), c1, c2, this.last())
        val input2 = State(
                input.a.inserting('D', 'D'),
                input.b.inserting('C', 'B'),
                input.c.inserting('B', 'A'),
                input.d.inserting('A', 'C'),
                input.hall
        )
        return findShortest(input2)
    }

    private fun findShortest(start: State): Long {
        val processed = mutableSetOf<State>()
        val toProcess = PriorityQueue<Total>()
        toProcess.offer(Total(0, start))

        while (toProcess.isNotEmpty()) {
            val u = toProcess.poll()
            if (u.state.isComplete()) {
                return u.cost
            }
            processed.add(u.state)
            u.state.getMoves().forEach { nextMove ->
                if (!processed.contains(nextMove.result)) {
                    val dist = u.cost + nextMove.cost
                    toProcess.offer(Total(dist, nextMove.result))
                }
            }
        }
        throw RuntimeException("oops")
    }
}

