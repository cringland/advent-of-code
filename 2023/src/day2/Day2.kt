package day2

import Day
import java.lang.Integer.max

class Day2 : Day {
    data class Hand(val num: Int, val colour: String)
    data class SetTotal(val red: Int, val blue: Int, val green: Int)

    private val input = inputFile().readLines().map { line ->
        line.substringAfter(": ").split("; ").map { sets ->
            sets.split(", ").map { hands ->
                hands.split(" ").let { Hand(it[0].toInt(), it[1]) }
            }
        }
    }.mapIndexed { i, it -> (i + 1) to it }.map { game ->
        game.first to game.second.map { sets ->
            sets.fold(SetTotal(0, 0, 0)) { total, it ->
                when (it.colour) {
                    "red" -> SetTotal(total.red + it.num, total.blue, total.green)
                    "blue" -> SetTotal(total.red, total.blue + it.num, total.green)
                    else -> SetTotal(total.red, total.blue, total.green + it.num)
                }
            }
        }
    }

    override fun problemOne(): Number {
        return input.filter { game ->
            game.second.none { total ->
                (total.red > 12 || total.green > 13 || total.blue > 14)
            }
        }.sumBy { it.first }
    }

    override fun problemTwo(): Number {
        return input.map { game ->
            game.second.fold(SetTotal(0, 0, 0)) { maxes, set ->
                SetTotal(max(maxes.red, set.red), max(maxes.blue, set.blue), max(maxes.green, set.green))
            }
        }.sumBy{ it.red * it.blue * it.green}
    }
}
