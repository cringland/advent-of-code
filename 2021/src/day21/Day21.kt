package day21

import Day

class Day21 : Day {


//    private val player1Start = 4
//    private val player2Start = 8
    private val player1Start = 5
    private val player2Start = 6

    class DeterministicDice {
        private var counter = 1
        var rolls = 0

        fun next(): Int = counter.also {
            rolls++
            counter++
            if (counter == 101) counter = 1
        }

        override fun toString(): String {
            return "DeterministicDice(counter=$counter, rolls=$rolls)"
        }

    }

    data class Player(private val position: Int, val score: Int = 0) {
        fun move(i: Int): Player {
            val newPosition = (position + i) % 10
            val newScore = score + if (newPosition == 0) 10 else newPosition
            return Player(newPosition, newScore)
        }
    }

    override fun problemOne(): Int {
        var player1 = Player(player1Start)
        var player2 = Player(player2Start)
        val dice = DeterministicDice()
        while (true) {
            player1 = player1.move(dice.next() + dice.next() + dice.next())
            if (player1.score >= 1000)
                return player2.score * dice.rolls
            player2 = player2.move(dice.next() + dice.next() + dice.next())
            if (player2.score >= 1000)
                return player1.score * dice.rolls
        }
    }

    override fun problemTwo(): Long {
        val player1 = Player(player1Start)
        val player2 = Player(player2Start)
        val solution = solve2(player1, player2, true, mutableMapOf())
        return if (solution.first > solution.second) solution.first else solution.second
    }

    data class Game(val p1: Player, val p2: Player, val p1Turn: Boolean)

    private fun solve2(p1: Player, p2: Player, p1Turn: Boolean, cache: MutableMap<Game, Pair<Long, Long>>): Pair<Long, Long> {
        if (p1.score >= 21) {
            return 1L to 0L
        } else if (p2.score >= 21) {
            return 0L to 1L
        }
        return cache.getOrElse(Game(p1, p2, p1Turn)) {
            var wins = 0L to 0L
            for (r1 in 1..3) {
                for (r2 in 1..3) {
                    for (r3 in 1..3) {
                        if (p1Turn)
                            wins += solve2(p1.move(r1 + r2 + r3), p2, false, cache)
                        else
                            wins += solve2(p1, p2.move(r1 + r2 + r3), true, cache)
                    }
                }
            }
            cache[Game(p1, p2, p1Turn)] = wins
            wins
        }
    }

    private operator fun Pair<Long, Long>.plus(that: Pair<Long, Long>) = (this.first + that.first) to (this.second + that.second)
}

